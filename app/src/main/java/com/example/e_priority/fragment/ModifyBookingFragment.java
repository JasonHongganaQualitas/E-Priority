package com.example.e_priority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e_priority.R;

public class ModifyBookingFragment extends Fragment {
    View view;
    TextView btnConfirm, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modify_booking, container, false);

        initialize();

        btnConfirm.setOnClickListener(v -> {

        });

        btnCancel.setOnClickListener(v -> {

        });

        return view;
    }

    private void initialize() {
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
    }
}