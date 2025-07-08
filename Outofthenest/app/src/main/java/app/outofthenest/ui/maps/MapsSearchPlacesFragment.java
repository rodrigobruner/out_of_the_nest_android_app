package app.outofthenest.ui.maps;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import app.outofthenest.ui.place.PlacesActivity;
import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentMapsSearchPlacesBinding;
import app.outofthenest.mocs.PlacesMoc;
import app.outofthenest.models.Place;

public class MapsSearchPlacesFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
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
        setupTagsRecyclerView();
        setupPlacesRecyclerView();
        setTagListener();
        setSearchListener();
    }

    private void setupPlacesRecyclerView() {
        placeAdapter = new PlaceAdapter(getContext(), new ArrayList<>());
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.placesRecyclerView.setAdapter(placeAdapter);
        setPlaceClickListeners();
    }

    private void setPlaceClickListeners() {
        placeAdapter.setOnPlaceClickListener(place -> {
//            Log.i(TAG, "Place clicked: " + place.getTitle());
            Intent intent = new Intent(getContext(), PlacesActivity.class);
//            intent.putExtra("place", place);
            startActivity(intent);
        });

        placeAdapter.setOnGoClickListener(place -> {
//            Log.i(TAG, "Go clicked for: " + place.getTitle());
            mapViewModel.setDestination(place);
        });
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