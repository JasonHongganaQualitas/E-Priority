package com.example.e_priority;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_priority.adapter.PassangerAdapter;

public class AdditionalPassengerFragment extends Fragment {
    View view;
    ImageView backBtn;
    RecyclerView passengerRV;
    PassangerAdapter adapter;
    TextView btnAdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_additional_passenger, container, false);

        initialize();
        iniAdapter();
        backBtn.setOnClickListener(v -> {

        });

        btnAdd.setOnClickListener(v -> {

        });

        return view;
    }

    private void iniAdapter() {
        passengerRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PassangerAdapter();
        passengerRV.setAdapter(adapter);
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        passengerRV = view.findViewById(R.id.passengerRV);
        btnAdd = view.findViewById(R.id.btnAdd);
    }
}