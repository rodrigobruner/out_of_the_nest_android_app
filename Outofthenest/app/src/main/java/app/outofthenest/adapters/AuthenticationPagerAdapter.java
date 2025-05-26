package app.outofthenest.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import app.outofthenest.ui.login.LoginFragment;
import app.outofthenest.ui.register.RegisterFragment;

public class AuthenticationPagerAdapter extends FragmentStateAdapter {

    private final Context context;

    public AuthenticationPagerAdapter(@NonNull FragmentActivity fa, Context context) {
        super(fa);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {

            Log.d("BRN", "RegisterFragment");
            return new RegisterFragment();
        } else {
            Log.d("BRN", "LoginFragment");
            return new LoginFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}