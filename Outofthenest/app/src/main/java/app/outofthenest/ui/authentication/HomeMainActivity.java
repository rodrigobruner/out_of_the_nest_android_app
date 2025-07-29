package app.outofthenest.ui.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.ActivityHomeMainBinding;
import app.outofthenest.utils.UserUtils;

/**
 * Is the entry point of the application.
 */
public class HomeMainActivity extends AppCompatActivity {

    private static final String TAG = "HomeMainActivity";
    private ActivityHomeMainBinding homeBinding;
    private AuthenticationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeMainBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        init();
    }


    private void init() {
        checkUserLoggedIn();
        setupButtons();
    }

    // Check if the user is logged in or not
    private void checkUserLoggedIn() {
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(AuthenticationViewModel.class);

        viewModel.getUserLoggedMLData().observe(this, isLogged -> {
            if (isLogged || UserUtils.hasUser(getBaseContext())) {
                Intent intent = new Intent(HomeMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // setup buttons for Get Started and Login
    private void setupButtons(){
        homeBinding.btGetStarted.setOnClickListener(
                v -> {
                    Intent intent = new Intent(HomeMainActivity.this, AuthenticationActivity.class);
                    intent.putExtra("action", R.string.btn_get_started);
                    startActivity(intent);
                }
        );

        homeBinding.btLogin.setOnClickListener(
                v -> {
                    Intent intent = new Intent(HomeMainActivity.this, AuthenticationActivity.class);
                    intent.putExtra("action", R.string.btn_login);
                    startActivity(intent);
                }
        );
    }

    // refresh token when app comes back from background
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel.getUserLoggedMLData().getValue() == Boolean.TRUE) {
            viewModel.refreshUserToken();
        }
    }
}