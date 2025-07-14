package id.co.qualitas.epriority.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;

import id.co.qualitas.epriority.databinding.ActivitySplashBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.session.SessionManager;

import com.github.ybq.android.spinkit.BuildConfig;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        testCrashlytic();
        initialize();
    }

    private void initialize() {
        session = new SessionManager(this);
        if (session.isUrlEmpty()) {
            Map<String, String> urlSession = session.getUrl();
            Helper.setItemParam(Constants.BASE_URL, urlSession.get(Constants.KEY_URL));
        } else {
            Helper.setItemParam(Constants.BASE_URL, Constants.BASE_URL);
        }
        String versionName = BuildConfig.VERSION_NAME;
        binding.txtVersion.setText(getResources().getString(R.string.app_name) + " v" + versionName);
        if (session.isLoggedIn()) {
            binding.btnGetStarted.setVisibility(View.GONE);
            Thread background = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        session.logoutUser();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            background.start();
        } else {
            binding.btnGetStarted.setOnClickListener(v -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            });
        }


    }

    public void testCrashlytic() {
        throw new RuntimeException("FIREBASE CRASHLYTICS TEST::" + DateFormat.getDateTimeInstance().format(new Date()));
    }
}