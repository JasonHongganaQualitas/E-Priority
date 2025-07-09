package com.example.e_priority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e_priority.R;

public class BookingDetailsFragment extends Fragment {
    View view;
    TextView btnViewQR, btnModifyTrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking_details, container, false);

        initialiaze();

        btnViewQR.setOnClickListener(v -> {
            QRFragment fragment = new QRFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        btnModifyTrip.setOnClickListener(v -> {
            ModifyBookingFragment fragment = new ModifyBookingFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        return view;
    }

    private void initialiaze() {
        btnViewQR = view.findViewById(R.id.btnViewQR);
        btnModifyTrip = view.findViewById(R.id.btnModifyTrip);
    }
}