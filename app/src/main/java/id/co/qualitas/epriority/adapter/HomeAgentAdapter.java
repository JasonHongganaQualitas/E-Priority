package id.co.qualitas.epriority.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.RowViewBookingsBinding;
import id.co.qualitas.epriority.fragment.HomeAgentFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Booking;

public class HomeAgentAdapter extends RecyclerView.Adapter<HomeAgentAdapter.ViewHolder> implements Filterable {
    private List<Booking> mList;
    private List<Booking> mFilteredList;
    private boolean onGoing = false;
    private HomeAgentFragment mContext;
    private OnAdapterListener onAdapterListener;

    public HomeAgentAdapter(HomeAgentFragment mContext, List<Booking> mList, boolean onGoing, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.onGoing = onGoing;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<Booking> filteredList) {
        this.mList = filteredList;
        this.mFilteredList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowViewBookingsBinding binding;
        OnAdapterListener onAdapterListener;

        public ViewHolder(RowViewBookingsBinding binding, OnAdapterListener onAdapterListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public HomeAgentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewBookingsBinding binding = RowViewBookingsBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAgentAdapter.ViewHolder holder, int position) {
        Booking booking = mFilteredList.get(position);
        String date = null, time = null;
        if (Helper.isNullOrEmpty(booking.getFlight_date())) {
            date = "-";
        } else {
            date = Helper.changeFormatDate1(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_8, booking.getFlight_date());
        }

        if (Helper.isNullOrEmpty(booking.getFlight_time())) {
            time = "-";
        } else {
            time = Helper.changeFormatDate1(Constants.DATE_PATTERN_13, Constants.DATE_PATTERN_9, booking.getFlight_time());
        }
        String flightInfo = "";
        if (Helper.isNullOrEmpty(booking.getCity())) {
            flightInfo = "";
        } else {
            flightInfo = booking.getCity();
        }

        if (Helper.isNullOrEmpty(booking.getFlight_no())) {
            flightInfo = flightInfo + "";
        } else {
            flightInfo = flightInfo + " - Flight "  + booking.getFlight_no();
        }

        holder.binding.tvName.setText(Helper.isEmpty(booking.getCustomer_name(), ""));
        holder.binding.tvBookingId.setText("Booking ID: #" + Helper.isEmpty(booking.getBooking_id(), ""));
        holder.binding.tvLocation.setText(flightInfo);
        holder.binding.tvPeople.setText(booking.getPassenger_count() + " People");
        holder.binding.tvDate.setText(date + " at " + time);
        holder.binding.tvStatus.setText(Helper.isEmpty(booking.getStatus(), ""));

        Context context = holder.itemView.getContext();
        if (!onGoing) {
            holder.binding.actionButtons.setVisibility(ViewGroup.VISIBLE);
            holder.binding.btnAccept.setVisibility(View.VISIBLE);
            holder.binding.btnDecline.setVisibility(View.VISIBLE);

            holder.binding.tvStatus.setTextColor(context.getColor(R.color.textPending));
            holder.binding.tvStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.badgePending)
            );
        } else {
            holder.binding.actionButtons.setVisibility(ViewGroup.GONE);
            holder.binding.btnAccept.setVisibility(View.GONE);
            holder.binding.btnDecline.setVisibility(View.GONE);
            if (booking.status.equals(mContext.getString(R.string.upcoming))) {
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.textUpcoming));
                holder.binding.tvStatus.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.badgeUpcoming)
                );
            } else {
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.textActive));
                holder.binding.tvStatus.setBackgroundTintList(
                        ContextCompat.getColorStateList(context, R.color.badgeActive)
                );
            }
        }

        // Click listeners
        holder.binding.btnDecline.setOnClickListener(v -> {
            // TODO: handle decline
        });

        holder.binding.btnAccept.setOnClickListener(v -> {
            // TODO: handle accept
        });
        holder.binding.cvBooking.setOnClickListener(v -> mContext.callBookingDetailsFragment(booking));
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mList;
                } else {
                    List<Booking> filteredList = new ArrayList<>();
                    for (Booking row : mList) {

                        /*filter by name*/
                        if (String.valueOf(row.getBooking_id()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Booking>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnAdapterListener {
        void onAdapterClick(Booking detail, int pos);
    }
}
