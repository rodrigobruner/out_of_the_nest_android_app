package app.outofthenest.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import app.outofthenest.adapters.FamilyMemberAdapter;
import app.outofthenest.databinding.DialogAddFamilyMemberBinding;
import app.outofthenest.models.FamilyMember;
import app.outofthenest.models.User;
import app.outofthenest.ui.authentication.HomeMainActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.FragmentProfileBinding;
import app.outofthenest.ui.authentication.AuthenticationViewModel;
import app.outofthenest.utils.UserUtils;

public class ProfileFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private FragmentProfileBinding binding;
    private DialogAddFamilyMemberBinding dialogBinding;
    private AuthenticationViewModel viewModel;
    private FamilyMemberAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        dialogBinding = DialogAddFamilyMemberBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        return root;
    }

    private void init() {
        setUpActionBar();
        setupAuthenticationViewModel();
        getUser();
        //Just To debug token
//        getUserToken();
        logout();
        setupFamilyMembers();
        setOnClickAddFamilyMember();
    }


    public void setUpActionBar() {
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_profile_bar_title);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_profile);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }


    private void setupFamilyMembers() {
        ArrayList<FamilyMember> familyMembers = UserUtils.getFamilyMembers(requireContext());

        adapter = new FamilyMemberAdapter(familyMembers, position -> {
            UserUtils.removeFamilyMember(requireContext(), position);
            familyMembers.remove(position);
            adapter.notifyItemRemoved(position);
        });

        binding.recyclerFamilyMembers.setAdapter(adapter);
        // Optionally set layout manager if not set in XML
         binding.recyclerFamilyMembers.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void setOnClickAddFamilyMember() {
        binding.btnAddFamilyMember.setOnClickListener(v -> {
            addFamilyMemberDialog();
        });
    }

    private void addFamilyMemberDialog() {
        dialogBinding = DialogAddFamilyMemberBinding.inflate(LayoutInflater.from(getContext()));
        Spinner spinnerType = dialogBinding.sprType;
        Button btnPickDate = dialogBinding.btnPickDate;
        TextView tvSelectedDate = dialogBinding.txvBirth;

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_family_member_types,
                android.R.layout.simple_spinner_item
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterSpinner);

        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    R.style.DatePickerDialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        tvSelectedDate.setText(
                                new SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()).format(calendar.getTime()));
                    },
                    year, month, day
            );

            // Set max date to today
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.YEAR, -120); // Minimum age 120
            dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

            dialog.show();
        });

        new AlertDialog.Builder(getContext())
                .setTitle("Add Family Member")
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Add", (dialog, which) -> {
                    String type = spinnerType.getSelectedItem().toString();
                    String dateStr = tvSelectedDate.getText().toString();
                    java.util.Date birth = null;
                    try {
                        birth = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr);
                    } catch (Exception e) {
                        // handle parse error
                    }
                    if (type != null && birth != null) {
                        FamilyMember member = new FamilyMember(type, birth);
                        UserUtils.addFamilyMember(requireContext(), member);
                        // Update your adapter data
                        ArrayList<FamilyMember> updatedList = UserUtils.getFamilyMembers(requireContext());
                        adapter = new FamilyMemberAdapter(updatedList, position -> {
                            UserUtils.removeFamilyMember(requireContext(), position);
                            updatedList.remove(position);
                            adapter.notifyItemRemoved(position);
                        });
                        binding.recyclerFamilyMembers.setAdapter(adapter);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void getUser() {
        User user = UserUtils.getUser(requireContext());
        binding.txvName.setText(user.getName());
        binding.txvUsername.setText(user.getEmail());
    }

    // Just To debug token
    private void getUserToken() {
        viewModel.getUserToken().observe(getViewLifecycleOwner(), token -> {
            //                Log.i(TAG, "User token: " + token);
            binding.txvToken.setText(
                    Objects.requireNonNullElseGet(token, () -> getString(R.string.txt_no_token_available)));
        });
    }


    private void setupAuthenticationViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);

        viewModel.getUserLoggedMLData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean login) {
                if (!login) {
                    Intent intent = new Intent(getActivity(), HomeMainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void logout(){
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "Logout button clicked");
                UserUtils.clearUser(getContext());
                viewModel.signOut();
            }
        });
    }
}