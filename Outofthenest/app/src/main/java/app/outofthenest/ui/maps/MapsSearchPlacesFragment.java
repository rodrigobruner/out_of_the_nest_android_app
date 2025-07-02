package app.outofthenest.ui.maps;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentMapsSearchPlacesBinding;
import app.outofthenest.models.Place;

public class MapsSearchPlacesFragment extends Fragment {

    private static final String TAG = "MapsSearchPlacesFragment";
    private PlaceViewModel mViewModel;
    private FragmentMapsSearchPlacesBinding binding;
    private TagsAdapter tagsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsSearchPlacesBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
        View root = binding.getRoot();
        init();
        return root;
    }

    private void init() {
        closeSearchCard();
        setButtonCancel();
        setupTagsRecyclerView();
        setTagListener();
        setSearchListener();
        searchOnFocusChange();
    }

    //Control the visibility of search card

    public int getExpandedHeight() {
        View rootView = requireActivity().getWindow().getDecorView();
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int height = r.height();
        Log.i(TAG, "Visible HEIGHT frame: " + height);


        return (height/100)*80;
    }

    public int getClosedHeight() {
        return 320;
    }

    private void expandSearchCard() {
        int height = getExpandedHeight();
        binding.buttonCancel.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = binding.searchCard.getLayoutParams();
        layoutParams.height = height;
    }

    private void closeSearchCard() {
        int height = getClosedHeight();
        binding.searchEditText.setText("");
        binding.searchEditText.clearFocus();
        binding.buttonCancel.setVisibility(View.GONE);
        binding.placesRecyclerView.setAdapter(null);
        ViewGroup.LayoutParams layoutParams = binding.searchCard.getLayoutParams();
        layoutParams.height = height;
    }

    private void searchOnFocusChange() {
        binding.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            expandSearchCard();
        });
    }

    private void setButtonCancel() {
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchCard();
            }
        });
    }

    // Serach functions
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
        Log.i(TAG, selectedTags.toString());

        String searchText = binding.searchEditText.getText().toString().trim();
        if (!searchText.isEmpty()) {
            Log.i(TAG, searchText);
        }

        //List<Place> placesSearchResults = mViewModel.getPlacesNear(searchText, lat, lng, selectedTags);


        List<Place> placesSearchResults = getPlaces();
        PlaceAdapter placeAdapter = new PlaceAdapter(placesSearchResults);

        binding.placesRecyclerView.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(getContext())
        );
        binding.placesRecyclerView.setAdapter(placeAdapter);
    }


    //Config recyclerView for tags

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
        tags.add("park");
        tags.add("cinema");
        tags.add("library");

        return new ArrayList<Place>(Arrays.asList(
                new Place(1,"Waterloo Park", "Public park", "Park", "Westmount Rd Entrance, 90 Westmount Rd N, Waterloo, ON N2L", "11 min", "5,2 km", "Open", 4, 43.460867, -80.509813, 0.0f, tags),
                new Place(2,"Waterloo Public Library", "Public library", "Library", "2001 University Ave E, Waterloo, ON N2K 0B3", "17 min", "6 km", "Close soon", 2, 43.460867, -80.509813, 0.0f,tags),
                new Place(3,"Playground - Bridge St W", "Public space","Playground","185 Bridge St W, Waterloo, ON N2K 1K9", "18 min", "7.3 km", "Closed", 5, 43.460867, -80.509813, 0.0f, tags),
                new Place(4,"Galaxy Cinemas Waterloo", "Cinema", "Indoor activity", "550 King St N, Waterloo, ON N2L 5W6", "21 min", "8.8 km", "Open", 5, 43.460867, -80.509813, 0.0f, tags)
        ));
    }

}