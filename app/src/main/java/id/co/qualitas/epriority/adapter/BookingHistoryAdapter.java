package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.TripsResponse;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> implements Filterable {
    private List<TripsResponse> mList, mFilteredList;
    private Fragment mContext;
    private OnAdapterListener onAdapterListener;

    public BookingHistoryAdapter(Fragment mContext, List<TripsResponse> mList, OnAdapterListener onAdapterListener) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_booking_history, parent, false);
        return new ViewHolder(view, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapter.ViewHolder holder, int posi) {
        String date = null;
        if (Helper.isNullOrEmpty(mFilteredList.get(holder.getAdapterPosition()).getTrip_date())) {
            date = "-";
        } else {
            date = Helper.changeFormatDate1(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_8, mFilteredList.get(holder.getAdapterPosition()).getTrip_date());
        }
        holder.titleTxt.setText("Booking ID: #" + Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getBooking_id(), ""));
        holder.txtLocation.setText(Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getRoute_to(), ""));
        holder.txtPrice.setText(Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getFlight_no(), ""));
        holder.txtDate.setText(date);
        holder.statusTxt.setText(Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getStatus(), ""));
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
        TextView titleTxt, txtLocation, txtDate, txtPrice, statusTxt;
        OnAdapterListener onAdapterListener;

        public ViewHolder(@NonNull View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);
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
