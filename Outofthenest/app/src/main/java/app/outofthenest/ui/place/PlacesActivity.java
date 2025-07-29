package app.outofthenest.ui.place;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;

import app.outofthenest.R;
import app.outofthenest.adapters.PlaceTabAdapter;
import app.outofthenest.databinding.ActivityPlaceBinding;
import app.outofthenest.models.Place;

/**
 * Display the details of a place and reviews
 */
public class PlacesActivity extends AppCompatActivity {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private ActivityPlaceBinding placeBinding;

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeBinding = ActivityPlaceBinding.inflate(getLayoutInflater());
        setContentView(placeBinding.getRoot());
        init();
    }

    private void init() {
        setUpPlaceTitle();
        setupTabs();
    }

    //set up the action bar
    private void setUpPlaceTitle() {
        place = getIntent().getSerializableExtra("place", Place.class);
    }

    // set up the tabs
    private void setupTabs(){
        PlaceTabAdapter adapter = new PlaceTabAdapter(this, this, place);
        placeBinding.viewPagerPlace.setAdapter(adapter);

        new TabLayoutMediator(
                placeBinding.tabLayoutPlace,
                placeBinding.viewPagerPlace,
                (tab, position) -> {
                    if (position == 0) tab.setText(R.string.txt_tab_detail);
                    else tab.setText(R.string.txt_tab_comments);
                }
        ).attach();

        int action = getIntent().getIntExtra("action", -1);

        if (action == R.string.btn_get_started) {
            placeBinding.tabLayoutPlace.selectTab(
                    placeBinding.tabLayoutPlace.getTabAt(0));
        } else if (action == R.string.btn_login) {
            placeBinding.tabLayoutPlace.selectTab(
                    placeBinding.tabLayoutPlace.getTabAt(1));
        }
    }
}