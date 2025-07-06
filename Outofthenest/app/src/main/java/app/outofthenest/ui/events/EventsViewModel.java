package app.outofthenest.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import app.outofthenest.models.Event;
import app.outofthenest.repository.EventsRepository;

public class EventsViewModel extends ViewModel {
    private final EventsRepository repository = new EventsRepository();
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public void searchEvents(double lat, double lng, double radius, String startDate, String endDate, ArrayList<String> targetAudience) {
        repository.searchEvents(lat, lng, radius, startDate, endDate, targetAudience)
                .observeForever(events::setValue);
    }
}