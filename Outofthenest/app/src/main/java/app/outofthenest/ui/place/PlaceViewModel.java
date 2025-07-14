package app.outofthenest.ui.place;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.mocs.PlacesMoc;
import app.outofthenest.models.Place;
import app.outofthenest.repository.PlaceRepository;
import app.outofthenest.utils.Constants;

public class PlaceViewModel extends AndroidViewModel {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private PlaceRepository placeRepository;
    private MutableLiveData<Place> createdPlace;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<List<Place>> places = new MutableLiveData<>();

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        placeRepository = new PlaceRepository(application);
        createdPlace = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public void createPlace(Place place) {
        Log.i(TAG, "Creating place: " + place.getDatetime());
        isLoading.setValue(true);
        errorMessage.setValue(null);
        placeRepository.createPlace(place).observeForever(new Observer<Place>() {
            @Override
            public void onChanged(Place result) {
                isLoading.setValue(false);
                if (result != null) {
                    createdPlace.setValue(result);
//                    Log.i(TAG, "Created: " + place.getTitle());
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.txt_place_creation_error));
//                    Log.i(TAG, "Error: " + place.getTitle());
                }
            }
        });
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public void fetchPlacesNear(double lat, double lng, double delta, String filter, ArrayList<String> tags) {

        if(Constants.USE_MOC_MODE){
            places.setValue(PlacesMoc.getPlaces());
            return;
        }

        isLoading.setValue(true);
        placeRepository.getPlacesNear(lat, lng, delta, filter, tags).observeForever(result -> {
            isLoading.setValue(false);
            if (result != null) {
                places.setValue(result);
            } else {
                errorMessage.setValue(getApplication().getString(R.string.txt_place_fetch_error));
            }
        });
    }

    public LiveData<Place> getCreatedPlace() {
        return createdPlace;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void clearCreatedPlace() {
        createdPlace.setValue(null);
    }
}