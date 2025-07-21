package id.co.qualitas.epriority.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.HomeAgentAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentHomeAgentBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAgentFragment extends BaseFragment implements IOnBackPressed {
    private FragmentHomeAgentBinding binding;
    private HomeAgentAdapter oAdapter, pAdapter;
    View view;
    private List<TripsResponse> onGoingList = new ArrayList<>(), pendingList = new ArrayList<>();
    private TripsResponse todayStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeAgentBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        init();
        initialize();
        return view;
    }

    private void initialize() {
        getOnGoingTrips();
        binding.tvWelcome.setText("Hello " + user.getName());
        initAdapter();

        binding.imgNotif.setOnClickListener(v -> {
            NotificationFragment fragment2 = new NotificationFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment2);
            fragmentTransaction.addToBackStack("Home Agent");
            fragmentTransaction.commit();
        });
        binding.lOngoingBookings.setOnClickListener(v -> {
            OngoingBookingFragment fragment2 = new OngoingBookingFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment2);
            fragmentTransaction.addToBackStack("Home Agent");
            fragmentTransaction.commit();
        });
        binding.lPendingBookings.setOnClickListener(v -> {
            PendingBookingFragment fragment2 = new PendingBookingFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment2);
            fragmentTransaction.addToBackStack("Home Agent");
            fragmentTransaction.commit();
        });
    }

    private void initAdapter() {
        binding.rvOngoingBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        oAdapter = new HomeAgentAdapter(this, onGoingList, true, (header, pos) -> {
        });
        binding.rvOngoingBookings.setAdapter(oAdapter);

        binding.rvPendingBookings.setLayoutManager(new LinearLayoutManager(getContext()));
        pAdapter = new HomeAgentAdapter(this, pendingList, false, (header, pos) -> {
        });
        binding.rvPendingBookings.setAdapter(pAdapter);
    }

    public void callBookingDetailsFragment(TripsResponse tripsResponse) {
        Helper.setItemParam(Constants.BOOKING_DETAIL, tripsResponse);
        BookingDetailsAgentFragment fragment2 = new BookingDetailsAgentFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment2);
        fragmentTransaction.addToBackStack("Ongoing Booking");
        fragmentTransaction.commit();
    }

    public boolean onBackPressed() {
        return true;
    }

    public void getOnGoingTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getOnGoingAgentBookings(Constants.DEFAULT_OFFSET, Constants.DEFAULT_LIMIT);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            onGoingList = new ArrayList<>();
                            onGoingList.addAll(tempList);
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
                    }
                } else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
                getPendingTrips();
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(Constants.INTERNAL_SERVER_ERROR);
                getPendingTrips();
            }
        });
    }

    public void getPendingTrips() {
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getPendingAgentBookings(Constants.DEFAULT_OFFSET, Constants.DEFAULT_LIMIT);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            pendingList = new ArrayList<>();
                            pendingList.addAll(tempList);
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
                    }
                } else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
                getTodayStatus();
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(Constants.INTERNAL_SERVER_ERROR);
                getTodayStatus();
            }
        });
    }

    private void getTodayStatus() {
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getStats(Helper.getDateNow(Constants.PATTERN_DATE_3));
        if (httpRequest != null) {
            httpRequest.enqueue(new Callback<WSMessage>() {
                @Override
                public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        WSMessage result = response.body();
                        if (result != null) {
                            if (result.getIdMessage() == 1) {
                                String jsonInString = new Gson().toJson(result.getResult());
                                todayStatus = new Gson().fromJson(jsonInString, TripsResponse.class);
                            } else {
                                setToast(result.getMessage());
                            }
                        } else {
                            setToast(response.message());
                        }
                    } else {
                        setToast(Constants.INTERNAL_SERVER_ERROR);
                    }
                    setListView();
                }

                @Override
                public void onFailure(Call<WSMessage> call, Throwable t) {
                    dialog.dismiss();
                    call.cancel();
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                    setListView();
                }
            });
        }
    }

    private void setListView() {
        binding.tvPendingBookingCount.setText(todayStatus.getPending_count() + "");
        binding.tvTotalBookingCount.setText(todayStatus.getTotal_count() + "");

        oAdapter.setFilteredList(onGoingList);
        pAdapter.setFilteredList(pendingList);
        if (Helper.isNotEmptyOrNull(pendingList)) {
            binding.rvPendingBookings.setVisibility(View.VISIBLE);
            binding.lEmptyPending.setVisibility(View.GONE);
        } else {
            binding.rvPendingBookings.setVisibility(View.GONE);
            binding.lEmptyPending.setVisibility(View.VISIBLE);
        }

        if (Helper.isNotEmptyOrNull(onGoingList)) {
            binding.rvOngoingBookings.setVisibility(View.VISIBLE);
            binding.lEmptyOnGoing.setVisibility(View.GONE);
        } else {
            binding.rvOngoingBookings.setVisibility(View.GONE);
            binding.lEmptyOnGoing.setVisibility(View.VISIBLE);
        }
    }

}