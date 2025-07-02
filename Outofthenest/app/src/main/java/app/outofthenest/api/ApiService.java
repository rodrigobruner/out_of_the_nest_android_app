package app.outofthenest.api;

import app.outofthenest.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class deals with API communication
 */
public class ApiService {
    private static final String BASE_URL = Constants.URL_API;
    private static Retrofit retrofit;

    //singleton pattern to provide a retrofit instance
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Log interceptor, show the communication on the log
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Authentication interceptor
            AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor();

            //Add the authentication
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build();

            //Call the api
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}