package app.outofthenest.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.outofthenest.R;
import app.outofthenest.mocs.EventsMoc;
import app.outofthenest.models.Event;
import app.outofthenest.ui.events.EventActivity;
import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.utils.Constants;

public class EventFMS extends FirebaseMessagingService {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();
    private static final String CHANNEL_ID = "events_channel";
    private static final String CHANNEL_NAME = "Events";
    private static final String EVENT_PARAMETER_NAME = "event";
    private static final String EVENT_ID_PARAMETER_NAME = "event_id";

    @Override
    public void onNewToken(String token) {
        android.util.Log.d("FCM", "Refreshed token: " + token);
        sendRegistrationToServer(token); // Implement this method to send the token to your backend if needed
    }

    private void sendRegistrationToServer(String token) {
        //TODO: Implement this method to send the token to your server
        Log.i(TAG, "Token sent to server: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String eventId = null;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        if (remoteMessage.getData() != null && remoteMessage.getData().containsKey(EVENT_PARAMETER_NAME)) {
            eventId = remoteMessage.getData().get(EVENT_PARAMETER_NAME);
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra(EVENT_ID_PARAMETER_NAME, eventId);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EVENT_PARAMETER_NAME, EVENT_PARAMETER_NAME);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_event)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager.notify(1, builder.build());
    }
}
