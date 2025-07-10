package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentFlightDetailsBinding;
import id.co.qualitas.epriority.databinding.FragmentLoungeAccessBinding;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;

public class FlightDetailsFragment extends BaseFragment implements IOnBackPressed {

    private FragmentFlightDetailsBinding binding;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFlightDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();

        return view;
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}