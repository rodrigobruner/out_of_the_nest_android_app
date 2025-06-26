package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Place;
import retrofit2.Call;
import retrofit2.http.*;

public interface PlaceApi {
    @POST("api/places/createPlace")
    Call<Place> createPlace(@Body Place place);

    @GET("api/places/getPlacesNear")
    Call<List<Place>> getPlacesNear(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("delta") double delta
    );

    @GET("api/places/getPlace")
    Call<Place> getPlace(@Query("id") int id);
}