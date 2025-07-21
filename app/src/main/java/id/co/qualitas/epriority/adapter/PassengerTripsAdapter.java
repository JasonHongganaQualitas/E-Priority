package id.co.qualitas.epriority.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.CardAgentBinding;
import id.co.qualitas.epriority.databinding.RowViewPassengerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Agent;
import id.co.qualitas.epriority.model.Passenger;

public class PassengerTripsAdapter extends RecyclerView.Adapter<PassengerTripsAdapter.ViewHolder> implements Filterable {
    private List<Passenger> mList, mFilteredList;
    private Context mContext;
    private OnAdapterListener onAdapterListener;

    public PassengerTripsAdapter(Context mContext, List<Passenger> mList, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<Passenger> filteredList) {
        this.mList = filteredList;
        this.mFilteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewPassengerBinding binding = RowViewPassengerBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Passenger agent = mFilteredList.get(holder.getAdapterPosition());
        String firstName = Helper.isEmpty(agent.getFirst_name(), "");
        String lastName = Helper.isEmpty(agent.getLast_name(), "");
        holder.binding.nameTxt.setText(firstName + " " + lastName);
        holder.binding.imgDelete.setOnClickListener(v -> {
            mList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), mList.size());
        });
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
                    List<Passenger> filteredList = new ArrayList<>();
                    for (Passenger row : mList) {

                        /*filter by name*/
                        if (String.valueOf(row.getFirst_name()).toLowerCase().contains(charString.toLowerCase()) ||
                                String.valueOf(row.getLast_name()).toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Passenger>) filterResults.values;
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
        RowViewPassengerBinding binding;

        public ViewHolder(@NonNull RowViewPassengerBinding binding, OnAdapterListener onAdapterListener) {
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
        void onAdapterClick(Passenger detail, int pos);
    }
}
