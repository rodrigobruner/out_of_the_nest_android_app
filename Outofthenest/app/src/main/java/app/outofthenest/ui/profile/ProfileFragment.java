package app.outofthenest.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import app.outofthenest.HomeMainActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.FragmentProfileBinding;
import app.outofthenest.ui.authentication.AuthenticationViewModel;

public class ProfileFragment extends Fragment {

//    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private AuthenticationViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        setupAuthenticationViewModel();
        getUserName();
        getUserToken();
        logout();
    }

//    private void getUserId() {
//        viewModel.getUserId().observe(getViewLifecycleOwner(), userId -> {
//            binding.txvUserId.setText(
//                    Objects.requireNonNullElseGet(userId, () -> getString(R.string.txt_no_user_id_available)));
//        });
//    }

    private void getUserName() {
        String fullName = viewModel.getUserFullName();
        binding.txvUsername.setText(fullName != null ? fullName : getString(R.string.txt_error_no_name_set));
    }


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