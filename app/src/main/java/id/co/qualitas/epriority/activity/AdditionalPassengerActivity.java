package id.co.qualitas.epriority.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Window;
import android.view.WindowManager;

import id.co.qualitas.epriority.adapter.PassangerAdapter;
import id.co.qualitas.epriority.databinding.FragmentAdditionalPassengerBinding;

public class AdditionalPassengerActivity extends BaseActivity {
    PassangerAdapter adapter;
    FragmentAdditionalPassengerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = FragmentAdditionalPassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();
        iniAdapter();

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnAdd.setOnClickListener(v -> {
            intent = new Intent(getApplicationContext(), PassengerInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        binding.btnCreate.setOnClickListener(v -> {
            intent = new Intent(getApplicationContext(), CreatePassengerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
    }

    private void iniAdapter() {
        binding.passengerRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new PassangerAdapter();
        binding.passengerRV.setAdapter(adapter);
    }
}