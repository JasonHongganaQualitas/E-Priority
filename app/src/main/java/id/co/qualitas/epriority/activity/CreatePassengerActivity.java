package id.co.qualitas.epriority.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.NationalityAdapter;
import id.co.qualitas.epriority.adapter.SpinnerDropDownAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.DialogChooseAirportBinding;
import id.co.qualitas.epriority.databinding.DialogChooseNationalityBinding;
import id.co.qualitas.epriority.databinding.FragmentCreatePassengerBinding;
import id.co.qualitas.epriority.fragment.DateBirthPickerFragment;
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

public class CreatePassengerActivity extends BaseActivity implements DatePickerFragment.DateSelectedListener, DateBirthPickerFragment.DateSelectedListener {
    private FragmentCreatePassengerBinding binding;
    private NationalityAdapter nationalityAdapter;
    private SpinnerDropDownAdapter flightClassAdapter;
    private EditText activeDateField;
    private Dropdown selectedNationality, selectedFlightClass, selectedNationalityPassport;
    private List<Dropdown> nationalityList, flightClassList = new ArrayList<>();
    boolean inFlightMealRequired;
    int offset;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayout;
    private @NonNull DialogChooseNationalityBinding dialogBinding;
    private TripsResponse createTrips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = FragmentCreatePassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initBase();

        binding.btnCreate.setOnClickListener(v -> {
            if (checkNoEmptyData()) {
                saveData();
                onBackPressed();
            }
        });

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        if (binding.edtDateBirth != null) {
            binding.edtDateBirth.setOnClickListener(v -> {
                activeDateField = binding.edtDateBirth; // Set active field
                showDateBirthPickerDialog();
            });
            binding.edtDateBirth.setFocusable(false);
            binding.edtDateBirth.setClickable(true);
        }

        if (binding.edtPassportIssueDate != null) {
            binding.edtPassportIssueDate.setOnClickListener(v -> {
                activeDateField = binding.edtPassportIssueDate; // Set active field
                showDateBirthPickerDialog();
            });
            binding.edtPassportIssueDate.setFocusable(false);
            binding.edtPassportIssueDate.setClickable(true);
        }

        if (binding.edtPassportExpiryDate != null) {
            binding.edtPassportExpiryDate.setOnClickListener(v -> {
                activeDateField = binding.edtPassportExpiryDate; // Set active field
                showDateBirthPickerDialog();
            });
            binding.edtPassportExpiryDate.setFocusable(false);
            binding.edtPassportExpiryDate.setClickable(true);
        }

        binding.txtNationality.setOnClickListener(v -> {
            openDialogNationality(true);
        });

        binding.txtCountryPassport.setOnClickListener(v -> {
            openDialogNationality(false);
        });

        getRadioButtonValue();
        getListFlightClass();

        binding.rgInFlightMeal.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMealYes) {
                inFlightMealRequired = true;
            } else if (checkedId == R.id.rbMealNo) {
                inFlightMealRequired = false;
            }
        });
    }

    private String getSelectedSalutation() {
        int selectedId = binding.radioGroupSalutation.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return null;
    }

    private boolean checkNoEmptyData() {
        int empty = 0;

        int selectedId = binding.radioGroupSalutation.getCheckedRadioButtonId();

        if (selectedId == -1) {
            setToast("Please select title");
            empty++;
        }

        if (Helper.getItemParam(Constants.DATA_CREATE_TRIPS) != null) {
            createTrips = (TripsResponse) Helper.getItemParam(Constants.DATA_CREATE_TRIPS);
        }
        if (Helper.isEmpty(binding.txtNationality)) {
            binding.txtNationality.setError("Please select nationality");
            empty++;
        } else {
            boolean isIndonesian = selectedNationality.getName().equalsIgnoreCase("indonesia");
            boolean isInternational = !createTrips.getSelectedAirport().getCountry().equalsIgnoreCase("indonesia");

            if (isIndonesian) {
                if (Helper.isEmpty(binding.edtNIK)) {
                    binding.edtNIK.setError("Please enter National Identity Number (NIN)");
                    empty++;
                } else {
                    binding.edtNIK.setError(null);
                }

                if (isInternational) {
                    empty += validatePassportFields();
                } else {
                    clearPassportErrors();
                }

            } else {
                binding.edtNIK.setError(null);
                empty += validatePassportFields();
            }
        }

        if (Helper.isEmpty(binding.edtFirstName)) {
            binding.edtFirstName.setError("Please enter first name");
            empty++;
        }
        if (Helper.isEmpty(binding.edtLastName)) {
            binding.edtLastName.setError("Please enter last name");
            empty++;
        }
        if (Helper.isEmpty(binding.edtEmail)) {
            binding.edtEmail.setError("Please enter email");
            empty++;
        }
        if (Helper.isEmpty(binding.edtPhoneNumber)) {
            binding.edtPhoneNumber.setError("Please enter phone number");
            empty++;
        }
        if (Helper.isEmpty(binding.edtDateBirth)) {
            binding.edtDateBirth.setError("Please enter date of birth");
            empty++;
        }
//        if (selectedFlightClass == null) {
//            setToast("Please select flight class");
//            empty++;
//        }
//        if (Helper.isEmpty(binding.edtCabin)) {
//            binding.edtCabin.setError("Please enter cabin");
//            empty++;
//        }
//        if (Helper.isEmpty(binding.edtBaggage)) {
//            binding.edtBaggage.setError("Please enter baggage");
//            empty++;
//        }
        return empty == 0;
    }

    private int validatePassportFields() {
        int count = 0;

        if (Helper.isEmpty(binding.edtPassportNumber)) {
            binding.edtPassportNumber.setError("Please enter passport number");
            count++;
        } else {
            binding.edtPassportNumber.setError(null);
        }

        if (Helper.isEmpty(binding.txtCountryPassport)) {
            binding.txtCountryPassport.setError("Please select country of passport");
            count++;
        } else {
            binding.txtCountryPassport.setError(null);
        }

        if (Helper.isEmpty(binding.edtPassportIssueDate)) {
            binding.edtPassportIssueDate.setError("Please enter passport issue date");
            count++;
        } else {
            binding.edtPassportIssueDate.setError(null);
        }

        if (Helper.isEmpty(binding.edtPassportExpiryDate)) {
            binding.edtPassportExpiryDate.setError("Please enter passport expiry date");
            count++;
        } else {
            binding.edtPassportExpiryDate.setError(null);
        }

        return count;
    }

    private void clearPassportErrors() {
        binding.edtPassportNumber.setError(null);
        binding.txtCountryPassport.setError(null);
        binding.edtPassportIssueDate.setError(null);
        binding.edtPassportExpiryDate.setError(null);
    }


    private void saveData() {
        Passenger passenger = new Passenger();

        int selectedId = binding.radioGroupSalutation.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedSalutation = selectedRadioButton.getText().toString();

        passenger.setTitle(selectedSalutation);
        passenger.setNik(!Helper.isEmpty(binding.edtNIK) ? binding.edtNIK.getText().toString() : null);
        passenger.setFirst_name(binding.edtFirstName.getText().toString());
        passenger.setLast_name(binding.edtLastName.getText().toString());
        passenger.setEmail(binding.edtEmail.getText().toString());
        passenger.setPhone_no(binding.edtPhoneNumber.getText().toString());
        passenger.setBirth_date(Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateBirth.getText().toString()));
        passenger.setNationality_id(selectedNationality != null ? selectedNationality.getId() : 0);
        passenger.setNationality_name(selectedNationality != null ? selectedNationality.getName() : null);
        passenger.setSelectedNationality(selectedNationality != null ? selectedNationality : null);

        passenger.setPassport_no(!Helper.isEmpty(binding.edtPassportNumber) ? binding.edtPassportNumber.getText().toString() : null);
        passenger.setIssue_date(!Helper.isEmpty(binding.edtPassportIssueDate)
                ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtPassportIssueDate.getText().toString())
                : null);
        passenger.setPassport_expdate(!Helper.isEmpty(binding.edtPassportExpiryDate)
                ? Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtPassportExpiryDate.getText().toString())
                : null);
        passenger.setPassport_country_id(selectedNationalityPassport != null ? selectedNationalityPassport.getId() : 0);
        passenger.setPassport_country_name(selectedNationalityPassport != null ? selectedNationalityPassport.getName() : null);
        passenger.setSelectedNationalityPassport(selectedNationalityPassport != null ? selectedNationalityPassport : null);

        passenger.setCabin(!Helper.isEmpty(binding.edtCabin) ? Integer.parseInt(binding.edtCabin.getText().toString()) : 0);
        passenger.setBaggage(!Helper.isEmpty(binding.edtBaggage) ? Integer.parseInt(binding.edtBaggage.getText().toString()) : 0);
        passenger.setInflight_meal(inFlightMealRequired ? 1 : 0);
        passenger.setFlight_class_id(selectedFlightClass != null ? selectedFlightClass.getId() : 0);
        passenger.setFlight_class_name(selectedFlightClass != null ? selectedFlightClass.getName() : null);
        passenger.setSelectedFlightClass(selectedFlightClass != null ? selectedFlightClass : null);
        passenger.setSeat_layout(null);
        Helper.setItemParam(Constants.DATA_PASSENGER, passenger);
    }

    private void getRadioButtonValue() {
        int selectedId = binding.rgInFlightMeal.getCheckedRadioButtonId();
        if (selectedId == binding.rbMealYes.getId()) {
            inFlightMealRequired = true;
        } else if (selectedId == binding.rbMealNo.getId()) {
            inFlightMealRequired = false;
        } else {
            setToast("Please select an in-flight meal option.");
        }
    }

    private void initAdapterFlightClass() {
        Dropdown hintItem = new Dropdown();
        hintItem.setId(0);
        hintItem.setName("Select flight class...");

        if (Helper.isEmptyOrNull(flightClassList)) {
            flightClassList.add(new Dropdown());
        }
        flightClassList.add(0, hintItem);
        flightClassAdapter = new SpinnerDropDownAdapter(getApplicationContext(), flightClassList);
        binding.spnFlightClass.setAdapter(flightClassAdapter);

        binding.spnFlightClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dropdown selectedItem = (Dropdown) parent.getItemAtPosition(position);
                if (selectedItem != null && selectedItem.getId() != 0) {
                    selectedFlightClass = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePicker = DatePickerFragment.newInstance(this); // 'this' is the Activity
        // Use getSupportFragmentManager() when calling from an Activity
        datePicker.show(getSupportFragmentManager(), "datePickerBirthActivity");
    }

    private void showDateBirthPickerDialog() {
        DateBirthPickerFragment datePicker = DateBirthPickerFragment.newInstance(this); // 'this' is the Activity
        // Use getSupportFragmentManager() when calling from an Activity
        datePicker.show(getSupportFragmentManager(), "datePickerBirthActivity");
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        // Month is 0-based, so add 1 for display
        int displayMonth = month + 1;

        // Format the date string as you like (e.g., dd/MM/yyyy)
        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, displayMonth, year);

        if (activeDateField != null) {
            activeDateField.setText(selectedDate);
        } else {
            setToast("Error: Date field not identified.");
        }
    }

    public void getListCountries(String search, boolean fromSearch) {
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT_DROPDOWN));
        tripRequest.setOffset(Integer.parseInt(Constants.DEFAULT_OFFSET));
        tripRequest.setSearch(search);
        Call<WSMessage> httpRequest = apiInterface.getListCountries(tripRequest);
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
                            nationalityList = new ArrayList<>();
                            nationalityList.addAll(tempList);
                            nationalityAdapter.setFilteredList(nationalityList);
                        }
                    }
                }
                if (Helper.isEmptyOrNull(nationalityList)) {
                    dialogBinding.llEmpty.setVisibility(View.VISIBLE);
                    dialogBinding.recyclerView.setVisibility(View.GONE);
                } else {
                    dialogBinding.llEmpty.setVisibility(View.GONE);
                    dialogBinding.recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(Constants.INTERNAL_SERVER_ERROR);
            }
        });
    }

    public void getListFlightClass() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT_ALL));
        tripRequest.setOffset(Integer.parseInt(Constants.DEFAULT_OFFSET));//no need limit
        tripRequest.setSearch("");
        Call<WSMessage> httpRequest = apiInterface.getListFlightClass(tripRequest);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<Dropdown>>() {
                            }.getType();
                            List<Dropdown> tempList = new Gson().fromJson(jsonInString, listType);
                            flightClassList = new ArrayList<>();
                            flightClassList.addAll(tempList);
                        }
                    }
                }
                initAdapterFlightClass();
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                setToast(Constants.INTERNAL_SERVER_ERROR);
                initAdapterFlightClass();
            }
        });
    }

    private void openDialogNationality(boolean fromNationality) {
        nationalityList = new ArrayList<>();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        dialogBinding = DialogChooseNationalityBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();

        dialogBinding.btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmpty(dialogBinding.editText)) {
                String search = dialogBinding.editText.getText().toString().trim();
                if (search.length() > 1) {
                    getListCountries(dialogBinding.editText.getText().toString().trim(), true);
                }
            }
        });

        linearLayout = new LinearLayoutManager(CreatePassengerActivity.this);
        dialogBinding.recyclerView.setLayoutManager(linearLayout);

        nationalityAdapter = new NationalityAdapter(CreatePassengerActivity.this, nationalityList, (header, pos) -> {
            if (fromNationality) {
                selectedNationality = header;
                binding.txtNationality.setText(selectedNationality.getName());
            } else {
                selectedNationalityPassport = header;
                binding.txtCountryPassport.setText(selectedNationalityPassport.getName());
            }
            bottomSheetDialog.dismiss();
        });
        dialogBinding.recyclerView.setAdapter(nationalityAdapter);
    }
}