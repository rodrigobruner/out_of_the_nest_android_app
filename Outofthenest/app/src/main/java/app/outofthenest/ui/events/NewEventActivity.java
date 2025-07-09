package app.outofthenest.ui.events;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import app.outofthenest.R;
import app.outofthenest.databinding.ActivityNewEventBinding;

public class NewEventActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivityNewEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityNewEventBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_new_event);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
        init();
    }

    private void init(){
        setUpActionBar();
    }

    public void setUpActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_new_event);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_event);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }


}