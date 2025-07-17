package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.model.Booking;

public class OngoingTripFragment extends Fragment {
    View view;
    ImageView backBtn;
    RecyclerView ongoingTripRV;
    private List<Booking> mList = new ArrayList<>();
    OnGoingTripAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ongoing_trip, container, false);

        initialize();
        initAdapter();

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void initAdapter() {
        ongoingTripRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OnGoingTripAdapter(OngoingTripFragment.this, mList, (header, pos) -> {
        });
        ongoingTripRV.setAdapter(adapter);
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        ongoingTripRV = view.findViewById(R.id.ongoingTripRV);
    }
}