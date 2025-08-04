package app.outofthenest.utils;

import app.outofthenest.R;

/**
 * Class to map notification types and icons.
 */
public class NotificationIconMapper {

    public static final String TYPE_INFO = "INFO";
    public static final String TYPE_EVENT = "EVENT";
    public static final String TYPE_MESSAGE = "MESSAGE";

    public static int getTypeIcon(String type) {
        if (type == null) {
            return R.drawable.ic_msg_default;
        }
        switch (type.toUpperCase()) {
            case TYPE_INFO:
                return R.drawable.ic_msg_info;
            case TYPE_EVENT:
                return R.drawable.ic_msg_event;
            case TYPE_MESSAGE:
                return R.drawable.ic_msg_message;
            default:
                return R.drawable.ic_msg_default;
        }
    }

    public static int getStatusIcon(boolean read) {
        return read ? R.drawable.ic_msg_read : R.drawable.ic_msg_unread;
    }
}