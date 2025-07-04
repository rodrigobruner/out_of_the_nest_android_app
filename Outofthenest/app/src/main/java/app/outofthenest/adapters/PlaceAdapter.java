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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<Place> placeList;
    private OnPlaceClickListener onPlaceClickListener;
    private OnGoClickListener onGoClickListener;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

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

    // CORRECTED: Use placeList instead of places
    public void updatePlaces(List<Place> newPlaces) {
        this.placeList.clear();
        this.placeList.addAll(newPlaces);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place_card, parent, false);
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