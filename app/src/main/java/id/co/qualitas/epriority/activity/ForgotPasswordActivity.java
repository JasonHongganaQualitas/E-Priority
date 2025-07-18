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
import id.co.qualitas.epriority.databinding.ActivityForgotPasswordBinding;
import id.co.qualitas.epriority.databinding.ActivitySignUpBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.SignUp;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {
    private ActivityForgotPasswordBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.txtSignIn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.btnSignUp.setOnClickListener(v -> {
            intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.btnSend.setOnClickListener(v -> {
            int error = 0;
            if (binding.etEmail.getText().toString().isEmpty()) {
                binding.etEmail.setError("This value is required.");
                error++;
            } else if (!Helper.isValidEmail(binding.etEmail.getText().toString().trim())) {
                binding.etEmail.setError("Invalid email address.");
                error++;
            }
            if (error == 0) {
                email = binding.etEmail.getText().toString().trim();
                forgotPassword();
            }
        });
    }


    public void forgotPassword() {
        openDialogProgress();
        dialog.show();
        SignUp signUp = new SignUp();
        signUp.setEmail(email);
        apiInterface = RetrofitAPIClient.getClientWithoutCookies().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.forgetPassword(signUp);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                            Helper.setItemParam(Constants.EMAIL_FORGOT_PASSWORD, email);
                            intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordVerificationActivity.class);
                            startActivity(intent);
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
}