package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Review;
import retrofit2.Call;
import retrofit2.http.*;

public interface ReviewApi {
    @POST("reviews/createReview")
    Call<Review> createReview(@Body Review request);

    @GET("reviews/getReviewsByPlace")
    Call<List<Review>> getReviewsByPlace(@Query("placeId") int placeId);

    @GET("reviews/getReviewsByUser")
    Call<List<Review>> getReviewsByUser(@Query("userId") String userId);

    @PUT("reviews/updateReview")
    Call<Review> updateReview(@Path("id") String id, @Body Review review);

    @DELETE("reviews/deleteReview")
    Call<Void> deleteReview(@Path("id") String id);
}