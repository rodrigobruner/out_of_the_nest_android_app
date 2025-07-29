package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.NotificationApi;
import app.outofthenest.models.Notification;
import app.outofthenest.mocs.NotificationsMoc;
import app.outofthenest.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * communicates with the Notification API to manage user notifications.
 */
public class NotificationRepository {

    String TAG = getClass().getSimpleName();
    private NotificationApi notificationApi;

    public NotificationRepository() {
        notificationApi = ApiService.getRetrofit().create(NotificationApi.class);
    }

    // get user notifications
    public LiveData<List<Notification>> getUserNotifications(String userId) {
        MutableLiveData<List<Notification>> data = new MutableLiveData<>();

        // Moc mode for testing
        if (Constants.USE_MOC_MODE) {
            data.setValue(NotificationsMoc.getNotifications());
            return data;
        }

        notificationApi.getUserNotifications(userId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    // mark a notification as read
    public LiveData<Boolean> markAsRead(String id) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        notificationApi.markAsRead(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                data.setValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }

    //delete a notification
    public LiveData<Boolean> deleteNotification(String id) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        notificationApi.deleteNotification(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                data.setValue(response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }
}