package id.co.qualitas.epriority.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OngoingBookingAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentOngoingBookingBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;

public class OngoingBookingFragment extends BaseFragment implements IOnBackPressed {
    private FragmentOngoingBookingBinding binding;
    private OngoingBookingAdapter adapter;
    private List<TripsResponse> mList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOngoingBookingBinding.inflate(inflater, container, false);
        init();
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        initAdapter();
    }

    private void initAdapter() {
        adapter = new OngoingBookingAdapter(this, mList);
        binding.recyclerViewOngoingBooking.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOngoingBooking.setAdapter(adapter);

        if (Helper.isNotEmptyOrNull(mList)) {
            binding.recyclerViewOngoingBooking.setVisibility(View.VISIBLE);
            binding.lEmpty.setVisibility(View.GONE);
        } else {
            binding.recyclerViewOngoingBooking.setVisibility(View.GONE);
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
