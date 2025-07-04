package app.outofthenest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivitySearchPlaceBinding;
import app.outofthenest.databinding.FragmentMapsSearchPlacesBinding;
import app.outofthenest.models.Place;

public class SearchPlaceActivity extends AppCompatActivity {

    private static final String TAG = "SearchPlaceActivity";
    private ActivitySearchPlaceBinding binding;
    private TagsAdapter tagsAdapter;
    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchPlaceBinding.inflate(getLayoutInflater());
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
        placeAdapter = new PlaceAdapter(new ArrayList<>());
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.placesRecyclerView.setAdapter(placeAdapter);
        setPlaceClickListeners();
    }

    private void setPlaceClickListeners() {
        placeAdapter.setOnPlaceClickListener(new PlaceAdapter.OnPlaceClickListener() {
            @Override
            public void onPlaceClick(Place place) {
                Log.i(TAG, "Place clicked: " + place.getTitle());
                Intent intent = new Intent(SearchPlaceActivity.this, PlacesActivity.class);
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
        List<Place> allPlaces = getAllPlaces();
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
        String[] tags = getResources().getStringArray(R.array.tags);
        return Arrays.asList(tags);
    }

    public List<String> getSelectedTags() {
        if (tagsAdapter != null) {
            return tagsAdapter.getSelectedTags();
        }
        return new ArrayList<>();
    }

    private ArrayList<Place> getAllPlaces() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Washroom");
        tags.add("Parking");
        tags.add("Pet friendly");

        return new ArrayList<>(Arrays.asList(
                new Place(1, "Waterloo Park", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et nisi tincidunt tortor pellentesque consequat. Proin ac volutpat ante. In gravida, enim a accumsan aliquet, leo urna maximus urna, vitae condimentum metus orci non tortor. Praesent at varius risus, eget fringilla mi. Proin lacinia a risus id mollis. Sed ante tortor, bibendum eu ultrices quis, sodales ut ligula. Praesent interdum quam id nisl aliquet tristique.", "Park", "Westmount Rd Entrance, 90 Westmount Rd N, Waterloo, ON N2L", "11 min", "5,2 km", "Open", 4, 43.460867, -80.509813, 0.0f, tags),
                new Place(2, "Waterloo Public Library", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et nisi tincidunt tortor pellentesque consequat. Proin ac volutpat ante. In gravida, enim a accumsan aliquet, leo urna maximus urna, vitae condimentum metus orci non tortor. Praesent at varius risus, eget fringilla mi. Proin lacinia a risus id mollis.", "Library", "2001 University Ave E, Waterloo, ON N2K 0B3", "17 min", "6 km", "Close soon", 2, 43.460867, -80.509813, 0.0f, tags),
                new Place(3, "Playground - Bridge St W", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis et nisi tincidunt tortor pellentesque consequat. Proin ac volutpat ante. In gravida, enim a accumsan aliquet, leo urna maximus urna, vitae condimentum metus orci non tortor.", "Playground", "185 Bridge St W, Waterloo, ON N2K 1K9", "18 min", "7.3 km", "Closed", 5, 43.460867, -80.509813, 0.0f, tags),
                new Place(4, "Galaxy Cinemas Waterloo", "Cinema", "Indoor activity", "550 King St N, Waterloo, ON N2L 5W6", "21 min", "8.8 km", "Open", 5, 43.460867, -80.509813, 0.0f, tags)
        ));
    }
}