package app.outofthenest.api;

import java.util.List;

import app.outofthenest.api.request.SendNotificationRequest;
import app.outofthenest.models.Notification;
import retrofit2.Call;
import retrofit2.http.*;

public interface NotificationApi {
    @POST("api/notifications/sendEventNotification")
    Call<Notification> sendEventNotification(@Body SendNotificationRequest request);

    @GET("api/notifications/getUserNotifications")
    Call<List<Notification>> getUserNotifications(@Query("userId") String userId);

    @PUT("api/notifications/markAsRead")
    Call<Void> markAsRead(@Path("id") String id);
}