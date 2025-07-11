package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.qualitas.epriority.R;

public class CreatePassengerFragment extends Fragment {
    View view;
    TextView btnCreate;
    ImageView backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_passenger, container, false);

        initialize();

        btnCreate.setOnClickListener(v -> {
            AdditionalPassengerFragment fragment = new AdditionalPassengerFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void initialize() {
        btnCreate = view.findViewById(R.id.btnCreate);
        backBtn = view.findViewById(R.id.backBtn);
    }
}