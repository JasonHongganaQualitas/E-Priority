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
import id.co.qualitas.epriority.model.TripsResponse;

public class PendingBookingAdapter extends RecyclerView.Adapter<PendingBookingAdapter.ViewHolder> {

    private final List<TripsResponse> tripsResponses;
    private PendingBookingFragment mContext;

    public PendingBookingAdapter(PendingBookingFragment mContext, List<TripsResponse> tripsResponses) {
        this.tripsResponses = tripsResponses;
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
        TripsResponse tripsResponse = tripsResponses.get(position);
        Context context = holder.itemView.getContext();
        holder.binding.tvStatus.setTextColor(context.getColor(R.color.textPending));
        holder.binding.tvStatus.setBackgroundTintList(
                ContextCompat.getColorStateList(context, R.color.badgePending)
        );
        holder.binding.tvStatus.setText(tripsResponse.status);
        holder.binding.tvName.setText(tripsResponse.name);
        holder.binding.tvBookingId.setText("Booking ID: " + tripsResponse.booking_id);
        holder.binding.tvDate.setText(tripsResponse.dateTime);
        holder.binding.tvLocation.setText(tripsResponse.locationAndFlight);
        holder.binding.tvPeople.setText(tripsResponse.peopleCount + " People");

        // Show buttons only if status is "Pending"
        if ("Pending".equalsIgnoreCase(tripsResponse.status)) {
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
                mContext.callBookingDetailsFragment(tripsResponse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripsResponses.size();
    }
}
