package app.outofthenest.ui.place.search;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivitySearchPlaceBinding;
import app.outofthenest.models.Place;
import app.outofthenest.ui.place.PlaceViewModel;
import app.outofthenest.ui.place.PlacesActivity;
import app.outofthenest.utils.Constants;
import app.outofthenest.utils.LocationProvider;

public class SearchPlaceActivity extends AppCompatActivity {

    private static final String TAG = "SearchPlaceActivity";
    private ActivitySearchPlaceBinding binding;
    private TagsAdapter tagsAdapter;
    private PlaceAdapter placeAdapter;
    private PlaceViewModel viewModel;
    private Location currentLocation;


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LocationProvider.getInstance(this).fetchLocation(this);
                } else {
                    Toast.makeText(this, getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchPlaceBinding.inflate(getLayoutInflater());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue_800));
        setContentView(binding.getRoot());
        init();
        requestPermission();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        setUpActionBar();
        setupTagsRecyclerView();
        setupPlacesRecyclerView();
        setTagListener();
        setSearchListener();
        observeViewModel();
        getUserLocation();
    }

    public void setUpActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_event_bar_search_place);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_maps);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    private void setupPlacesRecyclerView() {
        placeAdapter = new PlaceAdapter(getBaseContext(), new ArrayList<>());
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.placesRecyclerView.setAdapter(placeAdapter);
        setPlaceClickListeners();
    }

    private void setPlaceClickListeners() {
        placeAdapter.setOnPlaceClickListener(place -> {
            Log.i(TAG, "Place clicked: " + place.getTitle());
            Intent intent = new Intent(getBaseContext(), PlacesActivity.class);
            intent.putExtra("place", place);
            startActivity(intent);
        });

        placeAdapter.setOnGoClickListener(place -> {
            Intent intent = new Intent(SearchPlaceActivity.this, MainActivity.class);
            intent.putExtra("destination", place);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.getPlaces().observe(this, places -> {
            ArrayList<String> selectedTags = new ArrayList<>(getSelectedTags());
            String searchText = binding.searchEditText.getText().toString().trim();

            if (searchText.isEmpty() && selectedTags.isEmpty()) {
                placeAdapter.updatePlaces(new ArrayList<>());
                return;
            }

            List<Place> filteredPlaces = filterPlaces(searchText, selectedTags, places != null ? places : new ArrayList<>());
            placeAdapter.updatePlaces(filteredPlaces);
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            Log.d(TAG, "Loading: " + isLoading);
        });

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e(TAG, "Error: " + errorMessage);
                viewModel.clearErrorMessage();
            }
        });
    }

    private void setSearchListener() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchPlaces();
            }
        });
    }

    private void setTagListener() {
        tagsAdapter.setOnTagSelectedListener(new TagsAdapter.OnTagSelectedListener() {
            @Override
            public void onTagSelected(String tag) {
                Log.i(TAG, "Tag selected: " + tag);
                searchPlaces();
            }

            @Override
            public void onTagDeselected(String tag) {
                Log.i(TAG, "Tag deselected: " + tag);
                searchPlaces();
            }
        });
    }

    private void searchPlaces() {
        ArrayList<String> selectedTags = new ArrayList<>(getSelectedTags());
        String searchText = binding.searchEditText.getText().toString().trim();

        if (searchText.isEmpty() && selectedTags.isEmpty()) {
            placeAdapter.updatePlaces(new ArrayList<>());
            return;
        }

        Log.i(TAG, "Search text: " + searchText);
        Log.i(TAG, "Selected tags: " + selectedTags.toString());
        double lat = 0;
        double lng = 0;

        if(currentLocation != null) {
            lat = currentLocation.getLatitude();
            lng = currentLocation.getLongitude();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
        }

        viewModel.fetchPlacesNear(lat, lng, Constants.DEFAULT_SEARCH_PLACE_DELTA, searchText, selectedTags);
    }

    private List<Place> filterPlaces(String searchText, List<String> selectedTags, List<Place> allPlaces) {
        List<Place> filteredPlaces = new ArrayList<>();

        for (Place place : allPlaces) {
            boolean matchesText = searchText.isEmpty() ||
                    place.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    place.getDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                    place.getType().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesTags = selectedTags.isEmpty() ||
                    place.getTags().stream().anyMatch(selectedTags::contains);

            if (matchesText && matchesTags) {
                filteredPlaces.add(place);
            }
        }

        return filteredPlaces;
    }

    private void setupTagsRecyclerView() {
        List<String> availableTags = getAvailableTags();
        tagsAdapter = new TagsAdapter(availableTags);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.recyclerTags.setLayoutManager(layoutManager);
        binding.recyclerTags.setAdapter(tagsAdapter);
    }

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

    private void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getUserLocation() {
        LocationProvider.getInstance(this).getLocationLiveData()
                .observe(this, location -> {
                    if (location != null) {
                        Log.d(TAG, "Current location: " + location.getLatitude() + ", " + location.getLongitude());
                        currentLocation = location;
                        onGetLocation();
                    }
                });
    }

    private void onGetLocation() {
        Log.i(TAG, "Location obtained, ready to search places");
        // Você pode fazer uma busca automática aqui se desejar
        // searchPlaces();
    }
}