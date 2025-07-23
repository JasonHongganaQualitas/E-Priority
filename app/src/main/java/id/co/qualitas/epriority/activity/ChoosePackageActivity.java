package id.co.qualitas.epriority.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.SpinnerDropDownAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.DialogChooseAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentChoosePackageBinding;
import id.co.qualitas.epriority.fragment.DatePickerFragment;
import id.co.qualitas.epriority.fragment.TimePickerFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Agent;
import id.co.qualitas.epriority.model.Dropdown;
import id.co.qualitas.epriority.model.Packages;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePackageActivity extends BaseActivity implements TimePickerFragment.TimeSelectedListener, DatePickerFragment.DateSelectedListener {
    AgentAdapter adapter;
    List<Agent> mList = new ArrayList<>(), mChoosenAgentList = new ArrayList<>();
    private FragmentChoosePackageBinding binding;
    boolean airportSelected = false, loungeSelected = false, flightSelected = false, fastLaneSelected = false, baggageSelected = false;
    private Packages masterPackage;
    private SpinnerDropDownAdapter vehicleTypeAdapter, baggageHandlingAdapter, fastLaneAdapter, flightClassesAdapter, loungeTypeAdapter;
    private Dropdown selectedVehicleType, selectedLoungeType, selectedBaggage, selectedFastType, selectedFLightClass;
    private TripsResponse createTrips;
    private AgentAdapter adapterDialog;
    private LinearLayoutManager linearLayout;
    private int offset;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = FragmentChoosePackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        if (Helper.getItemParam(Constants.DATA_CREATE_TRIPS) != null) {
            createTrips = (TripsResponse) Helper.getItemParam(Constants.DATA_CREATE_TRIPS);
            initAdapter();
            getPackages();
        } else {
            setToast("Data Flight not found");
            onBackPressed();
        }

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnChooseAgent.setOnClickListener(v -> {
            openDialogChooseAgent();
        });

        binding.llAirportHeader.setOnClickListener(v -> {
            selectAirport();
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

        binding.btnReview.setOnClickListener(v -> {
            if (validationTrips()) {
                prepareDataTrips();
                intent = new Intent(getApplicationContext(), ReviewBookingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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
        getAgent();//openDialogChooseAgent
        DialogChooseAgentBinding dialogBinding = DialogChooseAgentBinding.inflate(LayoutInflater.from(ChoosePackageActivity.this));
        dialog = new Dialog(ChoosePackageActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        linearLayout = new LinearLayoutManager(ChoosePackageActivity.this);
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

        adapterDialog = new AgentAdapter(ChoosePackageActivity.this, mList, (header, pos) -> {
            mChoosenAgentList = new ArrayList<>();
            mChoosenAgentList.add(header);
            adapter.setFilteredList(mChoosenAgentList);
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

    private void initAdapter() {
        binding.agentRV.setLayoutManager(new LinearLayoutManager(ChoosePackageActivity.this));
        adapter = new AgentAdapter(ChoosePackageActivity.this, mChoosenAgentList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(adapter);
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
                                mList = new ArrayList<>();
                            }
                            mList.addAll(tempList);
                            adapterDialog.setFilteredList(mList);
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
                setToast(t.getMessage());
            }
        });
    }

    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
        binding.edtPickUpTime.setText(selectedTime);
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
    }

    private void setDetailAirport() {
        binding.edtPickUpTime.setOnClickListener(v -> {
            openDialogTimePicker();
        });

        binding.edtPickUpDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });
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

    private boolean validationTrips() {
        int empty = 0;
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

    private void prepareDataTrips() {
        Packages header = new Packages(), detail = new Packages();
        if (airportSelected) {
            detail = new Packages();
            detail.setVehicle_type(selectedVehicleType.getId());
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
            detail.setRequest_note(binding.edtOtherLounge.getText().toString());
            detail.setSelectedLoungeType(selectedLoungeType);
            header.setTrip_loungeaccess(detail);
        } else {
            header.setTrip_loungeaccess(null);
        }

        if (flightSelected) {
            detail = new Packages();
            detail.setFlight_class(selectedFLightClass.getId());
            detail.setRequest_note(binding.edtOtherFlight.getText().toString());
            detail.setSelectedFlightClasses(selectedFLightClass);
            header.setFlight_detail(detail);
        } else {
            header.setFlight_detail(null);
        }

        if (fastLaneSelected) {
            detail = new Packages();
            detail.setType_lane(selectedFastType.getId());
            detail.setRequest_note(binding.edtOtherFast.getText().toString());
            detail.setSelectedFastlaneType(selectedFastType);
            header.setTrip_fastlane(detail);
        } else {
            header.setTrip_fastlane(null);
        }

        if (baggageSelected) {
            detail = new Packages();
            detail.setType_baggage(selectedBaggage.getId());
            detail.setRequest_note(binding.edtOtherBaggage.getText().toString());
            detail.setSelectedBaggageType(selectedBaggage);
            header.setTrip_baggageservice(detail);
        } else {
            header.setTrip_baggageservice(null);
        }

        createTrips.setAgent_list(mChoosenAgentList);
        createTrips.setPackages(header);
        if (Helper.isNotEmptyOrNull(mChoosenAgentList)) {
            createTrips.setAgent_id(mChoosenAgentList.get(0).getId());
        }
        Helper.setItemParam(Constants.DATA_CREATE_TRIPS, createTrips);
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePicker = DatePickerFragment.newInstance(this);
        datePicker.show(getSupportFragmentManager(), "datePickerBirthActivity");
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        int displayMonth = month + 1;

        // Format the date string as you like (e.g., dd/MM/yyyy)
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, displayMonth, year);
        binding.edtPickUpDate.setText(selectedDate);
    }
}