
package app.outofthenest.utils;

import android.content.res.Resources;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDateTime(Resources resources, Date date) {

        if (date == null) {
            return "";
        }

        Locale currentLocale = resources.getConfiguration().getLocales().get(0);
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DEFAULT_DATETIME_FORMAT, currentLocale);
        String formatted = formatter.format(date);

        return formatted;
    }
}