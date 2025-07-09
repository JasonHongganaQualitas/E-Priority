package com.example.e_priority;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    View view;
    LinearLayout flightDetailsLL;
    TextView btnContinue, btnSearchFlight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initialize();
        flightDetailsLL.setVisibility(View.GONE);
        btnContinue.setVisibility(View.GONE);

        btnSearchFlight.setOnClickListener(v -> {
            flightDetailsLL.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
        });

        btnContinue.setOnClickListener(v -> {
            PassengerInformationFragment fragment = new PassengerInformationFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        return view;
    }

    private void initialize() {
        flightDetailsLL = view.findViewById(R.id.flightDetailsLL);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnSearchFlight = view.findViewById(R.id.btnSearchFlight);
    }

}