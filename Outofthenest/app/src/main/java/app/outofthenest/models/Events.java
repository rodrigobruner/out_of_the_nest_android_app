package app.outofthenest.models;

import java.util.Date;

public class Events {

    String id;

    String title;

    String description;

    Date datetime;


    public Events(String id, String title, String description, Date datetime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
