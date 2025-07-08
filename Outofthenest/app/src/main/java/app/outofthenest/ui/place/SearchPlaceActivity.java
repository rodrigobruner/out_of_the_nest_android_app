package app.outofthenest.ui.place;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivitySearchPlaceBinding;
import app.outofthenest.mocs.PlacesMoc;
import app.outofthenest.models.Place;

public class SearchPlaceActivity extends AppCompatActivity {

    private static final String TAG = "SearchPlaceActivity";
    private ActivitySearchPlaceBinding binding;
    private TagsAdapter tagsAdapter;
    private PlaceAdapter placeAdapter;

    ArrayList<Place> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchPlaceBinding.inflate(getLayoutInflater());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue_800));
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setupTagsRecyclerView();
        setupPlacesRecyclerView();
        setTagListener();
        setSearchListener();
    }

    private void setupPlacesRecyclerView() {
        placeAdapter = new PlaceAdapter(getBaseContext(), new ArrayList<>());
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.placesRecyclerView.setAdapter(placeAdapter);
        setPlaceClickListeners();
    }

    private void setPlaceClickListeners() {
        placeAdapter.setOnPlaceClickListener(new PlaceAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClick(Place place) {
                Log.i(TAG, "Place clicked: " + place.getTitle());
                Intent intent = new Intent(getBaseContext(), PlacesActivity.class);
                intent.putExtra("place", place);
                startActivity(intent);
            }
        });

        placeAdapter.setOnGoClickListener(new PlaceAdapter.OnGoClickListener() {
            @Override
            public void onGoClick(Place place) {
                Intent intent = new Intent(SearchPlaceActivity.this, MainActivity.class); // or the Activity hosting MapsFragment
                intent.putExtra("destination", place);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // Search functions
    private void setSearchListener() {
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPlaces(); // ADDED: Search when text changes
            }
        });
    }

    private void setTagListener() {
        tagsAdapter.setOnTagSelectedListener(new TagsAdapter.OnTagSelectedListener() {
            @Override
            public void onTagSelected(String tag) {
                Log.i(TAG, "Tag selected: " + tag);
                searchPlaces(); // ADDED: Search when tag is selected
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

        // MODIFIED: Only search if there's text or selected tags
        if (searchText.isEmpty() && selectedTags.isEmpty()) {
            // Clear results if no search criteria
            placeAdapter.updatePlaces(new ArrayList<>());
            return;
        }

        Log.i(TAG, "Search text: " + searchText);
        Log.i(TAG, "Selected tags: " + selectedTags.toString());

        List<Place> filteredPlaces = filterPlaces(searchText, selectedTags);
        placeAdapter.updatePlaces(filteredPlaces);
    }

    // ADDED: Filter places based on search text and selected tags
    private List<Place> filterPlaces(String searchText, List<String> selectedTags) {
        List<Place> allPlaces = getPlaces();
        List<Place> filteredPlaces = new ArrayList<>();

        for (Place place : allPlaces) {
            boolean matchesText = searchText.isEmpty() ||
                    place.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    place.getDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                    place.getType().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesTags = selectedTags.isEmpty() ||
                    place.getTags().stream().anyMatch(tag ->
                            selectedTags.contains(tag));

            if (matchesText && matchesTags) {
                filteredPlaces.add(place);
            }
        }

        return filteredPlaces;
    }

    // Config recyclerView for tags
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

    private ArrayList<Place> getPlaces() {
        return PlacesMoc.getPlaces();
    }
}