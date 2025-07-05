package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Arrays;

import app.outofthenest.models.Place;

public class PlacesMoc {
    public static ArrayList<Place> getPlaces(){
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Washroom");
        tags.add("Parking");
        tags.add("Pet friendly");

        return new ArrayList<>(Arrays.asList(
                new Place(1, "Waterloo Park", "Lorem ipsum dolor sit amet...", "Park", "50 Young St W, Waterloo, ON N2L 2Z4", "5 min", "1.2 km", "Open", 4, 43.466800, -80.525000, 0.0f, tags),
                new Place(2, "Waterloo Public Library", "Lorem ipsum dolor sit amet...", "Library", "35 Albert St, Waterloo, ON N2L 5E2", "6 min", "1.4 km", "Close soon", 2, 43.468200, -80.520500, 0.0f, tags),
                new Place(3, "Playground - Alexandra Park", "Lorem ipsum dolor sit amet...", "Playground", "75 William St N, Waterloo, ON N2J 3G4", "7 min", "1.6 km", "Closed", 5, 43.470000, -80.518000, 0.0f, tags),
                new Place(4, "Galaxy Cinemas Waterloo", "Cinema", "Indoor activity", "550 King St N, Waterloo, ON N2L 5W6", "8 min", "1.8 km", "Open", 5, 43.472000, -80.515500, 0.0f, tags),
                new Place(5, "Laurel Creek Conservation Area", "Lorem ipsum dolor sit amet...", "Park", "625 Westmount Rd N, Waterloo, ON N2V 2C9", "10 min", "2.1 km", "Open", 4, 43.475000, -80.530000, 0.0f, tags),
                new Place(6, "Perimeter Institute", "Lorem ipsum dolor sit amet...", "Institute", "31 Caroline St N, Waterloo, ON N2L 2Y5", "9 min", "1.9 km", "Open", 5, 43.464500, -80.522000, 0.0f, tags),
                new Place(7, "Waterloo Memorial Recreation Complex", "Lorem ipsum dolor sit amet...", "Recreation", "101 Father David Bauer Dr, Waterloo, ON N2L 6L1", "11 min", "2.3 km", "Open", 4, 43.463000, -80.530500, 0.0f, tags),
                new Place(8, "Canadian Clay and Glass Gallery", "Lorem ipsum dolor sit amet...", "Gallery", "25 Caroline St N, Waterloo, ON N2L 2Y5", "7 min", "1.5 km", "Open", 3, 43.465200, -80.521000, 0.0f, tags),
                new Place(9, "Waterloo City Hall", "Lorem ipsum dolor sit amet...", "Government", "100 Regina St S, Waterloo, ON N2J 4A8", "6 min", "1.3 km", "Open", 4, 43.466000, -80.520000, 0.0f, tags),
                new Place(10, "Wilfrid Laurier University", "Lorem ipsum dolor sit amet...", "University", "75 University Ave W, Waterloo, ON N2L 3C5", "8 min", "1.7 km", "Open", 5, 43.473500, -80.528000, 0.0f, tags)
        ));
    }
}
