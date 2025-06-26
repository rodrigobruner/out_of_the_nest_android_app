package app.outofthenest.models;

import java.util.List;

public class PlaceFilter {
    private List<String> types;
    private Double minRating;
    private Double maxDistance;
    private List<String> tags;

    public PlaceFilter(List<String> types, Double minRating, Double maxDistance, List<String> tags) {
        this.types = types;
        this.minRating = minRating;
        this.maxDistance = maxDistance;
        this.tags = tags;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
