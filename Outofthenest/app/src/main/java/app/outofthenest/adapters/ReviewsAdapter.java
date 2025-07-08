package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.outofthenest.R;
import app.outofthenest.models.Review;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to deal with review in the Place detail screen
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<Review> reviewList;

    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.txvReviewTitle.setText(review.getTitle());
        holder.txvDescription.setText(review.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.txvDateTime.setText(sdf.format(review.getDatetime()));
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txvReviewTitle, txvDescription, txvDateTime;
        RatingBar ratingBar;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txvReviewTitle = itemView.findViewById(R.id.txvReviewTitle);
            txvDescription = itemView.findViewById(R.id.txv_description);
            txvDateTime = itemView.findViewById(R.id.txvDateTime);
            ratingBar = itemView.findViewById(R.id.reviewRating);
        }
    }
}