package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.outofthenest.models.Event;
import app.outofthenest.models.Place;

public class EventsMoc {

// In EventsMoc.java
    public static ArrayList<Event> getEvents() {
        Place place1 = new Place(1, "Community Center", "A local community center for events.", "community", "123 Main St", "2024-07-01 10:00:00", "", "open", 4.5f, -23.0, -46.0, 0.0f, new ArrayList<>(Arrays.asList("family", "kids")));
        Place place2 = new Place(2, "Art Studio", "A creative space for workshops.", "studio", "456 Art Ave", "2024-07-01 14:00:00", "", "open", 4.2f, -23.1, -46.1, 0.0f, new ArrayList<>(Arrays.asList("art", "children")));
        Place place3 = new Place(3, "Sports Field", "Outdoor sports and activities.", "field", "789 Field Rd", "2024-07-01 09:30:00", "", "open", 4.0f, -23.2, -46.2, 0.0f, new ArrayList<>(Arrays.asList("sports", "teens")));
        Place place4 = new Place(4, "Library", "Public library with storytelling sessions.", "library", "101 Book Ln", "2024-07-01 11:00:00", "", "open", 4.8f, -23.3, -46.3, 0.0f, new ArrayList<>(Arrays.asList("books", "all ages")));
        Place place5 = new Place(5, "Senior Club", "Club for senior activities.", "club", "202 Golden Years Blvd", "2024-07-01 15:00:00", "", "open", 4.7f, -23.4, -46.4, 0.0f, new ArrayList<>(Arrays.asList("seniors", "music")));
        Place place6 = new Place(6, "Tech Hub", "Technology and coding events.", "hub", "303 Code St", "2024-07-01 16:00:00", "", "open", 4.6f, -23.5, -46.5, 0.0f, new ArrayList<>(Arrays.asList("tech", "girls")));
        Place place7 = new Place(7, "Adventure Park", "Outdoor adventure park.", "park", "404 Adventure Pkwy", "2024-07-01 13:00:00", "", "open", 4.3f, -23.6, -46.6, 0.0f, new ArrayList<>(Arrays.asList("adventure", "boys")));
        Place place8 = new Place(8, "City Park", "Pet-friendly city park.", "park", "505 Picnic Dr", "2024-07-01 12:00:00", "", "open", 4.1f, -23.7, -46.7, 0.0f, new ArrayList<>(Arrays.asList("pets", "all ages")));
        Place place9 = new Place(9, "Theater Hall", "Venue for theater performances.", "theater", "606 Stage Ave", "2024-07-01 19:00:00", "", "open", 4.9f, -23.8, -46.8, 0.0f, new ArrayList<>(Arrays.asList("theater", "adults")));
        Place place10 = new Place(10, "Event Plaza", "Plaza for celebrations and parties.", "plaza", "707 Celebration Sq", "2024-07-01 17:00:00", "", "open", 4.4f, -23.9, -46.9, 0.0f, new ArrayList<>(Arrays.asList("celebration", "all")));

        return new ArrayList<>(Arrays.asList(
                new Event("1", "Baby Playdate", "A fun morning for babies and their parents.", new Date(2024-1900, 6, 15, 10, 0), place1, new ArrayList<>(Arrays.asList("Babies (0-2 years)", "Parents"))),
                new Event("2", "Kids Art Workshop", "Arts & Crafts for children aged 6-9.", new Date(2024-1900, 6, 16, 14, 0), place2, new ArrayList<>(Arrays.asList("Children (6-9 years)", "Arts & Crafts"))),
                new Event("3", "Teen Sports Day", "Outdoor sports activities for teenagers.", new Date(2024-1900, 6, 17, 9, 30), place3, new ArrayList<>(Arrays.asList("Teenagers (13-17 years)", "Sports"))),
                new Event("4", "Family Storytelling", "Storytelling session for all ages.", new Date(2024-1900, 6, 18, 11, 0), place4, new ArrayList<>(Arrays.asList("All ages", "Storytelling"))),
                new Event("5", "Seniors Music Hour", "Live music for seniors.", new Date(2024-1900, 6, 19, 15, 0), place5, new ArrayList<>(Arrays.asList("Seniors", "Music"))),
                new Event("6", "Girls Coding Club", "Technology and games for girls.", new Date(2024-1900, 6, 20, 16, 0), place6, new ArrayList<>(Arrays.asList("Girls", "Games & Technology"))),
                new Event("7", "Boys Outdoor Adventure", "Outdoor activities for boys.", new Date(2024-1900, 6, 21, 13, 0), place7, new ArrayList<>(Arrays.asList("Boys", "Outdoor Activities"))),
                new Event("8", "Pet Friendly Picnic", "Picnic event where pets are allowed.", new Date(2024-1900, 6, 22, 12, 0), place8, new ArrayList<>(Arrays.asList("All ages", "Pets allowed"))),
                new Event("9", "Adult Theater Night", "Theater performance for adults.", new Date(2024-1900, 6, 23, 19, 0), place9, new ArrayList<>(Arrays.asList("Adults", "Theater"))),
                new Event("10", "Inclusive Celebration", "Celebration for all genders and ages.", new Date(2024-1900, 6, 24, 17, 0), place10, new ArrayList<>(Arrays.asList("All genders", "All ages", "Celebrations & Parties")))
        ));
    }
}
