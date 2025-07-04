package app.outofthenest.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.List;

import app.outofthenest.BuildConfig;
import app.outofthenest.NewPlaceActivity;
import app.outofthenest.R;
import app.outofthenest.SearchPlaceActivity;
import app.outofthenest.databinding.FragmentMapsBinding;
import app.outofthenest.models.Place;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    //Constants
    private static final String TAG = "MapsFragment";
    private static final float DEFAULT_ZOOM = 15f;

    //binding
    private FragmentMapsBinding binding;

    // Maps variables
    private MapViewModel mapViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private Place destination;
    private Location currentLocation;
    private GoogleMap mMap;

    private ActivityResultLauncher<String> locationPermissionRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                checkLocationPermission();
            } else {
                Toast.makeText(getContext(), "Location permission is required to show your position on the map.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        init();
    }

    private void init() {
        observeDestination();
        setAddButtonListener();
        setSearchButtonListener();
        setCancelButtonListener();
        initMap();

    }


    private void initMap(){
        Log.d(TAG, "Initializing map...");

        if (destination == null) {
            checkLocationPermission();
        } else {
            Log.i(TAG, destination.getTitle());
            traceRoute(destination);
        }
    }

    // Set a Route on the map
    private void traceRoute(Place destination) {
        Log.i(TAG, "Tracing route to destination: " + destination.getTitle());
        if (mMap != null && destination != null && currentLocation != null) {
            Log.d(TAG, "Tracing route to: " + destination.getTitle());

            LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng dest = new LatLng(destination.getLatitude(), destination.getLongitude());

            Log.i(TAG, "Origin: " + origin.latitude + ", " + origin.longitude);
            Log.i(TAG, "Destination: " + dest.latitude + ", " + dest.longitude);

            // Add markers for origin and destination
            mMap.addMarker(new MarkerOptions().position(origin).title("Your Location"));
            mMap.addMarker(new MarkerOptions().position(dest).title(destination.getTitle()));

            // Use a separate thread for the network request
            new Thread(() -> {
                try {
                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey(BuildConfig.MAPS_API_KEY)
                            .build();

                    DirectionsResult result = DirectionsApi.newRequest(context)
                            .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                            .destination(new com.google.maps.model.LatLng(dest.latitude, dest.longitude))
                            .await();

                    if (result.routes != null && result.routes.length > 0) {
                        DirectionsRoute route = result.routes[0];
                        List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();

                        // Switch back to the main thread to update the UI
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                PolylineOptions polylineOptions = new PolylineOptions();
                                for (com.google.maps.model.LatLng latLng : path) {
                                    polylineOptions.add(new LatLng(latLng.lat, latLng.lng));
                                }
                                polylineOptions.color(ContextCompat.getColor(requireContext(), R.color.blue_600));
                                polylineOptions.width(15);
                                mMap.addPolyline(polylineOptions);

                                // Adjust camera to show the entire route
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(dest);
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // offset from edges of the map in pixels
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                            });
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error tracing route", e);
                }
            }).start();

        } else {
            Log.w(TAG, "Map, destination, or current location is null. Cannot trace route.");
        }
    }

    // Set current location on the map
    private void setUserCurrentLocationOnMap() {
        if (mMap != null && currentLocation != null) {
            LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLatLng).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM));
        } else {
            Log.w(TAG, "Map or current location is null. Cannot set user location on map.");
        }
    }

    // Get parameters from the intent
    private void observeDestination() {
        mapViewModel.getDestination().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                this.destination = new Place(
                        String.valueOf(place.getId()),
                        place.getTitle(),
                        place.getDescription(),
                        place.getType(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getTags()
                );
                if (currentLocation != null) {
                    traceRoute(this.destination);
                } else {
                    checkLocationPermission();
                }
                // Only clear when you are sure you don't need it anymore
                // mapViewModel.clearDestination();
            }
        });
    }


    // Ui Listeners
    private void setAddButtonListener() {
        binding.btnAddPlace.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), NewPlaceActivity.class);
            startActivity(intent);
        });
    }

    private void setSearchButtonListener() {
        binding.btnSearchPlace.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchPlaceActivity.class);
            startActivity(intent);
        });
    }

    private void setCancelButtonListener() {
        binding.btnCancel.setOnClickListener(v -> {
            destination = null;
            mapViewModel.clearDestination();
            //Clear the map and set the current location
        });
    }

    private void setGoButtonListener() {
        binding.btnGo.setOnClickListener(v -> {
            if (destination != null) {
                // start the navigation to the destination
            } else {
                Toast.makeText(requireContext(), "Please select a destination first.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Permission and Location Handling
    @SuppressLint("MissingPermission")
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        currentLocation = location;
                        Log.d(TAG, "Current Location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());

                        if (destination != null) {
                            traceRoute(destination);
                        } else {
                            setUserCurrentLocationOnMap();
                        }
                    }
                });
    }
}