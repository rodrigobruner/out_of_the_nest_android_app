package app.outofthenest.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.R;
import app.outofthenest.ui.newplace.NewPlaceFragment;

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