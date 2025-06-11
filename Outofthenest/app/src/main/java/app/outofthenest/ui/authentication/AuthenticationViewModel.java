package app.outofthenest.view.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import app.outofthenest.repository.AuthenticationRepository;

public class AuthenticationViewModel extends AndroidViewModel {

    private static final String TAG = "AuthenticationViewModel";
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

    public void register(String name, String email , String pass){
        repository.register(email, pass);
    }
    public void signIn(String email , String pass){
        repository.login(email, pass);
    }
    public void signOut(){
        repository.signOut();
    }

}
