package id.co.qualitas.epriority.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.RowViewNotifcationBinding;
import id.co.qualitas.epriority.fragment.NotificationFragment;
import id.co.qualitas.epriority.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<Notification> items;
    private NotificationFragment mContext;

    public NotificationAdapter(NotificationFragment mContext, List<Notification> items) {
        this.items = items;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowViewNotifcationBinding binding;

        public ViewHolder(RowViewNotifcationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewNotifcationBinding binding = RowViewNotifcationBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification item = items.get(position);

        holder.binding.title.setText(item.title);
        holder.binding.description.setText(item.description);
        holder.binding.timestamp.setText(item.timestamp);

        if (item.isUnread) {
            holder.binding.getRoot().setBackground(
                    mContext.getActivity().getDrawable(R.drawable.bg_notification_unread)
            );
            holder.binding.icon.setColorFilter(
                    mContext.getActivity().getColor(R.color.colorPrimary)
            );
        } else {
            holder.binding.getRoot().setBackground(
                    mContext.getActivity().getDrawable(R.drawable.bg_notification_read)
            );
            holder.binding.icon.setColorFilter(
                    mContext.getActivity().getColor(R.color.notifUnread)
            );
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}

