package app.outofthenest.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportProblem {
    String userId;
    String objectId;
    String reportType;
    String report;
    String datetime;

    public ReportProblem(String userId, String objectId, String reportType, String report) {
        this.userId = userId;
        this.objectId = objectId;
        this.reportType = reportType;
        this.report = report;

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
        this.datetime = sdf.format(date);
    }

    // Getters e setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getObjectId() { return objectId; }
    public void setObjectId(String objectId) { this.objectId = objectId; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }

    public String getDatetime() { return datetime; }
    public void setDatetime(String datetime) { this.datetime = datetime; }
}