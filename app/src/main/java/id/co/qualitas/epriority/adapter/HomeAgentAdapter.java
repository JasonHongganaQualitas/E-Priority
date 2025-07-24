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
import id.co.qualitas.epriority.model.TripsResponse;

public class HomeAgentAdapter extends RecyclerView.Adapter<HomeAgentAdapter.ViewHolder> implements Filterable {
    private List<TripsResponse> mList;
    private List<TripsResponse> mFilteredList;
    private boolean onGoing = false;
    private HomeAgentFragment mContext;
    private OnAdapterListener onAdapterListener;

    public HomeAgentAdapter(HomeAgentFragment mContext, List<TripsResponse> mList, boolean onGoing, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.onGoing = onGoing;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<TripsResponse> filteredList) {
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
        TripsResponse tripsResponse = mFilteredList.get(position);
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

        holder.binding.tvName.setText(Helper.isEmpty(tripsResponse.getCustomer_name(), ""));
        holder.binding.tvBookingId.setText("Booking ID: #" + Helper.isEmpty(tripsResponse.getBooking_id(), ""));
        holder.binding.tvLocation.setText(flightInfo);
        holder.binding.tvPeople.setText(tripsResponse.getPassenger_count() + " People");
        holder.binding.tvDate.setText(date + " at " + time);
        holder.binding.tvStatus.setText(Helper.isEmpty(tripsResponse.getStatus(), ""));

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
            if (tripsResponse.status.equals(mContext.getString(R.string.upcoming))) {
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
            mContext.declineBooking(tripsResponse);
        });

        holder.binding.btnAccept.setOnClickListener(v -> {
            // TODO: handle accept
            mContext.acceptBooking(tripsResponse);
        });
        holder.binding.cvBooking.setOnClickListener(v -> mContext.callBookingDetailsFragment(tripsResponse));
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
                    List<TripsResponse> filteredList = new ArrayList<>();
                    for (TripsResponse row : mList) {

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
                mFilteredList = (ArrayList<TripsResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnAdapterListener {
        void onAdapterClick(TripsResponse detail, int pos);
    }
}
