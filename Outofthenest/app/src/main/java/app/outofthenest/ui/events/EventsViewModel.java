package app.outofthenest.ui.events;

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
import app.outofthenest.mocs.EventsMoc;
import app.outofthenest.mocs.NotificationsMoc;
import app.outofthenest.models.Event;
import app.outofthenest.repository.EventsRepository;
import app.outofthenest.utils.Constants;

/**
 * ViewModel for events.
 */
public class EventsViewModel extends AndroidViewModel {

    String TAG = getClass().getSimpleName();
    private EventsRepository repository;
    private MutableLiveData<Event> createdEvent;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public EventsViewModel(@NonNull Application application) {
        super(application);
        repository = new EventsRepository();
        createdEvent = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
    }

    // create event
    public void createEvent(Event event) {
        Log.i(TAG, "Creating event: " + event.getTitle());
        isLoading.setValue(true);
        errorMessage.setValue(null);
        repository.createEvent(event).observeForever(new Observer<Event>() {
            @Override
            public void onChanged(Event result) {
                isLoading.setValue(false);
                if (result != null) {
                    createdEvent.setValue(result);
                } else {
                    errorMessage.setValue(getApplication().getString(R.string.txt_event_creation_error));
                }
            }
        });
    }

    // search events by positionm, peiriod and target audience
    public void searchEvents(double lat, double lng, double radius, String startDate, String endDate, ArrayList<String> targetAudience) {
        isLoading.setValue(true);

        // Moc mode for testing
        if (Constants.USE_MOC_MODE){
            events.setValue(EventsMoc.getEvents());
            isLoading.setValue(false);
            return;
        }

        repository.searchEvents(lat, lng, radius, startDate, endDate, targetAudience)
                .observeForever(result -> {
                    isLoading.setValue(false);
                    if (result != null) {
                        events.setValue(result);
                    } else {
                        errorMessage.setValue(getApplication().getString(R.string.txt_event_search_error));
                    }
                });
    }

    // get event by id
    public void getEventById(String id) {
        isLoading.setValue(true);

        if (Constants.USE_MOC_MODE){
            Event event = EventsMoc.getEventById(id);
            createdEvent.setValue(event);
            isLoading.setValue(false);
            return;
        }

        repository.getEventById(id).observeForever(result -> {
            isLoading.setValue(false);
            if (result != null) {
                createdEvent.setValue(result);
            } else {
                errorMessage.setValue(getApplication().getString(R.string.txt_event_search_error));
            }
        });
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<Event> getCreatedEvent() {
        return createdEvent;
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

    public void clearCreatedEvent() {
        createdEvent.setValue(null);
    }
}