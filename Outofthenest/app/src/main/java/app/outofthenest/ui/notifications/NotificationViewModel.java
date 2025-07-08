package app.outofthenest.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.outofthenest.api.ApiService;
import app.outofthenest.api.NotificationApi;
import app.outofthenest.mocs.NotificationsMoc;
import app.outofthenest.models.Notification;
import app.outofthenest.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchNotifications(String userId) {

        // Moc mode for testing
        if (Constants.USE_MOC_MODE){
            notifications.setValue(NotificationsMoc.getNotifications());
            isLoading.setValue(false);
            return;
        }

        isLoading.setValue(true);
        NotificationApi api = ApiService.getRetrofit().create(NotificationApi.class);
        api.getUserNotifications(userId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    notifications.setValue(response.body());
                } else {
                    error.setValue("Failed to load notifications");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue(t.getMessage());
            }
        });
    }
}