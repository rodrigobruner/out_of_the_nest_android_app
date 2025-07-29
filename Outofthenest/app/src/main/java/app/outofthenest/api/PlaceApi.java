package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Place;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Calls to the places API.
 */
public interface PlaceApi {

    // create a place on the server
    @POST("places/createPlace")
    Call<Place> createPlace(@Body Place place);

    // search places based on geographic position, filter(optional) and tags(optional)
    @GET("places/getPlacesNear")
    Call<List<Place>> getPlacesNear(
            @Query("latitude") double lat,
            @Query("longitude") double lng,
            @Query("delta") double delta,
            @Query("filter") String filter,
            @Query("tags") List<String> tags
    );
}