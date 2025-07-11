package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OngoingTripAdapter;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;

public class HomeCustomerFragment extends BaseFragment {
    private FragmentHomeCustomerBinding binding;
    View view;
    OngoingTripAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeCustomerBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        init();
        initialize();
        binding.flightDetailsLL.setVisibility(View.GONE);
        binding.btnContinue.setVisibility(View.GONE);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Arrival"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Departure"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){

                }
                else {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnSearchFlight.setOnClickListener(v -> {
            binding.flightDetailsLL.setVisibility(View.VISIBLE);
            binding.btnContinue.setVisibility(View.VISIBLE);
        });

        binding.btnContinue.setOnClickListener(v -> {
            PassengerInformationFragment fragment = new PassengerInformationFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        binding.ongoingTripRV.setVisibility(View.VISIBLE);
        binding.noTripLL.setVisibility(View.GONE);
        initAdapter();

        return view;
    }

    private void initAdapter() {
        binding.ongoingTripRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OngoingTripAdapter();
        binding.ongoingTripRV.setAdapter(adapter);
    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
    }

}