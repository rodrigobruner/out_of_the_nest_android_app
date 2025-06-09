package app.outofthenest.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import app.outofthenest.HomeMainActivity;
import app.outofthenest.databinding.FragmentProfileBinding;
import app.outofthenest.view.authentication.AuthenticationViewModel;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private AuthenticationViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
//        Log.i(TAG, "Init "+TAG);
        setupAuthenticationViewModel();
        logout();
    }


    private void setupAuthenticationViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);

        viewModel.getUserLoggedMLData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean login) {
//                Log.i(TAG, "Logout finished");
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
                viewModel.signOut();
            }
        });
    }
}