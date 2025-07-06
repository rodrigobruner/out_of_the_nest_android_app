package app.outofthenest.api;

import java.util.ArrayList;
import java.util.List;

import app.outofthenest.models.Event;
import retrofit2.Call;
import retrofit2.http.*;

public interface EventsApi {
    @POST("events/createEvent")
    Call<Event> createEvent(@Body Event event);

    @GET("events/searchEvents")
    Call<List<Event>> searchEvents(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("radius") double radius,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("targetAudience") ArrayList<String> targetAudience
    );

    @GET("events/getEvent")
    Call<Event> getEvent(@Query("id") String id);

    @GET("events/getEventsByDate")
    Call<List<Event>> getEventsByDate(@Query("date") String date);
}