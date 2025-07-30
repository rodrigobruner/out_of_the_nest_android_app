package app.outofthenest.utils;

import android.content.Context;

import app.outofthenest.R;
import app.outofthenest.models.Place;

public class PlaceUtils {

    // update the status of the place
    // TODO: it should be done by the server
    public static Place updateStatus(Place place, Context context) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);

        String status;
        if (currentHour >= 6 && currentHour < 20) {
            if (currentHour >= 19) {
                status = context.getString(R.string.text_place_status_close_soon);
            } else {
                status = context.getString(R.string.text_place_status_open);
            }
        } else {
            status = context.getString(R.string.text_place_status_closed);
        }

        place.setStatus(status);
        return place;
    }
}