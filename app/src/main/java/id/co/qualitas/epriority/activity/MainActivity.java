package id.co.qualitas.epriority.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import id.co.qualitas.epriority.databinding.ActivityMainBinding;

import id.co.qualitas.epriority.R;

import id.co.qualitas.epriority.fragment.HomeAgentFragment;
import id.co.qualitas.epriority.fragment.HomeCustomerFragment;
import id.co.qualitas.epriority.fragment.ProfileFragment;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    int currentFabIconId = R.drawable.ic_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();
        initialize();


    }

    private void initialize() {
        if(user.getRole().equals("AGENT")){
            currentFabIconId = R.drawable.ic_scan;
            replaceFragment(new HomeAgentFragment());
        }else{
            currentFabIconId = R.drawable.ic_add;
            replaceFragment(new HomeCustomerFragment());
        }
        binding.fab.setImageResource(currentFabIconId);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home){
                replaceFragment(new HomeCustomerFragment());
            }
            else if (item.getItemId() == R.id.action_profile){
                replaceFragment(new ProfileFragment());
            }
            else {
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFabIconId == R.drawable.ic_scan){

                }else{

                }
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}