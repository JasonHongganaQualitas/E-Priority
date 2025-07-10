package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;

public class BookingDetailsAgentFragment extends BaseFragment implements IOnBackPressed {

    private FragmentBookingDetailsAgentBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingDetailsAgentBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();

        return view;
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        binding.passengerDetailsCard.btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassangerDetailsFragment fragment2 = new PassangerDetailsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });

        binding.packageDetailsCard.btnSeeMoreAirport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirportTransferFragment fragment2 = new AirportTransferFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
        binding.packageDetailsCard.btnSeeMoreLounge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoungeAccessFragment fragment2 = new LoungeAccessFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
        binding.packageDetailsCard.btnSeeMoreFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoungeAccessFragment fragment2 = new LoungeAccessFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}