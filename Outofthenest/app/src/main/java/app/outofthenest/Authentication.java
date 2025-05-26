package app.outofthenest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;

import app.outofthenest.adapters.AuthenticationPagerAdapter;
import app.outofthenest.databinding.ActivityAuthenticationBinding;

public class Authentication extends AppCompatActivity {

    ActivityAuthenticationBinding authenticationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(authenticationBinding.getRoot());

        EdgeToEdge.enable(this);

        AuthenticationPagerAdapter adapter = new AuthenticationPagerAdapter(this, this);
        authenticationBinding.viewPagerAuthentication.setAdapter(adapter);

        new TabLayoutMediator(
                authenticationBinding.tabLayoutAuthentication,
                authenticationBinding.viewPagerAuthentication,
                (tab, position) -> {
                    if (position == 0) tab.setText(R.string.bt_register);
                    else tab.setText(R.string.bt_login);
                }
        ).attach();

        int action = getIntent().getIntExtra("action", -1);

        if (action == R.string.bt_get_started) {
            authenticationBinding.tabLayoutAuthentication.selectTab(
                    authenticationBinding.tabLayoutAuthentication.getTabAt(0));
        } else if (action == R.string.bt_login) {
            authenticationBinding.tabLayoutAuthentication.selectTab(
                    authenticationBinding.tabLayoutAuthentication.getTabAt(1));
        }
    }
}