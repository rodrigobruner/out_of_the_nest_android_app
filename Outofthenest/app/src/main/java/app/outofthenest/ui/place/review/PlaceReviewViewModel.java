package app.outofthenest.ui.place.review;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import app.outofthenest.R;
import app.outofthenest.models.Place;
import app.outofthenest.models.Review;
import app.outofthenest.repository.PlaceRepository;
import app.outofthenest.repository.ReviewsRepository;

public class PlaceReviewViewModel extends AndroidViewModel {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private ReviewsRepository reviewRepository;
    private MutableLiveData<Review> createdReview;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;

    public PlaceReviewViewModel(@NonNull Application application) {
        super(application);
        reviewRepository = new ReviewsRepository();
        createdReview = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public void createReview(Review review) {
        Log.i(TAG, "Creating place: " + review.getDatetime());
        isLoading.setValue(true);
        errorMessage.setValue(null);
        reviewRepository.createReview(review.getTitle(),
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
//                    Log.i(TAG, "Created: " + place.getTitle());
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.txt_place_creation_error));
//                    Log.i(TAG, "Error: " + place.getTitle());
                }
            }
        });
    }

    public LiveData<Review> getCreatedreview() {
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
