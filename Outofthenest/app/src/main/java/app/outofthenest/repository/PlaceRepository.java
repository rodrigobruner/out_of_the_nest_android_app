package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import app.outofthenest.api.ApiService;
import app.outofthenest.api.PlaceApi;
import app.outofthenest.models.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {
    private PlaceApi placeApi;

    public PlaceRepository() {
        placeApi = ApiService.getRetrofit().create(PlaceApi.class);
    }

    public LiveData<List<Place>> getPlacesNear(double lat, double lng, double delta) {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
        placeApi.getPlacesNear(lat, lng, delta).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}