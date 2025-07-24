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

import androidx.recyclerview.widget.LinearLayoutManager;

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
import id.co.qualitas.epriority.databinding.DialogChooseNationalityBinding;
import id.co.qualitas.epriority.databinding.FragmentCreatePassengerBinding;
import id.co.qualitas.epriority.fragment.DatePickerFragment;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Dropdown;
import id.co.qualitas.epriority.model.Passenger;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePassengerActivity extends BaseActivity implements DatePickerFragment.DateSelectedListener {
    private FragmentCreatePassengerBinding binding;
    private NationalityAdapter nationalityAdapter;
    private SpinnerDropDownAdapter flightClassAdapter;
    private EditText activeDateField;
    private Dropdown selectedNationality, selectedFlightClass, selectedNationalityPassport;
    private List<Dropdown> nationalityList, flightClassList = new ArrayList<>();
    private int offset = 0;
    boolean inFlightMealRequired;

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
                showDatePickerDialog();
            });
            binding.edtDateBirth.setFocusable(false);
            binding.edtDateBirth.setClickable(true);
        }

        if (binding.edtPassportExpiryDate != null) {
            binding.edtPassportExpiryDate.setOnClickListener(v -> {
                activeDateField = binding.edtPassportExpiryDate; // Set active field
                showDatePickerDialog();
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

        getListFlightClass();
        getRadioButtonValue();

        binding.rgInFlightMeal.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMealYes) {
                inFlightMealRequired = true;
            } else if (checkedId == R.id.rbMealNo) {
                inFlightMealRequired = false;
            }
        });

    }

    private boolean checkNoEmptyData() {
        int empty = 0;
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
        if (Helper.isEmpty(binding.txtNationality)) {
            binding.txtNationality.setError("Please select nationality");
            empty++;
        }
        if (selectedFlightClass == null) {
            setToast("Please select flight class");
            empty++;
        }
        if (Helper.isEmpty(binding.edtCabin)) {
            binding.edtCabin.setError("Please enter cabin");
            empty++;
        }
        if (Helper.isEmpty(binding.edtBaggage)) {
            binding.edtBaggage.setError("Please enter baggage");
            empty++;
        }
        if (Helper.isEmpty(binding.edtPassportNumber)) {
            binding.edtPassportNumber.setError("Please enter passport number");
            empty++;
        }
        if (Helper.isEmpty(binding.txtCountryPassport)) {
            binding.txtCountryPassport.setError("Please select country of passport");
            empty++;
        }
        if (Helper.isEmpty(binding.edtPassportExpiryDate)) {
            binding.edtPassportExpiryDate.setError("Please enter passport expiry date");
            empty++;
        }

        if (empty == 0) {
            return true;
        }
        return false;
    }

    private void saveData() {
        Passenger passenger = new Passenger();
        passenger.setFirst_name(binding.edtFirstName.getText().toString());
        passenger.setLast_name(binding.edtLastName.getText().toString());
        passenger.setEmail(binding.edtEmail.getText().toString());
        passenger.setPhone_no(binding.edtPhoneNumber.getText().toString());
        passenger.setBirth_date(Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtDateBirth.getText().toString()));
        passenger.setCabin(Integer.parseInt(binding.edtCabin.getText().toString()));
        passenger.setBaggage(Integer.parseInt(binding.edtBaggage.getText().toString()));
        passenger.setInflight_meal(inFlightMealRequired ? 1 : 0);
        passenger.setNationality(selectedNationality.getId());
        passenger.setFlight_class(selectedFlightClass.getId());
        passenger.setPassport_no(binding.edtPassportNumber.getText().toString());
        passenger.setPassport_expdate(Helper.changeFormatDate(Constants.DATE_PATTERN_8, Constants.DATE_PATTERN_2, binding.edtPassportExpiryDate.getText().toString()));
        passenger.setPassport_country(selectedNationalityPassport.getId());
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

    // This is the callback method from DatePickerFragment.DateSelectedListener
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
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
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
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(t.getMessage());
            }
        });
    }

    public void getListFlightClass() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
        tripRequest.setOffset(Integer.parseInt(Constants.DEFAULT_OFFSET));
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
                setToast(t.getMessage());
                initAdapterFlightClass();
            }
        });
    }

    private void openDialogNationality(boolean fromNationality) {
        if (Helper.isEmptyOrNull(nationalityList)) {
            nationalityList = new ArrayList<>();
        }
        getListCountries("", false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        offset = 0;
        DialogChooseNationalityBinding dialogBinding = DialogChooseNationalityBinding.inflate(LayoutInflater.from(CreatePassengerActivity.this));
        dialog = new Dialog(CreatePassengerActivity.this);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialogBinding.btnSearch.setOnClickListener(v -> {
            getListCountries(dialogBinding.editText.getText().toString().trim(), false);
        });

        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(CreatePassengerActivity.this));
        nationalityAdapter = new NationalityAdapter(CreatePassengerActivity.this, nationalityList, (header, pos) -> {
            if (fromNationality) {
                selectedNationality = header;
                binding.txtNationality.setText(selectedNationality.getName());
            } else {
                selectedNationalityPassport = header;
                binding.txtCountryPassport.setText(selectedNationalityPassport.getName());
            }
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setAdapter(nationalityAdapter);
    }

}