package app.outofthenest.ui.newplace;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentNewPlaceBinding;
import app.outofthenest.models.Place;
import app.outofthenest.models.PlaceAddress;

public class NewPlaceFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private NewPlaceViewModel mViewModel;
    private FragmentNewPlaceBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String> locationPermissionLauncher;
    private AutocompleteSupportFragment autocompleteFragment;
    private TagsAdapter tagsAdapter;

    // Variables to store the current location
    private PlaceAddress cAddress;

    public static NewPlaceFragment newInstance() {
        return new NewPlaceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewPlaceViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        initLocationPermissionLauncher();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewPlaceBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        showProgressBar(false);
        cAddress = new PlaceAddress(null, 0.0, 0.0);
        setUpSpinner();
        setupCurrentLocationButton();
        setupTagsRecyclerView();
        setupObservers();
        setupSaveButton();
    }

    private void initLocationPermissionLauncher() {
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        getCurrentLocationAddress();
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupCurrentLocationButton() {
        binding.btnCurrentLocation.setOnClickListener(v -> {
            showProgressBar(true);
            binding.btnCurrentLocation.setEnabled(false);
            if (hasLocationPermission()) {
                getCurrentLocationAddress();
            } else {
                requestLocationPermission();
            }
        });
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getCurrentLocationAddress() {
        if (!hasLocationPermission()) return;
        showProgressBar(true);
        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //no permission granted
            return;
        }
        Task<android.location.Location> locationTask = fusedLocationClient.getCurrentLocation(request, null);
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                getAddressFromLocation(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(requireContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
            }
            binding.btnCurrentLocation.setEnabled(true);
            showProgressBar(false);
        });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        showProgressBar(true);
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                // Address ok
                Address address = addresses.get(0);
                String addressText = formatAddress(address);
                binding.inpAddress.setText(addressText);
                cAddress = new PlaceAddress(addressText, latitude, longitude);
            } else {
                // Fallback address not found
                String coordinates = latitude + ", " + longitude;
                cAddress = new PlaceAddress(null, latitude, longitude);
                binding.inpAddress.setText(coordinates);
                Toast.makeText(requireContext(), getString(R.string.txt_address_not_found), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            // Error handling
            String coordinates = latitude + ", " + longitude;
            cAddress = new PlaceAddress(null, latitude, longitude);
            binding.inpAddress.setText(coordinates);
            Toast.makeText(requireContext(), getString(R.string.txt_get_address_error), Toast.LENGTH_SHORT).show();
        }
    }

    private PlaceAddress getLatLongFromAddress(String address) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address addr = addresses.get(0);
                return new PlaceAddress(formatAddress(addr), addr.getLatitude(), addr.getLongitude());
            } else {
                Toast.makeText(requireContext(), getString(R.string.txt_address_not_found), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(requireContext(), getString(R.string.txt_get_address_error), Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    private String formatAddress(Address address) {
        StringBuilder addressText = new StringBuilder();

        // # + street name
        if (address.getSubThoroughfare() != null) {
            addressText.append(address.getSubThoroughfare()).append(" ");
        }
        if (address.getThoroughfare() != null) {
            addressText.append(address.getThoroughfare()).append(", ");
        }

        // City
        if (address.getLocality() != null) {
            addressText.append(address.getLocality()).append(", ");
        }

        // Province
        if (address.getAdminArea() != null) {
            addressText.append(address.getAdminArea());
        }

        // Country
        if (address.getCountryName() != null) {
            addressText.append(" - ").append(address.getCountryName());
        }

        return addressText.toString();
    }


    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.list_place_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnPlaceType.setAdapter(adapter);
    }


    private void setupTagsRecyclerView() {
        List<String> availableTags = getAvailableTags();
        tagsAdapter = new TagsAdapter(availableTags);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.recyclerTags.setLayoutManager(layoutManager);
        binding.recyclerTags.setAdapter(tagsAdapter);
    }

    // TODO: get from API on app load and save on shared preferences, here get from shered preferences
    public List<String> getAvailableTags() {
        String[] tags = getResources().getStringArray(R.array.list_tags);
        return Arrays.asList(tags);
    }

    public List<String> getSelectedTags() {
        if (tagsAdapter != null) {
            return tagsAdapter.getSelectedTags();
        }
        return new ArrayList<>();
    }


    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> {
            String name = binding.inpPlaceName.getText().toString();
            String address = binding.inpAddress.getText().toString();
            String placeType = binding.spnPlaceType.getSelectedItem().toString();
            List<String> tags = getSelectedTags();

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.txt_invalid_place_name), Toast.LENGTH_SHORT).show();
                return;
            }

            if (address.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.txt_invalid_address), Toast.LENGTH_SHORT).show();
                return;
            }

            if (placeType.equals(getString(R.string.place_type_default))) {
                Toast.makeText(requireContext(), getString(R.string.txt_invalid_place_type), Toast.LENGTH_SHORT).show();
                return;
            }

            if (tags.size() == 0) {
                Toast.makeText(requireContext(), getString(R.string.txt_invalid_tags), Toast.LENGTH_SHORT).show();
                return;
            }

            PlaceAddress placeAddress = cAddress;
            if(!address.equals(this.cAddress.getFullAddress())) {
                placeAddress = getLatLongFromAddress(address);
            }

            Place newPlace = new Place(name, address, placeType, placeAddress.getFullAddress(), placeAddress.getLatitude(), placeAddress.getLongitude(), new ArrayList<>(tags));
            Log.i(TAG, "New Place: " + placeAddress.getFullAddress());
            mViewModel.createPlace(newPlace);

        });
    }

    private void showProgressBar(boolean show) {

        if (binding != null && binding.progressBar != null) {
            Log.i(TAG, "showProgressBar: " + show);
            binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    private void setupObservers() {
        // Observer to created place
        mViewModel.getCreatedPlace().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                Toast.makeText(requireContext(), getString(R.string.txt_place_created), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                mViewModel.clearCreatedPlace(); // clear cache
            }
        });

        // Observer to erros
        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                mViewModel.clearErrorMessage(); // Clear cache
            }
        });
    }
}