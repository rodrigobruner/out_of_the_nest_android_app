package app.outofthenest.ui.maps;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.adapters.PlaceAdapter;
import app.outofthenest.databinding.FragmentMapsSearchPlacesBinding;
import app.outofthenest.models.Place;

public class MapsSearchPlacesFragment extends Fragment {

    private static final String TAG = "MapsSearchPlacesFragment";
    private FragmentMapsSearchPlacesBinding binding;
    private WindowInsetsCompat lastInsets;
    private int collapsedHeight;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsSearchPlacesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Store the latest WindowInsetsCompat
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            lastInsets = insets;
            return insets;
        });

        collapsedHeight = (int) (80 * getResources().getDisplayMetrics().density);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        // Listen for layout changes to update card height when keyboard appears/disappears
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (binding.searchEditText.hasFocus()) {
                int expandedHeight = getExpandedHeight();
                int startHeight = binding.searchCard.getLayoutParams().height;
                animateCard(binding.searchCard, startHeight, expandedHeight);
            }
        });
    }

    private void init() {
        searchOnFocusChange();
        setButtonCancel();
    }

    private void searchOnFocusChange() {
        binding.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            int startHeight = binding.searchCard.getLayoutParams().height;
            int endHeight = hasFocus ? getExpandedHeight() : collapsedHeight;
            binding.buttonCancel.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            animateCard(binding.searchCard, startHeight, endHeight);
            searchResults();
        });
    }

    public int getExpandedHeight() {
        View rootView = requireActivity().getWindow().getDecorView();
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        Log.i(TAG, "Visible HEIGHT frame: " + r.height());

        int keyboardHeight = 0;
        if (lastInsets != null) {
            Insets imeInsets = lastInsets.getInsets(WindowInsetsCompat.Type.ime());
            keyboardHeight = imeInsets.bottom;
        }

        Log.i(TAG, "Keyboard height: " + keyboardHeight);

        return r.height() - keyboardHeight - collapsedHeight;
    }

    private void animateCard(View card, int startHeight, int endHeight) {
        if (endHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            ViewGroup.LayoutParams params = card.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            card.setLayoutParams(params);
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
            animator.setDuration(300);
            animator.addUpdateListener(animation -> {
                ViewGroup.LayoutParams params = card.getLayoutParams();
                params.height = (int) animation.getAnimatedValue();
                card.setLayoutParams(params);
            });
            animator.start();
        }
        boolean showOptions = endHeight > startHeight;
        card.findViewById(R.id.container_options).setVisibility(showOptions ? View.VISIBLE : View.GONE);
    }

    private void setButtonCancel() {
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.searchEditText.getWindowToken(), 0);

                binding.searchEditText.clearFocus();
                binding.searchEditText.setText("");
                binding.buttonCancel.setVisibility(View.GONE);
                animateCard(binding.searchCard, binding.searchCard.getHeight(), collapsedHeight);
                searchResults();
            }
        });
    }

    private void searchResults() {
        List<Place> placesSearchResults = getPlaces();
        PlaceAdapter placeAdapter = new PlaceAdapter(placesSearchResults);

        binding.placesRecyclerView.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(getContext())
        );
        binding.placesRecyclerView.setAdapter(placeAdapter);
    }

    private List<Place> getPlaces() {
        return Arrays.asList(
                new Place("Waterloo Park", "Westmount Rd Entrance, 90 Westmount Rd N, Waterloo, ON N2L", "11 min", "5,2 km", "Open", 4, 43.460867, -80.509813, 0.0f),
                new Place("Waterloo Public Library", "2001 University Ave E, Waterloo, ON N2K 0B3", "17 min", "6 km", "Close soon", 5, 43.460867, -80.509813, 0.0f),
                new Place("Playground - Bridge St W", "185 Bridge St W, Waterloo, ON N2K 1K9", "18 min", "7.3 km", "Closed", 5, 43.460867, -80.509813, 0.0f),
                new Place("Galaxy Cinemas Waterloo", "550 King St N, Waterloo, ON N2L 5W6", "21 min", "8.8 km", "Open", 5, 43.460867, -80.509813, 0.0f)
        );
    }
}