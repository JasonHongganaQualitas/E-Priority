package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.adapter.BookingHistoryAdapter;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.databinding.FragmentProfileBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Booking;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding binding;
    View view;
    OnGoingTripAdapter adapter;
    private List<Booking> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        init();
        initialize();
        initAdapter();
        setData();

        binding.btnEditProfile.setOnClickListener(v -> {
            ProfileEditFragment fragment = new ProfileEditFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });
        return binding.getRoot();
    }

    private void setData() {
        binding.txtName.setText(Helper.isEmpty(user.getName(), ""));
        binding.txtEmail.setText(Helper.isEmpty(user.getEmail(), ""));
    }

    private void initAdapter() {
        binding.bookingHistoryRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OnGoingTripAdapter(ProfileFragment.this, mList, (header, pos) -> {
        });
        binding.bookingHistoryRV.setAdapter(adapter);

        if (Helper.isNotEmptyOrNull(mList)) {
            binding.bookingHistoryRV.setVisibility(View.VISIBLE);
            binding.noTripLL.setVisibility(View.GONE);
        } else {
            binding.bookingHistoryRV.setVisibility(View.GONE);
            binding.noTripLL.setVisibility(View.VISIBLE);
        }
    }

    private void initialize() {
        binding.lLogout.setOnClickListener(v -> ((MainActivity) getActivity()).callLogout());
    }
}