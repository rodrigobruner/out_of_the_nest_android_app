package app.outofthenest.ui.newplace;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import app.outofthenest.R;
import app.outofthenest.models.Place;
import app.outofthenest.repository.PlaceRepository;

public class NewPlaceViewModel extends AndroidViewModel {

    private static final String TAG = "NewPlaceViewModel";
    private PlaceRepository placeRepository;
    private MutableLiveData<Place> createdPlace;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;

    public NewPlaceViewModel(@NonNull Application application) {
        super(application);
        placeRepository = new PlaceRepository();
        createdPlace = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    public void createPlace(Place place) {
        Log.i(TAG, "Creating place: " + place.getTitle());
        isLoading.setValue(true);
        errorMessage.setValue(null);
        placeRepository.createPlace(place).observeForever(new Observer<Place>() {
            @Override
            public void onChanged(Place result) {
                isLoading.setValue(false);
                if (result != null) {
                    createdPlace.setValue(result);
                    Log.i(TAG, "Created: " + place.getTitle());
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.txt_place_creation_error));
                    Log.i(TAG, "Error: " + place.getTitle());
                }
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