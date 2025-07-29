package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.EventsApi;
import app.outofthenest.models.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Communicates with the Events API to manage events.
 */
public class EventsRepository {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private EventsApi eventsApi;

    public EventsRepository() {
        eventsApi = ApiService.getRetrofit().create(EventsApi.class);
    }

    // create a new event
    public LiveData<Event> createEvent(Event event) {
        MutableLiveData<Event> data = new MutableLiveData<>();
        eventsApi.createEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    // search events by location, date and target audience
    public LiveData<List<Event>> searchEvents(double lat, double lng, double radius, String startDate, String endDate, ArrayList<String> targetAudience) {
        MutableLiveData<List<Event>> data = new MutableLiveData<>();
        eventsApi.searchEvents(startDate, endDate, targetAudience).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    // get a event by ID
    public LiveData<Event> getEventById(String eventId) {
        MutableLiveData<Event> data = new MutableLiveData<>();
        eventsApi.getEvent(eventId).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}