package app.outofthenest.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app.outofthenest.R;
import app.outofthenest.models.Notification;
import app.outofthenest.utils.NotificationIconMapper;
import app.outofthenest.utils.NotificationsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to show notifications in a RecyclerView.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private OnNotificationClickListener onNotificationClickListener;

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateTime = notification.getDate() != null ? notification.getDate().toString() : "Unknown Date";
        if (notification.getDate() != null) {
            dateTime = sdf.format(notification.getDate());
        }
        holder.txvTitle.setText(notification.getTitle());
        holder.txvTitle.setCompoundDrawablesWithIntrinsicBounds(
                NotificationIconMapper.getTypeIcon(notification.getType()),
                0,
                NotificationIconMapper.getStatusIcon(NotificationsUtils.isRead(holder.itemView.getContext(), notification.getId())),
                0
        );
        holder.txvDatetime.setText(dateTime);
        holder.txvMessage.setText(notification.getMessage());

        holder.itemView.setOnClickListener(v -> {
            if (onNotificationClickListener != null) {
                onNotificationClickListener.onNotificationClick(notification);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    public ArrayList<Notification> getNotificationList() {
        return new ArrayList<>(notificationList);
    }

    public void removeNotification(int position) {
        notificationList.remove(position);
        notifyItemRemoved(position);
    }

    // deal with notification click events
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.onNotificationClickListener = listener;
    }

    // ViewHolder class
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
//        public BreakIterator txvMessage;
        TextView txvTitle, txvDatetime, txvMessage;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txvTitle = itemView.findViewById(R.id.txv_title);
            txvDatetime = itemView.findViewById(R.id.txv_datetime);
            txvMessage = itemView.findViewById(R.id.txv_message);
        }
    }
}