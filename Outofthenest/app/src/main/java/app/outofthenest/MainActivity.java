package app.outofthenest;

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

import app.outofthenest.databinding.ActivityMainBinding;
import app.outofthenest.models.Place;
import app.outofthenest.ui.maps.MapViewModel;

public class MainActivity extends AppCompatActivity {

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
        if (intent.hasExtra("destination")) {
            // Navigate to the maps fragment first
            navController.navigate(R.id.navigation_maps);

            Place place = (Place) intent.getSerializableExtra("destination", Place.class);
            if (place != null) {
                // Then set the destination in the ViewModel
                mapViewModel.setDestination(place);
            }
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