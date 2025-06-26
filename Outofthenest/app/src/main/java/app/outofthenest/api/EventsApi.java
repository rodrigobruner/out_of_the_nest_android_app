package app.outofthenest.api;

import java.util.List;

import app.outofthenest.models.Events;
import retrofit2.Call;
import retrofit2.http.*;

public interface EventsApi {
    @POST("api/events/createEvent")
    Call<Events> createEvent(@Body Events event);

    @GET("api/events/searchEvents")
    Call<List<Events>> searchEvents(
            @Query("lat") double lat,
            @Query("lng") double lng,
            @Query("radius") double radius,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    @GET("api/events/getEvent")
    Call<Events> getEvent(@Query("id") String id);

    @GET("api/events/getEventsByDate")
    Call<List<Events>> getEventsByDate(@Query("date") String date);
}