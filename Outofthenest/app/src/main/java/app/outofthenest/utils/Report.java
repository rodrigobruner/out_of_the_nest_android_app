package app.outofthenest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import app.outofthenest.R;
import app.outofthenest.api.ApiService;
import app.outofthenest.api.PlaceApi;
import app.outofthenest.api.ReportProblemApi;
import app.outofthenest.models.ReportProblem;
import app.outofthenest.models.User;
import retrofit2.Call;
import retrofit2.Callback;

public class Report {

    public final static String TYPE_EVENT = "Event";
    public final static String TYPE_PLACE = "Place";
    public final static String TYPE_REVIEW = "Review";

    public static void Problem(Context context, String type, String id) {
        User user = UserUtils.getUser(context);

        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(5);
        editText.setMinLines(5);
        editText.setHint(context.getString(R.string.hint_report_problem));


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_report_problem))
                .setView(editText)
                .setPositiveButton(context.getString(R.string.btn_report), (dialog, which) -> {
                    String reportText = editText.getText().toString().trim();
                    if (!reportText.isEmpty()) {

                        Log.i("Report_X", "Report submitted for " + type + " (ID: " + id + "): " + reportText);
                        Report.sendReport(context, type, id, user.getId(), reportText);


                        dialog.dismiss();
                    } else {
                        Toast.makeText(context,
                                context.getString(R.string.error_empty_report),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(context.getString(R.string.btn_cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public static void sendReport(Context context, String type, String objID, String userID, String reportText) {

        ReportProblem report = new ReportProblem(userID, objID, type, reportText);

        ReportProblemApi reportApi = ApiService.getRetrofit().create(ReportProblemApi.class);
        Call<Void> call = reportApi.sendReport(report);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context,
                            context.getString(R.string.txt_report_sent_admin),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.error_send_report), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error_send_report), Toast.LENGTH_SHORT).show();
            }
        });
    }
}