package app.outofthenest.ui.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import app.outofthenest.R;
import app.outofthenest.databinding.ActivityAuthenticationBinding;
import app.outofthenest.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private FragmentRegisterBinding registerBinding;
    private AuthenticationViewModel viewModel;
    private boolean buttonPressed = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        init();
        return registerBinding.getRoot();
    }


    private void init() {
        setupViewModel();
        errorHandling();
        register();
    }

    private void errorHandling() {
        viewModel.getErrorMessageMLData().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupStatusBar() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

    }

    private void setupViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);

        viewModel.getFirebaseUserMLData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                //Skip first call
                if (!buttonPressed) return;

                if (firebaseUser != null) {
                    Toast.makeText(getContext(), getString(R.string.txt_registration_success), Toast.LENGTH_SHORT).show();
                    changeTab();
                } else {
                    Toast.makeText(getContext(), getString(R.string.txt_registration_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void changeTab() {
        ActivityAuthenticationBinding authenticationBinding = ActivityAuthenticationBinding.bind(getActivity().findViewById(R.id.activity_authentication));
        TabLayout.Tab tab = authenticationBinding.tabLayoutAuthentication.getTabAt(1);
        if (tab != null) {
            tab.select();
        }
    }


    private void register() {

        registerBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "Clocked register button");
                buttonPressed=true;

                String name = registerBinding.inpName.getText().toString();
                String email = registerBinding.inpEmail.getText().toString();
                String password = registerBinding.inpPassword.getText().toString();
                String confirmPassword = registerBinding.inpConfirmPassword.getText().toString();

                if(name.isEmpty() || name.length() < 3) {
                    Toast.makeText(getContext(), getString(R.string.txt_invalid_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.length() < 3 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), getString(R.string.txt_invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty() || password.length() < 6) {
                    Toast.makeText(getContext(), getString(R.string.txt_invalid_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getContext(), getString(R.string.txt_password_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }

//                Log.i(TAG, "Attempting to register user with name: " + name + ", email: " + email);
                viewModel.register(name, email, password);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerBinding = null;
    }
}