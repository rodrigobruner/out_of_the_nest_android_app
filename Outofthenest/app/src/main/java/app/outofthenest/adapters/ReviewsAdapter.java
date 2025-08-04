package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.outofthenest.R;
import app.outofthenest.models.Review;
import app.outofthenest.utils.DateUtils;
import app.outofthenest.utils.Report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to deal with review in the Place detail screen
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<Review> reviewList;

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
        holder.ratingBar.setRating(review.getRating());
        holder.txvDateTime.setText(DateUtils.formatDateTime(
                holder.itemView.getResources(),
                review.getDatetime()));
        holder.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report.Problem(v.getContext(), Report.TYPE_REVIEW, review.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    public ReviewsAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    // ViewHolder class
    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txvReviewTitle, txvDescription, txvDateTime;
        RatingBar ratingBar;

        Button btnReport;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txvReviewTitle = itemView.findViewById(R.id.txvReviewTitle);
            txvDescription = itemView.findViewById(R.id.txv_description);
            txvDateTime = itemView.findViewById(R.id.txvDateTime);
            ratingBar = itemView.findViewById(R.id.reviewRating);
            btnReport = itemView.findViewById(R.id.btn_report);
        }
    }
}