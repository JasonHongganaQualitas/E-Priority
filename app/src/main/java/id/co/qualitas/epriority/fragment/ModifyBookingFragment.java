package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;

public class ModifyBookingFragment extends Fragment {
    View view;
    TextView btnConfirm, btnCancel;
    LinearLayout btnEditPlan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_modify_booking, container, false);

        initialize();

        btnConfirm.setOnClickListener(v -> {

        });

        btnCancel.setOnClickListener(v -> {

        });

        btnEditPlan.setOnClickListener(v -> {
            EditPlanDetailsFragment fragment = new EditPlanDetailsFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        });

        return view;
    }

    private void initialize() {
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnEditPlan = view.findViewById(R.id.btnEditPlan);
    }
}