package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import id.co.qualitas.epriority.databinding.RowViewBookingsBinding;
import id.co.qualitas.epriority.databinding.RowViewPassengerDetailsBinding;
import id.co.qualitas.epriority.fragment.PassangerDetailsFragment;
import id.co.qualitas.epriority.model.PassengerDetails;

public class PassengerDetailsAdapter extends RecyclerView.Adapter<PassengerDetailsAdapter.ViewHolder> {

    private List<PassengerDetails> passengers;
    private PassangerDetailsFragment mContext;

    public PassengerDetailsAdapter(PassangerDetailsFragment mContext, List<PassengerDetails> passengers) {
        this.passengers = passengers;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowViewPassengerDetailsBinding binding;

        public ViewHolder(RowViewPassengerDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowViewPassengerDetailsBinding binding = RowViewPassengerDetailsBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PassengerDetails p = passengers.get(position);
        RowViewPassengerDetailsBinding b = holder.binding;

        b.tvPassengerTitle.setText("👤 Passenger Details " + (position + 1));
        b.tvName.setText(p.name);
        b.tvPhone.setText(p.phone);
        b.tvEmail.setText(p.email);
        b.tvDob.setText(p.dob);
        b.tvClass.setText(p.travelClass);
        b.tvPassportNumber.setText(p.passportNumber);
        b.tvCountry.setText(p.issuingCountry);
        b.tvExpiry.setText(p.passportExpiry);
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }
}
