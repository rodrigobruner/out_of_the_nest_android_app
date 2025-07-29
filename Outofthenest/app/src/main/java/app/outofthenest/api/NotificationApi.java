package app.outofthenest.api;

import java.util.List;
import app.outofthenest.models.Notification;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Calls to the notifications API.
 */
public interface NotificationApi {

    // get all notifications for a user
    @GET("notifications/getUserNotifications")
    Call<List<Notification>> getUserNotifications(@Query("userId") String userId);

    // mark a notification as read
    @PUT("notifications/markAsRead/{id}")
    Call<Void> markAsRead(@Path("id") String id);

    // delete a notification
    @DELETE("notifications/delete/{id}")
    Call<Void> deleteNotification(@Path("id") String id);
}