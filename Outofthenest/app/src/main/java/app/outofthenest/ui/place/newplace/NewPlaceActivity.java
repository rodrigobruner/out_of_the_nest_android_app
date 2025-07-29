package app.outofthenest.ui.place.newplace;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.R;

/**
 * Activity for creating a new place.
 * TODO: check if this activity is used in the app, if not, remove it.
 */
public class NewPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, NewPlaceFragment.newInstance())
                    .commitNow();
        }
    }
}