package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Tag;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TagApi {
    @GET("dictionary/getTags")
    Call<List<Tag>> getTags();

    @GET("dictionary/getPlaceTypes")
    Call<List<String>> getPlaceTypes();
}