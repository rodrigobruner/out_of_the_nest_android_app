package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Tag;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Calls to the tags API.
 * Alternatively, get from string resources.
 */
public interface TagApi {

    // get all tags from the server
    @GET("dictionary/getTags")
    Call<List<Tag>> getTags();

    // get all place types from the server
    @GET("dictionary/getPlaceTypes")
    Call<List<String>> getPlaceTypes();
}