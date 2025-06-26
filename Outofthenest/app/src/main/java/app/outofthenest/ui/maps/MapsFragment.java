package app.outofthenest.ui.maps;

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

import com.google.android.gms.location.LocationServices;
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
import app.outofthenest.utils.Constants;

public class MapsFragment extends Fragment {

    private static final String TAG = "MapsFragment";

    FragmentMapsBinding binding;

    private FusedLocationProviderClient locationClient;

    private LatLng userLocation;

    private LatLng defaultLocation = Constants.defaultLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMapsBinding.bind(view);
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getUserLocation();
        init();
    }



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (userLocation != null) {
                googleMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            } else {  // location not available, show a default location
                googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("You are here"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
            }
        }
    };

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
    }

    private void init(){
        setupMap();
        setAddButtonListener();
    }

    private void setupMap(){
        // Initialize the map fragment and set the callback
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        mapFragment.getView().setClickable(true);
    }



    private void setAddButtonListener() {
        binding.btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), PlacesActivity.class);
                startActivity(intent);
            }
        });
    }


}