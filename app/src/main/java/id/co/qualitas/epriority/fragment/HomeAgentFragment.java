package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;

public class HomeAgentFragment extends BaseFragment {

    private FragmentHomeAgentBinding binding;
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
    }

}