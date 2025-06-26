package app.outofthenest.api.request;

public class SendNotificationRequest {
    private String userId;
    private String eventId;
    private String message;
    private String scheduledTime;

    public SendNotificationRequest(String userId, String eventId, String message, String scheduledTime) {
        this.userId = userId;
        this.eventId = eventId;
        this.message = message;
        this.scheduledTime = scheduledTime;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
