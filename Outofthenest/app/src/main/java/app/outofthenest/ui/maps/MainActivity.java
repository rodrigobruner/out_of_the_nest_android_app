package app.outofthenest.ui.maps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity {

    private static String PLACE_PARAMATER = "place";
    private static final String EVENT_PARAMETER_NAME = "event";
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(PLACE_PARAMATER)) {
            // Navigate to the maps fragment first
            navController.navigate(R.id.navigation_maps);

            Place place = (Place) intent.getSerializableExtra(PLACE_PARAMATER, Place.class);
            if (place != null) {
                // Then set the destination in the ViewModel
                mapViewModel.setDestination(place);
            }
        }

        if(intent.hasExtra(EVENT_PARAMETER_NAME)){
            navController.navigate(R.id.navigation_events);
        }
    }

    private void init(){
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        setupBottomNavigation();
    }

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