package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;

public class HomeCustomerFragment extends BaseFragment {
    private FragmentHomeCustomerBinding binding;
    View view;

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

        binding.btnSearchFlight.setOnClickListener(v -> {
            binding.flightDetailsLL.setVisibility(View.VISIBLE);
            binding.btnContinue.setVisibility(View.VISIBLE);
        });

        binding.btnContinue.setOnClickListener(v -> {
            PassengerInformationFragment fragment = new PassengerInformationFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        return view;
    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
    }

}