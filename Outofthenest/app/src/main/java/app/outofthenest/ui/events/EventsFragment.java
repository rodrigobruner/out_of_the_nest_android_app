package app.outofthenest.ui.events;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.EventAdapter;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.FragmentEventsBinding;
import app.outofthenest.models.Event;
import app.outofthenest.utils.Constants;
import app.outofthenest.utils.LocationProvider;

/**
 * Displays a list of events.
 */
public class EventsFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();

    private static final String EVENT_PARAMETER_NAME = "event";

    private FragmentEventsBinding binding;

    private EventsViewModel viewModel;

    private TagsAdapter audienceAdapter;

    private EventAdapter adapter;

    private Location currentLocation;

    private ArrayList<Event> eventsList = new ArrayList<>();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LocationProvider.getInstance(requireContext()).fetchLocation(requireActivity());
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        View root = binding.getRoot();
        init();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init(){
        setUpActionBar();
        sertUpAdapters();
        requestPermission();
        getUserLocation();
    }

    // set up the adapters for the RecyclerView
    private void sertUpAdapters() {
        adapter = new EventAdapter(new ArrayList<>());
        binding.recyclerEvents.setAdapter(adapter);
    }

    // set up the ActionBar
    public void setUpActionBar() {
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_event_bar_title);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_event);
            actionbar.setDisplayUseLogoEnabled(true);

            requireActivity().addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.add_button, menu);
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                    Intent intent = new Intent(requireContext(), NewEventActivity.class);
                    startActivity(intent);
                    return false;
                }
            }, getViewLifecycleOwner());
        }
    }

    // Deal with Location
    private void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // get the user location
    private void getUserLocation() {
//        Log.i(TAG, "Request user location...");
        LocationProvider.getInstance(requireContext()).getLocationLiveData()
            .observe(getViewLifecycleOwner(), location -> {
//                Log.i(TAG, "Location update: " + location);
                if (location != null) {
//                    Log.d(TAG, "Current location: " + location.getLatitude() + ", " + location.getLongitude());
                    currentLocation = location;

                    onGetLocation();
                }
            });
    }

    //after getting the location, fetch events and set up the recyclerView
    private void onGetLocation() {
        setupRecyclerView();
        setupAudienceRecyclerView();
        setOnEventClickListener();
        observeViewModel();
        getEvents();
        searchEvents();
    }

    // set up on event click listener
    private void setOnEventClickListener() {
        adapter.setOnEventClickListener(new EventAdapter.OnEventClickListener() {
            @Override
            public void OnEventClickListener(Event event) {
//                Log.i(TAG, "Event clicked: " + event.getTitle());
                Intent intent = new Intent(getContext(), EventActivity.class);
                intent.putExtra(EVENT_PARAMETER_NAME, event);
                startActivity(intent);
            }
        });
    }

    // Observe the ViewModel for events
    private void observeViewModel() {
        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            eventsList = (events != null) ? new ArrayList<>(events) : new ArrayList<>();
            adapter.setEventList(eventsList);
            binding.imgNoEvents.setVisibility(eventsList.isEmpty() ? View.VISIBLE : View.GONE);
        });

    }

    //Search functions
    private void getEvents() {
        audienceAdapter.setOnTagSelectedListener(new TagsAdapter.OnTagSelectedListener() {
            @Override
            public void onTagSelected(String tag) {
                searchEvents();
            }

            @Override
            public void onTagDeselected(String tag) {
                searchEvents();
            }
        });

        binding.radPeriods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              searchEvents();
          }
        });
    }


    // set up the RecyclerView for events
    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerEvents;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(eventsList);
        recyclerView.setAdapter(adapter);
    }

    // search for events
    private void searchEvents() {
        ArrayList<String> audience = new ArrayList<>(getSelectedTags());

        int numberOfDays = Constants.NUMBER_OF_DAYS_TO_LIST_EVENTS;
        int selectedPeriodId = binding.radPeriods.getCheckedRadioButtonId();

        if (selectedPeriodId == R.id.option2) {
            numberOfDays = 0;
        } else if (selectedPeriodId == R.id.option3) {
            numberOfDays = 7;
        } else if (selectedPeriodId == R.id.option4) {
            numberOfDays = 30;
        }

        viewModel.searchEvents(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                Constants.DEFAULT_SEARCH_EVENT_RADIUS,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(numberOfDays).toString(),
                audience);
    }

    // set up the RecyclerView for audience tags
    private void setupAudienceRecyclerView() {
        List<String> availableTags = Arrays.asList(getResources().getStringArray(R.array.list_target_audience));
        audienceAdapter = new TagsAdapter(availableTags);
        audienceAdapter.setSelectionEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.recyclerTags.setLayoutManager(layoutManager);
        binding.recyclerTags.setAdapter(audienceAdapter);
    }

    // get selected audience tags
    public List<String> getSelectedTags() {
        if (audienceAdapter != null) {
            return audienceAdapter.getSelectedTags();
        }
        return new ArrayList<>();
    }
}