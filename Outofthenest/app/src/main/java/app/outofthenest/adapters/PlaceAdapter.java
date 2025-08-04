package app.outofthenest.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.models.Place;
import app.outofthenest.utils.TagIconMap;

/**
 * This adapter is to deal with places on place search
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<Place> placeList = new ArrayList<>();

    private Context context;

    private OnPlaceClickListener onPlaceClickListener;

    //Click listener
    private OnGoClickListener onGoClickListener;

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.title.setText(place.getTitle());
        holder.address.setText(place.getAddress());
        holder.status.setText(place.getStatus());
        holder.ratingBar.setRating(place.getRating());

        Log.i("RATING", "R:"+place.getRating());

        // Deal with tags
        holder.tagList.removeAllViews();
        ArrayList<String> tags = place.getTags();
        // Prevent NullPointerException
        if (tags == null) {
            tags = new ArrayList<>();
        }
        String[] targetAudience = context.getResources().getStringArray(R.array.list_tags);

        // for each tag find the icon and add it to the list
        for (String tag : tags) {
            int index = Arrays.asList(targetAudience).indexOf(tag);
            ImageView tagIcon = new ImageView(context);
            tagIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics())
            ));

            int iconId = TagIconMap.getTagIconMap(index, TagIconMap.TYPE_PLACE);
            if (iconId > 0) {
                tagIcon.setImageResource(iconId);
            }
            tagIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            tagIcon.setPadding(5, 5, 5, 5);
            tagIcon.setColorFilter(context.getColor(R.color.chip_stroke_selected), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.tagList.addView(tagIcon);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onPlaceClickListener != null) {
                onPlaceClickListener.onPlaceClick(place);
            }
        });

        holder.btnGo.setOnClickListener(v -> {
            if (onGoClickListener != null) {
                onGoClickListener.onGoClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {

        return placeList.size();
    }

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    // to update the place list
    public void updatePlaces(List<Place> newPlaces) {
        if(newPlaces != null) {
            this.placeList.clear();
            this.placeList.addAll(newPlaces);
            notifyDataSetChanged(); //from parent
        }
    }


    // Implementation of the click listner

    // for place click events
    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.onPlaceClickListener = listener;
    }

    // for go button click events
    public interface OnGoClickListener {
        void onGoClick(Place place);
    }

    public void setOnGoClickListener(OnGoClickListener listener) {
        this.onGoClickListener = listener;
    }


    // ViewHolder class
    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView title, address, status;
        RatingBar ratingBar;
        Button btnGo;

        LinearLayout tagList;

        PlaceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
            status = itemView.findViewById(R.id.text_status);
            ratingBar = itemView.findViewById(R.id.place_rating);
            btnGo = itemView.findViewById(R.id.btnGo);
            tagList = itemView.findViewById(R.id.tags_Linear_layout);
        }
    }
}