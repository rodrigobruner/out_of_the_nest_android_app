package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to manage the onboarding process.
 */
public class OnboardUtils {

    private static final String PREF_NAME = Constants.APP_PAKEGE_NAME+".onboard_prefs";
    private static final String PREF_KEY_ONBOARDED = "is_first_time";

    // check if is the first time in the app
    public static boolean onboardDone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_KEY_ONBOARDED, false);
    }

    public static void markOnboardCompleted(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(PREF_KEY_ONBOARDED, true).apply();
    }
}
