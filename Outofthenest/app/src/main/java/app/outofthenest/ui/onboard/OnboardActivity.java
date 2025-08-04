package app.outofthenest.ui.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.outofthenest.databinding.ActivityOnboardBinding;
import app.outofthenest.ui.maps.MainActivity;


public class OnboardActivity extends AppCompatActivity {

    private static final String TAG = "OnboardActivity";
    ActivityOnboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        binding.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardActivity.this, MainActivity.class);
                intent.putExtra("profile", "true");
                startActivity(intent);
            }
        });
    }
}