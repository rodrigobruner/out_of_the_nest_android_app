package app.outofthenest.ui.newplace;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import app.outofthenest.R;
import app.outofthenest.databinding.FragmentNewPlaceBinding;

public class NewPlaceFragment extends Fragment {

    public static NewPlaceFragment newInstance() {
        return new NewPlaceFragment();
    }

    private static final String TAG = "NewPlaceFragment";
    private NewPlaceViewModel mViewModel;
    private FragmentNewPlaceBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewPlaceViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewPlaceBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot(); // Corrected here
    }

    private void init() {
        setUpSpinner();
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.place_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.placeTypeSpinner.setAdapter(adapter);
    }
}