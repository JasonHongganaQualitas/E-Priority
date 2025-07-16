package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OngoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.FlightInformation;
import id.co.qualitas.epriority.model.Trips;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCustomerFragment extends BaseFragment {
    private FragmentHomeCustomerBinding binding;
    View view;
    OngoingTripAdapter adapter;
    List<Trips> tripsList;
    FlightInformation information;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeCustomerBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        init();
        initialize();
        binding.flightDetailsLL.setVisibility(View.GONE);
        binding.btnContinue.setVisibility(View.GONE);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Arrival"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Departure"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){

                }
                else {

                }
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

        binding.ongoingTripRV.setVisibility(View.VISIBLE);
        binding.noTripLL.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOngoingTrips();
//        getFlightInformation();
    }

    private void initAdapter() {
        binding.ongoingTripRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OngoingTripAdapter(tripsList);
        binding.ongoingTripRV.setAdapter(adapter);
    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
    }

    public void getOngoingTrips(){
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getOngoingTrips();
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                 if (response.isSuccessful()){
                     WSMessage result = response.body();
                     if (result != null){
                         String jsonInString = new Gson().toJson(result.getResult());
                         Type listType = new TypeToken<ArrayList<Trips>>() {
                         }.getType();
                         List<Trips> tempList = new Gson().fromJson(jsonInString, listType);
                         if (Helper.isNotEmptyOrNull(tempList)){
                             tripsList = new ArrayList<>();
                             tripsList.addAll(tempList);
                             initAdapter();
                         }
                     }
                     else {
                         openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                     }
                 }
                 else {
                     openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                 }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {

            }
        });

    }

    public void getFlightInformation(){
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getFlightInformation("3354642b13de213ea1b4d32469d8686d", "GA404");
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    WSMessage result = response.body();
                    if (result != null){
                        String jsonInString = new Gson().toJson(result.getResult());
                        FlightInformation flightInformation = new Gson().fromJson(jsonInString, FlightInformation.class);
                        if (flightInformation != null){
                            information = flightInformation;
                            binding.flightTxt.setText(information.getFlight().getIata());
                        }
                    }
                    else {
                        openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                    }
                }
                else {
                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {

            }
        });
    }

}