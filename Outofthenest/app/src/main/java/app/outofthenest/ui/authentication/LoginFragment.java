package app.outofthenest.ui.authentication;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import app.outofthenest.models.User;
import app.outofthenest.ui.maps.MainActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.FragmentLoginBinding;
import app.outofthenest.ui.onboard.OnboardActivity;
import app.outofthenest.utils.UserUtils;
import app.outofthenest.utils.OnboardUtils;

/**
 * Fragment for log in.
 */
public class LoginFragment extends Fragment {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private FragmentLoginBinding loginBinding;
    private AuthenticationViewModel viewModel;
    private boolean buttonPressed = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        init();
        return loginBinding.getRoot();
    }

    private void init(){
        setUpView();
        errorHandling();
        checkIfUserIsLoggedIn();
        login();
    }

    // observe Error LiveData
    private void errorHandling() {
        viewModel.getErrorMessageMLData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.trim().isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Set up ViewModel
    private void setUpView() {
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);
        loginBinding.inpEmail.requestFocus();
    }

    // TODO: improve it, create a static method in Utils
    // Check if the user is logged in
    private void checkIfUserIsLoggedIn(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);

        viewModel.getFirebaseUserMLData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {

                if(!buttonPressed) return;

                if (firebaseUser != null) {

                    User user = new User(
                            firebaseUser.getUid(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getEmail(),
                            null,
                            new ArrayList<>()
                    );


                    UserUtils.saveUser(requireContext(), user);

                    Intent intent;
                    if(OnboardUtils.isFirstTime(getContext())){
                        intent = new Intent(getActivity(), OnboardActivity.class);
                    }else{
                        intent = new Intent(getActivity(), MainActivity.class);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // set up login button and validate user input
    private void login(){
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonPressed = true;

                String email = loginBinding.inpEmail.getText().toString();
                String password = loginBinding.inpPassword.getText().toString();

                if(email.length() < 3 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty() || password.length() < 6) {
                    Toast.makeText(getContext(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
                    return;
                }

                viewModel.signIn(email, password);
            }
        });
    }
}
