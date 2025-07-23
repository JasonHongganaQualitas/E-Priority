package id.co.qualitas.epriority.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.Window;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentBookingDetailsBinding;
import id.co.qualitas.epriority.fragment.ModifyBookingFragment;
import id.co.qualitas.epriority.fragment.QRFragment;
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
//            ModifyBookingFragment fragment = new ModifyBookingFragment();
//            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
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
        String dateFrom = "", dateTo = "", tripDate = "";
        if (tripDetail.getDate_from() != null) {
            dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, tripDetail.getDate_from());
        }
        if (tripDetail.getDate_to() != null) {
            dateTo = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, tripDetail.getDate_to());
        }
        if (tripDetail.getTrip_date() != null) {
            tripDate = Helper.changeFormatDate(Constants.DATE_PATTERN_2, Constants.DATE_PATTERN_4, tripDetail.getTrip_date());
        }

        binding.txtTripId.setText("Order No. #" + tripDetail.getTrip_id());
        binding.txtType.setText(Helper.isEmpty(tripDetail.getTrip_type(), ""));
        binding.txtDateFrom.setText(dateFrom);
        binding.txtDateTo.setText(dateTo);
        binding.txtRouteFrom.setText(Helper.isEmpty(tripDetail.getRoute_from(), ""));
        binding.txtRouteTo.setText(Helper.isEmpty(tripDetail.getRoute_to(), ""));
        binding.txtBookingId.setText(Helper.isEmpty(tripDetail.getBooking_id(), ""));
        binding.txtFlightNumber.setText(Helper.isEmpty(tripDetail.getFlight_no(), ""));
        binding.passangerDetTitleTxt.setText("Passenger Details (" + tripDetail.getPassenger_count() + ")");

        mAgentList = new ArrayList<>();
        mAgentList.addAll(tripDetail.getAgent_list());
        agentAdapter.setFilteredList(mAgentList);

        mPassengerList = new ArrayList<>();
        mPassengerList.addAll(tripDetail.getPassengers());
        passengerAdapter.setFilteredList(mPassengerList);

//        Packages packages = tripDetail.getPackages();
//        if (packages.getAirport_transfer() != null) {
//            Packages param = packages.getAirport_transfer();
//            binding.llAirportHeader.setVisibility(View.VISIBLE);
//            binding.llAirportDetail.setVisibility(View.VISIBLE);
//            if (param.getPickup_time() != null) {
//                dateFrom = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_5, param.getPickup_time());
//            }
//            binding.txtContact.setText(Helper.isEmpty(param.getContact_no(), ""));
//            binding.txtVehicleType.setText(Helper.isEmpty(param.getSelectedVehicleType().getName(), ""));
//            binding.txtOtherAirport.setText(Helper.isEmpty(param.getRequest_note(), ""));
//            binding.txtPickUpTime.setText(dateFrom);
//        } else {
//            binding.llAirportHeader.setVisibility(View.GONE);
//            binding.llAirportDetail.setVisibility(View.GONE);
//        }
//
//        if (packages.getLounge_access() != null) {
//            Packages param = packages.getLounge_access();
//            binding.llLoungeHeader.setVisibility(View.VISIBLE);
//            binding.llLoungeDetail.setVisibility(View.VISIBLE);
//            binding.txtLoungeType.setText(Helper.isEmpty(param.getSelectedLoungeType().getName(), ""));
//            binding.txtOtherLounge.setText(Helper.isEmpty(param.getRequest_note(), ""));
//        } else {
//            binding.llLoungeHeader.setVisibility(View.GONE);
//            binding.llLoungeDetail.setVisibility(View.GONE);
//        }
//
//        if (packages.getFlight_detail() != null) {
//            Packages param = packages.getFlight_detail();
//            binding.llFlightHeader.setVisibility(View.VISIBLE);
//            binding.llFlightDetail.setVisibility(View.VISIBLE);
//            binding.txtFLightClass.setText(Helper.isEmpty(param.getSelectedFlightClasses().getName(), ""));
//            binding.txtOtherFlight.setText(Helper.isEmpty(param.getRequest_note(), ""));
//        } else {
//            binding.llFlightHeader.setVisibility(View.GONE);
//            binding.llFlightDetail.setVisibility(View.GONE);
//        }
//
//        if (packages.getFast_lane() != null) {
//            Packages param = packages.getFast_lane();
//            binding.llFastLaneHeader.setVisibility(View.VISIBLE);
//            binding.llFastLaneDetail.setVisibility(View.VISIBLE);
//            binding.txtFastType.setText(Helper.isEmpty(param.getSelectedFastlaneType().getName(), ""));
//            binding.txtOtherFast.setText(Helper.isEmpty(param.getRequest_note(), ""));
//        } else {
//            binding.llFastLaneHeader.setVisibility(View.GONE);
//            binding.llFastLaneDetail.setVisibility(View.GONE);
//        }
//
//        if (packages.getBaggage_service() != null) {
//            Packages param = packages.getBaggage_service();
//            binding.llBaggageHeader.setVisibility(View.VISIBLE);
//            binding.llBaggageDetail.setVisibility(View.VISIBLE);
//            binding.txtBaggage.setText(Helper.isEmpty(param.getSelectedBaggageType().getName(), ""));
//            binding.txtOtherBaggage.setText(Helper.isEmpty(param.getRequest_note(), ""));
//        } else {
//            binding.llBaggageHeader.setVisibility(View.GONE);
//            binding.llBaggageDetail.setVisibility(View.GONE);
//        }
    }
}