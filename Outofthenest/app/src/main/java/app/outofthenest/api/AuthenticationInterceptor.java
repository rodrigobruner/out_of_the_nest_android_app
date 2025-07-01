package app.outofthenest.api;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AuthenticationInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private static final int TOKEN_TIMEOUT_SECONDS = 10;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // get Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.w(TAG, "No authenticated user, proceeding without token");
            return chain.proceed(originalRequest);
        }

        // get token synchronously
        String token = getTokenSync(user);
        if (token == null) {
            Log.w(TAG, "Failed to get token, proceeding without authentication");
            return chain.proceed(originalRequest);
        }

        // Add Authorization heder
        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        Log.d(TAG, "Request authenticated with token");
        return chain.proceed(authenticatedRequest);
    }

    private String getTokenSync(FirebaseUser user) {
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] tokenHolder = new String[1];

        user.getIdToken(false).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                tokenHolder[0] = task.getResult().getToken();
            } else {
                Log.e(TAG, "Failed to get token: " + task.getException());
            }
            latch.countDown();
        });

        try {
            if (!latch.await(TOKEN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                Log.e(TAG, "Token request timed out");
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "Token request interrupted", e);
            Thread.currentThread().interrupt();
        }

        return tokenHolder[0];
    }
}