package app.outofthenest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import app.outofthenest.ui.newplace.NewPlaceFragment;

public class PlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, NewPlaceFragment.newInstance())
                .commitNow();
        }
    }
}