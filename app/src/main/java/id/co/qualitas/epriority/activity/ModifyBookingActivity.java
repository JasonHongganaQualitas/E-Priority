package id.co.qualitas.epriority.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.adapter.SpinnerDropDownAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.BottomsheetFlightInstructionBinding;
import id.co.qualitas.epriority.databinding.DialogBookingCreatedBinding;
import id.co.qualitas.epriority.databinding.DialogChooseAgentBinding;
import id.co.qualitas.epriority.databinding.DialogUpdateSubmittedBinding;
import id.co.qualitas.epriority.databinding.FragmentModifyBookingBinding;
import id.co.qualitas.epriority.fragment.DatePickerFragment;
import id.co.qualitas.epriority.fragment.TimePickerFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Agent;
import id.co.qualitas.epriority.model.Dropdown;
import id.co.qualitas.epriority.model.Packages;
import id.co.qualitas.epriority.model.Passenger;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyBookingActivity extends BaseActivity implements TimePickerFragment.TimeSelectedListener, DatePickerFragment.DateSelectedListener {
    private EditText activeDateField, activeTimeField;
    private Passenger dataPassenger;
    private TripsResponse createTrips;
    private PassengerTripsAdapter adapter;
    AgentAdapter agentAdapter, adapterDialog;
    List<Passenger> passengerList = new ArrayList<>();
    private FragmentModifyBookingBinding binding;
    private List<Agent> mAgentList, mChoosenAgentList = new ArrayList<>();
    private Packages masterPackage;
    boolean airportSelected = false, loungeSelected = false, flightSelected = false, fastLaneSelected = false, baggageSelected = false;
    private SpinnerDropDownAdapter vehicleTypeAdapter, baggageHandlingAdapter, fastLaneAdapter, flightClassesAdapter, loungeTypeAdapter;
    private Dropdown selectedVehicleType, selectedLoungeType, selectedBaggage, selectedFastType, selectedFLightClass;
    private LinearLayoutManager linearLayout;
    private int offset;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = FragmentModifyBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        binding.btnConfirm.setOnClickListener(v -> {
            if (validateData()) {
                saveData();//btnConfirm
                modifyTrips();
            }
        });

        binding.btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private boolean validateData() {
        int empty = 0;
        if (Helper.isEmpty(binding.edtFlightReservationCode)) {
            binding.edtFlightReservationCode.setError("Please enter flight reservation code");
            empty++;
        }
        if (Helper.isEmpty(binding.edtDateFrom)) {
            binding.edtDateFrom.setError("Please enter date");
            empty++;
        }
        if (Helper.isEmpty(binding.edtDateTo)) {
            binding.edtDateTo.setError("Please enter date");
            empty++;
        }
        if (Helper.isEmpty(binding.edtTimeFrom)) {
            binding.edtTimeFrom.setError("Please enter time");
            empty++;
        }
        if (Helper.isEmpty(binding.edtTimeTo)) {
            binding.edtTimeTo.setError("Please enter time");
            empty++;
        }
        if (Helper.isEmpty(binding.edtAirline)) {
            binding.edtAirline.setError("Please enter airline");
            empty++;
        }
        if (Helper.isEmpty(binding.edtRouteFrom)) {
            binding.edtRouteFrom.setError("Please enter route from");
            empty++;
        }
        if (Helper.isEmpty(binding.edtRouteTo)) {
            binding.edtRouteTo.setError("Please enter route to");
            empty++;
        }
        if (Helper.isEmpty(binding.edtNumberPassenger)) {
            binding.edtNumberPassenger.setError("Please enter passenger count");
            empty++;
        }
        if (Helper.isEmptyOrNull(passengerList)) {
            setToast("Please add passenger");
            empty++;
        }

        if (airportSelected) {
            if (Helper.isEmpty(binding.edtPickUpTime)) {
                binding.edtPickUpTime.setError("Please enter pick up time");
                empty++;
            }
            if (Helper.isEmpty(binding.edtPickUpDate)) {
                binding.edtPickUpDate.setError("Please enter pick up date");
                empty++;
            }
            if (Helper.isEmpty(binding.edtContact)) {
                binding.edtContact.setError("Please enter contact number");
                empty++;
            }
            if (selectedVehicleType == null) {
                setToast("Please select vehicle type");
                empty++;
            }
        }

        if (loungeSelected) {
            if (selectedLoungeType == null) {
                setToast("Please select lounge type");
                empty++;
            }
        }

        if (flightSelected) {
            if (selectedFLightClass == null) {
                setToast("Please select flight class");
                empty++;
            }
        }

        if (fastLaneSelected) {
            if (selectedFastType == null) {
                setToast("Please select fast lane type");
                empty++;
            }
        }

        if (baggageSelected) {
            if (selectedBaggage == null) {
                setToast("Please select baggage type");
                empty++;
            }
        }

        return empty == 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void dialogBookingUpdated() {
        DialogUpdateSubmittedBinding dialogBinding = DialogUpdateSubmittedBinding.inflate(LayoutInflater.from(ModifyBookingActivity.this));
        dialog = new Dialog(ModifyBookingActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }, 500);
    }

    private void setData() {
        if (Helper.getItemParam(Constants.DATA_CREATE_TRIPS) != null) {
            createTrips = (TripsResponse) Helper.getItemParam(Constants.DATA_CREATE_TRIPS);

            if (Helper.getItemParam(Constants.DATA_PASSENGER) != null) {
                dataPassenger = (Passenger) Helper.getItemParam(Constants.DATA_PASSENGER);
                passengerList = new ArrayList<>();
                if (Helper.isNotEmptyOrNull(createTrips.getPassengers())) {
                    passengerList = createTrips.getPassengers();
                } else {
                    passengerList = new ArrayList<>();
                }
                passengerList.add(dataPassenger);
                createTrips.setPassengers(passengerList);
            }

            Helper.removeItemParam(Constants.DATA_PASSENGER);
        } else {
            passengerList = new ArrayList<>();
        }

        if (Helper.getItemParam(Constants.DETAIL_PASSENGER) != null) {
            Passenger edtPassenger = (Passenger) Helper.getItemParam(Constants.DETAIL_PASSENGER);
            if (Helper.isNotEmptyOrNull(passengerList)) {
                passengerList.remove(edtPassenger);
                passengerList.add(edtPassenger.getPos_passenger(), edtPassenger);
            }
            Helper.removeItemParam(Constants.DETAIL_PASSENGER);
        }
        getPackages();
    }

    public <T extends Dropdown> void selectSpinnerItemById(Spinner spinner, List<T> itemList, Dropdown selectedItem) {
        int positionToSelect = -1;

        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == selectedItem.getId()) {
                positionToSelect = i;
                break;
            }
        }

        if (positionToSelect != -1) {
            spinner.setSelection(positionToSelect);
        } else {
            if (!itemList.isEmpty() && itemList.get(0).getId() == 0) {
                spinner.setSelection(0);
            }
        }
    }

    private void setDataView() {
        String dateFrom = !Helper.isEmpty(createTrips.getDate_from()) ? Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_8, createTrips.getDate_from()) : null;
        String timeFrom = !Helper.isEmpty(createTrips.getDate_from()) ? Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_9, createTrips.getDate_from()) : null;
        String dateTo = !Helper.isEmpty(createTrips.getDate_to()) ? Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_8, createTrips.getDate_to()) : null;
        String timeTo = !Helper.isEmpty(createTrips.getDate_to()) ? Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_9, createTrips.getDate_to()) : null;

        binding.txtTripId.setText("Booking Trips No. #" + createTrips.getTrip_id());
        binding.txtType.setText(Helper.isEmpty(createTrips.getTrip_type(), ""));
        binding.edtFlightReservationCode.setText(Helper.isEmpty(createTrips.getBooking_id(), ""));
        binding.edtAirline.setText(Helper.isEmpty(createTrips.getFlight_no(), ""));
        binding.edtRouteFrom.setText(Helper.isEmpty(createTrips.getRoute_from(), ""));
        binding.edtRouteTo.setText(Helper.isEmpty(createTrips.getRoute_to(), ""));
        binding.edtNumberPassenger.setText(Helper.isEmpty(createTrips.getPassenger_count() + "", ""));
        binding.edtDateFrom.setText(dateFrom);
        binding.edtTimeFrom.setText(timeFrom);
        binding.edtDateTo.setText(dateTo);
        binding.edtTimeTo.setText(timeTo);

        mChoosenAgentList = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(createTrips.getAgent_list())) {
            mChoosenAgentList.addAll(createTrips.getAgent_list());
        }

        passengerList = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(createTrips.getPassengers())) {
            passengerList.addAll(createTrips.getPassengers());
        }

        if (createTrips.getPackages() != null) {
            Packages packages = createTrips.getPackages();
            if (packages.getTrip_airporttransfer() != null) {
                Packages param = packages.getTrip_airporttransfer();
                airportSelected = false;
                selectAirport();//set data
                String datePickUp = "", timePickUp = "";
                if (param.getPickup_time() != null) {
                    datePickUp = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_8, param.getPickup_time());
                    timePickUp = Helper.changeFormatDate(Constants.DATE_PATTERN_12, Constants.DATE_PATTERN_9, param.getPickup_time());
                }
                selectedVehicleType = new Dropdown(param.getVehicle_type_id(), param.getVehicle_type_name());
                binding.edtContact.setText(Helper.isEmpty(param.getContact_no(), ""));
                binding.edtOtherAirport.setText(Helper.isEmpty(param.getRequest_note(), ""));
                binding.edtPickUpTime.setText(timePickUp);
                binding.edtPickUpDate.setText(datePickUp);
                selectSpinnerItemById(binding.spnVehicleType, masterPackage.getVehicleTypes(), selectedVehicleType);
            }
            if (packages.getTrip_loungeaccess() != null) {
                Packages param = packages.getTrip_loungeaccess();
                loungeSelected = false;
                selectLounge();
                selectedLoungeType = new Dropdown(param.getLounge_type_id(), param.getLounge_type_name());
                selectSpinnerItemById(binding.spnLoungeType, masterPackage.getLoungeTypes(), selectedLoungeType);
                binding.edtOtherLounge.setText(Helper.isEmpty(param.getRequest_note(), ""));
            }
            if (packages.getTrip_fastlane() != null) {
                Packages param = packages.getTrip_fastlane();
                fastLaneSelected = false;
                selectFastLane();
                selectedFastType = new Dropdown(param.getLane_type_id(), param.getLane_type_name());
                selectSpinnerItemById(binding.spnFastType, masterPackage.getFastlaneTypes(), selectedFastType);
                binding.edtOtherFast.setText(Helper.isEmpty(param.getRequest_note(), ""));
            }
            if (packages.getTrip_baggageservice() != null) {
                Packages param = packages.getTrip_baggageservice();
                baggageSelected = false;
                selectBaggage();
                selectedBaggage = new Dropdown(param.getBaggage_type_id(), param.getBaggage_type_name());
                selectSpinnerItemById(binding.spnBaggage, masterPackage.getBaggageTypes(), selectedBaggage);
                binding.edtOtherBaggage.setText(Helper.isEmpty(param.getRequest_note(), ""));
            }
            if (packages.getFlight_detail() != null) {
                Packages param = packages.getFlight_detail();
                flightSelected = false;
                selectFlight();
                selectedFLightClass = new Dropdown(param.getFlight_class_id(), param.getFlight_class_name());
                selectSpinnerItemById(binding.spnFLightClass, masterPackage.getFlightClasses(), selectedFLightClass);
                binding.edtOtherFlight.setText(Helper.isEmpty(param.getRequest_note(), ""));
            }
        }

        iniAdapter();
    }

    private void saveData() {
        if (createTrips == null) {
            createTrips = new TripsResponse();
        }
        String dateFrom = !Helper.isEmpty(binding.edtDateFrom) ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateFrom.getText().toString()) : null;
        String dateTo = !Helper.isEmpty(binding.edtDateTo) ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateTo.getText().toString()) : null;
        String timeFrom = !Helper.isEmpty(binding.edtTimeFrom) ? Helper.changeFormatDate(Constants.DATE_PATTERN_9, Constants.DATE_PATTERN_13, binding.edtTimeFrom.getText().toString()) : null;
        String timeTo = !Helper.isEmpty(binding.edtTimeTo) ? Helper.changeFormatDate(Constants.DATE_PATTERN_9, Constants.DATE_PATTERN_13, binding.edtTimeTo.getText().toString()) : null;
        createTrips.setCustomer_id(user.getId());
        createTrips.setBooking_id(binding.txtType.getText().toString());
        createTrips.setBooking_id(binding.edtFlightReservationCode.getText().toString());
        createTrips.setFlight_no(binding.edtAirline.getText().toString());
        createTrips.setAirline(binding.edtAirline.getText().toString());
        createTrips.setDate_from(dateFrom + " " + timeFrom);
        createTrips.setDate_to(dateTo + " " + timeTo);
        createTrips.setAircraft("");
        createTrips.setRoute_from(binding.edtRouteFrom.getText().toString());
        createTrips.setRoute_to(binding.edtRouteTo.getText().toString());
        createTrips.setPassenger_count(Integer.parseInt(binding.edtNumberPassenger.getText().toString()));
        createTrips.setPassengers(passengerList);

        Packages header = new Packages(), detail = new Packages();
        if (airportSelected) {
            detail = new Packages();
            detail.setVehicle_type(selectedVehicleType.getId());
            detail.setVehicle_type_name(selectedVehicleType.getName());
            detail.setVehicle_type_id(selectedVehicleType.getId());
            String time = !Helper.isEmpty(binding.edtPickUpTime) ? Helper.changeFormatDate(Constants.DATE_PATTERN_9, Constants.DATE_PATTERN_13, binding.edtPickUpTime.getText().toString()) : null;
            String date = Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtPickUpDate.getText().toString());
            detail.setPickup_time(date + " " + time);
            detail.setContact_no(binding.edtContact.getText().toString());
            detail.setRequest_note(binding.edtOtherAirport.getText().toString());
            detail.setSelectedVehicleType(selectedVehicleType);
            header.setTrip_airporttransfer(detail);
        } else {
            header.setTrip_airporttransfer(null);
        }

        if (loungeSelected) {
            detail = new Packages();
            detail.setLounge_type(selectedLoungeType.getId());
            detail.setLounge_type_id(selectedLoungeType.getId());
            detail.setLounge_type_name(selectedLoungeType.getName());
            detail.setRequest_note(binding.edtOtherLounge.getText().toString());
            detail.setSelectedLoungeType(selectedLoungeType);
            header.setTrip_loungeaccess(detail);
        } else {
            header.setTrip_loungeaccess(null);
        }

        if (flightSelected) {
            detail = new Packages();
            detail.setFlight_class(selectedFLightClass.getId());
            detail.setFlight_class_id(selectedFLightClass.getId());
            detail.setFlight_class_name(selectedFLightClass.getName());
            detail.setRequest_note(binding.edtOtherFlight.getText().toString());
            detail.setSelectedFlightClasses(selectedFLightClass);
            header.setFlight_detail(detail);
        } else {
            header.setFlight_detail(null);
        }

        if (fastLaneSelected) {
            detail = new Packages();
            detail.setType_lane(selectedFastType.getId());
            detail.setLane_type_id(selectedFastType.getId());
            detail.setLane_type_name(selectedFastType.getName());
            detail.setRequest_note(binding.edtOtherFast.getText().toString());
            detail.setSelectedFastlaneType(selectedFastType);
            header.setTrip_fastlane(detail);
        } else {
            header.setTrip_fastlane(null);
        }

        if (baggageSelected) {
            detail = new Packages();
            detail.setType_baggage(selectedBaggage.getId());
            detail.setBaggage_type_id(selectedBaggage.getId());
            detail.setBaggage_type_name(selectedBaggage.getName());
            detail.setRequest_note(binding.edtOtherBaggage.getText().toString());
            detail.setSelectedBaggageType(selectedBaggage);
            header.setTrip_baggageservice(detail);
        } else {
            header.setTrip_baggageservice(null);
        }

        createTrips.setAgent_list(mChoosenAgentList);
        createTrips.setPackages(header);

        Helper.setItemParam(Constants.DATA_CREATE_TRIPS, createTrips);
    }

    public void modifyTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.modifyTrips(String.valueOf(createTrips.getTrip_id()), createTrips);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                            dialogBookingUpdated();
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

    private void setViewOnClick() {
        binding.llAddPassenger.setOnClickListener(v -> {
            if (Helper.isEmpty(binding.edtNumberPassenger)) {
                binding.edtNumberPassenger.setError("Please enter passenger count");
            } else if (passengerList.size() >= Integer.parseInt(binding.edtNumberPassenger.getText().toString())) {
                setToast("Passenger can't be more than passenger count");
            } else {
                Helper.removeItemParam(Constants.DATA_PASSENGER);
                saveData();//llAddPassenger
                intent = new Intent(getApplicationContext(), CreatePassengerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        if (binding.edtDateFrom != null) {
            binding.edtDateFrom.setOnClickListener(v -> {
                activeDateField = binding.edtDateFrom; // Set active field
                showDatePickerDialog();//edtDateFrom
            });
            binding.edtDateFrom.setFocusable(false);
            binding.edtDateFrom.setClickable(true);
        }

        if (binding.edtDateTo != null) {
            binding.edtDateTo.setOnClickListener(v -> {
                activeDateField = binding.edtDateTo; // Set active field
                showDatePickerDialog();//edtDateTo
            });
            binding.edtDateTo.setFocusable(false);
            binding.edtDateTo.setClickable(true);
        }

        if (binding.edtTimeFrom != null) {
            binding.edtTimeFrom.setOnClickListener(v -> {
                activeTimeField = binding.edtTimeFrom; // Set active field
                openDialogTimePicker();//edtTimeFrom
            });
            binding.edtTimeFrom.setFocusable(false);
            binding.edtTimeFrom.setClickable(true);
        }

        if (binding.edtTimeTo != null) {
            binding.edtTimeTo.setOnClickListener(v -> {
                activeTimeField = binding.edtTimeTo; // Set active field
                openDialogTimePicker();//edtTimeTo
            });
            binding.edtTimeTo.setFocusable(false);
            binding.edtTimeTo.setClickable(true);
        }

        binding.imgQuestionFN.setOnClickListener(v -> showFlightInstructionBottomSheet(createTrips.getTrip_type().equals("Arrival")));
        binding.imgQuestionFRC.setOnClickListener(v -> showFRCInstructionBottomSheet());

        binding.btnChooseAgent.setOnClickListener(v -> {
            openDialogChooseAgent();
        });

        binding.llAirportHeader.setOnClickListener(v -> {
            selectAirport();//click
        });

        binding.llLoungeHeader.setOnClickListener(v -> {
            selectLounge();
        });

        binding.llFlightHeader.setOnClickListener(v -> {
            selectFlight();
        });

        binding.llFastLaneHeader.setOnClickListener(v -> {
            selectFastLane();
        });

        binding.llBaggageHeader.setOnClickListener(v -> {
            selectBaggage();
        });

        binding.cbAirport.setOnClickListener(v -> {
            selectAirport();
        });

        binding.cbLounge.setOnClickListener(v -> {
            selectLounge();
        });

        binding.cbFlight.setOnClickListener(v -> {
            selectFlight();
        });

        binding.cbFastLane.setOnClickListener(v -> {
            selectFastLane();
        });

        binding.cbBaggage.setOnClickListener(v -> {
            selectBaggage();
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void showFRCInstructionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomsheetFlightInstructionBinding bottomSheetBinding = BottomsheetFlightInstructionBinding.inflate(getLayoutInflater());
        bottomSheetBinding.tvTitle.setText("Flight Reservation Code Intruction");
        bottomSheetBinding.tvInstruction.setText("As shown on your airline ticket");

        bottomSheetBinding.btnClose.setOnClickListener(v -> bottomSheetDialog.cancel());
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePicker = DatePickerFragment.newInstance(this); // 'this' is the Activity
        datePicker.show(getSupportFragmentManager(), "datePickerBirthActivity");
    }

    // This is the callback method from DatePickerFragment.DateSelectedListener
    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        int displayMonth = month + 1;
        // Format the date string as you like (e.g., dd/MM/yyyy)
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, displayMonth, year);//"%02d/%02d/%d"
        if (activeDateField != null) {
            activeDateField.setText(selectedDate);
        } else {
            setToast("Error: Date field not identified.");
        }
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
        if (activeTimeField != null) {
            activeTimeField.setText(selectedTime);
        } else {
            setToast("Error: Date field not identified.");
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void showFlightInstructionBottomSheet(boolean isArrival) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomsheetFlightInstructionBinding bottomSheetBinding = BottomsheetFlightInstructionBinding.inflate(getLayoutInflater());

        bottomSheetBinding.tvTitle.setText(R.string.flight_instruction_title);
        String instructionText = getString(R.string.departure_instruction);
        if (isArrival) {
            instructionText = getString(R.string.arrival_instruction);
        }
        bottomSheetBinding.tvInstruction.setText(instructionText);

        String note = getString(R.string.flight_instruction_note);
        bottomSheetBinding.tvInstruction.setText(instructionText + "\n\n" + note);

        bottomSheetBinding.btnClose.setOnClickListener(v -> bottomSheetDialog.cancel());
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();
    }

    private void iniAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ModifyBookingActivity.this));
        adapter = new PassengerTripsAdapter(ModifyBookingActivity.this, passengerList, false, (header, pos) -> {
            header.setPos_passenger(pos);
            Helper.setItemParam(Constants.DETAIL_PASSENGER, header);
            intent = new Intent(getApplicationContext(), ModifyPassengerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        binding.agentRV.setLayoutManager(new LinearLayoutManager(ModifyBookingActivity.this));
        agentAdapter = new AgentAdapter(ModifyBookingActivity.this, mChoosenAgentList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(agentAdapter);
    }

    public void getAgent() {
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getListAgent(Helper.getDateNow(Constants.DATE_PATTERN_2), String.valueOf(offset), Constants.DEFAULT_LIMIT);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<Agent>>() {
                            }.getType();
                            List<Agent> tempList = new Gson().fromJson(jsonInString, listType);
                            if (offset == 0) {
                                mAgentList = new ArrayList<>();
                            }
                            mAgentList.addAll(tempList);
                            adapterDialog.setFilteredList(mAgentList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void getPackages() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getListPackage(createTrips.getTrip_type().toLowerCase());
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            masterPackage = new Gson().fromJson(jsonInString, Packages.class);
                            setPackages();
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

    private void setPackages() {
        if (Helper.isNotEmptyOrNull(masterPackage.getPackages())) {
            for (Dropdown dropdown : masterPackage.getPackages()) {
                if (dropdown.getId() == 1) {
                    binding.llAirportHeader.setVisibility(View.VISIBLE);
                    binding.llAirportDetail.setVisibility(View.GONE);
                }
                if (dropdown.getId() == 2) {
                    binding.llLoungeHeader.setVisibility(View.VISIBLE);
                    binding.llLoungeDetail.setVisibility(View.GONE);
                }
                if (dropdown.getId() == 3) {
                    binding.llFlightHeader.setVisibility(View.VISIBLE);
                    binding.llFlightDetail.setVisibility(View.GONE);
                }
                if (dropdown.getId() == 4) {
                    binding.llFastLaneHeader.setVisibility(View.VISIBLE);
                    binding.llFastLaneDetail.setVisibility(View.GONE);
                }
                if (dropdown.getId() == 5) {
                    binding.llBaggageHeader.setVisibility(View.VISIBLE);
                    binding.llBaggageDetail.setVisibility(View.GONE);
                }
            }

            setDetailAirport();
            setDetailLounge();
            setDetailFlight();
            setDetailFast();
            setDetailBaggage();
        } else {
            binding.llAirportHeader.setVisibility(View.GONE);
            binding.llAirportDetail.setVisibility(View.GONE);
            binding.llLoungeHeader.setVisibility(View.GONE);
            binding.llLoungeDetail.setVisibility(View.GONE);
            binding.llFlightHeader.setVisibility(View.GONE);
            binding.llFlightDetail.setVisibility(View.GONE);
            binding.llFastLaneHeader.setVisibility(View.GONE);
            binding.llFastLaneDetail.setVisibility(View.GONE);
            binding.llBaggageHeader.setVisibility(View.GONE);
            binding.llBaggageDetail.setVisibility(View.GONE);
        }
        setDataView();
        setViewOnClick();
    }

    private void setDetailAirport() {
        if (binding.edtPickUpTime != null) {
            binding.edtPickUpTime.setOnClickListener(v -> {
                activeTimeField = binding.edtPickUpTime; // Set active field
                openDialogTimePicker();//edtPickUpTime
            });
            binding.edtPickUpTime.setFocusable(false);
            binding.edtPickUpTime.setClickable(true);
        }

        if (binding.edtPickUpDate != null) {
            binding.edtPickUpDate.setOnClickListener(v -> {
                activeDateField = binding.edtPickUpDate;
                showDatePickerDialog();//edtPickUpDate
            });
            binding.edtPickUpDate.setFocusable(false);
            binding.edtPickUpDate.setClickable(true);
        }

        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select vehicle type...");

        if (Helper.isEmptyOrNull(masterPackage.getVehicleTypes())) {
            masterPackage.getVehicleTypes().add(new Dropdown());
        }
        masterPackage.getVehicleTypes().add(0, hintItem);
        vehicleTypeAdapter = new SpinnerDropDownAdapter(getApplicationContext(), masterPackage.getVehicleTypes());
        binding.spnVehicleType.setAdapter(vehicleTypeAdapter);

        binding.spnVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedVehicleType = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDetailLounge() {
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select lounge type...");

        if (Helper.isEmptyOrNull(masterPackage.getLoungeTypes())) {
            masterPackage.getLoungeTypes().add(new Dropdown());
        }
        masterPackage.getLoungeTypes().add(0, hintItem);
        loungeTypeAdapter = new SpinnerDropDownAdapter(getApplicationContext(), masterPackage.getLoungeTypes());
        binding.spnLoungeType.setAdapter(loungeTypeAdapter);

        binding.spnLoungeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedLoungeType = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDetailFlight() {
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select flight classes...");

        if (Helper.isEmptyOrNull(masterPackage.getFlightClasses())) {
            masterPackage.getFlightClasses().add(new Dropdown());
        }
        masterPackage.getFlightClasses().add(0, hintItem);
        flightClassesAdapter = new SpinnerDropDownAdapter(getApplicationContext(), masterPackage.getFlightClasses());
        binding.spnFLightClass.setAdapter(flightClassesAdapter);

        binding.spnFLightClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedFLightClass = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDetailFast() {
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select fast lane type...");

        if (Helper.isEmptyOrNull(masterPackage.getFastlaneTypes())) {
            masterPackage.getFastlaneTypes().add(new Dropdown());
        }
        masterPackage.getFastlaneTypes().add(0, hintItem);
        fastLaneAdapter = new SpinnerDropDownAdapter(getApplicationContext(), masterPackage.getFastlaneTypes());
        binding.spnFastType.setAdapter(fastLaneAdapter);

        binding.spnFastType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedFastType = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDetailBaggage() {
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select baggage type...");

        if (Helper.isEmptyOrNull(masterPackage.getBaggageTypes())) {
            masterPackage.getBaggageTypes().add(new Dropdown());
        }
        masterPackage.getBaggageTypes().add(0, hintItem);
        baggageHandlingAdapter = new SpinnerDropDownAdapter(getApplicationContext(), masterPackage.getBaggageTypes());
        binding.spnBaggage.setAdapter(baggageHandlingAdapter);

        binding.spnBaggage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedBaggage = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selectBaggage() {
        if (baggageSelected) {
            baggageSelected = false;
            binding.llBaggageDetail.setVisibility(View.GONE);
            binding.cbBaggage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            binding.imgBaggage.setImageResource(R.drawable.ic_baggage_unchecked);
            binding.llBaggageHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_border_gray));
            binding.arrowBaggage.setImageResource(R.drawable.ic_arrow_down_gray);

            binding.spnBaggage.setSelection(0);
            binding.edtOtherBaggage.setText(null);
        } else {
            baggageSelected = true;
            binding.llBaggageDetail.setVisibility(View.VISIBLE);
            binding.cbBaggage.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            binding.imgBaggage.setImageResource(R.drawable.ic_baggage_checked);
            binding.llBaggageHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_blue_card));
            binding.arrowBaggage.setImageResource(R.drawable.ic_arrow_up_gray);
        }
    }

    private void selectFastLane() {
        if (fastLaneSelected) {
            fastLaneSelected = false;
            binding.llFastLaneDetail.setVisibility(View.GONE);
            binding.cbFastLane.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            binding.imgFastLane.setImageResource(R.drawable.ic_fast_unchecked);
            binding.llFastLaneHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_border_gray));
            binding.arrowFastLane.setImageResource(R.drawable.ic_arrow_down_gray);

            binding.spnFastType.setSelection(0);
            binding.edtOtherFast.setText(null);
        } else {
            fastLaneSelected = true;
            binding.llFastLaneDetail.setVisibility(View.VISIBLE);
            binding.cbFastLane.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            binding.imgFastLane.setImageResource(R.drawable.ic_fast_checked);
            binding.llFastLaneHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_blue_card));
            binding.arrowFastLane.setImageResource(R.drawable.ic_arrow_up_gray);
        }
    }

    private void selectFlight() {
        if (flightSelected) {
            flightSelected = false;
            binding.llFlightDetail.setVisibility(View.GONE);
            binding.cbFlight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            binding.imgFlight.setImageResource(R.drawable.ic_flight_unchecked);
            binding.llFlightHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_border_gray));
            binding.arrowFlight.setImageResource(R.drawable.ic_arrow_down_gray);

            binding.spnFLightClass.setSelection(0);
            binding.edtOtherFlight.setText(null);
        } else {
            flightSelected = true;
            binding.llFlightDetail.setVisibility(View.VISIBLE);
            binding.cbFlight.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            binding.imgFlight.setImageResource(R.drawable.ic_flight_checked);
            binding.llFlightHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_blue_card));
            binding.arrowFlight.setImageResource(R.drawable.ic_arrow_up_gray);
        }
    }

    private void selectLounge() {
        if (loungeSelected) {
            loungeSelected = false;
            binding.llLoungeDetail.setVisibility(View.GONE);
            binding.cbLounge.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            binding.imgLounge.setImageResource(R.drawable.ic_lounge_unchecked);
            binding.llLoungeHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_border_gray));
            binding.arrowLounge.setImageResource(R.drawable.ic_arrow_down_gray);

            binding.spnLoungeType.setSelection(0);
            binding.edtOtherLounge.setText(null);
        } else {
            loungeSelected = true;
            binding.llLoungeDetail.setVisibility(View.VISIBLE);
            binding.cbLounge.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            binding.imgLounge.setImageResource(R.drawable.ic_lounge_checked);
            binding.llLoungeHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_blue_card));
            binding.arrowLounge.setImageResource(R.drawable.ic_arrow_up_gray);
        }
    }

    private void selectAirport() {
        if (airportSelected) {
            airportSelected = false;
            binding.llAirportDetail.setVisibility(View.GONE);
            binding.cbAirport.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_unchecked));
            binding.imgAirport.setImageResource(R.drawable.ic_airport_unchecked);
            binding.llAirportHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_rounded_border_gray));
            binding.arrowAirport.setImageResource(R.drawable.ic_arrow_down_gray);

            binding.edtPickUpTime.setText(null);
            binding.edtPickUpDate.setText(null);
            binding.edtContact.setText(null);
            binding.edtOtherAirport.setText(null);
            binding.spnVehicleType.setSelection(0);
        } else {
            airportSelected = true;
            binding.llAirportDetail.setVisibility(View.VISIBLE);
            binding.cbAirport.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked));
            binding.imgAirport.setImageResource(R.drawable.ic_airport_checked);
            binding.llAirportHeader.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_blue_card));
            binding.arrowAirport.setImageResource(R.drawable.ic_arrow_up_gray);
        }
    }

    private void openDialogTimePicker() {
        TimePickerFragment timePicker = TimePickerFragment.newInstance(this);
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    private void openDialogChooseAgent() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        offset = 0;
        mAgentList = new ArrayList<>();
        getAgent();//openDialogChooseAgent
        DialogChooseAgentBinding dialogBinding = DialogChooseAgentBinding.inflate(LayoutInflater.from(ModifyBookingActivity.this));
        dialog = new Dialog(ModifyBookingActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        linearLayout = new LinearLayoutManager(ModifyBookingActivity.this);
        dialogBinding.recyclerView.setLayoutManager(linearLayout);
        dialogBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCount();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCount();
                    } else {
                        loading = true;
                    }
                }
            }
        });

        adapterDialog = new AgentAdapter(ModifyBookingActivity.this, mAgentList, (header, pos) -> {
            mChoosenAgentList = new ArrayList<>();
            mChoosenAgentList.add(header);
            agentAdapter.setFilteredList(mChoosenAgentList);
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setAdapter(adapterDialog);
    }

    public void itemCount() {
        visibleItemCount = linearLayout.getChildCount();
        totalItemCount = linearLayout.getItemCount();
        pastVisiblesItems = linearLayout.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                getAgent();//itemCount
            }
        }
    }
}