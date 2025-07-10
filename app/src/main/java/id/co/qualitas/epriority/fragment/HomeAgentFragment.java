package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.HomeAgentAdapter;
import id.co.qualitas.epriority.adapter.PendingBookingAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.Booking;

public class HomeAgentFragment extends BaseFragment implements IOnBackPressed {

    private FragmentHomeAgentBinding binding;
    private HomeAgentAdapter oAdapter, pAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeAgentBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();

        return view;
    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
        initAdapter();

        binding.imgNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationFragment fragment2 = new NotificationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Home Agent");
                fragmentTransaction.commit();
            }
        });
        binding.lOngoingBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OngoingBookingFragment fragment2 = new OngoingBookingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Home Agent");
                fragmentTransaction.commit();
            }
        });
        binding.lPendingBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingBookingFragment fragment2 = new PendingBookingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Home Agent");
                fragmentTransaction.commit();
            }
        });
    }

    private void initAdapter() {
        List<Booking> ongoingBookings = new ArrayList<>();
        ongoingBookings.add(new Booking("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Upcoming"));
        ongoingBookings.add(new Booking("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Active"));
        List<Booking> pendingBookings = new ArrayList<>();
        pendingBookings.add(new Booking("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        pendingBookings.add(new Booking("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));

        if(ongoingBookings.size() != 0 || pendingBookings.size() != 0) {
            oAdapter = new HomeAgentAdapter(this, ongoingBookings);
            binding.rvOngoingBookings.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvOngoingBookings.setAdapter(oAdapter);

            pAdapter = new HomeAgentAdapter(this, pendingBookings);
            binding.rvPendingBookings.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvPendingBookings.setAdapter(pAdapter);
            binding.lEmpty.setVisibility(View.GONE);
            binding.lOngoingBookings.setVisibility(View.VISIBLE);
            binding.lPendingBookings.setVisibility(View.VISIBLE);
            binding.rvPendingBookings.setVisibility(View.VISIBLE);
            binding.rvOngoingBookings.setVisibility(View.VISIBLE);
        }else{
            binding.lEmpty.setVisibility(View.VISIBLE);
            binding.lOngoingBookings.setVisibility(View.GONE);
            binding.lPendingBookings.setVisibility(View.GONE);
            binding.rvPendingBookings.setVisibility(View.GONE);
            binding.rvOngoingBookings.setVisibility(View.GONE);
        }
    }

    public void callBookingDetailsFragment(Booking booking) {
        Helper.setItemParam(Constants.BOOKING_DETAIL, booking);
        BookingDetailsAgentFragment fragment2 = new BookingDetailsAgentFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment2);
        fragmentTransaction.addToBackStack("Ongoing Booking");
        fragmentTransaction.commit();
    }

    public boolean onBackPressed() {
        return true;
    }

}