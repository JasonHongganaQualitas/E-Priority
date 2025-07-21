package id.co.qualitas.epriority.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import id.co.qualitas.epriority.constants.Constants;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import id.co.qualitas.epriority.databinding.ActivityMainBinding;

import id.co.qualitas.epriority.R;

import id.co.qualitas.epriority.databinding.DialogCreateNewTripBinding;
import id.co.qualitas.epriority.fragment.BookingDetailsAgentFragment;
import id.co.qualitas.epriority.fragment.HomeAgentFragment;
import id.co.qualitas.epriority.fragment.HomeCustomerFragment;
import id.co.qualitas.epriority.fragment.ProfileFragment;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (user.getType().toUpperCase().equals(getResources().getString(R.string.employee))) {
            currentFabIconId = R.drawable.ic_scan;
            replaceFragment(new HomeAgentFragment());
        } else {
            currentFabIconId = R.drawable.ic_add;
            replaceFragment(new HomeCustomerFragment());
        }
        binding.fab.setImageResource(currentFabIconId);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_home) {
                if (user.getType().toUpperCase().equals(getResources().getString(R.string.employee))) {
                    currentFabIconId = R.drawable.ic_scan;
                    replaceFragment(new HomeAgentFragment());
                } else {
                    currentFabIconId = R.drawable.ic_add;
                    replaceFragment(new HomeCustomerFragment());
                }
            } else if (item.getItemId() == R.id.action_profile) {
                replaceFragment(new ProfileFragment());
            } else {
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });

        binding.fab.setOnClickListener(v -> {
            if (currentFabIconId == R.drawable.ic_scan) {
                checkCameraPermissionAndStartScanner();
            } else {
                intent = new Intent(getApplicationContext(), CreateFlightDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
//                openDialogCreateNewTrip();
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

    private void openDialogCreateNewTrip() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        DialogCreateNewTripBinding dialogBinding = DialogCreateNewTripBinding.inflate(LayoutInflater.from(this));
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogBinding.getRoot());
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.setCanceledOnTouchOutside(false);

        dialogBinding.btnSearchFlight.setOnClickListener(v -> {
            dialogBinding.flightDetailsLL.setVisibility(View.VISIBLE);
            dialogBinding.btnContinue.setVisibility(View.VISIBLE);
        });

        dialogBinding.btnContinue.setOnClickListener(v -> {
            alertDialog.dismiss();
            intent = new Intent(getApplicationContext(), PassengerInformationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        alertDialog.show();
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

    private void replaceFragment(Fragment fragment) {
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
                .setPositiveButton("Yes", (dialog, id) -> logOut())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void logOut() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.logOut();
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            if (user.isFromGoogle()) {
                                signOutFromGoogle(getApplicationContext(), false);
                            }
                            session.logoutUser();
                            NotificationManagerCompat.from(getApplicationContext()).cancelAll();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        } else {
                            openDialogInformation(Constants.DATA_NOT_FOUND, response.message(), null);
                        }
                    } else {
                        openDialogInformation(Constants.DATA_NOT_FOUND, response.message(), null);
                    }
                } else {
                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
            }
        });
    }

    public void signOutFromGoogle(Context context, boolean revokeAccess) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);

        if (revokeAccess) {
            googleSignInClient.revokeAccess()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("GoogleSignOut", "Access revoked successfully.");
                        } else {
                            Log.e("GoogleSignOut", "Failed to revoke access.");
                        }
                    });
        } else {
            googleSignInClient.signOut()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("GoogleSignOut", "User signed out successfully.");
                        } else {
                            Log.e("GoogleSignOut", "Sign out failed.");
                        }
                    });
        }

//        // Just sign out (keep access)
//        signOutFromGoogle(this, false);
//
//// Or revoke access (logout + disconnect account)
//        signOutFromGoogle(this, true);
    }
}