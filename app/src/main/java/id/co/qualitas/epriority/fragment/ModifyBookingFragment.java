package id.co.qualitas.epriority.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;

public class ModifyBookingFragment extends BaseFragment {
    View view;
    TextView btnConfirm, btnCancel;
    RecyclerView agentRV;
    AgentAdapter adapter;
    ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modify_booking, container, false);

        initialize();
        iniAdapter();

        btnConfirm.setOnClickListener(v -> {
            dialogUpdateSubmitted();
        });

        btnCancel.setOnClickListener(v -> {

        });

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void dialogUpdateSubmitted() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_submitted, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BookingDetailsFragment fragment = new BookingDetailsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
            }
        }, 500);
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
        backBtn = view.findViewById(R.id.backBtn);
    }
}