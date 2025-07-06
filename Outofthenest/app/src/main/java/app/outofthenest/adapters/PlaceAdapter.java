package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.outofthenest.R;
import app.outofthenest.models.Place;

/**
 * This adapter is to deal with places on place search
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<Place> placeList;

    private OnPlaceClickListener onPlaceClickListener;

    //Click listener
    private OnGoClickListener onGoClickListener;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

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

    public void updatePlaces(List<Place> newPlaces) {
        this.placeList.clear();
        this.placeList.addAll(newPlaces);
        notifyDataSetChanged(); //from parent
    }


    // Implementation of the click listner
    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.onPlaceClickListener = listener;
    }

    public interface OnGoClickListener {
        void onGoClick(Place place);
    }

    public void setOnGoClickListener(OnGoClickListener listener) {
        this.onGoClickListener = listener;
    }


    // Implemetention of Holder

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView title, address, status;
        RatingBar ratingBar;
        Button btnGo;

        PlaceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
            status = itemView.findViewById(R.id.text_status);
            ratingBar = itemView.findViewById(R.id.place_rating);
            btnGo = itemView.findViewById(R.id.btnGo);
        }
    }
}