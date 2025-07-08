package app.outofthenest.ui.events;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.EventAdapter;
import app.outofthenest.databinding.FragmentEventsBinding;
import app.outofthenest.mocs.EventsMoc;
import app.outofthenest.models.Event;
import app.outofthenest.utils.Constants;
import app.outofthenest.utils.LocationProvider;

public class EventsFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    private String TAG = getClass().getSimpleName();

    private static final String EVENT_PARAMETER_NAME = "event";

    private FragmentEventsBinding binding;

    private EventsViewModel viewModel;

    private EventAdapter adapter;

    private Location currentLocation;

    private ArrayList<Event> eventsList;

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
        requestPermission();
        getUserLocation();
    }



    // Deal with Location
    private void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getUserLocation() {
//        Log.i(TAG, "Requesting user location...");
        LocationProvider.getInstance(requireContext()).getLocationLiveData()
            .observe(getViewLifecycleOwner(), location -> {
//                Log.i(TAG, "Location updated: " + location);
                if (location != null) {
//                    Log.d(TAG, "Current location: " + location.getLatitude() + ", " + location.getLongitude());
                    currentLocation = location;

                    onGetLocation();
                }
            });
    }

    //After getting the location, fetch events and set up the recyclerView
    private void onGetLocation() {
        getEvents();
        setupRecyclerView();
        setSearchListener();
        setOnEventClickListener();
    }

    // Set up the recyclerView
    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerEvents;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(eventsList);
        recyclerView.setAdapter(adapter);
    }

    private void setSearchListener() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchEvents();
            }
        });
    }

    private void setOnEventClickListener() {
        adapter.setOnEventClickListener(new EventAdapter.OnEventClickListener() {
            @Override
            public void OnEventClickListener(Event event) {
                Log.i(TAG, "Event clicked: " + event.getTitle());
                Intent intent = new Intent(getContext(), EventActivity.class);
                intent.putExtra(EVENT_PARAMETER_NAME, event);
                startActivity(intent);
            }
        });
    }



    //Search functions
    private void searchEvents() {
        String searchText = binding.edtSearch.getText().toString().trim();

        if (searchText.isEmpty()) {
            adapter.setEventList(eventsList);
            return;
        }

        List<Event> filteredEvents = filterEvents(searchText);
        binding.imgNoEvents.setVisibility(filteredEvents.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.updateEvents(filteredEvents);
    }

    private List<Event> filterEvents(String searchText) {
        List<Event> allEvents = eventsList;
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : allEvents) {
            boolean matchesText = event.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    event.getDescription().toLowerCase().contains(searchText.toLowerCase());

            if (matchesText) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    private void getEvents() {
//        Log.i(TAG, "fetch events.");
        //Comment here to use real data
        eventsList = EventsMoc.getEvents();

        LocalDate today = LocalDate.now();
        LocalDate until = today.plusDays(Constants.NUMBER_OF_DAYS_TO_LIST_EVENTS);
        int radius = Constants.DEFAULT_SEARCH_RADIUS;
        ArrayList<String> targetAudience = new ArrayList<>();

        viewModel.searchEvents(
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                radius,
                today.toString(),
                until.toString(),
                targetAudience);

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) {
                binding.imgNoEvents.setVisibility(View.GONE);
                eventsList = new ArrayList<>(events);
                adapter.setEventList(eventsList);
            } else {
                binding.imgNoEvents.setVisibility(View.VISIBLE);
            }
        });
    }


}