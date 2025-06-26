package app.outofthenest.models;

import java.util.Date;

public class Reviews {
    private String id;
    private String title;
    private String description;
    private int rating;
    private Date datetime;
    private String userId;

    public Reviews(String id, String title, String description, int rating, Date datetime, String userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.datetime = datetime;
        this.userId = userId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}