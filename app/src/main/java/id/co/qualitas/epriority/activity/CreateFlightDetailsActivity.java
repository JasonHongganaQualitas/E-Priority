package id.co.qualitas.epriority.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.AirportAdapter;
import id.co.qualitas.epriority.adapter.NationalityAdapter;
import id.co.qualitas.epriority.adapter.PassangerAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.ActivityCreateFlightDetailsBinding;
import id.co.qualitas.epriority.databinding.BottomsheetDetailPassengerBinding;
import id.co.qualitas.epriority.databinding.BottomsheetFlightInstructionBinding;
import id.co.qualitas.epriority.databinding.DialogChooseAgentBinding;
import id.co.qualitas.epriority.databinding.DialogChooseAirportBinding;
import id.co.qualitas.epriority.databinding.FragmentCreatePassengerBinding;
import id.co.qualitas.epriority.fragment.DatePickerFragment;
import id.co.qualitas.epriority.fragment.TimePickerFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Dropdown;
import id.co.qualitas.epriority.model.Passenger;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFlightDetailsActivity extends BaseActivity implements TimePickerFragment.TimeSelectedListener, DatePickerFragment.DateSelectedListener {
    private ActivityCreateFlightDetailsBinding binding;
    private EditText activeDateField, activeTimeField;
    private Passenger dataPassenger;
    private TripsResponse createTrips;
    private PassengerTripsAdapter adapter;
    List<Passenger> passengerList = new ArrayList<>();
    private String selectedTripType;
    private List<Dropdown> airportList;
    private LinearLayoutManager linearLayout;
    private AirportAdapter airportAdapter;
    private Dropdown selectedAirport;
    private DialogChooseAirportBinding bottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityCreateFlightDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();
        setupTripTypeSpinner();

        binding.btnChoose.setOnClickListener(v -> {
            if (checkNoEmptyData()) {
                saveData();//btnChoose
                intent = new Intent(getApplicationContext(), ChoosePackageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        binding.llAddPassenger.setOnClickListener(v -> {
            if (Helper.isEmpty(binding.edtNumberPassenger)) {
                binding.edtNumberPassenger.setError("Please enter passenger count");
            } else if (Integer.parseInt(binding.edtNumberPassenger.getText().toString()) < 0) {
                setToast("Number of passenger can't 0");
            } else if (passengerList.size() >= Integer.parseInt(binding.edtNumberPassenger.getText().toString())) {
                setToast("Passenger can't be more than passenger count");
            } else if (Helper.isEmpty(binding.edtRouteFrom)) {
                binding.edtRouteFrom.setError("Please enter route city");
            } else {
                Helper.removeItemParam(Constants.DATA_PASSENGER);
                saveData();//llAddPassenger
                intent = new Intent(getApplicationContext(), CreatePassengerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        if (binding.edtRouteFrom != null) {
            binding.edtRouteFrom.setOnClickListener(v -> {
                dialogBottomAirport();
            });
            binding.edtRouteFrom.setFocusable(false);
            binding.edtRouteFrom.setClickable(true);
        }

        if (binding.edtDateFrom != null) {
            binding.edtDateFrom.setOnClickListener(v -> {
                activeDateField = binding.edtDateFrom; // Set active field
                showDatePickerDialog();
            });
            binding.edtDateFrom.setFocusable(false);
            binding.edtDateFrom.setClickable(true);
        }

        if (binding.edtDateTo != null) {
            binding.edtDateTo.setOnClickListener(v -> {
                activeDateField = binding.edtDateTo; // Set active field
                showDatePickerDialog();
            });
            binding.edtDateTo.setFocusable(false);
            binding.edtDateTo.setClickable(true);
        }

        if (binding.edtTimeFrom != null) {
            binding.edtTimeFrom.setOnClickListener(v -> {
                activeTimeField = binding.edtTimeFrom; // Set active field
                openDialogTimePicker();
            });
            binding.edtTimeFrom.setFocusable(false);
            binding.edtTimeFrom.setClickable(true);
        }

        if (binding.edtTimeTo != null) {
            binding.edtTimeTo.setOnClickListener(v -> {
                activeTimeField = binding.edtTimeTo; // Set active field
                openDialogTimePicker();
            });
            binding.edtTimeTo.setFocusable(false);
            binding.edtTimeTo.setClickable(true);
        }

        binding.imgQuestionFN.setOnClickListener(v -> showFlightInstructionBottomSheet(selectedTripType.equals("Arrival")));
        binding.imgQuestionFRC.setOnClickListener(v -> showFRCInstructionBottomSheet());
    }

    @SuppressLint("ObsoleteSdkInt")
    private void dialogBottomAirport() {
        airportList = new ArrayList<>();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetBinding = DialogChooseAirportBinding.inflate(getLayoutInflater());
        linearLayout = new LinearLayoutManager(CreateFlightDetailsActivity.this);
        bottomSheetBinding.recyclerView.setLayoutManager(linearLayout);
        if (Helper.isEmptyOrNull(airportList)) {
            bottomSheetBinding.llEmpty.setVisibility(View.VISIBLE);
            bottomSheetBinding.recyclerView.setVisibility(View.GONE);
        } else {
            bottomSheetBinding.llEmpty.setVisibility(View.GONE);
            bottomSheetBinding.recyclerView.setVisibility(View.VISIBLE);
        }

        airportAdapter = new AirportAdapter(CreateFlightDetailsActivity.this, airportList, (header, pos) -> {
            selectedAirport = header;
            binding.edtRouteFrom.setText(selectedAirport.getCity() + " (" + selectedAirport.getIata() + ")");
            bottomSheetDialog.dismiss();
        });
        bottomSheetBinding.recyclerView.setAdapter(airportAdapter);

        bottomSheetBinding.btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmpty(bottomSheetBinding.editText)) {
                String search = bottomSheetBinding.editText.getText().toString().trim();
                if (search.length() > 1) {
                    getListAirport(bottomSheetBinding.editText.getText().toString().trim());
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();
    }

    public void getListAirport(String search) {
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT_DROPDOWN));
        tripRequest.setOffset(Integer.parseInt(Constants.DEFAULT_OFFSET));
        tripRequest.setSearch(search);
        Call<WSMessage> httpRequest = apiInterface.getListAirport(tripRequest);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<Dropdown>>() {
                            }.getType();
                            List<Dropdown> tempList = new Gson().fromJson(jsonInString, listType);
                            airportList = new ArrayList<>();
                            airportList.addAll(tempList);
                            airportAdapter.setFilteredList(airportList);
                        }
                    }
                }
                if (Helper.isEmptyOrNull(airportList)) {
                    bottomSheetBinding.llEmpty.setVisibility(View.VISIBLE);
                    bottomSheetBinding.recyclerView.setVisibility(View.GONE);
                } else {
                    bottomSheetBinding.llEmpty.setVisibility(View.GONE);
                    bottomSheetBinding.recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(t.getMessage());
                if (Helper.isEmptyOrNull(airportList)) {
                    bottomSheetBinding.llEmpty.setVisibility(View.VISIBLE);
                    bottomSheetBinding.recyclerView.setVisibility(View.GONE);
                } else {
                    bottomSheetBinding.llEmpty.setVisibility(View.GONE);
                    bottomSheetBinding.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void showFRCInstructionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        BottomsheetFlightInstructionBinding bottomSheetBinding = BottomsheetFlightInstructionBinding.inflate(getLayoutInflater());
        bottomSheetBinding.tvTitle.setText("Flight Reservation Code Instruction");
        bottomSheetBinding.tvInstruction.setText("As shown on your airline ticket");

        bottomSheetBinding.btnClose.setOnClickListener(v -> bottomSheetDialog.cancel());
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        bottomSheetDialog.show();
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

    private boolean checkNoEmptyData() {
        int empty = 0;
//        if (Helper.isEmpty(binding.edtFlightReservationCode)) {
//            binding.edtFlightReservationCode.setError("Please enter flight reservation code");
//            empty++;
//        }

        if (Helper.isEmpty(binding.edtDateFrom)) {
            binding.edtDateFrom.setError("Please enter date");
            empty++;
        }
//        if (Helper.isEmpty(binding.edtDateTo)) {
//            binding.edtDateTo.setError("Please enter date");
//            empty++;
//        }
        if (Helper.isEmpty(binding.edtTimeFrom)) {
            binding.edtTimeFrom.setError("Please enter time");
            empty++;
        }
//        if (Helper.isEmpty(binding.edtTimeTo)) {
//            binding.edtTimeTo.setError("Please enter time");
//            empty++;
//        }
        if (Helper.isEmpty(binding.edtAirline)) {
            binding.edtAirline.setError("Please enter flight number");
            empty++;
        }
        if (Helper.isEmpty(binding.edtRouteFrom)) {
            binding.edtRouteFrom.setError("Please enter route city");
            empty++;
        }
//        if (Helper.isEmpty(binding.edtRouteTo)) {
//            binding.edtRouteTo.setError("Please enter route to");
//            empty++;
//        }
        if (Helper.isEmpty(binding.edtNumberPassenger)) {
            binding.edtNumberPassenger.setError("Please enter passenger count");
            empty++;
        }
        if (Helper.isEmptyOrNull(passengerList)) {
            setToast("Please add passenger");
            empty++;
        }

        return empty == 0;
    }

    private void saveData() {
        if (createTrips == null) {
            createTrips = new TripsResponse();
        }
        if (selectedTripType != null) {
            createTrips.setTrip_type(selectedTripType);
        } else {
            String currentSpinnerValue = (String) binding.spnType.getSelectedItem();
            createTrips.setTrip_type(currentSpinnerValue);
        }
        String dateFrom = !Helper.isEmpty(binding.edtDateFrom) ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateFrom.getText().toString()) : null;
//        String dateTo = !Helper.isEmpty(binding.edtDateTo) ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateTo.getText().toString()) : null;
        String timeFrom = !Helper.isEmpty(binding.edtTimeFrom) ? Helper.changeFormatDate(Constants.DATE_PATTERN_9, Constants.DATE_PATTERN_13, binding.edtTimeFrom.getText().toString()) : null;
//        String timeTo = !Helper.isEmpty(binding.edtTimeTo) ? Helper.changeFormatDate(Constants.DATE_PATTERN_9, Constants.DATE_PATTERN_13, binding.edtTimeTo.getText().toString()) : null;

        if (createTrips.getTrip_type().toLowerCase().equals(Constants.ARRIVAL)) {
            createTrips.setDate_to(dateFrom + " " + timeFrom);
            createTrips.setDate_from(null);
            createTrips.setRoute_to(binding.edtRouteFrom.getText().toString());
            createTrips.setRoute_from(null);
        } else {
            createTrips.setDate_from(dateFrom + " " + timeFrom);
            createTrips.setDate_to(null);
            createTrips.setRoute_from(binding.edtRouteFrom.getText().toString());
            createTrips.setRoute_to(null);
        }
        createTrips.setTrip_date(dateFrom + " " + timeFrom);
        createTrips.setSelectedAirport(selectedAirport);
        createTrips.setCustomer_id(user.getId());
//        createTrips.setBooking_id(binding.edtFlightReservationCode.getText().toString());
        createTrips.setFlight_no(binding.edtAirline.getText().toString());
        createTrips.setAirline(binding.edtAirline.getText().toString());
//        createTrips.setDate_from(dateFrom + " " + timeFrom);
//        createTrips.setDate_to(dateTo + " " + timeTo);
        createTrips.setAircraft("");
//        createTrips.setRoute_from(binding.edtRouteFrom.getText().toString());
//        createTrips.setRoute_to(binding.edtRouteTo.getText().toString());
        createTrips.setPassenger_count(Integer.parseInt(binding.edtNumberPassenger.getText().toString()));
        createTrips.setPassengers(passengerList);
        Helper.setItemParam(Constants.DATA_CREATE_TRIPS, createTrips);
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePicker = DatePickerFragment.newInstance(this); // 'this' is the Activity
        // Use getSupportFragmentManager() when calling from an Activity
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
    protected void onResume() {
        super.onResume();
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
        initAdapter();
    }

    private void initAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(CreateFlightDetailsActivity.this));
        adapter = new PassengerTripsAdapter(CreateFlightDetailsActivity.this, passengerList, false, (header, pos) -> {
            bottomDialogDetailPassenger(header);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupTripTypeSpinner() {
        List<String> tripTypes = new ArrayList<>();
        tripTypes.add("Departure");
        tripTypes.add("Arrival");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tripTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnType.setAdapter(adapter);
        binding.spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTripType = (String) parent.getItemAtPosition(position);
//                if (selectedTripType.equals("Arrival")) {
//                    binding.txtTitle.setText("To");
//                } else {
//                    binding.txtTitle.setText("From");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        selectedTripType = "Departure";
    }

    private void openDialogTimePicker() {
        TimePickerFragment timePicker = TimePickerFragment.newInstance(this);
        timePicker.show(getSupportFragmentManager(), "timePicker");
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

}