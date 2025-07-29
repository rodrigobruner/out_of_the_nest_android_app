package app.outofthenest.ui.place.review;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import app.outofthenest.adapters.ReviewsAdapter;
import app.outofthenest.databinding.FragmentPlaceReviewsBinding;
import app.outofthenest.models.Place;

public class PlaceReviewFragment extends Fragment {

    private static final String PLACE_PARAMETER = "place";
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private FragmentPlaceReviewsBinding binding;
    private PlaceReviewViewModel viewModel;
    private Place place;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PlaceReviewViewModel.class);

        init();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerView();
    }

    private void init(){
        setUpPlaceTitle();
        setUpRecyclerView();
        setUpReviewButton();
    }


    //set up ui
    private void setUpPlaceTitle() {
        if (getArguments() != null) {
            place = getArguments().getSerializable("place", Place.class);
            binding.txvPlacesTitle.setText(place.getTitle());
            binding.ratingBar.setRating(place.getRating());
        }
    }

    //set up recycler view
    private void setUpRecyclerView() {
        int placeId = place != null ? place.getId() : 0;
        viewModel.getReviewsByPlace(placeId);

        viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            ReviewsAdapter adapter = new ReviewsAdapter(reviews);
            binding.recyclerViewComments.setAdapter(adapter);
            binding.recyclerViewComments.setLayoutManager(
                    new LinearLayoutManager(getContext())
            );
            setImageVisible(reviews.isEmpty());
        });
    }

//    private List<Review> getReviews() {
//        return ReviewsMoc.getReviews();
//    }

    // set up review button
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

    // set image visibilit
    private void setImageVisible(boolean visible) {
        binding.imgNoReviews.setVisibility(visible ? View.VISIBLE: View.GONE);
        binding.textNoReviews.setVisibility(visible ? View.VISIBLE: View.GONE);
    }

}
