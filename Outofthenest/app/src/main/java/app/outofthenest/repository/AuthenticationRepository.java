package app.outofthenest.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import app.outofthenest.R;


/**
 * Communicate with Google Firabase to manage users
 */

public class AuthenticationRepository {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();
    private Application app;
    private MutableLiveData<FirebaseUser> firebaseUserMLData;
    private MutableLiveData<Boolean> userLoggedMLData;
    private MutableLiveData<String> userTokenMLData;
    private MutableLiveData<String> userIdMLData;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private MutableLiveData<String> errorMessageMLData = new MutableLiveData<>();

    public AuthenticationRepository(Application application) {
        this.app = application;
        firebaseUserMLData = new MutableLiveData<>();
        userLoggedMLData = new MutableLiveData<>();
        userTokenMLData = new MutableLiveData<>();
        userIdMLData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            firebaseUserMLData.postValue(user);
            userLoggedMLData.postValue(user != null);

            if (user != null) {
                userIdMLData.postValue(user.getUid());
                // Get token when user state changes
                getUserToken();
            } else {
                userIdMLData.postValue(null);
                userTokenMLData.postValue(null);
            }
        };
        auth.addAuthStateListener(authStateListener);

        FirebaseUser user = auth.getCurrentUser();
        firebaseUserMLData.postValue(user);
        userLoggedMLData.postValue(user != null);
        if (user != null) {
            userIdMLData.postValue(user.getUid());
            getUserToken();
        }
    }

    // Register new user
    public void register(String email, String pass, String fullName) {
//        Log.i(TAG, "user with email: " + email);
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Log.i(TAG, "Register - Success!");
                    FirebaseUser user = auth.getCurrentUser();

                    // update user profile with full name
                    if (user != null && fullName != null && !fullName.isEmpty()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                            //TO Debug
//                            if (profileTask.isSuccessful()) {
//                                Log.i(TAG, "User profile updated with name: " + fullName);
//                            } else {
//                                Log.e(TAG, "Failed to update profile: " + profileTask.getException());
//                            }
                            firebaseUserMLData.postValue(user);
                        });
                    } else {
                        firebaseUserMLData.postValue(user);
                    }
                } else {
//                    Log.i(TAG, "Register - Fail!" + task.getException());
                    setErrorMessage(task.getException().getMessage());
                }
            }
        });
    }

    //Sing in process
    public void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    firebaseUserMLData.postValue(user);

                    if (user != null) {
//                        Log.i(TAG, "Login successful: " + user.getDisplayName());
//                        Log.i(TAG, "User ID: " + user.getUid());
                        refreshUserToken();
                    }
                } else {
                    setErrorMessage(task.getException().getMessage());
                }
            }
        });
    }

    // get Firebase ID Token for authentication
    public void getUserToken() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
//                    Log.i(TAG, "Token retrieved successfully");
                    userTokenMLData.postValue(token);
                } else {
//                    Log.e(TAG, "Failed to get token: " + task.getException());
                    setErrorMessage(app.getString(R.string.error_user_token));
                }
            });
        }
    }


    public void refreshUserToken() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            userTokenMLData.postValue(token);
                        }
                    });
        }
    }

    // get user full name
    public String getUserFullName() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getDisplayName() : null;
    }

    // get user ID
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // log out user
    public void signOut() {
        auth.signOut();
        userLoggedMLData.postValue(false);
        userTokenMLData.postValue(null);
        userIdMLData.postValue(null);
    }

    // Getters
    public MutableLiveData<FirebaseUser> getFirebaseUserMLData() {
        return firebaseUserMLData;
    }

    public MutableLiveData<Boolean> getUserLoggedMLData() {
        return userLoggedMLData;
    }

    public MutableLiveData<String> getUserTokenMLData() {
        return userTokenMLData;
    }

    public MutableLiveData<String> getUserIdMLData() {
        return userIdMLData;
    }

    public MutableLiveData<String> getErrorMessageMLData() {
        return errorMessageMLData;
    }

    public void setErrorMessage(String message) {
        errorMessageMLData.postValue(message);
    }

    public String getUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getEmail() : null;
    }
}