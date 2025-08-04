package app.outofthenest.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;
import java.util.List;
import app.outofthenest.R;
import app.outofthenest.utils.TagIconMap;

/**
 * Adapter to deal with Tags list
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<String> tags = new ArrayList<>();

    private String tagCategory = "NO";

    private List<String> selectedTags;

    //Listener
    private OnTagSelectedListener listener;

    //Enable/disable selection of tags, default enable
    private boolean isSelectionEnabled = true;


    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chip chip = (Chip) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(chip);
    }

    @Override
    public int getItemCount() {
        if (tags != null) {
            return tags.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.chip.setText(tag);
        holder.chip.setOnCheckedChangeListener(null);
        holder.chip.setChecked(selectedTags.contains(tag));
        holder.chip.setEnabled(isSelectionEnabled);

        // Set the tag icon
        if(!tagCategory.equals("NO")) {
//            Log.i(TAG, "Tag category: " + tagCategory + "position: " + position);

            // get the icon based on the tag and category
            Integer iconRes = TagIconMap.getTagIconMap(position, tagCategory);
            if(iconRes != null && iconRes > 0) {
                holder.chip.setChipIconResource(iconRes);
            }else {
                holder.chip.setChipIcon(null);
            }
        }

        if (isSelectionEnabled) {
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
        } else {
            holder.chip.setOnCheckedChangeListener(null);
        }
    }

    // Constructor without tag category
    public TagsAdapter(List<String> tags) {
        if (tags != null) {
            this.tags = tags;
        }

        this.selectedTags = new ArrayList<>();
    }

    // Constructor with tag category
    public TagsAdapter(List<String> tags, String tagCategory) {
        if (tags != null) {
            this.tags = tags;
        }
        this.tagCategory = tagCategory.toUpperCase();
        this.selectedTags = new ArrayList<>();
    }

    // static class to update the tags list
    static class TagViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        TagViewHolder(Chip chip) {
            super(chip);
            this.chip = chip;
        }
    }

    //Implementation of the selection
    public interface OnTagSelectedListener {
        void onTagSelected(String tag);
        void onTagDeselected(String tag);
    }

    public void setOnTagSelectedListener(OnTagSelectedListener listener) {
        this.listener = listener;
    }

    public List<String> getSelectedTags() {
        return new ArrayList<>(selectedTags);
    }

    //Enable/Disable selection
    public void setSelectionEnabled(boolean enabled) {
        this.isSelectionEnabled = enabled;
        notifyDataSetChanged();
    }
}