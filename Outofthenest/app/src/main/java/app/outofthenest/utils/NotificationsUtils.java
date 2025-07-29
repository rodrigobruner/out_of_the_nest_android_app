package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class to control notification read status.
 */
public class NotificationsUtils {
    private static final String PREFS_NAME = Constants.APP_PAKEGE_NAME+".notifications_prefs";

    // set the notification read status
    public static void setReadStatus(Context context, String notificationId, boolean isRead) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(notificationId, isRead).apply();
    }

    // get the notification read status
    public static boolean isRead(Context context, String notificationId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(notificationId, false);
    }
}
