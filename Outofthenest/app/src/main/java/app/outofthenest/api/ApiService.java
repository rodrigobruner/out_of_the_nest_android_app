package app.outofthenest.api;

import app.outofthenest.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * ApiService class to create a Retrofit instance to API calls.
 *  - This class use OkHttpClient to generate log and authentication
 */
public class ApiService {

    String TAG = getClass().getSimpleName();
    private static final String BASE_URL = Constants.URL_API;
    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofit() {

        //Singleton pattern to avoid multiple instances of Retrofit
        if (retrofit == null) {
            // the OkHttpClient will be used to log the requests and responses
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // this interceptor is to add authentication headers (Bearer token)
            AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor();

            // build the OkHttpClient with interceptors
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build();

            // TODO: check if it results in serialization problems and loss ratings score
            // define date format in the api
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            // build the Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}