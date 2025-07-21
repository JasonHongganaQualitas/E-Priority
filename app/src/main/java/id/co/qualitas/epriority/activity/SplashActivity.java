package id.co.qualitas.epriority.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import id.co.qualitas.epriority.BuildConfig;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;

import id.co.qualitas.epriority.databinding.ActivitySplashBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.WSMessage;
import id.co.qualitas.epriority.service.AsyncTaskExecutorService;
import id.co.qualitas.epriority.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private WSMessage resultVersion;
    private String apkV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
        getVersion();
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
    }

    private void dialogUpdateAPK() {
        if (apkV != null) {
            if (!apkV.equals(BuildConfig.VERSION_NAME)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New Update");
                builder.setMessage("A new version of E-Priority is available!\nplease update to version " + apkV)
                        .setCancelable(false)
                        .setPositiveButton("Update", (dialog, id) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                if (!getPackageManager().canRequestPackageInstalls()) {
                                    Helper.setItemParam(Constants.UNKNOWN_RESOURCE, Constants.UNKNOWN_RESOURCE);
                                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                            .setData(Uri.parse(String.format("package:%s", getPackageName()))), 1);
                                    dialog.dismiss();
                                } else {
                                    UpdateApp actualizeApp = new UpdateApp();
                                    actualizeApp.setContext(SplashActivity.this);
                                    actualizeApp.execute(Helper.getItemParam(Constants.BASE_URL).toString().concat(Helper.getItemParam(Constants.API_GET_APK).toString()));
                                }
                            } else {
                                UpdateApp actualizeApp = new UpdateApp();
                                actualizeApp.setContext(SplashActivity.this);
                                actualizeApp.execute(Helper.getItemParam(Constants.BASE_URL).toString().concat(Helper.getItemParam(Constants.API_GET_APK).toString()));
                            }

                        })
                        .setNegativeButton("Dismiss", (dialog, id) -> {
                            dialog.cancel();
                            checkLogin();
                        });
                AlertDialog alertVersion = builder.create();
                alertVersion.show();
            } else {
                checkLogin();
            }
        } else {
            checkLogin();
        }
    }

    private void checkLogin() {
        if (session.isLoggedIn()) {
            binding.btnGetStarted.setVisibility(View.GONE);
            Thread background = new Thread() {
                public void run() {
                    try {
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
            binding.btnGetStarted.setVisibility(View.VISIBLE);
            binding.loadingIcon.setVisibility(View.GONE);
            binding.btnGetStarted.setOnClickListener(v -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            });
        }
    }

    private void getVersion() {
        binding.loadingIcon.setVisibility(View.VISIBLE);
        // Rotate the ImageView (loadingIcon) infinitely
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(binding.loadingIcon, "rotation", 0f, 360f);
//        rotateAnim.setDuration(1500); // 1.5 seconds for one complete rotation
        rotateAnim.setRepeatCount(ObjectAnimator.INFINITE); // Repeat infinitely
        rotateAnim.setRepeatMode(ObjectAnimator.RESTART); // Restart from the beginning each time
        rotateAnim.start();
        apiInterface = RetrofitAPIClient.getClientWithoutCookies().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getVersion();

        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                if (response.isSuccessful()) {
                    String jsonInString = new Gson().toJson(response.body());
                    resultVersion = new Gson().fromJson(jsonInString, WSMessage.class);
                    if (resultVersion != null) {
                        apkV = resultVersion.getResult().toString();
                        dialogUpdateAPK();//from api
                    } else {
                        checkLogin();
                    }
                } else {
                    checkLogin();
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                setToast("Please check your internet connection, and try again");
                call.cancel();
                checkLogin();
            }

        });
    }

    public class UpdateApp extends AsyncTaskExecutorService<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;

        void setContext(Activity context) {
            mContext = context;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPDialog = new ProgressDialog(mContext);
                    mPDialog.setMessage("Please wait...");
                    mPDialog.setIndeterminate(true);
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mPDialog.setCancelable(false);
                    mPDialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String arg0) {
            try {
                URL url = new URL(arg0);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                String PATH = getFilesDir().getAbsolutePath() + File.separator;
                File file = new File(PATH);
                boolean isCreate = file.mkdirs();
                File outputFile = new File(file, "e-priority.apk");
                if (outputFile.exists()) {
                    boolean isDelete = outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = new BufferedInputStream(url.openStream(), 8192);
                byte[] buffer = new byte[1024];
                int len1;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    fos.write(buffer, 0, len1);
                    publishProgress((int) ((total * 100) / lengthOfFile));
                }
                fos.close();
                is.close();
                if (mPDialog != null)
                    mPDialog.dismiss();
            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPDialog != null)
                mPDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer values) {
            super.onProgressUpdate(values);
            if (mPDialog != null) {
                mPDialog.setIndeterminate(false);
                mPDialog.setMax(100);
                mPDialog.setProgress(values);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPDialog != null)
                mPDialog.dismiss();
            if (result != null) {
                Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();
                installApk();
            }
        }

        private void installApk() {
            try {
                String PATH = getFilesDir().getAbsolutePath() + File.separator;
                File file = new File(PATH + "/e-priority.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider", file);
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                    List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        mContext.grantUriPermission(mContext.getApplicationContext().getPackageName() + ".fileprovider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Helper.getItemParam(Constants.UNKNOWN_RESOURCE) != null) {
            Helper.removeItemParam(Constants.UNKNOWN_RESOURCE);
            dialogUpdateAPK();//onResume
        }
    }
}