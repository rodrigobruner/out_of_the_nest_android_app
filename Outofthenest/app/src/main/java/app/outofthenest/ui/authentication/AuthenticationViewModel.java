package app.outofthenest.ui.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import app.outofthenest.repository.AuthenticationRepository;

public class AuthenticationViewModel extends AndroidViewModel {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> firebaseUserMLData;
    private MutableLiveData<Boolean> userLoggedMLData;

    public AuthenticationViewModel(@NonNull Application app) {
        super(app);
        repository = new AuthenticationRepository(app);
        firebaseUserMLData = repository.getFirebaseUserMLData();
        userLoggedMLData = repository.getUserLoggedMLData();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMLData() {
        return firebaseUserMLData;
    }

    public MutableLiveData<Boolean> getUserLoggedMLData() {
        return userLoggedMLData;
    }

    public void register(String fullName, String email, String password) {
        repository.register(email, password, fullName);
    }

    public void signIn(String email , String pass){
        repository.login(email, pass);
    }

    public void signOut(){
        repository.signOut();
    }


    public MutableLiveData<String> getErrorMessageMLData() {
        return repository.getErrorMessageMLData();
    }

    public LiveData<String> getUserToken() {
        return repository.getUserTokenMLData();
    }

    public LiveData<String> getUserId() {
        return repository.getUserIdMLData();
    }

    public String getUserFullName() {
        return repository.getUserFullName();
    }

    public void refreshUserToken() {
        repository.refreshUserToken();
    }

}
