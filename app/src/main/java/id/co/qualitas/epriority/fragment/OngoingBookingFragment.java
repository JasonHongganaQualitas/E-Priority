package id.co.qualitas.epriority.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OngoingBookingAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentOngoingBookingBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.interfaces.IOnBackPressed;
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngoingBookingFragment extends BaseFragment implements IOnBackPressed {
    private FragmentOngoingBookingBinding binding;
    private OngoingBookingAdapter adapter;
    private List<TripsResponse> mList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOngoingBookingBinding.inflate(inflater, container, false);
        init();
        getOngoingTrips();
        initialize();
        return binding.getRoot();
    }

    private void getOngoingTrips() {
        openDialogProgress();
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Call<WSMessage> httpRequest = apiInterface.getOnGoingAgentBookings(Constants.DEFAULT_OFFSET, Constants.DEFAULT_LIMIT);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            mList = new ArrayList<>();
                            mList.addAll(tempList);
                            initAdapter();
                        } else {
                            setToast(result.getMessage());
                        }
                    } else {
                        setToast(response.message());
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

    private void initialize() {
        binding.btnBack.setOnClickListener(v -> getActivity().getOnBackPressedDispatcher().onBackPressed());
        initAdapter();
    }

    private void initAdapter() {
        adapter = new OngoingBookingAdapter(this, mList);
        binding.recyclerViewOngoingBooking.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOngoingBooking.setAdapter(adapter);

        if (Helper.isNotEmptyOrNull(mList)) {
            binding.recyclerViewOngoingBooking.setVisibility(View.VISIBLE);
            binding.lEmpty.setVisibility(View.GONE);
        } else {
            binding.recyclerViewOngoingBooking.setVisibility(View.GONE);
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
}
