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

/**
 * Communicates with the Place API to manage places.
 */
public class PlaceRepository {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private PlaceApi placeApi;
    private AuthenticationRepository authRepo;

    public PlaceRepository(Application application) {
        placeApi = ApiService.getRetrofit().create(PlaceApi.class);
        authRepo = new AuthenticationRepository(application);
    }

    // get a place by position, filter and tags
    public LiveData<List<Place>> getPlacesNear(double lat, double lng, double delta, String filter, ArrayList<String> tags) {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
//        Log.i(TAG, "places near: lat=" + lat + ", lng=" + lng + ", delta=" + delta + ", filter=" + filter + ", tags=" + tags);
        placeApi.getPlacesNear(lat, lng, delta, filter, tags).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String rawJson = new com.google.gson.Gson().toJson(response.body());
//                        Log.i(TAG, "Raw JSON: " + rawJson);
                    } catch (Exception e) {
//                        Log.e(TAG, "Error logging JSON: " + e.getMessage());
                    }
//                    Log.i(TAG, "response received: " + response.body().size());
                    data.setValue(response.body());
                } else {
//                    Log.i(TAG, "response error: " + response.code());
                    data.setValue(new ArrayList<>());
                }
            }
            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
//                Log.i(TAG, "error: " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    // create a new place
    public LiveData<Place> createPlace(Place place) {
        authRepo.refreshUserToken();
//        Log.i(TAG, "creating place: " + place.getTitle());
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