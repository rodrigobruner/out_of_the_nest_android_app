package app.outofthenest.ui.maps;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.PlacesActivity;
import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentMapsSearchPlacesBinding;
import app.outofthenest.models.Place;

public class MapsSearchPlacesFragment extends Fragment {

    private static final String TAG = "MapsSearchPlacesFragment";
    private FragmentMapsSearchPlacesBinding binding;
    private TagsAdapter tagsAdapter;
    private PlaceAdapter placeAdapter;
    private MapViewModel mapViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsSearchPlacesBinding.inflate(inflater, container, false);
        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        init();
        return binding.getRoot();
    }

    private void init() {
        closeSearchCard();
        setButtonCancel();
        setupTagsRecyclerView();
        setupPlacesRecyclerView();
        setTagListener();
        setSearchListener();
        searchOnFocusChange();
    }

    private void setupPlacesRecyclerView() {
        placeAdapter = new PlaceAdapter(new ArrayList<>());
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.placesRecyclerView.setAdapter(placeAdapter);
        setPlaceClickListeners();
    }

    private void setPlaceClickListeners() {
        placeAdapter.setOnPlaceClickListener(place -> {
            Log.i(TAG, "Place clicked: " + place.getTitle());
            Intent intent = new Intent(getContext(), PlacesActivity.class);
            intent.putExtra("place", place);
            startActivity(intent);
        });

        placeAdapter.setOnGoClickListener(place -> {
            Log.i(TAG, "Go clicked for: " + place.getTitle());
            mapViewModel.setDestination(place);
            closeSearchCard();
        });
    }

    //Control the visibility of search card

    public int getExpandedHeight() {
        View rootView = requireActivity().getWindow().getDecorView();
        return (rootView.getHeight() / 100) * 80;
    }

    public int getClosedHeight() {
        return 320;
    }

    private void expandSearchCard() {
        binding.buttonCancel.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = binding.searchCard.getLayoutParams();
        layoutParams.height = getExpandedHeight();
        binding.searchCard.setLayoutParams(layoutParams);
    }

    private void closeSearchCard() {
        binding.searchEditText.setText("");
        binding.searchEditText.clearFocus();
        binding.buttonCancel.setVisibility(View.GONE);
        placeAdapter.updatePlaces(new ArrayList<>());
        ViewGroup.LayoutParams layoutParams = binding.searchCard.getLayoutParams();
        layoutParams.height = getClosedHeight();
        binding.searchCard.setLayoutParams(layoutParams);
    }

    private void searchOnFocusChange() {
        binding.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                expandSearchCard();
            }
        });
    }

    private void setButtonCancel() {
        binding.buttonCancel.setOnClickListener(v -> closeSearchCard());
    }

    // Search functions
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
                searchPlaces();
            }

            @Override
            public void onTagDeselected(String tag) {
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

        List<Place> filteredPlaces = filterPlaces(searchText, selectedTags);
        placeAdapter.updatePlaces(filteredPlaces);
    }

    private List<Place> filterPlaces(String searchText, List<String> selectedTags) {
        List<Place> allPlaces = getPlaces();
        List<Place> filteredPlaces = new ArrayList<>();

        for (Place place : allPlaces) {
            boolean matchesText = searchText.isEmpty() ||
                    place.getTitle().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesTags = selectedTags.isEmpty() ||
                    place.getTags().stream().anyMatch(selectedTags::contains);

            if (matchesText && matchesTags) {
                filteredPlaces.add(place);
            }
        }
        return filteredPlaces;
    }

    //Config recyclerView for tags
    private void setupTagsRecyclerView() {
        List<String> availableTags = getAvailableTags();
        tagsAdapter = new TagsAdapter(availableTags);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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

    // Place Mock Data
    private ArrayList<Place> getPlaces() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Washroom");
        tags.add("Parking");
        tags.add("Pet friendly");

        return new ArrayList<>(Arrays.asList(
                new Place(1, "Waterloo Park", "Lorem ipsum dolor sit amet...", "Park", "50 Young St W, Waterloo, ON N2L 2Z4", "5 min", "1.2 km", "Open", 4, 43.466800, -80.525000, 0.0f, tags),
                new Place(2, "Waterloo Public Library", "Lorem ipsum dolor sit amet...", "Library", "35 Albert St, Waterloo, ON N2L 5E2", "6 min", "1.4 km", "Close soon", 2, 43.468200, -80.520500, 0.0f, tags),
                new Place(3, "Playground - Alexandra Park", "Lorem ipsum dolor sit amet...", "Playground", "75 William St N, Waterloo, ON N2J 3G4", "7 min", "1.6 km", "Closed", 5, 43.470000, -80.518000, 0.0f, tags),
                new Place(4, "Galaxy Cinemas Waterloo", "Cinema", "Indoor activity", "550 King St N, Waterloo, ON N2L 5W6", "8 min", "1.8 km", "Open", 5, 43.472000, -80.515500, 0.0f, tags),
                new Place(5, "Laurel Creek Conservation Area", "Lorem ipsum dolor sit amet...", "Park", "625 Westmount Rd N, Waterloo, ON N2V 2C9", "10 min", "2.1 km", "Open", 4, 43.475000, -80.530000, 0.0f, tags),
                new Place(6, "Perimeter Institute", "Lorem ipsum dolor sit amet...", "Institute", "31 Caroline St N, Waterloo, ON N2L 2Y5", "9 min", "1.9 km", "Open", 5, 43.464500, -80.522000, 0.0f, tags),
                new Place(7, "Waterloo Memorial Recreation Complex", "Lorem ipsum dolor sit amet...", "Recreation", "101 Father David Bauer Dr, Waterloo, ON N2L 6L1", "11 min", "2.3 km", "Open", 4, 43.463000, -80.530500, 0.0f, tags),
                new Place(8, "Canadian Clay and Glass Gallery", "Lorem ipsum dolor sit amet...", "Gallery", "25 Caroline St N, Waterloo, ON N2L 2Y5", "7 min", "1.5 km", "Open", 3, 43.465200, -80.521000, 0.0f, tags),
                new Place(9, "Waterloo City Hall", "Lorem ipsum dolor sit amet...", "Government", "100 Regina St S, Waterloo, ON N2J 4A8", "6 min", "1.3 km", "Open", 4, 43.466000, -80.520000, 0.0f, tags),
                new Place(10, "Wilfrid Laurier University", "Lorem ipsum dolor sit amet...", "University", "75 University Ave W, Waterloo, ON N2L 3C5", "8 min", "1.7 km", "Open", 5, 43.473500, -80.528000, 0.0f, tags)
        ));
    }
}