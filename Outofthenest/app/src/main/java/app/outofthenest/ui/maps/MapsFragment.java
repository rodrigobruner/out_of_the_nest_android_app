package app.outofthenest.ui.maps;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.pm.PackageManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.List;
import java.util.Locale;

import app.outofthenest.BuildConfig;
import app.outofthenest.ui.place.PlaceViewModel;
import app.outofthenest.ui.place.PlacesActivity;
import app.outofthenest.ui.place.newplace.NewPlaceActivity;
import app.outofthenest.R;
import app.outofthenest.ui.place.search.SearchPlaceActivity;
import app.outofthenest.databinding.FragmentMapsBinding;
import app.outofthenest.models.Place;
import app.outofthenest.utils.Constants;
import app.outofthenest.utils.LocationProvider;
import app.outofthenest.utils.PlaceUtils;

/**
 * Displaying and interacting with the map.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {
    String TAG = getClass().getSimpleName();
    // default map zoom
    private static final String PLACE_PARAM = "place";
    private static final float DEFAULT_ZOOM = Constants.DEFAULT_ZOOM;
    // View binding
    private FragmentMapsBinding binding;
    // ViewModels
    private MapViewModel mapViewModel;
    private PlaceViewModel placeViewModel;



    // place to navigate to
    private Location currentLocation;
    // google Map instance
    private GoogleMap mMap;
    // user marker on the map
    private Marker userMarker;


    // destination place for navigation
    private Place destination;
    // for live navigation
    private FusedLocationProviderClient fusedLocationClient;
    // for live navigation updates
    private LocationCallback liveNavLocationCallback;
    // to nkow if we are navigating
    private boolean isNavigating = false;
    // draw polylines on the map
    private Polyline currentRoutePolyline;
    // zoom for navigation
    private float navigationZoom = 18f;


    // Deal with permissions
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = true; // flag to check permissions
                for (String permission : result.keySet()) {
                    Boolean granted = result.get(permission);
                    if (!granted) {
                        allGranted = false;
                    }
                }

                if (allGranted) {
                    init();
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                }
            });


    // lifecycle methods
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        setUpActionBar();
        initMapObservers();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onDestroyView() { // reset resources
        super.onDestroyView();
        if (isNavigating) {
            stopLiveNavigation();
        }
        binding = null;
    }

    @Override
    public void onPause() { // stop live navigation to save battery
        super.onPause();
        if (isNavigating) {
            stopLiveNavigation();
        }
    }

    @Override
    public void onResume() { // resume live navigation
        super.onResume();
        if (isNavigating) {
            startLiveNavigation();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        requestPermissions();
    }


    private void init() {
        setSearchButtonListener();
        setCancelButtonListener();
        setGoButtonListener();
        setupMarkerClickListener();

        // control the navigation bar visibility
        if (destination == null) {
            hideShowNavigationBar(false);
        } else {
            hideShowNavigationBar(true);
        }

        // set up the map
        LocationProvider.getInstance(requireContext()).fetchLocation(requireActivity());
    }

    // init maps observers
    private void initMapObservers() {
        mapObserver();
        observeDestination();
        observePlaces();
    }

    //set up the action bar
    public void setUpActionBar() {

        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {

            // Set the action bar title and logo
            actionbar.setTitle(R.string.txt_maps_bar_title);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_maps);
            actionbar.setDisplayUseLogoEnabled(true);

            // add button
            requireActivity().addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.add_button, menu);
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                    Intent intent = new Intent(requireContext(), NewPlaceActivity.class);
                    startActivity(intent);
                    return false;
                }
            }, getViewLifecycleOwner());
        }
    }

    // observer to listen location updates
    private void mapObserver() {
        // start observing location changes
        LocationProvider.getInstance(requireContext()).getLocationLiveData()
                .observe(getViewLifecycleOwner(), location -> {
                    // check parameters
                    if (location != null && isAdded() && binding != null) {
                        currentLocation = location;
                        // navegation mode
                        if (destination != null && !isNavigating) {
                            traceRoute(destination);
                            binding.txvDistance.setText(destination.getDistance());
                            binding.txvStatus.setText(destination.getStatus());
                            setUpStatus(destination);
                        } else if (destination == null && !isNavigating) { //user location
                            setUserCurrentLocationOnMap();
                        }
                    }
                });
    }

    // observer to destination updates
    private void observeDestination() {
        mapViewModel.getDestination().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                // create a copy of the place
                this.destination = new Place(
                        place.getId(),
                        place.getTitle(),
                        place.getDescription(),
                        place.getType(),
                        place.getAddress(),
                        place.getDatetime(),
                        place.getDistance(),
                        place.getStatus(),
                        place.getRating(),
                        place.getLatitude(),
                        place.getLongitude(),
                        place.getDelta(),
                        place.getTags()
                );

                // trace route to the destination
                if (currentLocation != null) {
                    traceRoute(this.destination);
                }
            }
        });
    }

    // set up the status
    private void setUpStatus(Place place) {
        // change color of the status
        if(place.getStatus().equals(getString(R.string.text_place_status_closed))){
            binding.txvStatus.setTextColor(getResources().getColor(R.color.red_400, null));
        } else if(place.getStatus().equals("Open")){
            binding.txvStatus.setTextColor(getResources().getColor(R.color.green_400, null));
        } else if(place.getStatus().equals("Closing Soon")){
            binding.txvStatus.setTextColor(getResources().getColor(R.color.orange_400, null));
        } else {
            binding.txvStatus.setTextColor(getResources().getColor(R.color.gray, null));
        }
    }

    // Set up the search button listener
    private void setSearchButtonListener() {
        binding.btnSearchPlace.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchPlaceActivity.class);
            startActivity(intent);
        });
    }

    // cancel button listener
    private void setCancelButtonListener() {
        binding.btnCancel.setOnClickListener(v -> {
            clearNavigation();
        });
    }

    // clear and reset the map
    private void clearNavigation() {
        destination = null;
        mapViewModel.clearDestination();

        if (isNavigating) {
            stopLiveNavigation();
            isNavigating = false;
        }

        // Reset the UI elements
        binding.btnGo.setText(getString(R.string.btn_go_to_place_now));
        binding.btnGo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
        binding.btnCancel.setVisibility(View.VISIBLE);

        hideShowNavigationBar(false);

        if (mMap != null) {
            // clear the map polylines and markers
            mMap.clear();
            currentRoutePolyline = null;
            userMarker = null;

            if (currentLocation != null) {
                // get current location
                LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(userLatLng)
                        .zoom(DEFAULT_ZOOM)
                        .tilt(0f)
                        .bearing(0f)
                        .build();

                // move the camera
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // set user marker
                new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                    setUserCurrentLocationOnMap();
                }, 300);
            }
        }
    }

    // Go button listener
    private void setGoButtonListener() {
        binding.btnGo.setOnClickListener(v -> {
            showProgressBar(true); // show progress bar while process
            if (destination != null && currentLocation != null && !isNavigating) {
                // start live navigation
                startLiveNavigation();
                // update the UI elements
                binding.btnGo.setText(getString(R.string.btn_stop_to_place_now));
                binding.btnGo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_400));
                binding.btnCancel.setVisibility(View.GONE);

            } else if (isNavigating) {
                //stop live navigation
                stopLiveNavigation();
                // update the UI elements
                binding.btnGo.setText(getString(R.string.btn_go_to_place_now));
                binding.btnGo.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
                binding.btnCancel.setVisibility(View.VISIBLE);
                showProgressBar(false); // stop progress bar
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_set_destination), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // hide or show the navigation bar
    private void hideShowNavigationBar(boolean show) {
        if (show) {
            binding.txtTitlePlace.setText(destination.getTitle());
            binding.navegationBar.setVisibility(View.VISIBLE);
        } else {
            binding.navegationBar.setVisibility(View.GONE);
        }
    }

    // show or hide the progress bar
    private void showProgressBar(boolean show) {
        if (binding != null && binding.progressBar != null) {
            binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    // set up the marker click
    private void setupMarkerClickListener() {
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTitle() != null) {
                String userLocationTitle = getString(R.string.txt_your_location);
                if (userLocationTitle.equals(marker.getTitle())) {
                    Toast.makeText(getActivity(), userLocationTitle, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Place selectedPlace = findPlaceByTitle(marker.getTitle());
                    if (selectedPlace != null) {
                        openPlaceDetails(updateDistance(selectedPlace));
                    }
                    return true;
                }
            }
            return false;
        });
    }

    //open place details activity
    private void openPlaceDetails(Place place) {
        place = updateDistance(place);
        Intent intent = new Intent(requireContext(), PlacesActivity.class);
        intent.putExtra(PLACE_PARAM, place);
        startActivity(intent);
    }

    // find place by title
    private Place findPlaceByTitle(String title) {
        List<Place> places = placeViewModel.getPlaces().getValue();
        if (places != null) {
            for (Place place : places) {
                if (place.getTitle().equals(title)) {
                    return place;
                }
            }
        }
        return null;
    }

    // update the distance
    private Place updateDistance(Place place) {
        //skip if place is null
        if (currentLocation == null) {
            return null;
        }
        // calculate the distance
        float[] results = new float[1];
        Location.distanceBetween(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                place.getLatitude(),
                place.getLongitude(),
                results
        );
        float distanceInMeters = results[0]; // distance in meters
        String formattedDistance = formatDistance(distanceInMeters);
        place.setDistance(formattedDistance);
        return PlaceUtils.updateStatus(place, requireContext());
    }

    // format the distance
    private String formatDistance(float distanceInMeters) {
        if (distanceInMeters < 1000) {
            return String.format(Locale.getDefault(), "%.0f m", distanceInMeters);
        } else {
            return String.format(Locale.getDefault(), "%.1f km", distanceInMeters / 1000);
        }
    }

    // plot places on the map
    private void plotPlacesOnMap(List<Place> places) {
        for (Place place : places) {
            LatLng placeLatLng = new LatLng(place.getLatitude(), place.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(placeLatLng)
                    .title(place.getTitle())
                    .snippet(place.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        if (currentLocation != null) {
            LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if (userMarker != null) {
                userMarker.remove(); // Remove o marker anterior se existir
            }
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title(getString(R.string.txt_your_location))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    // trace route to the destination
    private void traceRoute(Place destination) {
        // check if map and locations is set
        if (mMap != null && destination != null && currentLocation != null) {

            LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng dest = new LatLng(destination.getLatitude(), destination.getLongitude());

            // set user marker on the map
            if (userMarker == null) {
                userMarker = mMap.addMarker(new MarkerOptions().position(origin)
                        .title(getString(R.string.txt_your_location))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else {
                userMarker.setPosition(origin);
            }

            // set destination marker on the map
            mMap.addMarker(new MarkerOptions().position(dest).title(destination.getTitle()));

            // this tread is used to calculate route in the background
            new Thread(() -> {
                try {
                    // create a GeoApiContext
                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey(BuildConfig.MAPS_API_KEY)
                            .build();

                    // request directions from origin to destination
                    DirectionsResult result = DirectionsApi.newRequest(context)
                            .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                            .destination(new com.google.maps.model.LatLng(dest.latitude, dest.longitude))
                            .mode(com.google.maps.model.TravelMode.DRIVING)
                            .await();

                    // validate the result
                    if (result.routes != null && result.routes.length > 0) {
                        DirectionsRoute route = result.routes[0]; // get the first route
                        // get the path
                        List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> { // update the UI on the main thread
                                // plot the route on the map
                                PolylineOptions polylineOptions = new PolylineOptions();
                                for (com.google.maps.model.LatLng latLng : path) {
                                    polylineOptions.add(new LatLng(latLng.lat, latLng.lng));
                                }
                                // set line options
                                polylineOptions.color(ContextCompat.getColor(requireContext(), R.color.blue_600));
                                polylineOptions.width(15);
                                mMap.addPolyline(polylineOptions);

                                // set the camera position
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(dest);
                                LatLngBounds bounds = builder.build();
                                int padding = 100;
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                            });
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                }
            }).start();
        }
    }

    // update live navigation
    private void updateLiveNavigation() {

        // abort if map or locations are not set
        if (mMap == null || destination == null || currentLocation == null) {
            return;
        }

        // get user and destination LatLng
        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng destLatLng = new LatLng(destination.getLatitude(), destination.getLongitude());

        // update user marker position
        if (userMarker == null) {
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(userLatLng)
                    .title(getString(R.string.txt_your_location))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        } else {
            userMarker.setPosition(userLatLng);
        }

        new Thread(() -> { // this thread is used to update the route in the background
            try {
                // create a GeoApiContext
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey(BuildConfig.MAPS_API_KEY)
                        .build();

                // request directions from user to destination
                DirectionsResult result = DirectionsApi.newRequest(context)
                        .origin(new com.google.maps.model.LatLng(userLatLng.latitude, userLatLng.longitude))
                        .destination(new com.google.maps.model.LatLng(destLatLng.latitude, destLatLng.longitude))
                        .mode(com.google.maps.model.TravelMode.DRIVING)
                        .await();

                // validate the result
                if (result.routes != null && result.routes.length > 0) {
                    // get the first route
                    List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> { // update the UI on the main thread

                            if (!isAdded() || !isNavigating || mMap == null) {
                                showProgressBar(false);
                                return;
                            }

                            // remove the previous polyline if exists
                            if (currentRoutePolyline != null) {
                                currentRoutePolyline.remove();
                            }

                            // plot the new route on the map
                            PolylineOptions polylineOptions = new PolylineOptions();
                            for (com.google.maps.model.LatLng latLng : path) {
                                polylineOptions.add(new LatLng(latLng.lat, latLng.lng));
                            }

                            // set line options
                            polylineOptions.color(ContextCompat.getColor(requireContext(), R.color.blue_600));
                            polylineOptions.width(15);
                            currentRoutePolyline = mMap.addPolyline(polylineOptions);

                            // set the camera position
                            float bearing = currentLocation.getBearing();
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(userLatLng)
                                    .zoom(navigationZoom)
                                    .tilt(60)
                                    .bearing(bearing)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            showProgressBar(false);
                        });
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }).start();
    }

    //set user current location on map
    private void setUserCurrentLocationOnMap() {

        if (mMap != null && currentLocation != null) {
            // set user marker on the map
            LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            if (userMarker == null) {
                userMarker = mMap.addMarker(new MarkerOptions().position(userLatLng)
                        .title(getString(R.string.txt_your_location))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else {
                userMarker.setPosition(userLatLng);
            }
            // move the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM));
        }
        getPlaces();
    }

    // start live navigation
    private void startLiveNavigation() {

        isNavigating = true;
        // get location updates
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        liveNavLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                // update current location
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                    if(isNavigating && destination != null) {
                        updateLiveNavigation();
                    }
                    if (destination != null) {
                        // update the distance and status
                        updateDistance(destination);
                        if (binding != null && binding.txvDistance != null) {
                            binding.txvDistance.setText(destination.getDistance());
                        }
                    }
                }
            }
        };

        // create a location request
        LocationRequest locationRequest = new LocationRequest.Builder(2000)
                .setMinUpdateIntervalMillis(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();

        // check permissions to request location update
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, liveNavLocationCallback, Looper.getMainLooper());
        }
    }

    // stop live navigation
    private void stopLiveNavigation() {
        isNavigating = false;
        if (fusedLocationClient != null && liveNavLocationCallback != null) {
            fusedLocationClient.removeLocationUpdates(liveNavLocationCallback);
        }
    }

    // get places near the current location through api
    private void getPlaces(){
        placeViewModel.getPlacesNear(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                Constants.DEFAULT_SEARCH_PLACE_DELTA,
                "",
                null
        );
    }

    // observe places from the ViewModel
    private void observePlaces() {
        // live data observer for places
        placeViewModel.getPlaces().observe(getViewLifecycleOwner(), places -> {
            if (places != null && mMap != null) {
                plotPlacesOnMap(places);
            }
        });
    }

    // Request permissions
    public void requestPermissions() {
        // Already have permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            init();
            return;
        }

        // request permissions
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
        };
        requestPermissionsLauncher.launch(permissions);
    }
}