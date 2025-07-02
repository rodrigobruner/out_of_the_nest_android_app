package app.outofthenest.ui.maps;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import app.outofthenest.models.Place;
import app.outofthenest.repository.PlaceRepository;

public class PlaceViewModel extends AndroidViewModel {
    private static final String TAG = "PlaceViewModel";
    private PlaceRepository placeRepository;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        if (placeRepository == null) {
            placeRepository = new PlaceRepository(getApplication());
        }
    }

    public ArrayList<Place> getPlacesNear(String filter, double lat, double lng, ArrayList<String> tags) {
        return (ArrayList<Place>) placeRepository.getPlacesNear(lat, lng, 0.1, filter, tags).getValue();
    }
}
