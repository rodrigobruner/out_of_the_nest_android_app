package app.outofthenest.ui.authentication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.tabs.TabLayoutMediator;

import app.outofthenest.R;
import app.outofthenest.adapters.AuthenticationPagerAdapter;
import app.outofthenest.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    ActivityAuthenticationBinding authenticationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(authenticationBinding.getRoot());
        EdgeToEdge.enable(this);

        init();
    }

    private void init() {
        setupTabs();
    }

    private void setupTabs(){
        AuthenticationPagerAdapter adapter = new AuthenticationPagerAdapter(this, this);
        authenticationBinding.viewPagerAuthentication.setAdapter(adapter);

        //Set title for the activity
        new TabLayoutMediator(
                authenticationBinding.tabLayoutAuthentication,
                authenticationBinding.viewPagerAuthentication,
                (tab, position) -> {
                    if (position == 0) tab.setText(R.string.btn_register);
                    else tab.setText(R.string.btn_login);
                }
        ).attach();

        // Set the initial tab
        int action = getIntent().getIntExtra("action", -1);

        if (action == R.string.btn_get_started) {
            authenticationBinding.tabLayoutAuthentication.selectTab(
                    authenticationBinding.tabLayoutAuthentication.getTabAt(0));
        } else if (action == R.string.btn_login) {
            authenticationBinding.tabLayoutAuthentication.selectTab(
                    authenticationBinding.tabLayoutAuthentication.getTabAt(1));
        }
    }
}