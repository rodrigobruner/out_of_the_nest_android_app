package app.outofthenest.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationRepository {

    private Application app;
    private MutableLiveData<FirebaseUser> firebaseUserMLData;
    private MutableLiveData<Boolean> userLoggedMLData;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public AuthenticationRepository(Application application){
        this.app = application;
        firebaseUserMLData = new MutableLiveData<>();
        userLoggedMLData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            firebaseUserMLData.postValue(user);
            userLoggedMLData.postValue(user != null);
        };
        auth.addAuthStateListener(authStateListener);

        FirebaseUser user = auth.getCurrentUser();
        firebaseUserMLData.postValue(user);
        userLoggedMLData.postValue(user != null);
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMLData() {
        return firebaseUserMLData;
    }

    public MutableLiveData<Boolean> getUserLoggedMLData() {
        return userLoggedMLData;
    }

    public void register(String email , String pass){
        Log.i("Register", "Attempting to register user with email: " + email);
        auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i("Register", "Success!");
                    firebaseUserMLData.postValue(auth.getCurrentUser());
                }else{
                    Log.i("Register", "Fail!"+ task.getException());
                    Toast.makeText(app, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void login(String email , String pass){
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUserMLData.postValue(auth.getCurrentUser());
                }else{
                    Toast.makeText(app, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signOut(){
        auth.signOut();
        userLoggedMLData.postValue(true);
    }

}
