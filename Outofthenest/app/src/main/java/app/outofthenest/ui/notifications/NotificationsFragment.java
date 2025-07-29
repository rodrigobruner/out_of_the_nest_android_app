package app.outofthenest.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.outofthenest.R;
import app.outofthenest.adapters.NotificationAdapter;
import app.outofthenest.databinding.FragmentNotificationsBinding;
import app.outofthenest.utils.NotificationsUtils;
import app.outofthenest.utils.UserUtils;
import app.outofthenest.models.Notification;

import java.util.ArrayList;

/**
 * Fragment to display notifications.
 */
public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationAdapter adapter;
    private NotificationViewModel viewModel;

//    private AuthenticationViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NotificationAdapter(new ArrayList<>());
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        init();

    }

    private void init(){
        setUpActionBar();
        setupRecyclerView();
        getNotifications();
        setOnNotificationClickListener();
        setupSwipeToDelete();
    }

    // set up the action bar
    public void setUpActionBar() {
        ActionBar actionbar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if(actionbar != null) {
            actionbar.setTitle(R.string.txt_notification_bar_title);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setLogo(R.drawable.ic_menu_notifications);
            actionbar.setDisplayUseLogoEnabled(true);
        }
    }

    //set up the recycler view
    private void setupRecyclerView() {
        binding.recyclerNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerNotifications.setAdapter(adapter);
    }

    // get notifications from the view model
    private void getNotifications() {
        // Observe notifications
        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                adapter.setNotificationList(notifications);
                setImageVisible(notifications.isEmpty());
            }
        });

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch notifications (replace with actual userId)
//        String userId = authViewModel.getUserId().toString();
        String userId = UserUtils.getUser(getContext()).getId();
        viewModel.getNotifications(userId);

    }

    // set up on click listener for notifications
    private void setOnNotificationClickListener(){
        adapter.setOnNotificationClickListener(notification -> {
            int position = adapter.getNotificationList().indexOf(notification);
            notification.setRead(!notification.isRead());
            viewModel.markAsRead(notification.getId());
            NotificationsUtils.setReadStatus(getContext(), notification.getId(), notification.isRead());
            adapter.notifyItemChanged(position);
        });
    }

    // set up delete functionality with swipe
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Notification notification = adapter.getNotificationList().get(position);
                viewModel.deleteNotification(notification.getId());
                adapter.removeNotification(position);
                Toast.makeText(getContext(), R.string.txt_notification_deleted, Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerNotifications);
    }

    // set up read status
    private void setImageVisible(boolean visible) {
        binding.imgNoNotifications.setVisibility(visible ? View.VISIBLE: View.GONE);
        binding.txtNoNotifications.setVisibility(visible ? View.VISIBLE: View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}