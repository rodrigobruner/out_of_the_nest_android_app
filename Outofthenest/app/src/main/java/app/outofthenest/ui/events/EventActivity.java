package app.outofthenest.ui.events;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivityEventBinding;
import app.outofthenest.models.Event;
import app.outofthenest.utils.DateUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.util.DataUtils;

/**
 * Displays the details of a event.
 */
public class EventActivity extends AppCompatActivity {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();

    private static final String EVENT_PARAMETER_NAME = "event";

    private static final String EVENT_ID_PARAMETER_NAME = "event_id";

    private ActivityEventBinding binding;

    private TagsAdapter tagsAdapter;

    private EventsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }


    private void init() {
        setUpActionBar();
        setUpViewModel();
        setUpEvent();
    }

    // Initializes the ViewModel
    private void setUpViewModel() {
        viewModel = new ViewModelProvider(this).get(EventsViewModel.class);
    }

    // set up the ActionBar
    public void setUpActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_event_detail);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_event);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    // Check intent parameters and set up a event
    private void setUpEvent() {
        Event event = null;

        if (getIntent().hasExtra(EVENT_PARAMETER_NAME)) {
            event = getIntent().getSerializableExtra(EVENT_PARAMETER_NAME, Event.class);
            setUpUi(event);
        }

        if (getIntent().hasExtra(EVENT_ID_PARAMETER_NAME)) {
            String eventId = getIntent().getStringExtra(EVENT_ID_PARAMETER_NAME);
            viewModel.getEventById(eventId);
            viewModel.getCreatedEvent().observe(this, newEvent -> {
                if (newEvent != null) {
                    setUpUi(newEvent);
                }
            });
        }
    }

    // Set up the UI
    private void setUpUi(Event event) {
        binding.txvEventTitle.setText(event.getTitle());
        binding.txvEventDate.setText(DateUtils.formatDateTime(getResources(), event.getDatetime()));
        binding.txvEventLocation.setText(event.getAddress());
        binding.txvEventDescription.setText(event.getDescription());
        setupTagsRecyclerView(event.getTargetAudience());
    }

    // set up the tags RecyclerView
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