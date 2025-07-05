package app.outofthenest.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.outofthenest.models.Place;

public class MapViewModel extends ViewModel {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private final MutableLiveData<Place> destination = new MutableLiveData<>();

    public void setDestination(Place place) {
        destination.setValue(place);
    }

    public LiveData<Place> getDestination() {
        return destination;
    }

    public void clearDestination() {
        destination.setValue(null);
    }
}