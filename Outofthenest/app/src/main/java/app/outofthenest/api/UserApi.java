package app.outofthenest.api;

import app.outofthenest.api.request.LoginRequest;
import app.outofthenest.models.User;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserApi {
    @POST("users/createUser")
    Call<User> createUser(@Body User user);

    @GET("users/getUser")
    Call<User> getUser(@Query("id") String id);

    @PUT("users/updateUser")
    Call<User> updateUser(@Path("id") String id, @Body User user);

    @POST("users/login")
    Call<User> login(@Body LoginRequest loginRequest);
}