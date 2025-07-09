package com.example.e_priority;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PassengerInformationFragment extends Fragment {
    View view;
    ImageView backBtn;
    TextView btnChoose;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_passenger_information, container, false);

        initialize();

        backBtn.setOnClickListener(v -> {

        });

        btnChoose.setOnClickListener(v -> {
            ChoosePackageFragment fragment = new ChoosePackageFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        return view;
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        btnChoose = view.findViewById(R.id.btnChoose);
    }
}