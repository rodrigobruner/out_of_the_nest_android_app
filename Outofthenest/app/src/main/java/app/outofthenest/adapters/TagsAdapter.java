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
        holder.chip.setChecked(selectedTags.contains(tag));

        holder.chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                if (!selectedTags.contains(tag)) {
                    selectedTags.add(tag);
                }
            } else {
                selectedTags.remove(tag);
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

    static class TagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        TagViewHolder(Chip chip) {
            super(chip);
            this.chip = chip;
        }
    }
}