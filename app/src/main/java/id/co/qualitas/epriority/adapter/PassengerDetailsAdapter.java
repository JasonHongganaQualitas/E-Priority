package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.qualitas.epriority.databinding.RowViewPassengerDetailsBinding;
import id.co.qualitas.epriority.fragment.PassangerDetailsFragment;
import id.co.qualitas.epriority.model.Passenger;

public class PassengerDetailsAdapter extends RecyclerView.Adapter<PassengerDetailsAdapter.ViewHolder> {

    private List<Passenger> passengers;
    private PassangerDetailsFragment mContext;

    public PassengerDetailsAdapter(PassangerDetailsFragment mContext, List<Passenger> passengers) {
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
        Passenger p = passengers.get(position);
        RowViewPassengerDetailsBinding b = holder.binding;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");

        b.tvPassengerTitle.setText("👤 Passenger Details " + (position + 1));
        b.tvName.setText(p.getFirst_name() + " " + p.getLast_name());
        b.tvPhone.setText(p.getPhone_no());
        b.tvEmail.setText(p.getEmail());
        b.tvDob.setText(p.getBirth_date());
        b.tvClass.setText(p.getFlight_class_name());
        b.tvPassportNumber.setText(p.getPassport_no());
        b.tvCountry.setText(p.getPassport_country_name());
        try {
            Date dateDOB = inputFormat.parse(p.getBirth_date());
            Date dateExp = inputFormat.parse(p.getPassport_expdate());

            b.tvDob.setText(outputFormat.format(dateDOB));
            b.tvExpiry.setText(outputFormat.format(dateExp));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }
}
