package app.outofthenest.ui.place;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentPlaceDetailBinding;
import app.outofthenest.models.Place;

public class PlaceDetailFragment extends Fragment {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    static String PLACE_PARAMATER = "place";

    private FragmentPlaceDetailBinding placeBinding;

    private Place place;

    private TagsAdapter tagsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        placeBinding = FragmentPlaceDetailBinding.inflate(inflater, container, false);

        init();
        return placeBinding.getRoot();
    }

    private void init(){
        setUpPlace();
        setUpButton();
    }

    private void setUpPlace() {
        if (getArguments() != null) {
            place = getArguments().getSerializable(PLACE_PARAMATER, Place.class);
            placeBinding.txvPlacesTitle.setText(place.getTitle());
            placeBinding.txvAddress.setText(place.getAddress());
            placeBinding.txvDistance.setText(place.getDistance());
            placeBinding.txvStatus.setText(place.getStatus());
            placeBinding.txvDescription.setText(place.getDescription());
            setupTagsRecyclerView(place.getTags());

        }
    }

    private void setUpButton(){
        placeBinding.btnGoToPlace.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("destination", place);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_maps, bundle);
        });
    }


    private void setupTagsRecyclerView(List<String> availableTags) {
        tagsAdapter = new TagsAdapter(availableTags);
        tagsAdapter.setSelectionEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        placeBinding.recyclerTags.setLayoutManager(layoutManager);
        placeBinding.recyclerTags.setAdapter(tagsAdapter);
    }
}
