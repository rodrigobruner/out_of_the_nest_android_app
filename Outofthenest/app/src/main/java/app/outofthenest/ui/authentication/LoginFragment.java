package app.outofthenest.view.authentication;

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

import app.outofthenest.MainActivity;
import app.outofthenest.R;
import app.outofthenest.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding loginBinding;
    private AuthenticationViewModel viewModel;

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
        checkIfUserIsLoggedIn();
        login();
    }

    private void setUpView() {
        loginBinding.inpEmail.requestFocus();
    }

    private void checkIfUserIsLoggedIn(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthenticationViewModel.class);

        viewModel.getFirebaseUserMLData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.txt_login_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login(){
        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginBinding.inpEmail.getText().toString();
                String password = loginBinding.inpPassword.getText().toString();

                if(email.length() < 3 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), getString(R.string.txt_invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty() || password.length() < 6) {
                    Toast.makeText(getContext(), getString(R.string.txt_invalid_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                viewModel.signIn(email, password);
            }
        });
    }
}
