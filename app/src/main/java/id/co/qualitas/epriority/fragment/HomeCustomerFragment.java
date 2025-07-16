package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Booking;
import id.co.qualitas.epriority.model.FlightInformation;
import id.co.qualitas.epriority.model.Trips;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCustomerFragment extends BaseFragment {
    private FragmentHomeCustomerBinding binding;
    boolean arrival = true;
    OnGoingTripAdapter adapter;
    private List<Booking> mList = new ArrayList<>(), arrivalList = new ArrayList<>(), departureList = new ArrayList<>();
    private FlightInformation information;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeCustomerBinding.inflate(inflater, container, false);

        init();
        initialize();
        arrival = true;
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Arrival"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Departure"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                arrival = tab.getPosition() == 0;
                setListView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnSearchFlight.setOnClickListener(v -> {
            binding.flightDetailsLL.setVisibility(View.VISIBLE);
            binding.btnContinue.setVisibility(View.VISIBLE);
        });

        binding.btnContinue.setOnClickListener(v -> {
            PassengerInformationFragment fragment = new PassengerInformationFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        initAdapter();
        getOnGoingCustomerTrips();
        return binding.getRoot();
    }

    private void initAdapter() {
        binding.ongoingTripRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OnGoingTripAdapter(HomeCustomerFragment.this, mList, (header, pos) -> {
        });
        binding.ongoingTripRV.setAdapter(adapter);
    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
    }

    public void getOnGoingCustomerTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getOnGoingCustomerTrips();
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Trips trips = new Gson().fromJson(jsonInString, Trips.class);
                            if (trips != null) {
                                arrivalList = new ArrayList<>();
                                departureList = new ArrayList<>();
                                if (trips.getArrivalTrips() != null) {
                                    arrivalList.addAll(trips.getArrivalTrips());
                                }
                                if (trips.getDepartureTrips() != null) {
                                    departureList.addAll(trips.getDepartureTrips());
                                }
                            }
                        } else {
                            openDialogInformation(Constants.DATA_NOT_FOUND, response.message(), null);
                        }
                    } else {
                        openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                    }
                } else {
                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                }
                setListView();
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
                setListView();
            }
        });

    }

    public void getFlightInformation() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getFlightInformation("3354642b13de213ea1b4d32469d8686d", "GA404");
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        String jsonInString = new Gson().toJson(result.getResult());
                        FlightInformation flightInformation = new Gson().fromJson(jsonInString, FlightInformation.class);
                        if (flightInformation != null) {
                            information = flightInformation;
                            binding.flightTxt.setText(information.getFlight().getIata());
                        }
                    } else {
                        openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                    }
                } else {
                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
            }
        });
    }

    private void setListView() {
        mList = new ArrayList<>();
        if (arrival) {
            mList.addAll(arrivalList);
        } else {
            mList.addAll(departureList);
        }
        adapter.setFilteredList(mList);
        if (Helper.isNotEmptyOrNull(mList)) {
            binding.ongoingTripRV.setVisibility(View.VISIBLE);
            binding.noTripLL.setVisibility(View.GONE);
        } else {
            binding.ongoingTripRV.setVisibility(View.GONE);
            binding.noTripLL.setVisibility(View.VISIBLE);
        }
    }
}