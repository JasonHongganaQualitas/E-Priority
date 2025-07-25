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
import id.co.qualitas.epriority.databinding.RowViewAirportsBinding;
import id.co.qualitas.epriority.databinding.SpinnerFilteredItemBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Dropdown;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.ViewHolder> implements Filterable {
    private List<Dropdown> mList, mFilteredList;
    private Context mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public AirportAdapter(Context mContext, List<Dropdown> mList, OnAdapterListener onAdapterListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mFilteredList = mList;
        this.onAdapterListener = onAdapterListener;
    }

    public void setFilteredList(List<Dropdown> filteredList) {
        this.mList = filteredList;
        this.mFilteredList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewAirportsBinding binding = RowViewAirportsBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setFormatSeparator();
        Dropdown currentItem = mFilteredList.get(holder.getAdapterPosition());
        String id = currentItem.getId() + " - ";
        String city = Helper.isEmpty(currentItem.getCity(), "");
        String country = Helper.isEmpty(currentItem.getCountry(), "");
        holder.binding.txtIata.setText(Helper.isEmpty(currentItem.getIata(),""));
        holder.binding.txtAirport.setText(Helper.isEmpty(currentItem.getName(),""));
        holder.binding.txtCity.setText(city + ", " + country);
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
                    List<Dropdown> filteredList = new ArrayList<>();
                    for (Dropdown row : mList) {

                        /*filter by name*/
                        if (String.valueOf(row.getId()).toLowerCase().contains(charString.toLowerCase()) ||
                                String.valueOf(row.getName()).toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Dropdown>) filterResults.values;
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
        RowViewAirportsBinding binding;

        public ViewHolder(@NonNull RowViewAirportsBinding binding, OnAdapterListener onAdapterListener) {
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
        void onAdapterClick(Dropdown detail, int pos);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat(Constants.DECIMAL_PATTERN, otherSymbols);
    }
}
