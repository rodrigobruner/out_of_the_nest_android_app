package app.outofthenest;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.databinding.ActivityHomeMainBinding;


public class HomeMainActivity extends AppCompatActivity {

    private ActivityHomeMainBinding homeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        homeBinding = ActivityHomeMainBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        homeBinding.btGetStarted.setOnClickListener(
                v -> {
                    Intent intent = new Intent(HomeMainActivity.this, Authentication.class);
                    intent.putExtra("action", R.string.bt_get_started);
                    startActivity(intent);
                }
        );

        homeBinding.btLogin.setOnClickListener(
                v -> {
                    Intent intent = new Intent(HomeMainActivity.this, Authentication.class);
                    intent.putExtra("action", R.string.bt_login);
                    startActivity(intent);
                }
        );

    }
}