package app.outofthenest.ui.maps;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import app.outofthenest.ui.place.newplace.NewPlaceActivity;
import app.outofthenest.R;
import app.outofthenest.ui.place.SearchPlaceActivity;
import app.outofthenest.databinding.FragmentMapsBinding;
import app.outofthenest.models.Place;
import app.outofthenest.utils.LocationProvider;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 15f;
    private FragmentMapsBinding binding;
    private MapViewModel mapViewModel;
    private Place destination;
    private Location currentLocation;
    private GoogleMap mMap;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LocationProvider.getInstance(requireContext()).fetchLocation(requireActivity());
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                }
            });


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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Observe location updates from LocationProvider
        LocationProvider.getInstance(requireContext()).getLocationLiveData()
                .observe(getViewLifecycleOwner(), location -> {
                    if (location != null) {
                        currentLocation = location;
                        if (destination != null) {
                            traceRoute(destination);
                        } else {
                            setUserCurrentLocationOnMap();
                        }
                    }
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        init();
    }


    private void init() {
        setUpActionBar();
        observeDestination();
//        setAddButtonListener();
        setSearchButtonListener();
        setCancelButtonListener();
        setGoButtonListener();
        initMap();
    }

    public void setUpActionBar() {
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_maps_bar_title);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_maps);
            actionbar.setDisplayUseLogoEnabled(true);

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

    private void initMap() {
        if (destination == null) {
            hideShowNavigationBar(false);
            requestLocationPermission();
        } else {
            hideShowNavigationBar(true);
            traceRoute(destination);
        }
    }

    private void traceRoute(Place destination) {
        showProgressBar(true);
        if (mMap != null && destination != null && currentLocation != null) {
            LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng dest = new LatLng(destination.getLatitude(), destination.getLongitude());

            mMap.addMarker(new MarkerOptions().position(origin).title(getString(R.string.txt_your_location)));
            mMap.addMarker(new MarkerOptions().position(dest).title(destination.getTitle()));

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

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                PolylineOptions polylineOptions = new PolylineOptions();
                                for (com.google.maps.model.LatLng latLng : path) {
                                    polylineOptions.add(new LatLng(latLng.lat, latLng.lng));
                                }
                                polylineOptions.color(ContextCompat.getColor(requireContext(), R.color.blue_600));
                                polylineOptions.width(15);
                                mMap.addPolyline(polylineOptions);

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(dest);
                                LatLngBounds bounds = builder.build();
                                int padding = 100;
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                                showProgressBar(false);
                            });
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                }
            }).start();

        }
    }

    private void setUserCurrentLocationOnMap() {
        if (mMap != null && currentLocation != null) {
            LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(userLatLng).title(getString(R.string.txt_your_location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, DEFAULT_ZOOM));
        } else {
            Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
        }
    }

    private void observeDestination() {
        mapViewModel.getDestination().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
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
                if (currentLocation != null) {
                    traceRoute(this.destination);
                } else {
                    requestLocationPermission();
                }
            }
        });
    }

    private void showProgressBar(boolean show) {
        if (binding != null && binding.progressBar != null) {
            binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void hideShowNavigationBar(boolean show) {
        if (show) {
            binding.txtTitlePlace.setText(destination.getTitle());
            binding.navegationBar.setVisibility(View.VISIBLE);
        } else {
            binding.navegationBar.setVisibility(View.GONE);
        }
    }

//    private void setAddButtonListener() {
//        binding.btnAddPlace.setOnClickListener(v -> {
//            Intent intent = new Intent(requireContext(), NewPlaceActivity.class);
//            startActivity(intent);
//        });
//    }

    private void setSearchButtonListener() {
        binding.btnSearchPlace.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchPlaceActivity.class);
            startActivity(intent);
        });
    }

    private void setCancelButtonListener() {
        binding.btnCancel.setOnClickListener(v -> {
            destination = null;
            hideShowNavigationBar(false);
            mapViewModel.clearDestination();
            if (mMap != null) {
                mMap.clear();
                setUserCurrentLocationOnMap();
            }
        });
    }

    private void setGoButtonListener() {
        binding.btnGo.setOnClickListener(v -> {
            if (destination != null) {
                String uri = "google.navigation:q=" + destination.getLatitude() + "," + destination.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_maps_not_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_set_destination), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
}