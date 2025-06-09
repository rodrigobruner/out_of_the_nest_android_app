package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import app.outofthenest.R;
import app.outofthenest.models.Place;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
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
        holder.time.setText(place.getTime());
        holder.distance.setText(place.getDistance());
        holder.status.setText(place.getStatus());
        holder.ratingBar.setRating(place.getRating());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView title, address, time, distance, status;
        RatingBar ratingBar;

        PlaceViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.place_name);
            address = itemView.findViewById(R.id.place_address);
            time = itemView.findViewById(R.id.place_time);
            distance = itemView.findViewById(R.id.place_distance);
            status = itemView.findViewById(R.id.text_status);
            ratingBar = itemView.findViewById(R.id.place_rating);
        }
    }
}