package id.co.qualitas.epriority.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import id.co.qualitas.epriority.R;

import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.session.SessionManager;

import id.co.qualitas.epriority.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if(binding.etUsername.getText().toString().isEmpty()){
                Snackbar.make(binding.lParent, R.string.emailCannotEmpty, Snackbar.LENGTH_SHORT).show();
            }else if(binding.etPassword.getText().toString().isEmpty()){
                Snackbar.make(binding.lParent, R.string.passwordCannotEmpty, Snackbar.LENGTH_SHORT).show();
            }else {
                user = new User();
                if (binding.etUsername.getText().toString().toUpperCase().equals("AGENT")) {
                    user.setRole("AGENT");
                    user.setName("Agent");
                    user.setEmail("agent@gmail.com");
                    user.setPhone("12345678");
                } else {
                    user.setRole("CUSTOMER");
                    user.setName("Customer");
                    user.setEmail("customer@gmail.com");
                    user.setPhone("12345678");
                }
                new SessionManager(getApplicationContext()).createLoginSession(Helper.objectToString(user));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
}