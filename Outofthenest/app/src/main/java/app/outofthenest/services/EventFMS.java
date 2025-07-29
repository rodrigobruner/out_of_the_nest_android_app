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

/**
 * This class deal with Firebase Cloud Messaging (FCM) for events.
 */
public class EventFMS extends FirebaseMessagingService {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();
    private static final String CHANNEL_ID = "events_channel";
    private static final String CHANNEL_NAME = "Events";
    private static final String EVENT_PARAMETER_NAME = "event";
    private static final String EVENT_ID_PARAMETER_NAME = "event_id";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // get message data
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String eventId = null;

        // create a new empty pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        // have event id, on click got o Event screen
        if (remoteMessage.getData() != null && remoteMessage.getData().containsKey(EVENT_PARAMETER_NAME)) {
            // get event id from data
            eventId = remoteMessage.getData().get(EVENT_PARAMETER_NAME);
            // create a intent to go to Event screen
            Intent intent = new Intent(this, EventActivity.class);
            // add event id as parameter
            intent.putExtra(EVENT_ID_PARAMETER_NAME, eventId);
            // set the intent to pending intent
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            // no event id, on click go to event list screen
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EVENT_PARAMETER_NAME, EVENT_PARAMETER_NAME);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        // create a new notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

       // create notification channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        // create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_event)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent) // add action to the notification
                .setAutoCancel(true); // remove the notification when clicked

        // notify the user
        manager.notify(1, builder.build());
    }
}
