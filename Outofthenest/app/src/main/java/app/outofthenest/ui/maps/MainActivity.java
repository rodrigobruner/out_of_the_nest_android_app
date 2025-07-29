package app.outofthenest.ui.maps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import app.outofthenest.BuildConfig;
import app.outofthenest.R;
import app.outofthenest.databinding.ActivityMainBinding;
import app.outofthenest.models.Place;

/**
 * Main screen of the application after user authentication.
 */
public class MainActivity extends AppCompatActivity {

    private static final String PLACE_PARAMATER = "place";
    private static final String EVENT_PARAMETER = "event";
    private static final String PROFILE_PARAMETER = "profile";

    private ActivityMainBinding mainBinding;
    private MapViewModel mapViewModel;
    private NavController navController; // Add NavController variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        init();
    }

    // when receives a new intent.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // go to places activity
        if (intent.hasExtra(PLACE_PARAMATER)) {
            // Navigate to the maps fragment first
            navController.navigate(R.id.navigation_maps);

            Place place = (Place) intent.getSerializableExtra(PLACE_PARAMATER, Place.class);
            if (place != null) {
                // Then set the destination in the ViewModel
                mapViewModel.setDestination(place);
            }
        }

        // go to events activity
        if(intent.hasExtra(EVENT_PARAMETER)){
            navController.navigate(R.id.navigation_events);
        }

        // go to profile activity
        if(intent.hasExtra(PROFILE_PARAMETER)){
            navController.navigate(R.id.navigation_profile);
        }
    }

    private void init(){
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        setupBottomNavigation();
    }

    // Set up the NavController.
    private void setupBottomNavigation(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_maps, R.id.navigation_events, R.id.navigation_notifications, R.id.navigation_profile)
                .build();
        // Initialize NavController here
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(mainBinding.navView, navController);
    }
}