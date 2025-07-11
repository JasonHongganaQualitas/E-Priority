package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.PassangerAdapter;

public class AdditionalPassengerFragment extends Fragment {
    View view;
    ImageView backBtn;
    RecyclerView passengerRV;
    PassangerAdapter adapter;
    TextView btnAdd;
    LinearLayout btnCreate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_additional_passenger, container, false);

        initialize();
        iniAdapter();
        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnAdd.setOnClickListener(v -> {
            PassengerInformationFragment fragment = new PassengerInformationFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        btnCreate.setOnClickListener(v -> {
            CreatePassengerFragment fragment = new CreatePassengerFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }

    private void iniAdapter() {
        passengerRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PassangerAdapter();
        passengerRV.setAdapter(adapter);
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        passengerRV = view.findViewById(R.id.passengerRV);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCreate = view.findViewById(R.id.btnCreate);
    }
}