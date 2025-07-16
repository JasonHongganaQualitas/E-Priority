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
import id.co.qualitas.epriority.model.Booking;


public class OnGoingTripAdapter extends RecyclerView.Adapter<OnGoingTripAdapter.ViewHolder> implements Filterable {
    private List<Booking> mList, mFilteredList;
    private Fragment mContext;
    private OnAdapterListener onAdapterListener;

    public OnGoingTripAdapter(Fragment mContext, List<Booking> mList, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<Booking> filteredList) {
        this.mList = filteredList;
        this.mFilteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ongoing_trip, parent, false);
        return new ViewHolder(view, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int posi) {
        String date = null;
        if (Helper.isNullOrEmpty(mFilteredList.get(holder.getAdapterPosition()).getTripDate())) {
            date = "-";
        } else {
            date = Helper.changeFormatDate1(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_8, mFilteredList.get(holder.getAdapterPosition()).getTripDate());
        }
        holder.bookingIdTxt.setText("Booking ID: #" + Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getBooking_id(), ""));
        holder.destinationTxt.setText(Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getRoute_to(), ""));
        holder.flightTxt.setText("flightTxt: " + Helper.isEmpty(mFilteredList.get(holder.getAdapterPosition()).getFlight_no(), ""));
        holder.dateTxt.setText(date);
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

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookingIdTxt, destinationTxt, dateTxt, flightTxt, statusTxt;
        OnAdapterListener onAdapterListener;

        public ViewHolder(@NonNull View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            destinationTxt = itemView.findViewById(R.id.destinationTxt);
            bookingIdTxt = itemView.findViewById(R.id.bookingIdTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            flightTxt = itemView.findViewById(R.id.flightTxt);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface OnAdapterListener {
        void onAdapterClick(Booking detail, int pos);
    }
}
