package id.co.qualitas.epriority.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.LoginActivity;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.adapter.BookingHistoryAdapter;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentOngoingBookingBinding;
import id.co.qualitas.epriority.databinding.FragmentProfileBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.session.SessionManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    View view;
    BookingHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initialize();
        iniAdapter();
        return binding.getRoot();
    }

    private void iniAdapter() {
        binding.bookingHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingHistoryAdapter();
        binding.bookingHistoryRV.setAdapter(adapter);
    }

    private void initialize() {
        binding.lLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).callLogout();
            }
        });
    }
}