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

public class PassengerInformationFragment extends Fragment {
    View view;
    ImageView backBtn;
    TextView btnChoose, addPassengerTxt;
    Spinner travelClassSpinner, numPassengerSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_passenger_information, container, false);

        initialize();

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnChoose.setOnClickListener(v -> {
            ChoosePackageFragment fragment = new ChoosePackageFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        String[] travelClasses = {
                "Economy Class",
                "First Class"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                travelClasses
        );
        travelClassSpinner.setAdapter(adapter);

        String[] numPassenger = {
                "1 Passenger",
                "2 Passengers",
                "3 Passengers",
                "4 Passengers",
        };
        ArrayAdapter<String> adapterNum = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                numPassenger
        );
        adapterNum.setDropDownViewResource(R.layout.spinner_dropdown_item);
        numPassengerSpinner.setAdapter(adapterNum);

        addPassengerTxt.setOnClickListener(v -> {
            AdditionalPassengerFragment fragment = new AdditionalPassengerFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        btnChoose = view.findViewById(R.id.btnChoose);
        travelClassSpinner = view.findViewById(R.id.travelClassSpinner);
        addPassengerTxt = view.findViewById(R.id.addPassengerTxt);
        numPassengerSpinner = view.findViewById(R.id.numPassengerSpinner);
    }
}