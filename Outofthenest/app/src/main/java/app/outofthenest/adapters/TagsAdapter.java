package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;
import app.outofthenest.R;



public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagViewHolder> {

    private static final String TAG = "TagsAdapter";
    private List<String> tags;
    private List<String> selectedTags;
    private OnTagSelectedListener listener;

    public TagsAdapter(List<String> tags) {
        this.tags = tags;
        this.selectedTags = new ArrayList<>();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chip chip = (Chip) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(chip);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.chip.setText(tag);
        holder.chip.setOnCheckedChangeListener(null);
        holder.chip.setChecked(selectedTags.contains(tag));

        holder.chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (!selectedTags.contains(tag)) {
                    selectedTags.add(tag);
                    if (listener != null) listener.onTagSelected(tag);
                }
            } else {
                selectedTags.remove(tag);
                if (listener != null) listener.onTagDeselected(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public List<String> getSelectedTags() {
        return new ArrayList<>(selectedTags);
    }

    public interface OnTagSelectedListener {
        void onTagSelected(String tag);
        void onTagDeselected(String tag);
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        TagViewHolder(Chip chip) {
            super(chip);
            this.chip = chip;
        }
    }

    public void setOnTagSelectedListener(OnTagSelectedListener listener) {
        this.listener = listener;
    }
}