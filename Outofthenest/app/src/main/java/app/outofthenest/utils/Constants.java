package app.outofthenest.utils;

public class Constants {
    // API base URL
    public static final String URL_API = "https://out-of-the-nest-android-api.onrender.com/api/";
    // Default delta for location search
    public static final double DEFAULT_SEARCH_PLACE_DELTA = 10.0; // 10 km radius

    public static final double DEFAULT_SEARCH_PLACE_LAT = 43.4794047;
    public static final double DEFAULT_SEARCH_PLACE_LON = -80.5205838;

    public static final String DEFAULT_DATETIME_FORMAT = "EEE, MMM d, yyyy - HH:mm";

    public static final float DEFAULT_ZOOM = 15f;
    // How many days in the future to list events
    public static final int NUMBER_OF_DAYS_TO_LIST_EVENTS = 45; // 45 days from today

    //Event search radius in meters
    public static final int DEFAULT_SEARCH_EVENT_RADIUS = 5000; // 5 km

    // Enable this to use mock data for testing
    public static final boolean USE_MOC_MODE = true;

    // I set the Conestoga College's address as the default location
//    public static final LatLng defaultLocation = new LatLng(43.4794047, -80.5180089);
}
