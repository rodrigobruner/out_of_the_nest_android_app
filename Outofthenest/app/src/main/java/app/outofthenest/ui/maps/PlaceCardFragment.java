package app.outofthenest.ui.maps;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.outofthenest.R;
import app.outofthenest.databinding.FragmentPlaceCardBinding;
import app.outofthenest.models.Place;


public class PlaceCardFragment extends Fragment {
    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_card, container, false);
    }
}