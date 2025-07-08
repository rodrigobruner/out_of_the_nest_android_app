package app.outofthenest.utils;

import app.outofthenest.R;

public class NotificationIconMapper {
    public static int getTypeIcon(String type) {
        switch (type.toUpperCase()) {
            case "INFO":
                return R.drawable.ic_msg_info;
            case "EVENT":
                return R.drawable.ic_msg_event;
            case "MESSAGE":
                return R.drawable.ic_msg_message;
            default:
                return R.drawable.ic_msg_default;
        }
    }

    public static int getStatusIcon(boolean read) {
        return read ? R.drawable.ic_msg_read : R.drawable.ic_msg_unread;
    }
}