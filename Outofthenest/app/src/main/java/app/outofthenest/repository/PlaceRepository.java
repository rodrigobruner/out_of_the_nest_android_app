package app.outofthenest.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import app.outofthenest.api.ApiService;
import app.outofthenest.api.PlaceApi;
import app.outofthenest.models.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private PlaceApi placeApi;
    private AuthenticationRepository authRepo;

    public PlaceRepository(Application application) {
        placeApi = ApiService.getRetrofit().create(PlaceApi.class);
        authRepo = new AuthenticationRepository(application);
    }

    public LiveData<List<Place>> getPlacesNear(double lat, double lng, double delta, String filter, ArrayList<String> tags) {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
        placeApi.getPlacesNear(lat, lng, delta, filter, tags).enqueue(new Callback<List<Place>>() {
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
        authRepo.refreshUserToken();
        Log.i(TAG, "Creating place: " + place.getTitle());
        MutableLiveData<Place> data = new MutableLiveData<>();
        placeApi.createPlace(place).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                data.setValue(response.body());
//                Log.i(TAG, "Created: " + place.getTitle());
            }
            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                data.setValue(null);
//                Log.i(TAG, "Error: " + place.getTitle() + " - " + t.getMessage());
            }
        });
        return data;
    }
}