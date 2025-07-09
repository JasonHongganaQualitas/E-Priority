package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.BookingHistoryAdapter;

public class ProfileFragment extends Fragment {
    View view;
    RecyclerView bookingHistoryRV;
    BookingHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initialize();
        iniAdapter();

        return view;
    }

    private void iniAdapter() {
        bookingHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookingHistoryAdapter();
        bookingHistoryRV.setAdapter(adapter);
    }

    private void initialize() {
        bookingHistoryRV = view.findViewById(R.id.bookingHistoryRV);
    }
}