package app.outofthenest.ui.place.review;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.mocs.ReviewsMoc;
import app.outofthenest.models.Review;
import app.outofthenest.repository.ReviewsRepository;
import app.outofthenest.utils.Constants;

public class PlaceReviewViewModel extends AndroidViewModel {

    String TAG = getClass().getSimpleName();
    private ReviewsRepository reviewRepository;
    private MutableLiveData<Review> createdReview;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<List<Review>> reviews = new MutableLiveData<>();

    public PlaceReviewViewModel(@NonNull Application application) {
        super(application);
        reviewRepository = new ReviewsRepository();
        createdReview = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public void createReview(Review review) {

        isLoading.setValue(true);
        errorMessage.setValue(null);

        reviewRepository.createReview(
                review.getTitle(),
                review.getDescription(),
                review.getRating(),
                review.getUserId(),
                review.getPlaceId()
        ).observeForever(new Observer<Review>() {
            @Override
            public void onChanged(Review result) {
                isLoading.setValue(false);
                if (result != null) {
                    createdReview.setValue(result);
                    Log.i(TAG, "API Review created: " + result.getTitle());
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.txt_place_creation_error));
                    Log.e(TAG, "Error creating review via API");
                }
            }
        });
    }

    public void fetchReviewsByPlace(int placeId) {

        if(Constants.USE_MOC_MODE){
            reviews.setValue(ReviewsMoc.getReviews());
            isLoading.setValue(false);
            return;
        }

        isLoading.setValue(true);
        reviewRepository.getReviewsByPlace(placeId).observeForever(result -> {
            isLoading.setValue(false);
            reviews.setValue(result != null ? result : new ArrayList<>());
        });
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<Review> getCreatedReview() {
        return createdReview;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void clearCreatedReview() {
        createdReview.setValue(null);
    }
}