package id.co.qualitas.epriority.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import id.co.qualitas.epriority.R;

import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Employee;
import id.co.qualitas.epriority.model.LoginResponse;
import id.co.qualitas.epriority.model.SignUp;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import id.co.qualitas.epriority.session.SessionManager;

import id.co.qualitas.epriority.databinding.ActivityLoginBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private User user;
    private String registerID;
    private String username, password;
    private String token;

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
        session = new SessionManager(this);

        if (Helper.getItemParam(Constants.REGIISTERID) == null) {
            try {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                String refreshedToken = task.getResult();
//                                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                Helper.setItemParam(Constants.REGIISTERID, refreshedToken);
                                registerID = refreshedToken;

                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), getString(R.string.getTokenError), Toast.LENGTH_LONG).show();
            }
        } else {
            registerID = Helper.getItemParam(Constants.REGIISTERID).toString();
        }

        if (session.isRememberMe()) {
            Map<String, String> rememberMe = new HashMap<>();
            rememberMe = session.getRememberMe();
            binding.etUsername.setText(rememberMe.get(Constants.KEY_USER_ID));
            binding.etPassword.setText(rememberMe.get(Constants.KEY_PASSWORD));
            binding.rememberMeCB.setChecked(true);
        }

        getRegisterID();
        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if (binding.etUsername.getText().toString().isEmpty()) {
                Snackbar.make(binding.lParent, R.string.emailCannotEmpty, Snackbar.LENGTH_SHORT).show();
            } else if (binding.etPassword.getText().toString().isEmpty()) {
                Snackbar.make(binding.lParent, R.string.passwordCannotEmpty, Snackbar.LENGTH_SHORT).show();
            } else {
                username = binding.etUsername.getText().toString().trim().toLowerCase();
                password = binding.etPassword.getText().toString().trim();
                openDialogProgress();
                getToken();
//                user = new User();
//                if (binding.etUsername.getText().toString().toUpperCase().equals("AGENT")) {
//                    user.setRole("AGENT");
//                    user.setName("Agent");
//                    user.setEmail("agent@gmail.com");
//                    user.setPhone("12345678");
//                } else {
//                    user.setRole("CUSTOMER");
//                    user.setName("Customer");
//                    user.setEmail("customer@gmail.com");
//                    user.setPhone("12345678");
//                }
//                new SessionManager(getApplicationContext()).createLoginSession(Helper.objectToString(user));
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//                finish();
            }
        });
    }

    public void getToken() {
//        String pwd = "&".concat("password").concat("=") + password;
//        String email1 = "&" + Constants.USERNAME.concat("=") + username;
//        String baseUrl = Helper.getItemParam(Constants.BASE_URL).toString();
//        final String url = baseUrl.concat(Constants.OAUTH_TOKEN_PATH).concat(Constants.QUESTION_MARK).concat(Constants.GRANT_TYPE.concat(email1).concat(pwd));
        SignUp signUp = new SignUp();
        signUp.setUsername(username);
        signUp.setPassword(password);
        apiInterface = RetrofitAPIClient.getClientWithoutCookies().create(APIInterface.class);
        Call<LoginResponse> httpRequest = apiInterface.getToken(signUp);
        httpRequest.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse result = response.body();
                    if (result != null) {
                        if (result.getStatus() == 200) {
                            token = result.getAccess_token();
                            getEmployeeDetail();
                        } else {
                            setToast(getString(R.string.wrongUser));
                            dialog.dismiss();
                        }
//                        if (String.valueOf(result.getError()).equals("invalid_grant")) {
//                            setToast(getString(R.string.wrongUser));
//                            dialog.dismiss();
//                        } else {
//                            token = result.getAccess_token();
//                            getEmployeeDetail();
//                        }
                    } else {
                        openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                        dialog.dismiss();
                    }
                } else {
                    setToast(getResources().getString(R.string.wrongUser));
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
            }

        });
    }

    private void getEmployeeDetail() {
        Helper.setItemParam(Constants.TOKEN, token);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getEmployeeDetail(new Employee(registerID));
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        String jsonInString = new Gson().toJson(result.getResult());
                        User user = new Gson().fromJson(jsonInString, User.class);
                        if (user != null) {
                            user.setDateLogin(Helper.getDateNow(Constants.PATTERN_DATE_3));
//                            if(employee.getGroup_id().equals(Constants.ROLE_ATTENDANCE) || employee.getGroup_id().equals(Constants.ROLE_REGISTER)) {
                            if (binding.rememberMeCB.isChecked()) {
                                new SessionManager(getApplicationContext()).createRememberMeSession(binding.etUsername.getText().toString(), binding.etPassword.getText().toString());
                            } else {
                                new SessionManager(getApplicationContext()).clearRememberMe();
                            }
                            new SessionManager(getApplicationContext()).createTokenSession(token);
                            new SessionManager(getApplicationContext()).createLoginSession(Helper.objectToString(user));

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    private void getRegisterID() {
        if (Helper.getItemParam(Constants.REGIISTERID) == null) {
            try {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String refreshedToken = task.getResult();
//                                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            Helper.setItemParam(Constants.REGIISTERID, refreshedToken);
                            registerID = refreshedToken;

                            // Log and toast
//                                String msg = getString(R.string.msg_token_fmt, token);
//                                Log.d(TAG, msg);
//                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), getString(R.string.getTokenError), Toast.LENGTH_LONG).show();
            }
        } else {
            registerID = Helper.getItemParam(Constants.REGIISTERID).toString();
        }
    }
}