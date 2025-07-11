package id.co.qualitas.epriority.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import id.co.qualitas.epriority.databinding.ActivityMainBinding;

import id.co.qualitas.epriority.R;

import id.co.qualitas.epriority.fragment.BookingDetailsAgentFragment;
import id.co.qualitas.epriority.fragment.BookingDetailsFragment;
import id.co.qualitas.epriority.fragment.HomeAgentFragment;
import id.co.qualitas.epriority.fragment.HomeCustomerFragment;
import id.co.qualitas.epriority.fragment.ProfileFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.session.SessionManager;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    int currentFabIconId = R.drawable.ic_add;
    private ActivityResultLauncher<Intent> qrScannerLauncher;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
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
                if(user.getRole().equals("AGENT")){
                    currentFabIconId = R.drawable.ic_scan;
                    replaceFragment(new HomeAgentFragment());
                }else{
                    currentFabIconId = R.drawable.ic_add;
                    replaceFragment(new HomeCustomerFragment());
                }
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
                    checkCameraPermissionAndStartScanner();
                }else{

                }
            }
        });

        qrScannerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String qr = result.getData().getStringExtra("scanned_qr");
                        // Handle the scanned QR result here
                        Toast.makeText(this, "QR: " + qr, Toast.LENGTH_SHORT).show();
                        replaceFragment(new BookingDetailsAgentFragment());
                    }
                }
        );
    }

    private void checkCameraPermissionAndStartScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            startQrScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQrScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startQrScanner() {
        Intent intent = new Intent(this, CameraActivity.class);
        qrScannerLauncher.launch(intent); // using ActivityResultLauncher
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

    public void callLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.logoutUser();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
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