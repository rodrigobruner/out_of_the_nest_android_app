package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import app.outofthenest.models.Notification;

/**
 * This class provides mock data for notifications.
 */
public class NotificationsMoc {

    public static ArrayList<Notification> getNotifications() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        return new ArrayList<>(Arrays.asList(
                new Notification("1", "Welcome!", "Thanks for joining our app.", "INFO", new Date(),false),
                new Notification("2", "Event Reminder", "Don't forget the event tomorrow at 10am.", "EVENT",   new Date(), false),
                new Notification("3", "Update", "Your profile was updated successfully.", "INFO", new Date(),true),
                new Notification("4", "New Message", "You have a new message from the admin.", "MESSAGE", new Date(),false),
                new Notification("5", "Event Cancelled", "The event 'Family Storytelling' was cancelled.", "EVENT", new Date(),true)
        ));
    }
}