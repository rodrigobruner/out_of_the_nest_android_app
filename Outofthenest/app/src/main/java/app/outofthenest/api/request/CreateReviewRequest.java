package app.outofthenest.api.request;

/**
 * This class is used to create a Review Request
 */
public class CreateReviewRequest {
    private String title;
    private String description;
    private int rating;
    private String userId;
    private int placeId;

    public CreateReviewRequest(String title, String description, int rating, String userId, int placeId) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.userId = userId;
        this.placeId = placeId;
    }

    // Getters and setters
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
