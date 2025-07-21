package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.databinding.FragmentChoosePackageBinding;
import id.co.qualitas.epriority.databinding.FragmentPassengerInformationBinding;

public class PassengerInformationFragment extends BaseFragment {
    View view;
    FragmentPassengerInformationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPassengerInformationBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        init();

        binding.backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnChoose.setOnClickListener(v -> {
            ChoosePackageFragment fragment = new ChoosePackageFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        String[] travelClasses = {
                "Economy Class",
                "Business Class",
                "First Class"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                travelClasses
        );
        binding.spnTravelClass.setAdapter(adapter);

//        String[] numPassenger = {
//                "1 Passenger",
//                "2 Passengers",
//                "3 Passengers",
//                "4 Passengers",
//        };
//        ArrayAdapter<String> adapterNum = new ArrayAdapter<>(
//                getContext(),
//                R.layout.spinner_item,
//                numPassenger
//        );
//        adapterNum.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        numPassengerSpinner.setAdapter(adapterNum);

        binding.llAddPassenger.setOnClickListener(v -> {
            AdditionalPassengerFragment fragment = new AdditionalPassengerFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }
}