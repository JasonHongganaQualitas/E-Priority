package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.R;

public class UpdateSubmittedFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BookingDetailsFragment fragment = new BookingDetailsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
            }
        }, 5000);
        return inflater.inflate(R.layout.fragment_update_submitted, container, false);
    }
}