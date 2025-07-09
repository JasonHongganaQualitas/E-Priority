package id.co.qualitas.epriority.fragment;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.session.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class BaseFragment extends Fragment {
    protected BottomSheetDialog bottomSheetDialog;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    public Dialog progressDialog;
    protected float mScaleFactor = 1.0f;
    protected ImageView img;
    protected Integer port;
    protected Integer uId;
    protected String errorMessage;
    protected int PARAM = 0;
    protected SessionManager session;
    protected View dialogview;
    protected LayoutInflater inflater;
    protected Dialog alertDialog;
    protected User user;
    protected Dialog dialog;
    protected APIInterface apiInterface;
    protected ArrayAdapter<String> spinnerAdapter;

    protected void init(){
        setSessionUser();
    }

    private void setSessionUser() {
        session = new SessionManager(getActivity());
        if(session.isLoggedIn()) {
            Map<String, String> userSession = session.getLoginDetails();
            user = (User) Helper.stringToObject(userSession.get(Constants.KEY_LOGIN));
        }
    }

    public static <X, Y> List<Y> processElements(Iterable<X> source, Function<X, Y> mapper) {
        List<Y> l = new ArrayList<>();
        for (X p : source)
            l.add(mapper.apply(p));
        return l;
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

    protected void initDialog(int resource) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogview  = getActivity().getLayoutInflater().inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void openDialogProgress() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        dialog.show();
    }

    public void syncScannerProgress() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        txtMsg.setText("Sync Scanner");
        dialog.show();
    }


//    public void getImageGlide(Context context, String imageUrl, ImageView image) {
//        if (!imageUrl.equals("false")) {
//            Glide.with(context)
//                    .applyDefaultRequestOptions(new RequestOptions()
//                            .placeholder(R.drawable.ic_no_image)
//                            .error(R.drawable.ic_no_image))
//                    .asBitmap()
//                    .load(Base64.decode(imageUrl, Base64.DEFAULT))
//                    .into(image);
//        } else {
//            Glide.with(context)
//                    .applyDefaultRequestOptions(new RequestOptions()
//                            .placeholder(R.drawable.ic_no_image)
//                            .error(R.drawable.ic_no_image))
//                    .asBitmap()
//                    .load(imageUrl)
//                    .into(image);
//        }
//    }

    public void setToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }

    protected void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        format = new DecimalFormat("#,###,###,###.##", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

//    public void showDialogFullImage(Product data) {
//        Utils.setItemParam(Constants.FROM_FULL_IMAGE, data);
//        dialog = new Dialog(getActivity());
//        dialog.setContentView(R.layout.dialog_full_image);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ImageView imgClose = dialog.findViewById(R.id.imgClose);
//        img = dialog.findViewById(R.id.img);
////        getImageGlide(getContext(), data.getImage_128(), img);
//        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
//
//        img.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                scaleGestureDetector.onTouchEvent(motionEvent);
//                return true;
//            }
//        });
//
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                onResume();
//            }
//        });
//
//        dialog.show();
//    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            img.setScaleX(mScaleFactor);
            img.setScaleY(mScaleFactor);
            return true;
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    public void logOut(){
//        Utils.removeItemParam(Constants.KEY_DATA);
//        Utils.removeItemParam(Constants.KEY_USER);
//        Utils.removeItemParam(Constants.KEY_OC);
//        Utils.removeItemParam(Constants.WISH_LIST);
//        Utils.removeItemParam(Constants.CART_LIST);
//        session.clearSession();
//
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        getActivity().finish();
//    }

    public void openDialogInformation(String title, String msg, String intent) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Dialog dialog = new Dialog(getActivity());
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
                if(intent != null){
                    getFragmentManager().popBackStack(intent, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });
        dialog.show();
    }

    public void openDialogSuccess(String title, String msg, String intent) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        txtTitle.setText(title);
        txtMsg.setText(msg);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(intent != null){
                    getFragmentManager().popBackStack(intent, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });
        dialog.show();
    }

    public byte[] getByteArrayFromUri(Uri uri) {
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = getActivity().getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
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

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showDialogProgress() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        progressDialog = new Dialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView txtTitle = progressDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = progressDialog.findViewById(R.id.txtMsg);
        progressDialog.show();
    }
    public void dismissDialog(){
        progressDialog.dismiss();
    }

    public boolean isServiceRunning(String serviceName){
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                serviceRunning = true;

                if(runningServiceInfo.foreground)
                {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }

//    public void loadImageCenterCrop(ImageView imgView, String url) {
//        apiInterface = RetrofitAPIClient.getClient(getActivity()).create(APIInterface.class);
//        Call<ResponseBody> httpRequest = apiInterface.getImage(url);
//        httpRequest.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    byte[] bytes = Helper.readAllBytes(response.body().byteStream());
//                    if(getActivity() != null) {
//                        Glide.with(getActivity())
//                                .applyDefaultRequestOptions(new RequestOptions()
//                                        .placeholder(R.mipmap.ic_logo)
//                                        .error(R.mipmap.ic_logo))
//                                .load(bytes)
//                                .centerCrop()
//                                .into(imgView);
//                    }
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
//
//    public void loadImage(ImageView imgView, String url) {
//        apiInterface = RetrofitAPIClient.getClient(getActivity()).create(APIInterface.class);
//        Call<ResponseBody> httpRequest = apiInterface.getImage(url);
//        httpRequest.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    byte[] bytes = Helper.readAllBytes(response.body().byteStream());
//                    if(getActivity() != null) {
//                        Glide.with(getActivity())
//                                .applyDefaultRequestOptions(new RequestOptions()
//                                        .placeholder(R.drawable.ic_logo)
//                                        .error(R.drawable.ic_logo))
//                                .load(bytes)
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
//                                .fitCenter()
//                                .into(imgView);
//                    }
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
//
//    public void setSpinnerAdapter2(ArrayList<String> items, Spinner spinner) {
//        spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_2) {
//
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = view.findViewById(R.id.text1);
////                text.setTextColor(getResources().getColor(R.color.black));
//                return view;
//            }
//        };
//
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerAdapter.addAll(items);
//        spinner.setAdapter(spinnerAdapter);
//    }

    public void sentCart(String qty){
        Intent broadcast = new Intent();
        broadcast.setAction("CART");
        broadcast.putExtra("message", qty);
        getActivity().sendBroadcast(broadcast);
    }
}
