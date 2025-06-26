package app.outofthenest.models;

import java.util.Date;

public class Notification {
    private String id;
    private String userId;
    private String message;
    private String type; // "EVENT", "REVIEW", "GENERAL"
    private Date scheduledTime;
    private boolean sent;


    public Notification(String id, String userId, String message, String type, Date scheduledTime, boolean sent) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.scheduledTime = scheduledTime;
        this.sent = sent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
