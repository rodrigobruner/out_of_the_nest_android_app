package app.outofthenest.models;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    String id;

    String title;

    String description;

    Date datetime;

    String place;

    ArrayList<String> targetAudience;


    public Event(String id, String title, String description, Date datetime, String placeId, ArrayList<String> targetAudience) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.place = placeId;
        this.targetAudience = targetAudience;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ArrayList<String> getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(ArrayList<String> targetAudience) {
        this.targetAudience = targetAudience;
    }
}
