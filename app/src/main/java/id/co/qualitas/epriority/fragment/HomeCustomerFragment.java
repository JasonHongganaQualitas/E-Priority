package id.co.qualitas.epriority.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.activity.BookingDetailsActivity;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentHomeCustomerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCustomerFragment extends BaseFragment {
    private FragmentHomeCustomerBinding binding;
    boolean arrival = true;
    OnGoingTripAdapter adapter;
    private List<TripsResponse> mList = new ArrayList<>(), arrivalList = new ArrayList<>(), departureList = new ArrayList<>();
    private String searchText;
    int offsetDeparture, offsetArrival;
    private boolean loadingDeparture = true, loadingArrival = true;
    protected int pastVisiblesItemsDeparture, visibleItemCountDeparture, totalItemCountDeparture;
    protected int pastVisiblesItemsArrival, visibleItemCountArrival, totalItemCountArrival;
    private LinearLayoutManager linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeCustomerBinding.inflate(inflater, container, false);

        init();
        initialize();
        arrival = true;
        initAdapter();
        setupKeyboardSearchActionListener();
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Arrival"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Departure"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                arrival = tab.getPosition() == 0;
                getOnGoingCustomerTrips(searchText);//onTabSelected
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        binding.moreTripsImgView.setOnClickListener(v -> {
//            Helper.setItemParam(Constants.TYPE_TAB, arrival ? Constants.ARRIVAL : Constants.DEPARTURE);
//            OngoingTripFragment fragment = new OngoingTripFragment();
//            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
//        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        offsetArrival = 0;
        offsetDeparture = 0;
        getOnGoingCustomerTrips(searchText);//onresume
    }

    private void initAdapter() {
        linearLayout = new LinearLayoutManager(getActivity());
        binding.ongoingTripRV.setLayoutManager(linearLayout);
        adapter = new OnGoingTripAdapter(HomeCustomerFragment.this, mList, (header, pos) -> {
            Helper.setItemParam(Constants.TRIP_HEADER, header);
            Intent intent = new Intent(getContext(), BookingDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        binding.ongoingTripRV.setAdapter(adapter);

        binding.ongoingTripRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (arrival) {
                    if (offsetArrival == 0) {
                        itemCount();
                    } else {
                        if (dy > 0) //check for scroll down
                        {
                            itemCount();
                        } else {
                            binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                            loadingArrival = true;
                        }
                    }
                } else {
                    if (offsetDeparture == 0) {
                        itemCount();
                    } else {
                        if (dy > 0) //check for scroll down
                        {
                            itemCount();
                        } else {
                            binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                            loadingDeparture = true;
                        }
                    }
                }
            }
        });
    }

    public void itemCount() {
        if (arrival) {
            visibleItemCountArrival = linearLayout.getChildCount();
            totalItemCountArrival = linearLayout.getItemCount();
            pastVisiblesItemsArrival = linearLayout.findFirstVisibleItemPosition();

            if (loadingArrival) {
                if ((visibleItemCountArrival + pastVisiblesItemsArrival) >= totalItemCountArrival) {
                    loadingArrival = false;
                    binding.loadingDataBottom.relativeProgress.setVisibility(View.VISIBLE);
                    offsetArrival = totalItemCountArrival;
                }
            }
        } else {
            visibleItemCountDeparture = linearLayout.getChildCount();
            totalItemCountDeparture = linearLayout.getItemCount();
            pastVisiblesItemsDeparture = linearLayout.findFirstVisibleItemPosition();
            if (loadingDeparture) {
                if ((visibleItemCountDeparture + pastVisiblesItemsDeparture) >= totalItemCountDeparture) {
                    loadingDeparture = false;
                    binding.loadingDataBottom.relativeProgress.setVisibility(View.VISIBLE);
                    offsetDeparture = totalItemCountDeparture;
                }
            }
        }
        getOnGoingCustomerTrips(searchText);//paging

    }

    private void initialize() {
        binding.tvWelcome.setText("Hello " + user.getName());
    }

    public void getOnGoingCustomerTrips(String search) {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
        tripRequest.setOffset(arrival ? offsetArrival : offsetDeparture);
        tripRequest.setTrip_type((arrival ? Constants.ARRIVAL : Constants.DEPARTURE));
        tripRequest.setSearch(search);
        Call<WSMessage> httpRequest = apiInterface.getOnGoingCustomerTrips(tripRequest);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            if (arrival) {
                                if (Helper.isNotEmptyOrNull(tempList)) {
                                    if (offsetArrival == 0) {
                                        arrivalList = new ArrayList<>();
                                    }
                                    arrivalList.addAll(tempList);
                                }
                            } else {
                                if (Helper.isNotEmptyOrNull(tempList)) {
                                    if (offsetDeparture == 0) {
                                        departureList = new ArrayList<>();
                                    }
                                    departureList.addAll(tempList);
                                }
                            }
                        }
                    }
                }
                setListView();//onResponse
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
                binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                setListView();//onFailure
            }
        });
    }

//    public void getFlightInformation() {
//        openDialogProgress();
//        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
//        Call<WSMessage> httpRequest = apiInterface.getFlightInformation("3354642b13de213ea1b4d32469d8686d", "GA404");
//        httpRequest.enqueue(new Callback<WSMessage>() {
//            @Override
//            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
//                dialog.dismiss();
//                if (response.isSuccessful()) {
//                    WSMessage result = response.body();
//                    if (result != null) {
//                        String jsonInString = new Gson().toJson(result.getResult());
//                        FlightInformation flightInformation = new Gson().fromJson(jsonInString, FlightInformation.class);
//                        if (flightInformation != null) {
//                            information = flightInformation;
//                            binding.flightTxt.setText(information.getFlight().getIata());
//                        }
//                    } else {
//                        openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
//                    }
//                } else {
//                    openDialogInformation(Constants.INTERNAL_SERVER_ERROR, response.message(), null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WSMessage> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                openDialogInformation(Constants.INTERNAL_SERVER_ERROR, t.getMessage(), null);
//            }
//        });
//    }

    private void setListView() {
        mList = new ArrayList<>();
        if (arrival) {
            mList.addAll(arrivalList);
        } else {
            mList.addAll(departureList);
        }
        adapter.setFilteredList(mList);
        if (Helper.isNotEmptyOrNull(mList)) {
            binding.ongoingTripRV.setVisibility(View.VISIBLE);
            binding.noTripLL.setVisibility(View.GONE);
        } else {
            binding.ongoingTripRV.setVisibility(View.GONE);
            binding.noTripLL.setVisibility(View.VISIBLE);
        }
    }

    private void setupKeyboardSearchActionListener() {
        if (binding.etSearchTrips != null) {
            binding.etSearchTrips.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchText = v.getText().toString().trim();
                    offsetDeparture = 0;
                    offsetArrival = 0;
                    getOnGoingCustomerTrips(searchText);//search
                    hideKeyboard(v);
                    return true; // Consume the event
                }
                return false; // Let the system handle other actions
            });
        }
    }

    // Helper method to hide keyboard
    private void hideKeyboard(View view) {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}