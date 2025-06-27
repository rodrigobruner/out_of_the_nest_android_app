package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Place;
import retrofit2.Call;
import retrofit2.http.*;

public interface PlaceApi {
    @POST("places/createPlace")
    Call<Place> createPlace(@Body Place place);

    @GET("places/getPlacesNear")
    Call<List<Place>> getPlacesNearWithFilter(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("delta") double delta,
            @Query("filter") String filter,
            @Query("minRating") Double minRating
    );

    @GET("places/getPlace")
    Call<Place> getPlace(@Query("id") int id);
}