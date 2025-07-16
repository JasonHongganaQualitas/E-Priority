package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;

public class ModifyBookingFragment extends Fragment {
    View view;
    TextView btnConfirm, btnCancel;
    RecyclerView agentRV;
    AgentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modify_booking, container, false);

        initialize();
        iniAdapter();

        btnConfirm.setOnClickListener(v -> {
            UpdateSubmittedFragment fragment = new UpdateSubmittedFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        btnCancel.setOnClickListener(v -> {

        });

        return view;
    }

    private void iniAdapter() {
        agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter();
        agentRV.setAdapter(adapter);
    }

    private void initialize() {
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        agentRV = view.findViewById(R.id.agentRV);
    }
}