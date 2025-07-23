package id.co.qualitas.epriority.activity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.databinding.FragmentModifyBookingBinding;
import id.co.qualitas.epriority.model.Agent;

public class ModifyBookingActivity extends BaseActivity {
    AgentAdapter adapter;
    List<Agent> mList = new ArrayList<>();
    private FragmentModifyBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = FragmentModifyBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        iniAdapter();

        binding.btnConfirm.setOnClickListener(v -> {
            dialogUpdateSubmitted();
        });

        binding.btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void dialogUpdateSubmitted() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_submitted, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyBookingActivity.this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(() -> {
//                BookingDetailsActivity fragment = new BookingDetailsActivity();
//                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        }, 500);
    }

    private void iniAdapter() {
        binding.agentRV.setLayoutManager(new LinearLayoutManager(ModifyBookingActivity.this));
        adapter = new AgentAdapter(ModifyBookingActivity.this, mList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(adapter);
    }
}