package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.ReviewApi;
import app.outofthenest.models.Review;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Communicates with the Review API to manage user reviews.
 */
public class ReviewsRepository {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private ReviewApi reviewApi;

    public ReviewsRepository() {
        reviewApi = ApiService.getRetrofit().create(ReviewApi.class);
    }

    // create a new review
    public LiveData<Review> createReview(String title, String description, int rating, String userId, String placeId) {
        MutableLiveData<Review> data = new MutableLiveData<>();
        Review request = new Review(null, title, description, rating, null, userId, placeId);
        reviewApi.createReview(request).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    // get reviews by place ID
    public LiveData<List<Review>> getReviewsByPlace(int placeId) {
        MutableLiveData<List<Review>> data = new MutableLiveData<>();
        reviewApi.getReviewsByPlace(placeId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}