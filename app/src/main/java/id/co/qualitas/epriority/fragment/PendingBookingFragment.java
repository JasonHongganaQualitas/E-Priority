package id.co.qualitas.epriority.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.activity.MainActivity;
import id.co.qualitas.epriority.adapter.PendingBookingAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentPendingBookingBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingBookingFragment extends BaseFragment implements IOnBackPressed{

    private FragmentPendingBookingBinding binding;
    private PendingBookingAdapter adapter;
    private List<TripsResponse> pendingList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getPendingTrips();
        initialize();
    }

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        initAdapter();
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
                            pendingList.addAll(tempList);
                            initAdapter();
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(Constants.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                setToast(Constants.INTERNAL_SERVER_ERROR);
            }
        });
    }

    private void initAdapter() {
        List<TripsResponse> tripsResponses = new ArrayList<>();
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        tripsResponses.add(new TripsResponse("John Smith", "#129-B012", "05/07/25 at 14:30",
                "Tokyo, Japan – Flight NH782", 5, "Pending"));
        if(pendingList.size() != 0) {
            adapter = new PendingBookingAdapter(this, pendingList);
            binding.recyclerViewPendingBooking.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerViewPendingBooking.setAdapter(adapter);
            binding.recyclerViewPendingBooking.setVisibility(View.VISIBLE);
            binding.lEmpty.setVisibility(View.GONE);
        }else{
            binding.recyclerViewPendingBooking.setVisibility(View.GONE);
            binding.lEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public boolean onBackPressed() {
        return false;
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

    public void acceptBooking(TripsResponse tripsResponse){
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.acceptBooking(String.valueOf(tripsResponse.getId()));
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    WSMessage result = response.body();
                    if (result != null){
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                        } else {
                            setToast(result.getMessage());
                        }
                    }
                    else {
                        setToast(Constants.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                setToast(Constants.INTERNAL_SERVER_ERROR);
            }
        });
    }

    public void declineBooking(TripsResponse tripsResponse){
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.declineBooking(String.valueOf(tripsResponse.getId()));
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    WSMessage result = response.body();
                    if (result != null){
                        if (result.getIdMessage() == 1) {
                            setToast(result.getMessage());
                        } else {
                            setToast(result.getMessage());
                        }
                    }
                    else {
                        setToast(Constants.INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    setToast(Constants.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                setToast(Constants.INTERNAL_SERVER_ERROR);
            }
        });
    }
}
