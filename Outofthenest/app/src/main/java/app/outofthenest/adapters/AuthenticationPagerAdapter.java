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

    private static final String TAG = "AuthenticationPagerAdapter";

    private final Context context;

    public AuthenticationPagerAdapter(@NonNull FragmentActivity fa, Context context) {
        super(fa);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) { //If first tab
//            Log.d(TAG, "RegisterFragment");
            return new RegisterFragment();
        } else {
//            Log.d(TAG, "LoginFragment");
            return new LoginFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2; //2 tabs
    }
}