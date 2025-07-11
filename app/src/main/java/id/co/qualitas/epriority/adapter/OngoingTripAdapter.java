package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.co.qualitas.epriority.R;

public class OngoingTripAdapter extends RecyclerView.Adapter<OngoingTripAdapter.ViewHolder> {
    @NonNull
    @Override
    public OngoingTripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ongoing_trip, parent, false);
        return new OngoingTripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingTripAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, descTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            descTxt = itemView.findViewById(R.id.descTxt);

        }
    }
}
