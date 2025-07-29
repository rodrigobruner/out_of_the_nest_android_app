package app.outofthenest.mocs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.outofthenest.models.Place;

/**
 * This class provides mock data for places.
 */
public class PlacesMoc {
    public static ArrayList<Place> getPlaces(){
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Washroom");
        tags.add("Parking");
        tags.add("Pet friendly");

        return new ArrayList<>(Arrays.asList(
                new Place(1, "Waterloo Park", "Lorem ipsum dolor sit amet...", "Park", "50 Young St W, Waterloo, ON N2L 2Z4", new Date(), "1.2 km", "Open", 4, 43.4642, -80.5283, 0.0f, tags),
                new Place(2, "Waterloo Public Library", "Lorem ipsum dolor sit amet...", "Library", "35 Albert St, Waterloo, ON N2L 5E2", new Date(), "0.1 km", "Close soon", 2, 43.4706, -80.5256, 0.0f, tags),
                new Place(3, "Playground - Alexandra Park", "Lorem ipsum dolor sit amet...", "Playground", "75 William St N, Waterloo, ON N2J 3G4", new Date(), "1.6 km", "Closed", 5, 43.4669, -80.5132, 0.0f, tags),
                new Place(4, "Galaxy Cinemas Waterloo", "Lorem ipsum dolor sit amet...", "Indoor activity", "550 King St N, Waterloo, ON N2L 5W6", new Date(), "1.8 km", "Open", 5, 43.4890, -80.5272, 0.0f, tags),
                new Place(5, "Laurel Creek Conservation Area", "Lorem ipsum dolor sit amet...", "Park", "625 Westmount Rd N, Waterloo, ON N2V 2C9", new Date(), "2.1 km", "Open", 4, 43.4942, -80.5581, 0.0f, tags),
                new Place(6, "Perimeter Institute", "Lorem ipsum dolor sit amet...", "Institute", "31 Caroline St N, Waterloo, ON N2L 2Y5", new Date(), "1.9 km", "Open", 5, 43.4647, -80.5226, 0.0f, tags),
                new Place(7, "Waterloo Memorial Recreation Complex", "Lorem ipsum dolor sit amet...", "Recreation", "101 Father David Bauer Dr, Waterloo, ON N2L 6L1", new Date(), "2.3 km", "Open", 4, 43.4572, -80.5351, 0.0f, tags),
                new Place(8, "Canadian Clay and Glass Gallery", "Lorem ipsum dolor sit amet...", "Gallery", "25 Caroline St N, Waterloo, ON N2L 2Y5", new Date(), "1.5 km", "Open", 3, 43.4649, -80.5220, 0.0f, tags),
                new Place(9, "Waterloo City Hall", "Lorem ipsum dolor sit amet...", "Government", "100 Regina St S, Waterloo, ON N2J 4A8", new Date(), "1.3 km", "Open", 4, 43.4640, -80.5204, 0.0f, tags),
                new Place(10, "Wilfrid Laurier University", "Lorem ipsum dolor sit amet...", "University", "75 University Ave W, Waterloo, ON N2L 3C5", new Date(), "1.7 km", "Open", 5, 43.4732, -80.5263, 0.0f, tags)
        ));
    }
}
