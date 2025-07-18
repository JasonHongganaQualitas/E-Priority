package id.co.qualitas.epriority.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.PackageDetailAdapter;
import id.co.qualitas.epriority.adapter.PassangerDetailsAdapter;

public class ReviewBookingFragment extends BaseFragment {
    View view;
    RecyclerView passengerDetailsRV, packageDetRV;
    TextView passangerDetTitleTxt, btnBack, btnCreate;
    PassangerDetailsAdapter adapter;
    PackageDetailAdapter packageDetailAdapter;
    ImageView backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_review_booking, container, false);

        initialize();
        initAdapter();

        btnCreate.setOnClickListener(v -> {
            dialogBookingCreated();
        });

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void dialogBookingCreated() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_booking_created, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BookingDetailsFragment fragment = new BookingDetailsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
            }
        }, 500);
    }

    private void initAdapter() {
        passengerDetailsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PassangerDetailsAdapter();
        passengerDetailsRV.setAdapter(adapter);

        packageDetRV.setLayoutManager(new LinearLayoutManager(getContext()));
        packageDetailAdapter = new PackageDetailAdapter();
        packageDetRV.setAdapter(packageDetailAdapter);
    }

    private void initialize() {
        passengerDetailsRV = view.findViewById(R.id.passengerDetailsRV);
        passangerDetTitleTxt = view.findViewById(R.id.passangerDetTitleTxt);
        packageDetRV = view.findViewById(R.id.packageDetRV);
        btnBack = view.findViewById(R.id.btnBack);
        btnCreate = view.findViewById(R.id.btnCreate);
        backBtn = view.findViewById(R.id.backBtn);
    }
}