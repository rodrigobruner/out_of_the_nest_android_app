package app.outofthenest.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Event model class
 */
public class Event  implements Serializable {

    String id;

    String title;

    String description;

    Date datetime;

    // should be a PlaceAddress object, but for simplicity using String
    String address;

    ArrayList<String> targetAudience;

    public Event(String id, String title, String description, Date datetime, String address, ArrayList<String> targetAudience) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(ArrayList<String> targetAudience) {
        this.targetAudience = targetAudience;
    }
}
