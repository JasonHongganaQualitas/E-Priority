package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.CardOngoingTripBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.TripsResponse;


public class OnGoingTripAdapter extends RecyclerView.Adapter<OnGoingTripAdapter.ViewHolder> implements Filterable {
    private List<TripsResponse> mList, mFilteredList;
    private Fragment mContext;
    private OnAdapterListener onAdapterListener;

    public OnGoingTripAdapter(Fragment mContext, List<TripsResponse> mList, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<TripsResponse> filteredList) {
        this.mList = filteredList;
        this.mFilteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardOngoingTripBinding binding = CardOngoingTripBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int posi) {
        TripsResponse tripsResponse = mFilteredList.get(holder.getAdapterPosition());
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

        holder.binding.bookingIdTxt.setText("Booking Trips No.: #" + tripsResponse.getId());
        holder.binding.destinationTxt.setText(Helper.isEmpty(tripsResponse.getRoute_to(), ""));
        holder.binding.flightTxt.setText("Flight " + Helper.isEmpty(tripsResponse.getFlight_no(), ""));
        holder.binding.dateTxt.setText(date + " at " + time);
        holder.binding.statusTxt.setText(Helper.isEmpty(tripsResponse.getStatus(), ""));
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

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnAdapterListener onAdapterListener;
        CardOngoingTripBinding binding;

        public ViewHolder(@NonNull CardOngoingTripBinding binding, OnAdapterListener onAdapterListener) {
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

    public interface OnAdapterListener {
        void onAdapterClick(TripsResponse detail, int pos);
    }
}
