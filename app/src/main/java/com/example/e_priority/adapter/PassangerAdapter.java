package com.example.e_priority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_priority.R;

public class PassangerAdapter extends RecyclerView.Adapter<PassangerAdapter.ViewHolder> {
    @NonNull
    @Override
    public PassangerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_passenger, parent, false);
        return new PassangerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassangerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxt;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
