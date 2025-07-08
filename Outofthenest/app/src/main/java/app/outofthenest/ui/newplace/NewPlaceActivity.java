package app.outofthenest.ui.newplace;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.R;

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