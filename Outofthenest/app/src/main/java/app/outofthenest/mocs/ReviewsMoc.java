package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.outofthenest.models.Review;

/**
 * This class provides mock data for reviews.
 */
public class ReviewsMoc {
    public static List<Review> getReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("1", "Amazing!", "Absolutely loved this place. Will come back for sure.", 5, new Date(), "user1", "1"));
        reviews.add(new Review("2", "Good experience", "Nice atmosphere and friendly staff.", 4, new Date(), "user2", "2"));
        reviews.add(new Review("3", "Not bad", "It was okay. Lorem ipsum dolor sit amet.", 3, new Date(), "user3", "3"));
        reviews.add(new Review("4", "Could be better", "Service was slow and the food was cold.", 2, new Date(), "user4", "4"));
        reviews.add(new Review("5", "Disappointing", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque euismod.", 1, new Date(), "user5", "5"));
        reviews.add(new Review("6", "Fantastic place!", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 5, new Date(), "user6", "6"));
        reviews.add(new Review("7", "Just okay", "Nothing special. Lorem ipsum dolor sit amet.", 3, new Date(), "user7", "7"));
        reviews.add(new Review("8", "Loved it!", "Great service, delicious food, and a wonderful ambiance. Highly recommended for families and friends. Lorem ipsum dolor sit amet, consectetur.", 5, new Date(), "user8", "8"));
        return reviews;
    }
}
