package app.outofthenest.utils;// app/outofthenest/util/LocationProvider.java


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Singleton class to provide location services.
 * TODO: apply in the screens where location is needed.
 */
public class LocationProvider {
    private static LocationProvider instance;
    private final FusedLocationProviderClient fusedLocationClient;
    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();

    private static int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private LocationProvider(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
    }

    public static synchronized LocationProvider getInstance(Context context) {
        if (instance == null) {
            instance = new LocationProvider(context);
        }
        return instance;
    }

    public LiveData<Location> getLocationLiveData() {
        return locationLiveData;
    }

    public void fetchLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    locationLiveData.setValue(location);
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}