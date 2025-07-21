package id.co.qualitas.epriority.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentPassengerInformationBinding;

public class PassengerInformationActivity extends BaseActivity {
    FragmentPassengerInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = FragmentPassengerInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnChoose.setOnClickListener(v -> {
            intent = new Intent(getApplicationContext(), ChoosePackageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        String[] travelClasses = {
                "Economy Class",
                "Premium Economy Class",
                "Business Class",
                "First Class"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplication(),
                android.R.layout.simple_spinner_dropdown_item,
                travelClasses
        );
        binding.spnTravelClass.setAdapter(adapter);

        binding.llAddPassenger.setOnClickListener(v -> {
            intent = new Intent(getApplicationContext(), AdditionalPassengerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
    }
}