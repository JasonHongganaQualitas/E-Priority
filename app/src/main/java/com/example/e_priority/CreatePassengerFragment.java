package com.example.e_priority;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CreatePassengerFragment extends Fragment {
    View view;
    TextView btnCreate;
    ImageView backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_passenger, container, false);

        initialize();

        btnCreate.setOnClickListener(v -> {

        });

        backBtn.setOnClickListener(v -> {

        });

        return view;
    }

    private void initialize() {
        btnCreate = view.findViewById(R.id.btnCreate);
        backBtn = view.findViewById(R.id.backBtn);
    }
}