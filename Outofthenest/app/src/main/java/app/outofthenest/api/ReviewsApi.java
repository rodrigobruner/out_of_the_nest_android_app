package app.outofthenest.api;

import java.util.List;

import app.outofthenest.api.request.CreateReviewRequest;
import app.outofthenest.models.Reviews;
import retrofit2.Call;
import retrofit2.http.*;

public interface ReviewsApi {
    @POST("reviews/createReview")
    Call<Reviews> createReview(@Body CreateReviewRequest request);

    @GET("reviews/getReviewsByPlace")
    Call<List<Reviews>> getReviewsByPlace(@Query("placeId") int placeId);

    @GET("reviews/getReviewsByUser")
    Call<List<Reviews>> getReviewsByUser(@Query("userId") String userId);

    @PUT("reviews/updateReview")
    Call<Reviews> updateReview(@Path("id") String id, @Body Reviews review);

    @DELETE("reviews/deleteReview")
    Call<Void> deleteReview(@Path("id") String id);
}