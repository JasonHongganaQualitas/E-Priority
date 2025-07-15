package id.co.qualitas.epriority.activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private User user;
    private String registerID;
    private String email, password;
    private String token;
    boolean showPassword = false;
    GetGoogleIdOption googleIdOption;
    String nonce = UUID.randomUUID().toString();

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
            binding.etEmail.setText(rememberMe.get(Constants.KEY_USER_ID));
            binding.etPassword.setText(rememberMe.get(Constants.KEY_PASSWORD));
            binding.rememberMeCB.setChecked(true);
        }

        getRegisterID();
        binding.txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if (binding.etEmail.getText().toString().isEmpty()) {
                Snackbar.make(binding.lParent, R.string.emailCannotEmpty, Snackbar.LENGTH_SHORT).show();
            } else if (binding.etPassword.getText().toString().isEmpty()) {
                Snackbar.make(binding.lParent, R.string.passwordCannotEmpty, Snackbar.LENGTH_SHORT).show();
            } else {
                email = binding.etEmail.getText().toString().trim().toLowerCase();
                password = binding.etPassword.getText().toString().trim();
                openDialogProgress();
                getToken();
            }
        });

        binding.imgShowPassword.setOnClickListener(view -> {
            if (!showPassword) {
                //show password
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_pass_hide));
                showPassword = true;
            } else {
                // hide password
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_pass_show));
                showPassword = false;
            }
            binding.etPassword.setSelection(Helper.isEmpty(binding.etPassword) ? 0 : binding.etPassword.getText().length());
        });

        binding.btnGoogleSignIn.setOnClickListener(view -> {
            googleSignIn();
        });

    }

    private void googleSignIn() {
        googleIdOption = new GetGoogleIdOption.Builder()
                .setServerClientId(getString(R.string.server_client_id))
                .setFilterByAuthorizedAccounts(false) // false allows sign-up with unregistered accounts
                .setAutoSelectEnabled(false) // Optional: true for seamless sign-in
                .setNonce(nonce) // Optional for security
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(this);

        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse getCredentialResponse) {
                        handleSignInWithGoogleOption(getCredentialResponse);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("SignUp", "Credential error: " + e.getMessage());

                    }
                }
        );
    }

    private void handleSignInWithGoogleOption(GetCredentialResponse result) {
        Credential credential = result.getCredential();

        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;

            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(customCredential.getType())) {
                GoogleIdTokenCredential googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(customCredential.getData());

                JSONObject payload = decodeJwtPayload(googleIdTokenCredential.getIdToken());
                if (payload != null) {
//                    String email = payload.optString("email");
//                    String name = payload.optString("name");
//                    String picture = payload.optString("picture");
//                    String sub = payload.optString("sub"); // user ID

//                    Log.d("DecodedJWT", "Email: " + email);
                    getGoogleToken(googleIdTokenCredential.getIdToken());
                    openDialogProgress();
                }
            } else {
                Log.e("SignUp", "Unsupported credential type");
            }
        } else {
            Log.e("SignUp", "Unexpected credential type");
        }
    }

    public void getGoogleToken(String googleToken) {
        Map<String, Object> request = new HashMap<>();
        request.put("idToken", googleToken);
        apiInterface = RetrofitAPIClient.getClientWithoutCookies().create(APIInterface.class);
        Call<LoginResponse> httpRequest = apiInterface.getGoogleToken(request);
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

    private JSONObject decodeJwtPayload(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            String payloadJson = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            return new JSONObject(payloadJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getToken() {
        SignUp signUp = new SignUp();
        signUp.setUsername(email);
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
                                new SessionManager(getApplicationContext()).createRememberMeSession(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
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