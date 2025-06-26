package app.outofthenest.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.EventsApi;
import app.outofthenest.models.Events;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRepository {
    private EventsApi eventsApi;

    public EventsRepository() {
        eventsApi = ApiService.getRetrofit().create(EventsApi.class);
    }

    public LiveData<Events> createEvent(Events event) {
        MutableLiveData<Events> data = new MutableLiveData<>();
        eventsApi.createEvent(event).enqueue(new Callback<Events>() {
            @Override
            public void onResponse(Call<Events> call, Response<Events> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Events> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Events>> searchEvents(double lat, double lng, double radius, String startDate, String endDate) {
        MutableLiveData<List<Events>> data = new MutableLiveData<>();
        eventsApi.searchEvents(lat, lng, radius, startDate, endDate).enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Events>> getEventsByDate(String date) {
        MutableLiveData<List<Events>> data = new MutableLiveData<>();
        eventsApi.getEventsByDate(date).enqueue(new Callback<List<Events>>() {
            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}