package app.outofthenest.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import app.outofthenest.ui.authentication.LoginFragment;
import app.outofthenest.ui.authentication.RegisterFragment;


/**
 * This adapter is to deal with tab on authentication Page
 */
public class AuthenticationPagerAdapter extends FragmentStateAdapter {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private final Context context;

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
//            Log.d(TAG, "RegisterFragment");
            return new RegisterFragment();
        } else {
//            Log.d(TAG, "LoginFragment");
            return new LoginFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public AuthenticationPagerAdapter(@NonNull FragmentActivity fa, Context context) {
        super(fa);
        this.context = context;
    }
}