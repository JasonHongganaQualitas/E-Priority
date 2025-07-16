package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
public class EditProfileFragment extends Fragment {
    View view;
    TextView btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initialize();

        btnSave.setOnClickListener(v -> {
            ProfileFragment fragment = new ProfileFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }

    private void initialize() {
        btnSave = view.findViewById(R.id.btnSave);
    }
}