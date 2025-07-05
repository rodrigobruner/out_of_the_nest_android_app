package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Notification;
import retrofit2.Call;
import retrofit2.http.*;

public interface NotificationApi {
    @POST("notifications/sendEventNotification")
    Call<Notification> sendEventNotification(@Body Notification request);

    @GET("notifications/getUserNotifications")
    Call<List<Notification>> getUserNotifications(@Query("userId") String userId);

    @PUT("notifications/markAsRead")
    Call<Void> markAsRead(@Path("id") String id);
}