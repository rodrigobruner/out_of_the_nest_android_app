package app.outofthenest.ui.place;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentPlaceDetailBinding;
import app.outofthenest.models.Place;
import app.outofthenest.ui.events.NewEventActivity;
import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.ui.maps.MapViewModel;
import app.outofthenest.utils.Constants;

/**
 * Fragment to display the details of a place.
 */
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
        setUpActionBar();
        setUpPlace();
        setUpButton();
        setUpStatus();
    }

    //set up the action bar
    public void setUpActionBar() {
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_place_detail_search_place);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_maps);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    // set up ui
    private void setUpPlace() {
        if (getArguments() != null) {
            place = getArguments().getSerializable(PLACE_PARAMATER, Place.class);

//            String imageUrl = Constants.DEFAULT_IMAGE_PATH + "pool.png";
//            Log.d(TAG, "Image URL: " + imageUrl);
//            Glide.with(this)
//                    .load(imageUrl)
//                    .error(R.drawable.img_no_image)
//                    .into(placeBinding.imageView);

            Log.i(TAG, "Place: " + place.getRating());

            placeBinding.txvPlacesTitle.setText(place.getTitle());
            placeBinding.txvAddress.setText(place.getAddress());
            placeBinding.txvDistance.setText(place.getDistance());
            placeBinding.txvStatus.setText(place.getStatus());
            placeBinding.txvDescription.setText(place.getDescription());
            setupTagsRecyclerView(place.getTags());
        }
    }

    // set up go button
    private void setUpButton(){
        placeBinding.btnGoToPlace.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class); // or the Activity hosting MapsFragment
            intent.putExtra(PLACE_PARAMATER, place);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    //set up status text color
    private void setUpStatus(){
        if(place.getStatus().equals(getString(R.string.text_place_status_closed))){
            placeBinding.txvStatus.setTextColor(getResources().getColor(R.color.red_400, null));
        } else if(place.getStatus().equals("Open")){
            placeBinding.txvStatus.setTextColor(getResources().getColor(R.color.green_400, null));
        } else if(place.getStatus().equals("Closing Soon")){
            placeBinding.txvStatus.setTextColor(getResources().getColor(R.color.orange_400, null));
        } else {
            placeBinding.txvStatus.setTextColor(getResources().getColor(R.color.gray, null));
        }
    }

    // set up tags recycler view
    private void setupTagsRecyclerView(List<String> availableTags) {
        tagsAdapter = new TagsAdapter(availableTags, "PLACE");
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
