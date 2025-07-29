package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to manage the onboarding process.
 */
public class OnboardUtils {

    private static final String PREF_NAME = Constants.APP_PAKEGE_NAME+".onboard_prefs";

    // check if is the first time in the app
    public static boolean isFirstTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(prefs.getBoolean("is_first_time", true)) {
            prefs.edit().putBoolean("is_first_time", false).apply();
            return true;
        } else {
            return false;
        }
    }
}
