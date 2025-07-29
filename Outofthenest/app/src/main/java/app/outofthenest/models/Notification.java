package app.outofthenest.models;

import java.util.Date;

/**
 * Notification model class
 */
public class Notification {
    private String id;
    private String title;
    private String message;
    private String type; // "EVENT"
    private Date date;
    private boolean read;

    public Notification(String id, String title, String message, String type, Date date, boolean read) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.date = date;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
