package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Review;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Calls to the reviews API.
 */
public interface ReviewApi {

    // create a review of a place on the server
    @POST("reviews/createReview")
    Call<Review> createReview(@Body Review request);

    // get all reviews for a place
    @GET("reviews/getReviewsByPlace")
    Call<List<Review>> getReviewsByPlace(@Query("placeId") int placeId);
}