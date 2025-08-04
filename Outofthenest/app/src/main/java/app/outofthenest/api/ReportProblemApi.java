package app.outofthenest.api;

import app.outofthenest.models.ReportProblem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportProblemApi {
    @POST("reports")
    Call<Void> sendReport(@Body ReportProblem report);
}
