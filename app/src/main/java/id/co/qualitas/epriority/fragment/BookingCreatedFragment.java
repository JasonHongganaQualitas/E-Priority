package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.R;

public class BookingCreatedFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking_created, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BookingDetailsFragment fragment = new BookingDetailsFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
            }
        }, 5000);
        return view;
    }
}