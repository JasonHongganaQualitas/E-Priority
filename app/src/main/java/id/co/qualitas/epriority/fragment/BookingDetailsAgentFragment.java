package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsAgentFragment extends BaseFragment implements IOnBackPressed {
    private FragmentBookingDetailsAgentBinding binding;
    View view;
    TripsResponse tripsResponse, currTrip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingDetailsAgentBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        getBookingDetails();
        init();
        initialize();

        return view;
    }

    private void getBookingDetails() {
        tripsResponse = (TripsResponse) Helper.getItemParam(Constants.BOOKING_DETAIL);
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getBookingDetails(String.valueOf(tripsResponse.getId()));
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            TripsResponse tempTrip = new Gson().fromJson(jsonInString, TripsResponse.class);
                            if (tempTrip != null){
                                currTrip = tempTrip;
                                setData();
                            }

                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
                    }
                } else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(Constants.INTERNAL_SERVER_ERROR);
            }
        });
    }

    private void setData() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy 'at' HH:mm");
        SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd MMMM yyyy");

        binding.bookingNoTxt.setText("Booking No. " + currTrip.getBooking_id());
        try {
            Date date = inputFormat1.parse(currTrip.getTrip_date());

            binding.dateTxt.setText(outputFormat1.format(date));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        binding.flightDetailsCard.tvFlight.setText(currTrip.getFlight_no());
        binding.flightDetailsCard.tvAirline.setText(currTrip.getAirline());
        binding.flightDetailsCard.tvAircraft.setText(currTrip.getAircraft());
        binding.flightDetailsCard.tvRouteFrom.setText(currTrip.getRoute_from());
        binding.flightDetailsCard.tvRouteTo.setText(currTrip.getRoute_to());
        try {
            Date dateFrom = inputFormat.parse(currTrip.getDate_from());
            Date dateTo = inputFormat.parse(currTrip.getDate_to());

            binding.flightDetailsCard.tvDepartureDate.setText(outputFormat.format(dateFrom));
            binding.flightDetailsCard.tvArrivalDate.setText(outputFormat.format(dateTo));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        binding.passengerDetailsCard.tvPassengerCount.setText(currTrip.getPassengers().size() + "");
        binding.contactCard.tvContactName.setText(currTrip.getCustomer_name());
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        binding.passengerDetailsCard.btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.setItemParam(Constants.PASSANGER_DETAIL, currTrip.getPassengers());
                PassangerDetailsFragment fragment2 = new PassangerDetailsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });

        binding.packageDetailsCard.btnSeeMoreAirport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirportTransferFragment fragment2 = new AirportTransferFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
        binding.packageDetailsCard.btnSeeMoreLounge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoungeAccessFragment fragment2 = new LoungeAccessFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
        binding.packageDetailsCard.btnSeeMoreFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoungeAccessFragment fragment2 = new LoungeAccessFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_container, fragment2);
                fragmentTransaction.addToBackStack("Booking Details Agent");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}