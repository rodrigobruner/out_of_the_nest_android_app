package app.outofthenest.ui.place.review;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import app.outofthenest.R;
import app.outofthenest.databinding.ActivityNewReviewBinding;
import app.outofthenest.models.Place;
import app.outofthenest.models.Review;
import app.outofthenest.ui.authentication.AuthenticationViewModel;
import app.outofthenest.utils.UserUtils;

public class NewPlaceReviewActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private static final String PLACE_PARAMETER = "place";

    ActivityNewReviewBinding binding;
    PlaceReviewViewModel viewModel;

//    private AuthenticationViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
//        Log.i(TAG, "Init");
        setUpViewModels();
        setUpActionBar();
        setUpSaveButton();
        observeViewModel();
    }

    //set up view models
    private void setUpViewModels(){
        viewModel = new ViewModelProvider(this).get(PlaceReviewViewModel.class);
//        authViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
//                .getInstance(getApplication())).get(AuthenticationViewModel.class);
    }


    //set up action bar
    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.txt_event_bar_review_place);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setLogo(R.drawable.ic_add_location);
            actionBar.setDisplayUseLogoEnabled(true);
        }
    }

    //set up save button
    private void setUpSaveButton(){
//        Log.i(TAG, "setUpSaveButton");
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "Save button clicked");
                saveReview();
            }
        });
    }

    //set up save button
    private void saveReview(){
        String title = binding.edtTitle.getText().toString().trim();
        String description = binding.edtDescription.getText().toString().trim();
        int rating = (int) binding.ratingBar.getRating();

        if (title.isEmpty() || description.isEmpty() || rating == 0) {
            Toast.makeText(this, getText(R.string.alert_review_field_validate), Toast.LENGTH_SHORT).show();
            return;
        }

        Place place = (Place) getIntent().getSerializableExtra(PLACE_PARAMETER, Place.class);

        Review review = new Review(
                null,
                title,
                description,
                rating,
                new Date(),
                UserUtils.getUser(getApplicationContext()).getId(),
                Integer.toString(place.getId())
        );

        viewModel.createReview(review);
    }

    // observe Viewmodel for changes
    private void observeViewModel() {
        // observe created review
        viewModel.getCreatedReview().observe(this, new Observer<Review>() {
            @Override
            public void onChanged(Review review) {
                if (review != null) {
                    Toast.makeText(NewPlaceReviewActivity.this, getString(R.string.txt_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        // observe load state
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                showProgressBar(isLoading);
            }
        });

        // observe error
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(NewPlaceReviewActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    viewModel.clearErrorMessage();
                }
            }
        });
    }

    //show or hide progress bar
    private void showProgressBar(boolean show) {
        if (binding != null && binding.progressBar != null) {
            binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            binding.btnSave.setEnabled(!show);
            binding.btnSave.setText(show ?
                                        getString(R.string.txt_saving) :
                                        getString(R.string.btn_review));

        }
    }
}