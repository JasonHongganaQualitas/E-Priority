package id.co.qualitas.epriority.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.PendingBookingAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentPendingBookingBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;

public class PendingBookingFragment extends BaseFragment implements IOnBackPressed{

    private FragmentPendingBookingBinding binding;
    private PendingBookingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initialize();
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        initAdapter();
    }

    private void initAdapter() {
        List<TripsResponse> tripsResponses = new ArrayList<>();
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        if(tripsResponses.size() != 0) {
            adapter = new PendingBookingAdapter(this, tripsResponses);
            binding.recyclerViewPendingBooking.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerViewPendingBooking.setAdapter(adapter);
            binding.recyclerViewPendingBooking.setVisibility(View.VISIBLE);
            binding.lEmpty.setVisibility(View.GONE);
        }else{
            binding.recyclerViewPendingBooking.setVisibility(View.GONE);
            binding.lEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public boolean onBackPressed() {
        return false;
    }

    public void callBookingDetailsFragment(TripsResponse tripsResponse) {
        Helper.setItemParam(Constants.BOOKING_DETAIL, tripsResponse);
        BookingDetailsAgentFragment fragment2 = new BookingDetailsAgentFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment2);
        fragmentTransaction.addToBackStack("Ongoing Booking");
        fragmentTransaction.commit();
    }
}
