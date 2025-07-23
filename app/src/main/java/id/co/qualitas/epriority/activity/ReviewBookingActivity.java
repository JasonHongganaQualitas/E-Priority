package id.co.qualitas.epriority.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.DialogBookingCreatedBinding;
import id.co.qualitas.epriority.databinding.FragmentReviewBookingBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Agent;
import id.co.qualitas.epriority.model.Packages;
import id.co.qualitas.epriority.model.Passenger;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewBookingActivity extends BaseActivity {
    PassengerTripsAdapter adapter;
    private FragmentReviewBookingBinding binding;
    List<Passenger> passengerList = new ArrayList<>();
    private TripsResponse createTrips;
    private List<Agent> mChoosenAgentList = new ArrayList<>();
    private AgentAdapter agentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = FragmentReviewBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        if (Helper.getItemParam(Constants.DATA_CREATE_TRIPS) != null) {
            createTrips = (TripsResponse) Helper.getItemParam(Constants.DATA_CREATE_TRIPS);
            passengerList = new ArrayList<>();
            passengerList.addAll(createTrips.getPassengers());
            mChoosenAgentList = new ArrayList<>();
            mChoosenAgentList.addAll(createTrips.getAgent_list());
            initAdapter();
            setData();
        } else {
            onBackPressed();
        }

        binding.btnCreate.setOnClickListener(v -> {
            createTrips();
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setData() {
        String dateFrom = "", dateTo = "";
        if (createTrips.getDate_from() != null) {
            dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, createTrips.getDate_from());
        }
        if (createTrips.getDate_to() != null) {
            dateTo = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, createTrips.getDate_to());
        }

        binding.txtType.setText(Helper.isEmpty(createTrips.getTrip_type(), ""));
        binding.txtDateFrom.setText(dateFrom);
        binding.txtDateTo.setText(dateTo);
        binding.txtRouteFrom.setText(Helper.isEmpty(createTrips.getRoute_from(), ""));
        binding.txtRouteTo.setText(Helper.isEmpty(createTrips.getRoute_to(), ""));
        binding.txtBookingId.setText(Helper.isEmpty(createTrips.getBooking_id(), ""));
        binding.txtFlightNumber.setText(Helper.isEmpty(createTrips.getFlight_no(), ""));
        binding.passangerDetTitleTxt.setText("Passenger Details (" + createTrips.getPassenger_count() + ")");

        Packages packages = createTrips.getPackages();
        if (packages.getTrip_airporttransfer() != null) {
            Packages param = packages.getTrip_airporttransfer();
            binding.llAirportHeader.setVisibility(View.VISIBLE);
            binding.llAirportDetail.setVisibility(View.VISIBLE);
            if (param.getPickup_time() != null) {
                dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, param.getPickup_time());
            }
            binding.txtContact.setText(Helper.isEmpty(param.getContact_no(), ""));
            binding.txtVehicleType.setText(Helper.isEmpty(param.getSelectedVehicleType().getName(), ""));
            binding.txtOtherAirport.setText(Helper.isEmpty(param.getRequest_note(), ""));
            binding.txtPickUpTime.setText(dateFrom);
        } else {
            binding.llAirportHeader.setVisibility(View.GONE);
            binding.llAirportDetail.setVisibility(View.GONE);
        }

        if (packages.getTrip_loungeaccess() != null) {
            Packages param = packages.getTrip_loungeaccess();
            binding.llLoungeHeader.setVisibility(View.VISIBLE);
            binding.llLoungeDetail.setVisibility(View.VISIBLE);
            binding.txtLoungeType.setText(Helper.isEmpty(param.getSelectedLoungeType().getName(), ""));
            binding.txtOtherLounge.setText(Helper.isEmpty(param.getRequest_note(), ""));
        } else {
            binding.llLoungeHeader.setVisibility(View.GONE);
            binding.llLoungeDetail.setVisibility(View.GONE);
        }

        if (packages.getFlight_detail() != null) {
            Packages param = packages.getFlight_detail();
            binding.llFlightHeader.setVisibility(View.VISIBLE);
            binding.llFlightDetail.setVisibility(View.VISIBLE);
            binding.txtFLightClass.setText(Helper.isEmpty(param.getSelectedFlightClasses().getName(), ""));
            binding.txtOtherFlight.setText(Helper.isEmpty(param.getRequest_note(), ""));
        } else {
            binding.llFlightHeader.setVisibility(View.GONE);
            binding.llFlightDetail.setVisibility(View.GONE);
        }

        if (packages.getTrip_fastlane() != null) {
            Packages param = packages.getTrip_fastlane();
            binding.llFastLaneHeader.setVisibility(View.VISIBLE);
            binding.llFastLaneDetail.setVisibility(View.VISIBLE);
            binding.txtFastType.setText(Helper.isEmpty(param.getSelectedFastlaneType().getName(), ""));
            binding.txtOtherFast.setText(Helper.isEmpty(param.getRequest_note(), ""));
        } else {
            binding.llFastLaneHeader.setVisibility(View.GONE);
            binding.llFastLaneDetail.setVisibility(View.GONE);
        }

        if (packages.getTrip_baggageservice() != null) {
            Packages param = packages.getTrip_baggageservice();
            binding.llBaggageHeader.setVisibility(View.VISIBLE);
            binding.llBaggageDetail.setVisibility(View.VISIBLE);
            binding.txtBaggage.setText(Helper.isEmpty(param.getSelectedBaggageType().getName(), ""));
            binding.txtOtherBaggage.setText(Helper.isEmpty(param.getRequest_note(), ""));
        } else {
            binding.llBaggageHeader.setVisibility(View.GONE);
            binding.llBaggageDetail.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        binding.passengerDetailsRV.setLayoutManager(new LinearLayoutManager(ReviewBookingActivity.this));
        adapter = new PassengerTripsAdapter(ReviewBookingActivity.this, passengerList, true, (header, pos) -> {
        });
        binding.passengerDetailsRV.setAdapter(adapter);

        binding.agentRV.setLayoutManager(new LinearLayoutManager(ReviewBookingActivity.this));
        agentAdapter = new AgentAdapter(ReviewBookingActivity.this, mChoosenAgentList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(agentAdapter);
    }

    public void createTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.createTrips(createTrips);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                            dialogBookingCreated();
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
                    }
                } else {
                    setToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                setToast(t.getMessage());
            }
        });
    }

    private void dialogBookingCreated() {
        DialogBookingCreatedBinding dialogBinding = DialogBookingCreatedBinding.inflate(LayoutInflater.from(ReviewBookingActivity.this));
        dialog = new Dialog(ReviewBookingActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }, 500);
    }
}