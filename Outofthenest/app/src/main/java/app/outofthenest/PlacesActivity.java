package app.outofthenest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayoutMediator;

import app.outofthenest.adapters.PlacePagerAdapter;
import app.outofthenest.databinding.ActivityAuthenticationBinding;
import app.outofthenest.databinding.ActivityMainBinding;
import app.outofthenest.databinding.ActivityPlacesBinding;
import app.outofthenest.models.Place;
import app.outofthenest.ui.newplace.NewPlaceFragment;

public class PlacesActivity extends AppCompatActivity {

    private ActivityPlacesBinding placeBinding;

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeBinding = ActivityPlacesBinding.inflate(getLayoutInflater());
        setContentView(placeBinding.getRoot());
        init();
    }

    private void init() {
        setUpPlaceTitle();
        setupTabs();
    }

    private void setUpPlaceTitle() {
        place = getIntent().getSerializableExtra("place", Place.class);
    }

    private void setupTabs(){
        PlacePagerAdapter adapter = new PlacePagerAdapter(this, this, place);
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