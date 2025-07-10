package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentAirportTransferBinding;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsAgentBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.Booking;

public class AirportTransferFragment extends BaseFragment implements IOnBackPressed {

    private FragmentAirportTransferBinding binding;
    private Booking booking;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAirportTransferBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();

        return view;
    }

    private void initialize() {
        booking = (Booking) Helper.getItemParam(Constants.BOOKING_DETAIL);
        if(booking.getStatus().equals(getString(R.string.pending))){
            binding.btnCheckIn.setVisibility(View.GONE);
            binding.btnCheckOut.setVisibility(View.GONE);
        }else{
            binding.btnCheckIn.setVisibility(View.VISIBLE);
            binding.btnCheckOut.setVisibility(View.VISIBLE);
        }
        binding.btnBack.setOnClickListener(v ->  getActivity().getOnBackPressedDispatcher().onBackPressed());

        binding.btnCheckIn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Checked In!", Toast.LENGTH_SHORT).show();
            binding.txtStatus.setText("In Progress");
        });

        binding.btnCheckOut.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Checked Out!", Toast.LENGTH_SHORT).show();
            binding.txtStatus.setText("Completed");
            binding.txtStatus.setTextColor(getActivity().getColor(R.color.gray2));
            binding.txtStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(getActivity(), R.color.gray1)
            );
        });

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}