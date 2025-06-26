package app.outofthenest.api;

import java.util.List;

import app.outofthenest.api.request.CreateReviewRequest;
import app.outofthenest.models.Reviews;
import retrofit2.Call;
import retrofit2.http.*;

public interface ReviewsApi {
    @POST("api/reviews/createReview")
    Call<Reviews> createReview(@Body CreateReviewRequest request);

    @GET("api/reviews/getReviewsByPlace")
    Call<List<Reviews>> getReviewsByPlace(@Query("placeId") int placeId);

    @GET("api/reviews/getReviewsByUser")
    Call<List<Reviews>> getReviewsByUser(@Query("userId") String userId);

    @PUT("api/reviews/updateReview")
    Call<Reviews> updateReview(@Path("id") String id, @Body Reviews review);

    @DELETE("api/reviews/deleteReview")
    Call<Void> deleteReview(@Path("id") String id);
}