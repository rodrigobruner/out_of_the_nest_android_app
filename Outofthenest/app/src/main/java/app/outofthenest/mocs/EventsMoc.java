package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.outofthenest.models.Event;
import app.outofthenest.models.Place;

public class EventsMoc {

    // In EventsMoc.java
    public static ArrayList<Event> getEvents() {

        return new ArrayList<>(Arrays.asList(
                new Event("1", "Baby Playdate", "A fun morning for babies and their parents.", new Date(2024-1900, 6, 15, 10, 0), "123 King St N, Waterloo, ON", new ArrayList<>(Arrays.asList("Babies (0-2 years)", "Parents"))),
                new Event("2", "Kids Art Workshop", "Arts & Crafts for children aged 6-9.", new Date(2024-1900, 6, 16, 14, 0), "456 University Ave W, Waterloo, ON", new ArrayList<>(Arrays.asList("Children (6-9 years)", "Arts & Crafts"))),
                new Event("3", "Teen Sports Day", "Outdoor sports activities for teenagers.", new Date(2024-1900, 6, 17, 9, 30), "789 Columbia St W, Waterloo, ON", new ArrayList<>(Arrays.asList("Teenagers (13-17 years)", "Sports"))),
                new Event("4", "Family Storytelling", "Storytelling session for all ages.", new Date(2024-1900, 6, 18, 11, 0), "101 Erb St E, Waterloo, ON", new ArrayList<>(Arrays.asList("All ages", "Storytelling"))),
                new Event("5", "Seniors Music Hour", "Live music for seniors.", new Date(2024-1900, 6, 19, 15, 0), "202 Regina St S, Waterloo, ON", new ArrayList<>(Arrays.asList("Seniors", "Music"))),
                new Event("6", "Girls Coding Club", "Technology and games for girls.", new Date(2024-1900, 6, 20, 16, 0), "303 Albert St, Waterloo, ON", new ArrayList<>(Arrays.asList("Girls", "Games & Technology"))),
                new Event("7", "Boys Outdoor Adventure", "Outdoor activities for boys.", new Date(2024-1900, 6, 21, 13, 0), "404 Westmount Rd N, Waterloo, ON", new ArrayList<>(Arrays.asList("Boys", "Outdoor Activities"))),
                new Event("8", "Pet Friendly Picnic", "Picnic event where pets are allowed.", new Date(2024-1900, 6, 22, 12, 0), "505 Parkside Dr, Waterloo, ON", new ArrayList<>(Arrays.asList("All ages", "Pets allowed"))),
                new Event("9", "Adult Theater Night", "Theater performance for adults.", new Date(2024-1900, 6, 23, 19, 0), "606 Weber St N, Waterloo, ON", new ArrayList<>(Arrays.asList("Adults", "Theater"))),
                new Event("10", "Inclusive Celebration", "Celebration for all genders and ages.", new Date(2024-1900, 6, 24, 17, 0), "707 Bridgeport Rd E, Waterloo, ON", new ArrayList<>(Arrays.asList("All genders", "All ages", "Celebrations & Parties")))
        ));
    }

    public static Event getEventById(String id) {
        for (Event event : getEvents()) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null; // or throw an exception if preferred
    }


}
