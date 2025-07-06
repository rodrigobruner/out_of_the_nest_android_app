package app.outofthenest.ui.events;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;

import app.outofthenest.R;
import app.outofthenest.adapters.EventAdapter;
import app.outofthenest.databinding.FragmentEventsBinding;
import app.outofthenest.mocs.EventsMoc;
import app.outofthenest.models.Event;
import app.outofthenest.utils.LocationProvider;

public class EventsFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private FragmentEventsBinding binding;

    private EventsViewModel viewModel;

    ArrayList<Event> eventsList;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LocationProvider.getInstance(requireContext()).fetchLocation(requireActivity());
                } else {
                    Toast.makeText(getContext(), getString(R.string.permission_location_denied), Toast.LENGTH_SHORT).show();
                }
            });

    private FusedLocationProviderClient fusedLocationClient;
    double latitude;
    double longitude;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EventsViewModel.class);
        View root = binding.getRoot();
        init();
        return root;
    }

    private void init(){
        requestPermission();
        getEvents();
        setupRecyclerView();
    }

    private void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerEvents;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        EventAdapter adapter = new EventAdapter(eventsList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getEvents() {
//        eventsList = EventsMoc.getEvents();
//        return;

        double lat = 0.0, lng = 0.0, radius = 10.0;
        String startDate = "2024-06-01", endDate = "2024-06-30";
        ArrayList<String> targetAudience = new ArrayList<>();

        viewModel.searchEvents(lat, lng, radius, startDate, endDate, targetAudience);

        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) {
                eventsList = new ArrayList<>(events);;
            }
        });
    }
}