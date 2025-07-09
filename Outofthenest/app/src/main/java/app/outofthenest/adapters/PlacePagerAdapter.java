package app.outofthenest.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.outofthenest.models.Place;
import app.outofthenest.ui.place.review.PlaceReviewFragment;
import app.outofthenest.ui.place.PlaceDetailFragment;

/**
 * Adapter to deal with tabs on Page detail screen
 */

public class PlacePagerAdapter extends FragmentStateAdapter {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private final Context context;

    private final Place place;

    public PlacePagerAdapter(@NonNull FragmentActivity fragmentActivity, Context context, Place place) {
        super(fragmentActivity);
        this.context = context;
        this.place = place;
    }

    @Override
    public Fragment createFragment(int position) {
        // To pass place as a parameter to the fragments
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        Fragment fragment;

        if (position == 0) {
            fragment = new PlaceDetailFragment();
        } else {
            fragment = new PlaceReviewFragment();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2; //2 tabs
    }

}
