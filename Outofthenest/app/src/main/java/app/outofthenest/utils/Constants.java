package app.outofthenest.utils;

public class Constants {
    // pakage name of the app
    public static final String APP_PAKEGE_NAME = "app.outofthenest";

    // API base URL
    public static final String URL_API = "https://outofnest.top/api/";

    // permission code for location access
    public static final int PERMITION_CODE = 1000;

    // Default delta for location search
    public static final double DEFAULT_SEARCH_PLACE_DELTA = 10.0;

    // default zoom on the map
    public static final float DEFAULT_ZOOM = 15f;

    // Default datetime format
    public static final String DEFAULT_DATETIME_FORMAT = "EEE, MMM d, yyyy - HH:mm";

    // Default date format
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    // How many days in the future to list events
    public static final int NUMBER_OF_DAYS_TO_LIST_EVENTS = 90;

    //Event search radius in meters
    public static final int DEFAULT_SEARCH_EVENT_RADIUS = 5000; // 5 km

    // Enable this to use mock data for testing
    public static final boolean USE_MOC_MODE = true;

    // I set the Conestoga College's address as the default location
//    public static final LatLng defaultLocation = new LatLng(43.4794047, -80.5180089);
}
