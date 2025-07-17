package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.OnGoingTripAdapter;
import id.co.qualitas.epriority.constants.Constants;
import id.co.qualitas.epriority.databinding.FragmentOngoingTripCustomerBinding;
import id.co.qualitas.epriority.helper.Helper;
import id.co.qualitas.epriority.helper.RetrofitAPIClient;
import id.co.qualitas.epriority.interfaces.APIInterface;
import id.co.qualitas.epriority.model.Booking;
import id.co.qualitas.epriority.model.Trips;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngoingTripFragment extends BaseFragment {
    private List<Booking> mList = new ArrayList<>();
    OnGoingTripAdapter adapter;
    private boolean arrival = true;
    private FragmentOngoingTripCustomerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOngoingTripCustomerBinding.inflate(inflater, container, false);
        if (Helper.getItemParam(Constants.TYPE_TAB) != null) {
            String type = (String) Helper.getItemParam(Constants.TYPE_TAB);
            binding.txtTitle.setText("Ongoing " + type.toUpperCase() + " trip");
            arrival = type.equals(Constants.ARRIVAL);
        }

        initAdapter();
        getOnGoingCustomerTrips();

        binding.backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }

    private void initAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OnGoingTripAdapter(OngoingTripFragment.this, mList, (header, pos) -> {
        });
        binding.recyclerView.setAdapter(adapter);
    }

    public void getOnGoingCustomerTrips() {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        Trips trips = new Trips();
        trips.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
        trips.setOffset(Integer.parseInt(Constants.DEFAULT_OFFSET));
        trips.setTripType((arrival ? Constants.ARRIVAL : Constants.DEPARTURE));
//        trips.setSearch(search);
        Call<WSMessage> httpRequest = apiInterface.getOnGoingCustomerTrips(trips);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<Booking>>() {
                            }.getType();
                            List<Booking> tempList = new Gson().fromJson(jsonInString, listType);
                            mList = new ArrayList<>();
                            mList.addAll(tempList);
                        }
                    }
                }
                setListView();//onResponse
            }

            @Override
            public void onFailure(Call<WSMessage> call, Throwable t) {
                call.cancel();
                binding.progressBar.setVisibility(View.GONE);
                setListView();//onFailure
            }
        });
    }

    private void setListView() {
        adapter.setFilteredList(mList);
        if (Helper.isNotEmptyOrNull(mList)) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.noTripLL.setVisibility(View.GONE);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.noTripLL.setVisibility(View.VISIBLE);
        }
    }
}