package app.outofthenest.models;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("rating")
    @Expose
    private float rating;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("delta")
    @Expose
    private float delta;

    @SerializedName("tags")
    @Expose
    private ArrayList<String> tags;

    public Place(String title, String address, String datetime, String distance, String status, float rating, double latitude, double longitude, float delta) {
        this.title = title;
        this.address = address;
        this.datetime = datetime;
        this.distance = distance;
        this.status = status;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.delta = delta;
    }

    public Place(int id, String title, String description, String type, String address, String datetime, String distance, String status, float rating, double latitude, double longitude, float delta, ArrayList<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.address = address;
        this.datetime = datetime;
        this.distance = distance;
        this.status = status;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
