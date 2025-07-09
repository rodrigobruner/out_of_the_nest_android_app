package app.outofthenest.ui.place.review;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.R;
import app.outofthenest.databinding.ActivityNewReviewBinding;
import app.outofthenest.databinding.ActivityPlaceBinding;

public class NewReviewActivity extends AppCompatActivity {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    ActivityNewReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_new_review);
        Log.i(TAG, "Created");
        init();
    }

    private void init(){
        Log.i(TAG, "Init");
        setUpActionBar();
        setUpSaveButton();
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.txt_event_bar_review_place);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.drawable.ic_add_location);
            actionBar.setDisplayUseLogoEnabled(true);
        }
    }

    private void setUpSaveButton(){
        Log.i(TAG, "setUpSaveButton");
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked");
            }
        });
    }

    private void saveReview(){

    }
}


