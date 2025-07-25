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
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.RowViewBookingsBinding;
import id.co.qualitas.epriority.fragment.OngoingBookingFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.TripsResponse;

public class OngoingBookingAdapter extends RecyclerView.Adapter<OngoingBookingAdapter.ViewHolder> {
    private final List<TripsResponse> tripsResponses;
    private OngoingBookingFragment mContext;

    public OngoingBookingAdapter(OngoingBookingFragment mContext, List<TripsResponse> tripsResponses) {
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
    public OngoingBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewBookingsBinding binding = RowViewBookingsBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingBookingAdapter.ViewHolder holder, int position) {
        TripsResponse tripsResponse = tripsResponses.get(position);
        Context context = holder.itemView.getContext();
        if(tripsResponse.status.equals(mContext.getString(R.string.upcoming))) {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.textUpcoming));
            holder.binding.tvStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.badgeUpcoming)
            );
        }else{
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.textActive));
            holder.binding.tvStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.badgeActive)
            );
        }

        String date = null, time = null;
        if (Helper.isNullOrEmpty(tripsResponse.getFlight_date())) {
            date = "-";
        } else {
            date = Helper.changeFormatDate1(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_8, tripsResponse.getFlight_date());
        }

        if (Helper.isNullOrEmpty(tripsResponse.getFlight_time())) {
            time = "-";
        } else {
            time = Helper.changeFormatDate1(Constants.DATE_PATTERN_13, Constants.DATE_PATTERN_9, tripsResponse.getFlight_time());
        }
        String flightInfo = "";
        if (Helper.isNullOrEmpty(tripsResponse.getCity())) {
            flightInfo = "";
        } else {
            flightInfo = tripsResponse.getCity();
        }

        if (Helper.isNullOrEmpty(tripsResponse.getFlight_no())) {
            flightInfo = flightInfo + "";
        } else {
            flightInfo = flightInfo + " - Flight "  + tripsResponse.getFlight_no();
        }
        holder.binding.tvStatus.setText(tripsResponse.status);
        holder.binding.tvName.setText(Helper.isEmpty(tripsResponse.getCustomer_name(), ""));
        holder.binding.tvBookingId.setText("Booking Trips No.: #" + tripsResponse.getId());
        holder.binding.tvDate.setText(date + " " + time);
        holder.binding.tvLocation.setText(flightInfo);
        holder.binding.tvPeople.setText(tripsResponse.getPassenger_count() + " People");

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
