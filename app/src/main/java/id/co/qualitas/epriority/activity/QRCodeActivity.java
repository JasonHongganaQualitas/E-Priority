package id.co.qualitas.epriority.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.WriterException;

import java.util.ConcurrentModificationException;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.ActivityQrcodeBinding;
import id.co.qualitas.epriority.databinding.ActivitySplashBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.TripsResponse;

public class QRCodeActivity extends AppCompatActivity {

    private ActivityQrcodeBinding binding;
    private TripsResponse tripDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialize();
    }

    public void initialize() {
        tripDetails = (TripsResponse) Helper.getItemParam(Constants.TRIP_DETAILS);

        binding.imgQR.setImageBitmap((Bitmap) Helper.getItemParam(Constants.QR_DATA));
        binding.txtBookingId.setText("#" + tripDetails.getTrip_id());
    }
}