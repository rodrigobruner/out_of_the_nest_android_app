package app.outofthenest.ui.place;

import static android.content.Intent.getIntent;

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
import app.outofthenest.databinding.FragmentPlaceDetailBinding;
import app.outofthenest.models.Place;
import app.outofthenest.models.Reviews;

public class PlaceCommentsFragment extends Fragment {

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
        List<Reviews> reviewsList = getReviews();
        ReviewsAdapter adapter = new ReviewsAdapter(reviewsList);
        placeCommentsBinding.recyclerViewComments.setAdapter(adapter);
        placeCommentsBinding.recyclerViewComments.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
    }

    private List<Reviews> getReviews() {
        List<Reviews> reviews = new ArrayList<>();
        reviews.add(new Reviews("1", "Amazing!", "Absolutely loved this place. Will come back for sure.", 5, new Date(), "user1"));
        reviews.add(new Reviews("2", "Good experience", "Nice atmosphere and friendly staff.", 4, new Date(), "user2"));
        reviews.add(new Reviews("3", "Not bad", "It was okay. Lorem ipsum dolor sit amet.", 3, new Date(), "user3"));
        reviews.add(new Reviews("4", "Could be better", "Service was slow and the food was cold.", 2, new Date(), "user4"));
        reviews.add(new Reviews("5", "Disappointing", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque euismod.", 1, new Date(), "user5"));
        reviews.add(new Reviews("6", "Fantastic place!", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 5, new Date(), "user6"));
        reviews.add(new Reviews("7", "Just okay", "Nothing special. Lorem ipsum dolor sit amet.", 3, new Date(), "user7"));
        reviews.add(new Reviews("8", "Loved it!", "Great service, delicious food, and a wonderful ambiance. Highly recommended for families and friends. Lorem ipsum dolor sit amet, consectetur.", 5, new Date(), "user8"));
        return reviews;
    }

}
