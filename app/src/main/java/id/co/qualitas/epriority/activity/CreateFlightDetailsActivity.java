package id.co.qualitas.epriority.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.PassangerAdapter;
import id.co.qualitas.epriority.adapter.PassengerTripsAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.ActivityCreateFlightDetailsBinding;
import id.co.qualitas.epriority.databinding.FragmentCreatePassengerBinding;
import id.co.qualitas.epriority.fragment.DatePickerFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.model.Passenger;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.TripsResponse;

public class CreateFlightDetailsActivity extends BaseActivity implements DatePickerFragment.DateSelectedListener {
    private ActivityCreateFlightDetailsBinding binding;
    private EditText activeDateField;
    private Passenger dataPassenger;
    private TripsResponse createTrips;
    private PassengerTripsAdapter adapter;
    List<Passenger> passengerList = new ArrayList<>();
    private String selectedTripType;

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
            } else if (passengerList.size() > Integer.parseInt(binding.edtNumberPassenger.getText().toString())) {
                setToast("Passenger can't be more than passenger count");
            } else {
                saveData();//llAddPassenger
                intent = new Intent(getApplicationContext(), CreatePassengerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

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
    }

    private boolean checkNoEmptyData() {
        int empty = 0;
        if (Helper.isEmpty(binding.edtBookingID)) {
            binding.edtBookingID.setError("Please enter booking ID");
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

        if (empty == 0) {
            return true;
        }
        return false;
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
        createTrips.setCustomer_id(user.getId());
        createTrips.setBooking_id(binding.edtBookingID.getText().toString());
        createTrips.setFlight_no(binding.edtAirline.getText().toString());
        createTrips.setAirline(binding.edtAirline.getText().toString());
        createTrips.setDate_from(!Helper.isEmpty(binding.edtDateFrom)
                ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateFrom.getText().toString()) : null);
        createTrips.setDate_to(!Helper.isEmpty(binding.edtDateTo)
                ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateTo.getText().toString()) : null);
        createTrips.setAircraft("Boeing 787-9");
        createTrips.setRoute_from(binding.edtRouteFrom.getText().toString());
        createTrips.setRoute_to(binding.edtRouteTo.getText().toString());
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
            }
            if (Helper.isNotEmptyOrNull(createTrips.getPassengers())) {
                passengerList = new ArrayList<>();
                passengerList = createTrips.getPassengers();
                passengerList.add(dataPassenger);
                createTrips.setPassengers(passengerList);
            } else {
                passengerList = new ArrayList<>();
                passengerList.add(dataPassenger);
            }
            Helper.removeItemParam(Constants.DATA_PASSENGER);
        } else {
            passengerList = new ArrayList<>();
        }
        initAdapter();
    }

    private void initAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(CreateFlightDetailsActivity.this));
        adapter = new PassengerTripsAdapter(CreateFlightDetailsActivity.this, passengerList, (header, pos) -> {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}