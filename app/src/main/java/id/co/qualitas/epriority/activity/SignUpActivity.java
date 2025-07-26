package id.co.qualitas.epriority.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.ActivitySignUpBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.SignUp;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    boolean showPassword = false, showConfirmPassword = false;
    private String email, phoneNumber, password, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                finish();
                intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnBack.setOnClickListener(v -> {
//            getOnBackPressedDispatcher().onBackPressed();
            intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.txtSignIn.setOnClickListener(v -> {
            intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.imgShowPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.ic_pass_show));
                showPassword = true;
            } else {
                // hide password
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.ic_pass_hide));
                showPassword = false;
            }
            binding.etPassword.setSelection(Helper.isEmpty(binding.etPassword) ? 0 : binding.etPassword.getText().length());
        });

        binding.imgShowConfirmPassword.setOnClickListener(view -> {
            if (!showConfirmPassword) {
                //show password
                binding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.ic_pass_show));
                showConfirmPassword = true;
            } else {
                // hide password
                binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowConfirmPassword.setImageDrawable(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.ic_pass_hide));
                showConfirmPassword = false;
            }
            binding.etConfirmPassword.setSelection(Helper.isEmpty(binding.etConfirmPassword) ? 0 : binding.etConfirmPassword.getText().length());
        });

        binding.btnSignup.setOnClickListener(v -> {
            int error = 0;
            if (binding.etEmail.getText().toString().isEmpty()) {
                binding.etEmail.setError("This value is required.");
                error++;
            } else if (!Helper.isValidEmail(binding.etEmail.getText().toString().trim())) {
                binding.etEmail.setError("Invalid email address.");
                error++;
            }
            if (binding.etPhoneNumber.getText().toString().isEmpty()) {
                binding.etPhoneNumber.setError("This value is required.");
                error++;
            }
            if (binding.etFullName.getText().toString().isEmpty()) {
                binding.etFullName.setError("This value is required.");
                error++;
            }
            if (binding.etPassword.getText().toString().isEmpty()) {
                binding.etPassword.setError("This value is required.");
                error++;
            }
            if (binding.etConfirmPassword.getText().toString().isEmpty()) {
                binding.etConfirmPassword.setError("This value is required.");
                error++;
            }
            if (!binding.etPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
                binding.etConfirmPassword.setError("Passwords do not match.");
                error++;
            }
            if (error == 0) {
                fullName = binding.etFullName.getText().toString().trim();
                email = binding.etEmail.getText().toString().trim();
                phoneNumber = binding.etPhoneNumber.getText().toString().trim();
                password = binding.etPassword.getText().toString().trim();
                signUp();
            }
        });
    }


    public void signUp() {
        openDialogProgress();
        dialog.show();
        SignUp signUp = new SignUp();
        signUp.setName(fullName);
        signUp.setUsername(email);
        signUp.setPhoneNumber(phoneNumber);
        signUp.setPassword(password);
        apiInterface = RetrofitAPIClient.getClientWithoutCookies().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.signUp(signUp);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            openDialog("Success", result.getMessage());
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast("Failed");
                    }
                } else {
                    setToast("Failed");
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

    public void openDialog(String title, String msg) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_information);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        txtTitle.setVisibility(View.GONE);
        txtTitle.setText(title);
        txtMsg.setText(msg);

        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
            onBackPressed();
        });
        dialog.show();
    }
}