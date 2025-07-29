package app.outofthenest.ui.notifications;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.models.Notification;
import app.outofthenest.repository.NotificationRepository;

/**
 * ViewModel for notifications.
 */
public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository repository;
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public NotificationViewModel(Application application) {
        super(application);
        repository = new NotificationRepository();
    }

    public LiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    // get notifications for a user
    public void getNotifications(String userId) {
        isLoading.setValue(true);
        repository.getUserNotifications(userId).observeForever(result -> {
            isLoading.setValue(false);
            if (result != null) {
                notifications.setValue(result);
                error.setValue(null);
            } else {
                error.setValue(getApplication().getString(R.string.error_load_notifications));
            }
        });
    }

    // mark a notification as read
    public void markAsRead(String notificationId) {
        repository.markAsRead(notificationId).observeForever(success -> {
            if (!success) {
                error.setValue(getApplication().getString(R.string.error_read_notifications));
            }
        });
    }

    // delete a notification
    public void deleteNotification(String notificationId) {
        repository.deleteNotification(notificationId).observeForever(success -> {
            if (!success) {
                error.setValue(getApplication().getString(R.string.error_delete_notifications));
            }
        });
    }
}