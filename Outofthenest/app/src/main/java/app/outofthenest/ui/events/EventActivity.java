package app.outofthenest.ui.events;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivityEventBinding;
import app.outofthenest.models.Event;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class EventActivity extends AppCompatActivity {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();

    private static final String EVENT_PARAMETER_NAME = "event";

    private ActivityEventBinding binding;

    private TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }


    private void init() {
        setUpActionBar();
        setUpEvent();
    }

    public void setUpActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_event_detail);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_event);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    private void setUpEvent() {
        if (getIntent().hasExtra(EVENT_PARAMETER_NAME)) {
            Event event = getIntent().getSerializableExtra(EVENT_PARAMETER_NAME, Event.class);
            binding.txvEventTitle.setText(event.getTitle());
            binding.txvEventDate.setText(event.getDatetime().toString());
            binding.txvEventLocation.setText(event.getAddress());
            binding.txvEventDescription.setText(event.getDescription());
            setupTagsRecyclerView(event.getTargetAudience());
        }
    }


    private void setupTagsRecyclerView(List<String> availableTags) {
        tagsAdapter = new TagsAdapter(availableTags);
        tagsAdapter.setSelectionEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.recyclerTags.setLayoutManager(layoutManager);
        binding.recyclerTags.setAdapter(tagsAdapter);
    }
}