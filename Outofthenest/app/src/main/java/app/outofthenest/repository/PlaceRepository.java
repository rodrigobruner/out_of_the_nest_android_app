package app.outofthenest.repository;

import android.util.Log;

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
    private static final String TAG = "PlaceRepository";
    private PlaceApi placeApi;

    public PlaceRepository() {
        placeApi = ApiService.getRetrofit().create(PlaceApi.class);
    }

    public LiveData<List<Place>> getPlacesNearWithFilter(double lat, double lng, double delta, String filter, Double minRating) {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
        placeApi.getPlacesNearWithFilter(lat, lng, delta, filter, minRating).enqueue(new Callback<List<Place>>() {
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

    public LiveData<Place> createPlace(Place place) {
        Log.i(TAG, "Creating place: " + place.getTitle());
        MutableLiveData<Place> data = new MutableLiveData<>();
        placeApi.createPlace(place).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                data.setValue(response.body());
                Log.i(TAG, "Created: " + place.getTitle());
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                data.setValue(null);
                Log.i(TAG, "Error: " + place.getTitle() + " - " + t.getMessage());
            }
        });
        return data;
    }
}