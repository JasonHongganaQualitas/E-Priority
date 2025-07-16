package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.model.Trips;

public class OngoingTripAdapter extends RecyclerView.Adapter<OngoingTripAdapter.ViewHolder> {
    List<Trips> tripsList;

    public OngoingTripAdapter(List<Trips> tripsList) {
        this.tripsList = tripsList;
    }

    @NonNull
    @Override
    public OngoingTripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ongoing_trip, parent, false);
        return new OngoingTripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingTripAdapter.ViewHolder holder, int position) {
        holder.bookingIdTxt.setText("Booking ID:" + tripsList.get(position).getBookingId());
        holder.flightTxt.setText("Flight " + tripsList.get(position).getFlightNo());
        holder.statusTxt.setText(tripsList.get(position).getStatus());
        String dateRaw = tripsList.get(position).getTripDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            Date date = inputFormat.parse(dateRaw);

            String formattedDate = outputFormat.format(date);
            holder.dateTxt.setText(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.destinationTxt.setText(tripsList.get(position).getRouteTo());
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, descTxt, flightTxt, statusTxt, bookingIdTxt, dateTxt, destinationTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            descTxt = itemView.findViewById(R.id.descTxt);
            flightTxt = itemView.findViewById(R.id.flightTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            bookingIdTxt = itemView.findViewById(R.id.bookingIdTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            destinationTxt = itemView.findViewById(R.id.destinationTxt);
        }
    }
}
