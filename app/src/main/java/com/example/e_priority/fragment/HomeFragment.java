package com.example.e_priority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e_priority.R;

public class HomeFragment extends Fragment {
    View view;
    LinearLayout destinationLL, calendarLL;
    TextView btnCreateTrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initialize();
        destinationLL.setVisibility(View.GONE);
        calendarLL.setVisibility(View.GONE);
        btnCreateTrip.setVisibility(View.GONE);

        return view;
    }

    private void initialize() {
        destinationLL = view.findViewById(R.id.destinationLL);
        calendarLL = view.findViewById(R.id.calendarLL);
        btnCreateTrip = view.findViewById(R.id.btnCreateTrip);
    }
}