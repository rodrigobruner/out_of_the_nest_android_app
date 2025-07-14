package app.outofthenest.ui.place.review;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import app.outofthenest.adapters.ReviewsAdapter;
import app.outofthenest.databinding.FragmentPlaceReviewsBinding;
import app.outofthenest.mocs.ReviewsMoc;
import app.outofthenest.models.Place;
import app.outofthenest.models.Review;

public class PlaceReviewFragment extends Fragment {

    private static final String PLACE_PARAMETER = "place";

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private FragmentPlaceReviewsBinding binding;



    private Place place;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);

        init();
        return binding.getRoot();
    }

    private void init(){
        setUpPlaceTitle();
        setUpRecyclerView();
        setUpReviewButton();
    }



    private void setUpPlaceTitle() {
        if (getArguments() != null) {
            place = getArguments().getSerializable("place", Place.class);
            binding.txvPlacesTitle.setText(place.getTitle());
            binding.ratingBar.setRating(place.getRating());
        }
    }

    private void setUpRecyclerView() {
        List<Review> reviewList = getReviews();
        ReviewsAdapter adapter = new ReviewsAdapter(reviewList);
        binding.recyclerViewComments.setAdapter(adapter);
        binding.recyclerViewComments.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
    }

    private List<Review> getReviews() {
        return ReviewsMoc.getReviews();
    }

    private void setUpReviewButton(){
        binding.btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewPlaceReviewActivity.class); // or the Activity hosting MapsFragment
                intent.putExtra(PLACE_PARAMETER, place);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

}
