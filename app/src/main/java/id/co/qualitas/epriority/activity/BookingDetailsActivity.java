package id.co.qualitas.epriority.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.Window;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsBinding;
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

public class BookingDetailsActivity extends BaseActivity {
    AgentAdapter agentAdapter;
    List<Agent> mAgentList = new ArrayList<>();
    List<Passenger> mPassengerList = new ArrayList<>();
    private FragmentBookingDetailsBinding binding;
    private TripsResponse tripHeader;
    private TripsResponse tripDetail;
    private PassengerTripsAdapter passengerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = FragmentBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        initAdapter();

        binding.btnViewQR.setOnClickListener(v -> {
//            QRFragment fragment = new QRFragment();
//            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        binding.btnModifyTrip.setOnClickListener(v -> {
            intent = new Intent(BookingDetailsActivity.this, ModifyBookingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        if (Helper.getItemParam(Constants.TRIP_HEADER) != null) {
            tripHeader = (TripsResponse) Helper.getItemParam(Constants.TRIP_HEADER);
            getDetails();
        } else {
            setToast("Please try again later.");
            onBackPressed();
        }
    }

    private void initAdapter() {
        binding.passengerDetailsRV.setLayoutManager(new LinearLayoutManager(BookingDetailsActivity.this));
        passengerAdapter = new PassengerTripsAdapter(BookingDetailsActivity.this, mPassengerList, true, (header, pos) -> {
        });
        binding.passengerDetailsRV.setAdapter(passengerAdapter);

        binding.agentRV.setLayoutManager(new LinearLayoutManager(BookingDetailsActivity.this));
        agentAdapter = new AgentAdapter(BookingDetailsActivity.this, mAgentList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(agentAdapter);
    }

    public void getDetails() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getDetailTrips(String.valueOf(tripHeader.getId()));
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            tripDetail = new Gson().fromJson(jsonInString, TripsResponse.class);
                            setData();
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

    private void setData() {
        String dateFrom = "", dateTo = "", date = null, time = null;
        if (tripDetail.getDate_from() != null) {
            dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, tripDetail.getDate_from());
        }
        if (tripDetail.getDate_to() != null) {
            dateTo = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, tripDetail.getDate_to());
        }

        if (Helper.isNullOrEmpty(tripDetail.getFlight_date())) {
            date = "-";
        } else {
            date = Helper.changeFormatDate1(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_8, tripDetail.getFlight_date());
        }

        if (Helper.isNullOrEmpty(tripDetail.getFlight_time())) {
            time = "-";
        } else {
            time = Helper.changeFormatDate1(Constants.DATE_PATTERN_13, Constants.DATE_PATTERN_9, tripDetail.getFlight_time());
        }

        binding.txtTripId.setText("Booking Trips No. #" + tripDetail.getTrip_id());
        binding.txtType.setText(Helper.isEmpty(tripDetail.getTrip_type(), ""));
        binding.txtTripDate.setText(date + " at " + time);
        binding.txtDateFrom.setText(dateFrom);
        binding.txtDateTo.setText(dateTo);
        binding.txtRouteFrom.setText(Helper.isEmpty(tripDetail.getRoute_from(), ""));
        binding.txtRouteTo.setText(Helper.isEmpty(tripDetail.getRoute_to(), ""));
        binding.txtBookingId.setText(Helper.isEmpty(tripDetail.getBooking_id(), ""));
        binding.txtFlightNumber.setText(Helper.isEmpty(tripDetail.getFlight_no(), ""));
        binding.passangerDetTitleTxt.setText("Passenger Details (" + tripDetail.getPassenger_count() + ")");

        mAgentList = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(tripDetail.getAgent_list())) {
            mAgentList.addAll(tripDetail.getAgent_list());
        }
        agentAdapter.setFilteredList(mAgentList);

        mPassengerList = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(tripDetail.getPassengers())) {
            mPassengerList.addAll(tripDetail.getPassengers());
        }
        passengerAdapter.setFilteredList(mPassengerList);

        Packages packages = tripDetail.getPackages();
        if (packages != null) {
            if (packages.getTrip_airporttransfer() != null) {
                Packages param = packages.getTrip_airporttransfer();
                binding.llAirportHeader.setVisibility(View.VISIBLE);
                binding.llAirportDetail.setVisibility(View.VISIBLE);
                if (param.getPickup_time() != null) {
                    dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, param.getPickup_time());
                }
                binding.txtContact.setText(Helper.isEmpty(param.getContact_no(), ""));
                binding.txtVehicleType.setText(Helper.isEmpty(param.getVehicle_type_name(), ""));
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
                binding.txtLoungeType.setText(Helper.isEmpty(param.getLounge_type_name(), ""));
                binding.txtOtherLounge.setText(Helper.isEmpty(param.getRequest_note(), ""));
            } else {
                binding.llLoungeHeader.setVisibility(View.GONE);
                binding.llLoungeDetail.setVisibility(View.GONE);
            }

            if (packages.getFlight_detail() != null) {
                Packages param = packages.getFlight_detail();
                binding.llFlightHeader.setVisibility(View.VISIBLE);
                binding.llFlightDetail.setVisibility(View.VISIBLE);
                binding.txtFLightClass.setText(Helper.isEmpty(param.getFlight_class_name(), ""));
                binding.txtOtherFlight.setText(Helper.isEmpty(param.getRequest_note(), ""));
            } else {
                binding.llFlightHeader.setVisibility(View.GONE);
                binding.llFlightDetail.setVisibility(View.GONE);
            }

            if (packages.getTrip_fastlane() != null) {
                Packages param = packages.getTrip_fastlane();
                binding.llFastLaneHeader.setVisibility(View.VISIBLE);
                binding.llFastLaneDetail.setVisibility(View.VISIBLE);
                binding.txtFastType.setText(Helper.isEmpty(param.getLane_type_name(), ""));
                binding.txtOtherFast.setText(Helper.isEmpty(param.getRequest_note(), ""));
            } else {
                binding.llFastLaneHeader.setVisibility(View.GONE);
                binding.llFastLaneDetail.setVisibility(View.GONE);
            }

            if (packages.getTrip_baggageservice() != null) {
                Packages param = packages.getTrip_baggageservice();
                binding.llBaggageHeader.setVisibility(View.VISIBLE);
                binding.llBaggageDetail.setVisibility(View.VISIBLE);
                binding.txtBaggage.setText(Helper.isEmpty(param.getBaggage_type_name(), ""));
                binding.txtOtherBaggage.setText(Helper.isEmpty(param.getRequest_note(), ""));
            } else {
                binding.llBaggageHeader.setVisibility(View.GONE);
                binding.llBaggageDetail.setVisibility(View.GONE);
            }
        }
    }
}