package app.outofthenest.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.outofthenest.models.Place;
import app.outofthenest.ui.authentication.LoginFragment;
import app.outofthenest.ui.authentication.RegisterFragment;
import app.outofthenest.ui.place.PlaceCommentsFragment;
import app.outofthenest.ui.place.PlaceDetailFragment;

public class PlacePagerAdapter extends FragmentStateAdapter {

        private static final String TAG = "AuthenticationPagerAdapter";

        private final Context context;

        private final Place place;

        public PlacePagerAdapter(@NonNull FragmentActivity fragmentActivity, Context context, Place place) {
            super(fragmentActivity);
            this.context = context;
            this.place = place;
        }


    @Override
    public Fragment createFragment(int position) {
        // I need to pass place as a parameter to the fragments
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        Fragment fragment;

        if (position == 0) {
            fragment = new PlaceDetailFragment();
        } else {
            fragment = new PlaceCommentsFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

        @Override
        public int getItemCount() {
            return 2; //2 tabs
        }

}
