package app.outofthenest.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for the Profile Fragment.
 * TODO: check if this ViewModel is used in the app, if not, remove it.
 */
public class ProfileViewModel extends ViewModel {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}