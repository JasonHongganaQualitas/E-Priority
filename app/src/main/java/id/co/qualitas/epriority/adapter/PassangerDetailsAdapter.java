package id.co.qualitas.epriority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import id.co.qualitas.epriority.R;

public class PassangerDetailsAdapter extends RecyclerView.Adapter<PassangerDetailsAdapter.ViewHolder> {
    @NonNull
    @Override
    public PassangerDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_passenger_details, parent, false);
        return new PassangerDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassangerDetailsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt, typeTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            typeTxt = itemView.findViewById(R.id.typeTxt);

        }
    }
}
