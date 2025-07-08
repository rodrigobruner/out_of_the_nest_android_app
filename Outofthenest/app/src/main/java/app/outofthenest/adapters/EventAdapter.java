package app.outofthenest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.outofthenest.R;
import app.outofthenest.models.Event;
import app.outofthenest.models.Place;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // To use Log.d(TAG, "message") for debugging
    String TAG = getClass().getSimpleName();

    private List<Event> eventList;

    private OnEventClickListener onEventClickListener;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.txvEventTitle.setText(event.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.txvEventDate.setText(sdf.format(event.getDatetime()));
        holder.txvEventDescription.setText(event.getDescription());
        holder.txvEventAudience.setText(android.text.TextUtils.join(", ", event.getTargetAudience()));

        holder.itemView.setOnClickListener(v -> {
            if (onEventClickListener != null) {
                onEventClickListener.OnEventClickListener(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    public void updateEvents(List<Event> newEvents) {
        this.eventList = new ArrayList<>(newEvents);
        notifyDataSetChanged();
    }

    public interface OnEventClickListener {
        void OnEventClickListener(Event event);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.onEventClickListener = listener;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView txvEventTitle, txvEventDate, txvEventDescription, txvEventAudience;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txvEventTitle = itemView.findViewById(R.id.txvEventTitle);
            txvEventDate = itemView.findViewById(R.id.txvEventDate);
            txvEventDescription = itemView.findViewById(R.id.txvEventDescription);
            txvEventAudience = itemView.findViewById(R.id.txvEventAudience);
        }
    }
}