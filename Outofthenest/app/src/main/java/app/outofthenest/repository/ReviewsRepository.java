package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.ReviewsApi;
import app.outofthenest.models.Reviews;
import app.outofthenest.api.request.CreateReviewRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsRepository {
    private ReviewsApi reviewsApi;

    public ReviewsRepository() {
        reviewsApi = ApiService.getRetrofit().create(ReviewsApi.class);
    }

    public LiveData<Reviews> createReview(String title, String description, int rating, String userId, int placeId) {
        MutableLiveData<Reviews> data = new MutableLiveData<>();
        CreateReviewRequest request = new CreateReviewRequest(title, description, rating, userId, placeId);
        reviewsApi.createReview(request).enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Reviews>> getReviewsByPlace(int placeId) {
        MutableLiveData<List<Reviews>> data = new MutableLiveData<>();
        reviewsApi.getReviewsByPlace(placeId).enqueue(new Callback<List<Reviews>>() {
            @Override
            public void onResponse(Call<List<Reviews>> call, Response<List<Reviews>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Reviews>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Reviews>> getReviewsByUser(String userId) {
        MutableLiveData<List<Reviews>> data = new MutableLiveData<>();
        reviewsApi.getReviewsByUser(userId).enqueue(new Callback<List<Reviews>>() {
            @Override
            public void onResponse(Call<List<Reviews>> call, Response<List<Reviews>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Reviews>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}