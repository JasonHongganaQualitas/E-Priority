package com.example.e_priority;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_priority.adapter.AgentAdapter;

public class ChoosePackageFragment extends Fragment {
    View view;
    RecyclerView agentRV;
    AgentAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_package, container, false);

        initialize();
        initAdapter();

        return view;
    }

    private void initAdapter() {
        agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter();
        agentRV.setAdapter(adapter);
    }

    private void initialize() {
        agentRV = view.findViewById(R.id.agentRV);
    }
}