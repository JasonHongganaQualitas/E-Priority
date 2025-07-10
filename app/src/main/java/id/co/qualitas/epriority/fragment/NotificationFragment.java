package id.co.qualitas.epriority.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.adapter.NotificationAdapter;
import id.co.qualitas.epriority.databinding.FragmentNotificationBinding;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.Notification;

public class NotificationFragment extends Fragment implements IOnBackPressed{

    private FragmentNotificationBinding binding;
    private NotificationAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Sample notification data
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("New trip Assignment",
                "You have been assigned to assist John Smith (Flight AA123 to Paris)",
                "Jul 08, 10:19", true));
        notifications.add(new Notification("New trip Assignment",
                "You have been assigned to assist John Smith (Flight AA123 to Paris)",
                "Jul 08, 10:19", true));
        notifications.add(new Notification("New trip Assignment",
                "You have been assigned to assist John Smith (Flight AA123 to Paris)",
                "Jul 08, 10:19", false));

        // Setup RecyclerView
        adapter = new NotificationAdapter(this, notifications);
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNotifications.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // prevent memory leaks
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
