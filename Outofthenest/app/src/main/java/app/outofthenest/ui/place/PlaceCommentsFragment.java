package app.outofthenest.ui.place;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.outofthenest.adapters.ReviewsAdapter;
import app.outofthenest.databinding.FragmentPlaceCommentsBinding;
import app.outofthenest.mocs.ReviewsMoc;
import app.outofthenest.models.Place;
import app.outofthenest.models.Review;

public class PlaceCommentsFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private FragmentPlaceCommentsBinding placeCommentsBinding;

    private Place place;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        placeCommentsBinding = placeCommentsBinding.inflate(inflater, container, false);

        init();
        return placeCommentsBinding.getRoot();
    }

    private void init(){
        setUpPlaceTitle();
        setUpRecyclerView();
    }

    private void setUpPlaceTitle() {
        if (getArguments() != null) {
            place = getArguments().getSerializable("place", Place.class);
            placeCommentsBinding.txvPlacesTitle.setText(place.getTitle());
            placeCommentsBinding.ratingBar.setRating(place.getRating());
        }
    }

    private void setUpRecyclerView() {
        List<Review> reviewList = getReviews();
        ReviewsAdapter adapter = new ReviewsAdapter(reviewList);
        placeCommentsBinding.recyclerViewComments.setAdapter(adapter);
        placeCommentsBinding.recyclerViewComments.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
    }

    private List<Review> getReviews() {
        return ReviewsMoc.getReviews();
    }

}
