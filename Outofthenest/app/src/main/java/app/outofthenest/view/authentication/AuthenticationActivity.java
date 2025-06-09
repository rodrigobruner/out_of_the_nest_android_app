package app.outofthenest.view.authentication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;

import app.outofthenest.R;
import app.outofthenest.adapters.AuthenticationPagerAdapter;
import app.outofthenest.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";
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

        new TabLayoutMediator(
                authenticationBinding.tabLayoutAuthentication,
                authenticationBinding.viewPagerAuthentication,
                (tab, position) -> {
                    if (position == 0) tab.setText(R.string.btn_register);
                    else tab.setText(R.string.btn_login);
                }
        ).attach();

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