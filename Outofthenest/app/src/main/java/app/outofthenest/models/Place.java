package app.outofthenest.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place implements Serializable {

    // UID
    @SerializedName("id")
    @Expose
    private int id;

    // Name of the place
    @SerializedName("title")
    @Expose
    private String title;

    // Short description of the place
    @SerializedName("description")
    @Expose
    private String description;

    // Type of the place (e.g., restaurant, park)
    @SerializedName("type")
    @Expose
    private String type;

    // Full address of the place
    @SerializedName("address")
    @Expose
    private String address;

    // Date and time created
    @SerializedName("datetime")
    @Expose
    private String datetime;

    // Use when getting places from the API
    @SerializedName("distance")
    @Expose
    private String distance;

    // Status of the place (e.g., open, closed)
    @SerializedName("status")
    @Expose
    private String status;

    // Rating of the place (0.0 to 5.0)
    @SerializedName("rating")
    @Expose
    private float rating;

    // latitude
    @SerializedName("latitude")
    @Expose
    private double latitude;

    // longitude
    @SerializedName("longitude")
    @Expose
    private double longitude;


    @SerializedName("delta")
    @Expose
    private float delta;

    // Tags associated with the place
    @SerializedName("tags")
    @Expose
    private ArrayList<String> tags;

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
        this.delta = delta;
        this.tags = tags;
    }

    // Constructor for creating a new place
    public Place(String title, String description, String type, String address, double latitude, double longitude, ArrayList<String> tags) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;

        // Current date and time
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String datetime = sdf.format(currentDate);
        this.datetime = datetime;
        //Default
        this.distance = "0.0 km";
        this.status = "open";
        this.rating = 0.0f;
        this.id = 0;
        this.delta = 0.0f;
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
