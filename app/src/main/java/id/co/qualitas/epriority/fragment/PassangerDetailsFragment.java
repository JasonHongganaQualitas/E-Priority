package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.adapter.OngoingBookingAdapter;
import id.co.qualitas.epriority.adapter.PassengerDetailsAdapter;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentPassengerDetailsBinding;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.PassengerDetails;

public class PassangerDetailsFragment extends BaseFragment implements IOnBackPressed {

    private FragmentPassengerDetailsBinding binding;
    private PassengerDetailsAdapter adapter;
    private List<PassengerDetails> passengers;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPassengerDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();

        return view;
    }

    private void initialize() {
        passengers = new ArrayList<>();
        passengers.add(new PassengerDetails(
                "John Smith", "+123456788", "johnsmith@gmail.com", "20/07/2005", "Economy Class",
                "TR000123", "JAPAN", "20/07/2023"));

        passengers.add(new PassengerDetails(
                "Jane Doe", "+987654321", "janedoe@gmail.com", "10/03/1998", "Business Class",
                "TR000456", "INDONESIA", "15/10/2025"));

        if(passengers.size() != 0) {
            adapter = new PassengerDetailsAdapter(this, passengers);
            binding.rvPassengerDetails.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvPassengerDetails.setAdapter(adapter);
            binding.rvPassengerDetails.setVisibility(View.VISIBLE);
            binding.lEmpty.setVisibility(View.GONE);
        }else{
            binding.rvPassengerDetails.setVisibility(View.GONE);
            binding.lEmpty.setVisibility(View.VISIBLE);
        }
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}