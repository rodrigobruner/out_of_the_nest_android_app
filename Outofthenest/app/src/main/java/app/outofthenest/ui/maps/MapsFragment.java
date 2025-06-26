package app.outofthenest.ui.maps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import app.outofthenest.PlacesActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.FragmentMapsBinding;

public class MapsFragment extends Fragment {
    // binding for the fragment
    private FragmentMapsBinding binding;
    // Google Maps variable
    private GoogleMap mMap;
    // location variables
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    // location services client
    private FusedLocationProviderClient locationClient;
    // current user location
    private LatLng userLocation;
    // location permission launcher
    private ActivityResultLauncher<String> locationPermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    // initialize the fragment
    public void init(){
        // get the user location
        initPermissionLauncher();
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        createLocationRequest();
        createLocationCallback();
        getUserLocation();

        //prepare the map and set listeners
        setupMap();
        setAddButtonListener();

    }

    // permission launcher initialization
    private void initPermissionLauncher() {
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        getUserLocation(); // get the user location
                    } else {
                        // show a message if permission is denied
                        Toast.makeText(requireContext(), getString(R.string.txt_location_permission_denied), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    // request location permission
    private void requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // get the user's current location
    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        updateMapLocation();
                    }
                    startLocationUpdates();
                });
    }

    // create location request
    private void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .build();
    }

    // handle with location updates
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updateMapLocation();
                }
            }
        };
    }

    // update the map with user current location
    private void updateMapLocation() {
        if (mMap != null && userLocation != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
        }
    }

    // start and stop location updates
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    // stop location updates
    private void stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback);
    }

    // setup the map fragment
    private void setupMap() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    // set listener for the add button
    private void setAddButtonListener() {
        binding.btnAddPlace.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PlacesActivity.class);
            startActivity(intent);
        });
    }

    // callback for when the map is ok
    private final OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;
        if (userLocation != null) {
            updateMapLocation();
        } else {
            startLocationUpdates();
        }
    };

    // when app is paused, stop location updates
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    // when app is resumed, start location updates
    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            startLocationUpdates();
        }
    }
}