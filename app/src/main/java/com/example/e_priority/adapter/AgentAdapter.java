package com.example.e_priority.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_priority.R;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ViewHolder> {
    @NonNull
    @Override
    public AgentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_agent, parent, false);
        return new AgentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentAdapter.ViewHolder holder, int position) {
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.itemView.setSelected(isChecked);
            if (isChecked){
                holder.agentLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                holder.agentLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout agentLL;
        TextView nameTxt;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            checkBox = itemView.findViewById(R.id.checkBox);
            agentLL = itemView.findViewById(R.id.agentLL);
        }
    }
}
