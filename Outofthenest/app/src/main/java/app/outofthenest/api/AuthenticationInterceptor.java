package app.outofthenest.api;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class deal with Authorization on retrofit
 */
public class AuthenticationInterceptor implements Interceptor {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private static final int TOKEN_TIMEOUT_SECONDS = 10;
    private static final String MAX_RETRY = "3";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response = auth(chain, originalRequest, false);

        // If 401, user token refresh
        if (response.code() == 401 || response.code() == 403 && !hasRetryHeader(originalRequest)) {
            response.close(); // close failed response

            Request retryRequest = originalRequest.newBuilder()
                    .header("X-Retry-Attempt", MAX_RETRY)
                    .build();
            // force refresh
            return auth(chain, retryRequest, true);
        }
        return response;
    }
    //Add the auth on request
    private Response auth(Chain chain, Request request, boolean forceRefresh) throws IOException {
        // Get User from firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return chain.proceed(request);
        }

        // Get token
        String token = getToken(user, forceRefresh);
        if (token == null) {
            return chain.proceed(request);
        }

        //Change request to add auth
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer " + token)
                // Remove retry header
                .removeHeader("X-Retry-Attempt")
                .build();

        return chain.proceed(authenticatedRequest);
    }

    private boolean hasRetryHeader(Request request) {
        return request.header("X-Retry-Attempt") != null;
    }

    private String getToken(FirebaseUser user, boolean forceRefresh) {
        //use to wait for the token
        final CountDownLatch latch = new CountDownLatch(1);
        //use to hold the token
        final String[] tokenHolder = new String[1];

        // get the token async
        user.getIdToken(forceRefresh).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                tokenHolder[0] = task.getResult().getToken();
            }
            // signal that the token is complete
            latch.countDown();
        });

        try {
            // wait for the token or timeout
            latch.await(TOKEN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //interrupt the thread
            Thread.currentThread().interrupt();
        }

        return tokenHolder[0];
    }
}