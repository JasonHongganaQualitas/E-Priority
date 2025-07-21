package id.co.qualitas.epriority.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;
import id.co.qualitas.epriority.adapter.SpinnerDropDownAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.DialogChooseAgentBinding;
import id.co.qualitas.epriority.databinding.FragmentChoosePackageBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Agent;
import id.co.qualitas.epriority.model.Dropdown;
import id.co.qualitas.epriority.model.Packages;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.User;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePackageFragment extends BaseFragment implements TimePickerFragment.TimeSelectedListener {
    View view;
    AgentAdapter adapter;
    List<Agent> mList = new ArrayList<>(), mChoosenAgentList = new ArrayList<>();
    private FragmentChoosePackageBinding binding;
    boolean airportSelected = false, loungeSelected = false, flightSelected = false, fastLaneSelected = false, baggageSelected = false;
    private int offset;
    private Packages masterPackage;
    private SpinnerDropDownAdapter vehicleTypeAdapter, baggageHandlingAdapter, fastLaneAdapter, flightClassesAdapter, loungeTypeAdapter;
    private Dropdown selectedVehicleType, selectedLoungeType, selectedBaggage, selectedFastType, selectedFLightClass;
    private TripsResponse createTrips;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChoosePackageBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initAdapter();
        getPackages();

        binding.backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        binding.btnChooseAgent.setOnClickListener(v -> {
            getAgent();
        });

        binding.llAirportHeader.setOnClickListener(v -> {
            if (airportSelected) {
                airportSelected = false;
                binding.llAirportDetail.setVisibility(View.GONE);
                binding.cbAirport.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unchecked));
                binding.imgAirport.setImageResource(R.drawable.ic_airport_unchecked);
                binding.llAirportHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_border_gray));
                binding.arrowAirport.setImageResource(R.drawable.ic_arrow_down_gray);

                binding.edtPickUpTime.setText(null);
                binding.edtContact.setText(null);
                binding.edtOtherAirport.setText(null);
                binding.spnVehicleType.setSelection(0);
            } else {
                airportSelected = true;
                binding.llAirportDetail.setVisibility(View.VISIBLE);
                binding.cbAirport.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checked));
                binding.imgAirport.setImageResource(R.drawable.ic_airport_checked);
                binding.llAirportHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blue_card));
                binding.arrowAirport.setImageResource(R.drawable.ic_arrow_up_gray);
            }
        });

        binding.llLoungeHeader.setOnClickListener(v -> {
            if (loungeSelected) {
                loungeSelected = false;
                binding.llLoungeDetail.setVisibility(View.GONE);
                binding.cbLounge.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unchecked));
                binding.imgLounge.setImageResource(R.drawable.ic_lounge_unchecked);
                binding.llLoungeHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_border_gray));
                binding.arrowLounge.setImageResource(R.drawable.ic_arrow_down_gray);

                binding.spnLoungeType.setSelection(0);
                binding.edtOtherLounge.setText(null);
            } else {
                loungeSelected = true;
                binding.llLoungeDetail.setVisibility(View.VISIBLE);
                binding.cbLounge.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checked));
                binding.imgLounge.setImageResource(R.drawable.ic_lounge_checked);
                binding.llLoungeHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blue_card));
                binding.arrowLounge.setImageResource(R.drawable.ic_arrow_up_gray);
            }
        });

        binding.llFlightHeader.setOnClickListener(v -> {
            if (flightSelected) {
                flightSelected = false;
                binding.llFlightDetail.setVisibility(View.GONE);
                binding.cbFlight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unchecked));
                binding.imgFlight.setImageResource(R.drawable.ic_flight_unchecked);
                binding.llFlightHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_border_gray));
                binding.arrowFlight.setImageResource(R.drawable.ic_arrow_down_gray);

                binding.spnFLightClass.setSelection(0);
                binding.edtOtherFlight.setText(null);
            } else {
                flightSelected = true;
                binding.llFlightDetail.setVisibility(View.VISIBLE);
                binding.cbFlight.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checked));
                binding.imgFlight.setImageResource(R.drawable.ic_flight_checked);
                binding.llFlightHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blue_card));
                binding.arrowFlight.setImageResource(R.drawable.ic_arrow_up_gray);
            }
        });

        binding.llFastLaneHeader.setOnClickListener(v -> {
            if (fastLaneSelected) {
                fastLaneSelected = false;
                binding.llFastLaneDetail.setVisibility(View.GONE);
                binding.cbFastLane.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unchecked));
                binding.imgFastLane.setImageResource(R.drawable.ic_fast_unchecked);
                binding.llFastLaneHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_border_gray));
                binding.arrowFastLane.setImageResource(R.drawable.ic_arrow_down_gray);

                binding.spnFastType.setSelection(0);
                binding.edtOtherFast.setText(null);
            } else {
                fastLaneSelected = true;
                binding.llFastLaneDetail.setVisibility(View.VISIBLE);
                binding.cbFastLane.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checked));
                binding.imgFastLane.setImageResource(R.drawable.ic_fast_checked);
                binding.llFastLaneHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blue_card));
                binding.arrowFastLane.setImageResource(R.drawable.ic_arrow_up_gray);
            }
        });

        binding.llBaggageHeader.setOnClickListener(v -> {
            if (baggageSelected) {
                baggageSelected = false;
                binding.llBaggageDetail.setVisibility(View.GONE);
                binding.cbBaggage.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_unchecked));
                binding.imgBaggage.setImageResource(R.drawable.ic_baggage_unchecked);
                binding.llBaggageHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_rounded_border_gray));
                binding.arrowBaggage.setImageResource(R.drawable.ic_arrow_down_gray);

                binding.spnBaggage.setSelection(0);
                binding.edtOtherBaggage.setText(null);
            } else {
                baggageSelected = true;
                binding.llBaggageDetail.setVisibility(View.VISIBLE);
                binding.cbBaggage.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checked));
                binding.imgBaggage.setImageResource(R.drawable.ic_baggage_checked);
                binding.llBaggageHeader.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_blue_card));
                binding.arrowBaggage.setImageResource(R.drawable.ic_arrow_up_gray);
            }
        });

        binding.btnReview.setOnClickListener(v -> {
            createTrips();
//            ReviewBookingFragment fragment = new ReviewBookingFragment();
//            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }

    private void openDialogTimePicker() {
        TimePickerFragment timePicker = TimePickerFragment.newInstance(this);
        timePicker.show(getChildFragmentManager(), "timePicker");
    }

    private void openDialogChooseAgent() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        offset = 0;
        DialogChooseAgentBinding dialogBinding = DialogChooseAgentBinding.inflate(LayoutInflater.from(getContext()));
        dialog = new Dialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AgentAdapter adapterDialog = new AgentAdapter(this, mList, (header, pos) -> {
            mChoosenAgentList = new ArrayList<>();
            mChoosenAgentList.add(header);
            adapter.setFilteredList(mChoosenAgentList);
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setAdapter(adapterDialog);
    }

    private void initAdapter() {
        binding.agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter(this, mChoosenAgentList, (header, pos) -> {
        });
        binding.agentRV.setAdapter(adapter);
    }

    public void getAgent() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getListAgent(Helper.getDateNow(Constants.DATE_PATTERN_2), Constants.DEFAULT_OFFSET, Constants.DEFAULT_LIMIT);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<Agent>>() {
                            }.getType();
                            List<Agent> tempList = new Gson().fromJson(jsonInString, listType);
                            mList = new ArrayList<>();
                            mList.addAll(tempList);
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
                    }
                } else {
                    setToast(response.message());
                }
                openDialogChooseAgent();
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                setToast(t.getMessage());
                openDialogChooseAgent();
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
        Call<WSMessage> httpRequest = apiInterface.getListPackage("departure");
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
                openDialogChooseAgent();
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
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select vehicle type...");

        if (Helper.isEmptyOrNull(masterPackage.getVehicleTypes())) {
            masterPackage.getVehicleTypes().add(new Dropdown());
        }
        masterPackage.getVehicleTypes().add(0, hintItem);
        vehicleTypeAdapter = new SpinnerDropDownAdapter(getContext(), masterPackage.getVehicleTypes());
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
        loungeTypeAdapter = new SpinnerDropDownAdapter(getContext(), masterPackage.getLoungeTypes());
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
        flightClassesAdapter = new SpinnerDropDownAdapter(getContext(), masterPackage.getFlightClasses());
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
        fastLaneAdapter = new SpinnerDropDownAdapter(getContext(), masterPackage.getFastlaneTypes());
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
        baggageHandlingAdapter = new SpinnerDropDownAdapter(getContext(), masterPackage.getBaggageTypes());
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

    public void createTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        prepareDataTrips();
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
                            requireActivity().getSupportFragmentManager().popBackStack();
                            requireActivity().getSupportFragmentManager().popBackStack();
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

    private void prepareDataTrips() {
        Packages header = new Packages(), detail = new Packages();
        createTrips = new TripsResponse();

        if (airportSelected) {
            detail = new Packages();
            detail.setVehicle_type(selectedVehicleType.getId());
            detail.setPickup_time(binding.edtPickUpTime.getText().toString());
            detail.setContact_no(binding.edtContact.getText().toString());
            detail.setRequest_note(binding.edtOtherAirport.getText().toString());
            header.setAirport_transfer(detail);
        }

        if (loungeSelected) {
            detail = new Packages();
            detail.setLounge_type(selectedLoungeType.getId());
            detail.setRequest_note(binding.edtOtherLounge.getText().toString());
            header.setLounge_access(detail);
        }

        if (flightSelected) {
            detail = new Packages();
            detail.setFlight_class(selectedFLightClass.getId());
            detail.setRequest_note(binding.edtOtherFlight.getText().toString());
            header.setFlight_detail(detail);
        }

        if (fastLaneSelected) {
            detail = new Packages();
            detail.setType_lane(selectedFastType.getId());
            detail.setRequest_note(binding.edtOtherFast.getText().toString());
            header.setFast_lane(detail);
        }

        if (baggageSelected) {
            detail = new Packages();
            detail.setType_baggage(selectedBaggage.getId());
            detail.setRequest_note(binding.edtOtherLounge.getText().toString());
            header.setBaggage_service(detail);
        }

        createTrips.setPackages(header);
        createTrips.setTrip_date("2025-07-19");
        createTrips.setTrip_type("DEPARTURE");
        createTrips.setCustomer_id(15);
        createTrips.setBooking_id("BOOK20250810");
        createTrips.setFlight_no("NH856");
        createTrips.setAirline("All Nippon Airways");
        createTrips.setAircraft("Boeing 787-9");
        createTrips.setRoute_from("JAKARTA");
        createTrips.setRoute_to("KYOTO");
        createTrips.setPassenger_count(1);
        if (Helper.isNotEmptyOrNull(mChoosenAgentList)) {
            createTrips.setAgent_id(mChoosenAgentList.get(0).getId());
        }
        createTrips.setPassengers(new ArrayList<>());

//        "passengers": [
//            {
//                "item_no": 1, //ini apa?
//                    "first_name": "Kaoruko",
//                    "last_name": "Waguri",
//                    "email": "kaorukowaguri@qualitas.co.id",
//                    "phone_no": "+81-90-1234-5678",
//                    "birth_date": "1994-07-10",
//                    "nationality": 2,
//                    "flight_class": 1,
//                    "passport_no": "JP556677",
//                    "passport_country": 2,
//                    "passport_expdate": "2030-12-31"
//            }
//        ],
    }
}