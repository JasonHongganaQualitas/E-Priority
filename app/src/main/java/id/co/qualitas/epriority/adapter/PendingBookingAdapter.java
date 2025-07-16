package id.co.qualitas.epriority.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.RowViewBookingsBinding;
import id.co.qualitas.epriority.fragment.PendingBookingFragment;
import id.co.qualitas.epriority.model.Booking;

public class PendingBookingAdapter extends RecyclerView.Adapter<PendingBookingAdapter.ViewHolder> {

    private final List<Booking> bookings;
    private PendingBookingFragment mContext;

    public PendingBookingAdapter(PendingBookingFragment mContext, List<Booking> bookings) {
        this.bookings = bookings;
        this.mContext= mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowViewBookingsBinding binding;

        public ViewHolder(RowViewBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public PendingBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewBookingsBinding binding = RowViewBookingsBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingBookingAdapter.ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        Context context = holder.itemView.getContext();
        holder.binding.tvStatus.setTextColor(context.getColor(R.color.textPending));
        holder.binding.tvStatus.setBackgroundTintList(
                ContextCompat.getColorStateList(context, R.color.badgePending)
        );
        holder.binding.tvStatus.setText(booking.status);
        holder.binding.tvName.setText(booking.name);
        holder.binding.tvBookingId.setText("Booking ID: " + booking.booking_id);
        holder.binding.tvDate.setText(booking.dateTime);
        holder.binding.tvLocation.setText(booking.locationAndFlight);
        holder.binding.tvPeople.setText(booking.peopleCount + " People");

        // Show buttons only if status is "Pending"
        if ("Pending".equalsIgnoreCase(booking.status)) {
            holder.binding.actionButtons.setVisibility(ViewGroup.VISIBLE);
        } else {
            holder.binding.actionButtons.setVisibility(ViewGroup.GONE);
        }

        // Click listeners
        holder.binding.btnDecline.setOnClickListener(v -> {
            // TODO: handle decline
        });

        holder.binding.btnAccept.setOnClickListener(v -> {
            // TODO: handle accept
        });
        holder.binding.cvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.callBookingDetailsFragment(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
