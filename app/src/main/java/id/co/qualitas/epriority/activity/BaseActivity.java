package id.co.qualitas.epriority.activity;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {
    protected View dialogview;
    protected LayoutInflater inflater;
    protected Dialog alertDialog;
    protected Intent intent;
    protected SessionManager session;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected LinearLayoutManager mManager;
    boolean isKeyboardShowing = false;
    protected Dialog dialog;
    protected User user;
    protected APIInterface apiInterface;
    protected Button btnChangePassword;

    public void getSession() {
        Map<String, String> tokenSession = session.getToken();
        Helper.setItemParam(Constants.TOKEN, tokenSession.get(Constants.KEY_TOKEN));
        Map<String, String> userSession = session.getLoginDetails();
        user = (User) Helper.stringToObject(userSession.get(Constants.KEY_LOGIN));
    }

    public void sessionExpired() {
        setToast("Session expired");
        Intent intent = new Intent(getApplicationContext(),
                LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

//    @Override
//    protected void onStart() {
//        // TODO Auto-generated method stub
//        super.onStart();
//        if (onStartCount > 1) {
//            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//        } else if (onStartCount == 1) {
//            onStartCount++;
//        }
//
//    }

    protected void initBase() {
//        progress = BackgroundHelper.createProgressDialog(this);
        session = new SessionManager(getApplicationContext());
        getSession();
    }

//    public void checkCart(Context context) {
//        cartDao.getCartCountLive(String.valueOf(user.getId())).observe(this, items -> {
//            if(items == null)
//                items = 0;
//            Intent broadcast = new Intent();
//            broadcast.setAction("CART");
//            broadcast.putExtra("message", String.valueOf(items));
//            context.sendBroadcast(broadcast);
//        });
//    }

    public void openDialogProgress() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        dialog.show();
    }

    public void syncScannerProgress() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        txtMsg.setText("Sync Scanner");
        dialog.show();
    }

    public void changeStatusBarColor() {
        int statusBarColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }
    }

//    public void showSnackBar(View view, String msg) {
//        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
//                .setAction("Action", null);
//        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//        TextView tv = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
//        tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
//        snackbar.show();
//    }

//    public void getSession() {
//        session = new SessionManager(getApplicationContext());
//        Map<String, String> userSession = session.getUserDetails();
//        String mUSer = userSession.get(Constants.KEY_USER);
//
//        if (Helper.getItemParam(Constants.USER_DETAIL) == null) {
//            if (mUSer != null) {
//                employeeDetail = (EmployeeDetail) Helper.stringToObject(mUSer);
//                try {
//                    if (employeeDetail.getAccess_token() != null) {
//                        Helper.setItemParam(Constants.TOKEN, employeeDetail.getAccess_token());
//                    }
//                } catch (Exception ex) {
//                    setToast("Can't get data. Please re-login");
//                    employeeDetail = new EmployeeDetail();
//                    session.logoutUser();
//                    intent = new Intent(getApplication(), LoginActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//
//                Helper.setItemParam(Constants.USER_DETAIL, employeeDetail);
//            }
//        } else {
//            employeeDetail = (EmployeeDetail) Helper.getItemParam(Constants.USER_DETAIL);
//        }
//
//        if (session.isUrlEmpty()) {
//            Map<String, String> urlSession = session.getUrl();
//            Constants.IP = urlSession.get(Constants.KEY_URL);
//            Constants.BASE_URL = Constants.IP;
//            Helper.setItemParam(Constants.BASE_URL, Constants.BASE_URL);
//        } else {
//            Constants.IP = Constants.BASE_URL;
//            Constants.BASE_URL = Constants.IP;
//            Helper.setItemParam(Constants.BASE_URL, Constants.BASE_URL);
//        }
//    }

//    public void getImageGlide(Context context, String imageUrl, ImageView image) {
//        Glide.with(context)
//                .applyDefaultRequestOptions(new RequestOptions()
//                        .placeholder(R.drawable.ic_no_image)
//                        .error(R.drawable.ic_no_image))
//                .asBitmap()
//                .load(imageUrl)
//                // .load(Base64.decode(imageUrl, Base64.DEFAULT))
//                .into(image);
//    }

    public String generateUniqueIdentifier() throws Exception {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void initRecycleView(RecyclerView recyclerView, RecyclerView.Adapter mAdapter, boolean dividerDecoration) {
        if (dividerDecoration) {
            DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecor);
        }
        mManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
    }

    protected void initDialog(int resource) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

//    public static <T> void loadImageFit(Context mContext, T res, ImageView image) {
//        Glide.with(mContext)
//                .load(res)
//                .fitCenter()
//                .into(image);
//    }

    public void setToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        if (progress.isShowing()) {
//            progress.dismiss();
//        }
//        super.onStop();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }

    public void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        format = new DecimalFormat(Constants.PATTERN_DECIMAL, otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    public String getEncodedImage(Bitmap bm) {
        String encodedImage = "";
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return encodedImage;
    }

    public static String encodeImageBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public byte[] getByteArrayFromBitmap(Bitmap bm) {
        String encodedImage = "";
        byte[] b = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
//            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return b;
    }

    public byte[] getByteArrayFromUri(Uri uri) {
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
    }

    public byte[] getByteArrayFromUriGallery(Uri uri) {
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = getContentResolver().openInputStream(uri);
            inputData = getBytesGallery(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
    }

    public byte[] getBytesGallery(InputStream inputStream) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteBuffer);
        bmp = Bitmap.createScaledBitmap(bmp, 600, 250, true);
        return encodeTobase64(bmp);
    }

    public byte[] encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return b;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public Bitmap getBitmapFromByteArray(byte[] array) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        } catch (Exception ex) {
            bitmap = null;
        }
        return bitmap;
    }

    public byte[] getByteArray(Bitmap bm) {
        byte[] b = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
        }
        return b;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

//    public void openDialogSetting() {
//        Dialog dialog = new Dialog(this);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setContentView(R.layout.dialog_setting_ip);
//        Button btnCancel = dialog.findViewById(R.id.btnCancel);
//        Button btnSave = dialog.findViewById(R.id.btnSave);
//        EditText edtTxtIpAddress = dialog.findViewById(R.id.edtTxtIpAddress);
//        String ssid = "";
//        try {
//            ssid = generateUniqueIdentifier();
//        } catch (Exception e) {
//            ssid = "";
//            e.printStackTrace();
//        }
//
//        String finalSsid = ssid;
//
//        if (Helper.getItemParam(Constants.BASE_URL) != null) {
//            String urltemp = Helper.getItemParam(Constants.BASE_URL).toString();
//            String temp[] = urltemp.split("/");//temp[2]
//            edtTxtIpAddress.setText(urltemp);
//        }
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard();
//                if (edtTxtIpAddress.getText().toString().trim().equals(null) || edtTxtIpAddress.getText().toString().trim().equals("")) {
//                    edtTxtIpAddress.setError(getString(R.string.please_fill));
//                } else {
//                    Constants.IP = edtTxtIpAddress.getText().toString();
//                    Constants.BASE_URL = Constants.IP;
//                    session.createUrlSession(Constants.BASE_URL);
//                    Helper.setItemParam(Constants.BASE_URL, Constants.BASE_URL);
//                    setToast(getString(R.string.ipaddress_has_been_changed));
//                    dialog.dismiss();
//                }
//            }
//        });
//        dialog.show();
//    }
//
//    public void openDialogForgetPassword() {
//        Dialog dialog = new Dialog(this);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setContentView(R.layout.dialog_forgot_password);
//        Button btnCancel = dialog.findViewById(R.id.btnCancel);
//        Button btnSave = dialog.findViewById(R.id.btnSubmit);
//        EditText edtTxtIpAddress = dialog.findViewById(R.id.edtEmail);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard();
//                dialog.dismiss();
//                openDialogInformation("", getResources().getString(R.string.forgetPasswordConfirmation), null);
////                if (edtTxtIpAddress.getText().toString().trim().equals(null) || edtTxtIpAddress.getText().toString().trim().equals("")) {
////                    edtTxtIpAddress.setError(getString(R.string.please_fill));
////                } else {
////                    Constants.IP = edtTxtIpAddress.getText().toString();
////                    Constants.BASE_URL = Constants.IP;
////                    session.createUrlSession(Constants.BASE_URL);
////                    Helper.setItemParam(Constants.BASE_URL, Constants.BASE_URL);
////                    setToast(getString(R.string.ipaddress_has_been_changed));
////                    dialog.dismiss();
////                }
//            }
//        });
//        dialog.show();
//    }
//


    public void openDialogInformation(String title, String msg, Intent intent) {
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialog.show();
    }


    public boolean onKeyboardVisibilityChanged(View contentView) {
        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        Log.d("TAG", "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                            }
                        }
                    }
                });
        return isKeyboardShowing;
    }

    public File getDirLoc(Context applicationContext) {
        String PDF_FOLDER_NAME = "/OEM/";
        File directory = null;
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            directory = new File(Environment.getDataDirectory() + PDF_FOLDER_NAME);
            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            try {
                int version = Build.VERSION.SDK_INT;
                if (version >= 30) {
                    directory = new File(applicationContext.getFilesDir() + PDF_FOLDER_NAME);
                } else {
                    directory = new File(Environment.getExternalStorageDirectory() + PDF_FOLDER_NAME);
                }
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } catch (Exception ex) {
                setToast(ex.getMessage());
            }
        }// end of SD card checking

        return directory;
    }

    public String getFileFolderPath() {
        File f = new File(Environment.getExternalStorageDirectory() + Constants.FOLDER_NAME);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

//    public void setDataBreakdown() {
//        ArrayList<Breakdown> breakdownArrayList = new ArrayList<>();
//        breakdownArrayList.add(new Breakdown("101", "PART A", "Excavator"));
//        breakdownArrayList.add(new Breakdown("102", "PART A", "Mini"));
//        breakdownArrayList.add(new Breakdown("201", "PART B", "xx"));
//        breakdownArrayList.add(new Breakdown("202", "PART B", "try"));
//        breakdownArrayList.add(new Breakdown("301", "PART C", "xx"));
//        breakdownArrayList.add(new Breakdown("401", "PART D", "Excavator"));
//        breakdownArrayList.add(new Breakdown("402", "PART D", "xx"));
//        breakdownArrayList.add(new Breakdown("501", "PART E", "Mini"));
//        breakdownArrayList.add(new Breakdown("601", "PART F", "try"));
//        if (db == null) {
//            db = new DatabaseHandler(this.getApplicationContext());
//        }
//
//        db.deleteAllDataBreakdown();
//        for (Breakdown breakdown : breakdownArrayList) {
//            db.addBreakdown(breakdown);
//        }
//    }

//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }

    public static class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    public long daysBetween(Calendar first, Calendar second) {
        long diffInMillis = second.getTimeInMillis() - first.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }

    public boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }

//    public void loadImage(ImageView imgView, String url) {
//        apiInterface = RetrofitAPIClient.getClient(this).create(APIInterface.class);
//        Call<ResponseBody> httpRequest = apiInterface.getImage(url);
//        httpRequest.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    byte[] bytes = Helper.readAllBytes(response.body().byteStream());
//                    Glide.with(getApplicationContext())
//                            .applyDefaultRequestOptions(new RequestOptions()
//                                    .placeholder(R.drawable.ic_logo)
//                                    .error(R.drawable.ic_logo))
//                            .load(bytes)
//                            .centerCrop()
//                            .into(imgView);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//            }
//        });
//    }
}
