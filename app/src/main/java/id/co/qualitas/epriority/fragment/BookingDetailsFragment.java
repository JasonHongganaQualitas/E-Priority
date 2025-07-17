package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;

public class BookingDetailsFragment extends Fragment {
    View view;
    TextView btnViewQR, btnModifyTrip;
    RecyclerView agentRV;
    AgentAdapter adapter;
    ImageView backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking_details, container, false);

        initialiaze();
        initAdapter();

        btnViewQR.setOnClickListener(v -> {
            QRFragment fragment = new QRFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        btnModifyTrip.setOnClickListener(v -> {
            ModifyBookingFragment fragment = new ModifyBookingFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void initAdapter() {
        agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter();
        agentRV.setAdapter(adapter);
    }

    private void initialiaze() {
        btnViewQR = view.findViewById(R.id.btnViewQR);
        btnModifyTrip = view.findViewById(R.id.btnModifyTrip);
        agentRV = view.findViewById(R.id.agentRV);
        backBtn = view.findViewById(R.id.backBtn);
    }
}