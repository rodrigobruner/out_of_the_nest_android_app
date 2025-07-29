package app.outofthenest.api;

import java.util.ArrayList;
import java.util.List;

import app.outofthenest.models.Event;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Calls to the events API.
 */
public interface EventsApi {

    // create an event on the server
    @POST("events/createEvent")
    Call<Event> createEvent(@Body Event event);

    // search events based on date range and target audience
    // Mo request me to remove lat, lng, radius
    @GET("events/searchEvents")
    Call<List<Event>> searchEvents(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("targetAudience") ArrayList<String> targetAudience
    );

    // get all events for a user
    @GET("events/getEvent")
    Call<Event> getEvent(@Query("id") String id);
}