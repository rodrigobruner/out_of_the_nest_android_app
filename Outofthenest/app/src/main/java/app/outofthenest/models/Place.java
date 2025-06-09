package app.outofthenest.models;

import java.util.ArrayList;

public class Place {
    private int id;
    private String title;
    private String description;
    private String type;
    private String address;
    private String time;
    private String distance;
    private String status;
    private float rating;
    private double latitude;
    private double longitude;
    private float delta;
    private ArrayList<String> tags;

    public Place(String title, String address, String time, String distance, String status, float rating, double latitude, double longitude, float delta) {
        this.title = title;
        this.address = address;
        this.time = time;
        this.distance = distance;
        this.status = status;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.delta = delta;
    }

    public Place(int id, String title, String description, String type, String address, String time, String distance, String status, float rating, double latitude, double longitude, float delta, ArrayList<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.address = address;
        this.time = time;
        this.distance = distance;
        this.status = status;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.delta = delta;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
