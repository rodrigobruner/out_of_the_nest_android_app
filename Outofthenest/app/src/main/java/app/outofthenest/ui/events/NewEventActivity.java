package app.outofthenest.ui.events;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import app.outofthenest.R;
import app.outofthenest.adapters.TagsAdapter;
import app.outofthenest.databinding.ActivityNewEventBinding;
import app.outofthenest.models.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private ActivityNewEventBinding binding;

    private EventsViewModel viewModel;

    private TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        setUpActionBar();
        setUpViewModel();
        setUpAdapters();
        observeViewModel();
        showProgressBar(false);
        setUpTargetRecyclerView();
        setUpDatePicker();
        setupSaveButton();
    }

    private void setUpActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle(R.string.txt_new_event);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_event);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    private void setUpViewModel() {
        viewModel = new ViewModelProvider(this).get(EventsViewModel.class);
    }

    private void setUpAdapters(){
        tagsAdapter = new TagsAdapter(getAvailableTags(), "EVENT");
        binding.recyclerTags.setAdapter(tagsAdapter);
    }

    private void observeViewModel() {
        viewModel.getCreatedEvent().observe(this, event -> {
            if (event != null) {
                Toast.makeText(this, getString(R.string.txt_event_created), Toast.LENGTH_SHORT).show();
                finish();
                viewModel.clearCreatedEvent();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            showProgressBar(isLoading);
            binding.button.setEnabled(!isLoading);
            binding.button.setText(isLoading
                    ? getString(R.string.txt_saving)
                    : getString(R.string.btn_new_event_save));
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                viewModel.clearErrorMessage();
            }
        });
    }

    private void setUpDatePicker() {
        binding.inpEventDate.setFocusable(false);
        binding.inpEventDate.setClickable(true);

        binding.inpEventDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    R.style.DatePickerDialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // After date is picked, show time picker
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        showTimePicker(calendar);
                    },
                    year, month, day
            );

            // Set minimum date to tomorrow
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });
    }

    private void showTimePicker(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new android.app.TimePickerDialog(
                this,
                R.style.DatePickerDialog, // Use the same style as DatePickerDialog
                (view, selectedHour, selectedMinute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    String dateTimeStr = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE));
                    binding.inpEventDate.setText(dateTimeStr);
                },
                hour, minute, true
        ).show();
    }


    private void setupSaveButton() {
        binding.button.setOnClickListener(v -> {
            String title = binding.inpEventTitle.getText().toString();
            String description = binding.inpEventDescription.getText().toString();
            String address = binding.inpEventAddress.getText().toString();
            String dateStr = binding.inpEventDate.getText().toString();
            List<String> audience = getSelectedTags();
//            String audience = binding.inpEventAudience.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, getString(R.string.txt_invalid_event_title), Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {
                Toast.makeText(this, getString(R.string.txt_invalid_event_description), Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.isEmpty()) {
                Toast.makeText(this, getString(R.string.txt_invalid_address), Toast.LENGTH_SHORT).show();
                return;
            }
            if (dateStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.txt_invalid_event_date), Toast.LENGTH_SHORT).show();
                return;
            }
            if (audience.size() == 0) {
                Toast.makeText(this, getString(R.string.txt_invalid_event_audience), Toast.LENGTH_SHORT).show();
                return;
            }



            Date date = new Date();
            ArrayList<String> targetAudience = new ArrayList<>();


            Event newEvent = new Event(null, title, description, date, address, new ArrayList<>(audience));
            viewModel.createEvent(newEvent);
        });
    }


    private void setUpTargetRecyclerView() {

        List<String> availableTags = getAvailableTags();
        tagsAdapter = new TagsAdapter(availableTags, "EVENT");
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        binding.recyclerTags.setLayoutManager(layoutManager);
        binding.recyclerTags.setAdapter(tagsAdapter);
    }

    public List<String> getAvailableTags() {
        String[] tags = getResources().getStringArray(R.array.list_target_audience);
        return Arrays.asList(tags);
    }

    public List<String> getSelectedTags() {
        if (tagsAdapter != null) {
            return tagsAdapter.getSelectedTags();
        }
        return new ArrayList<>();
    }

    private void showProgressBar(boolean show) {
        if (binding.progressBar != null) {
            binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            binding.button.setEnabled(!show);
        }
    }
}