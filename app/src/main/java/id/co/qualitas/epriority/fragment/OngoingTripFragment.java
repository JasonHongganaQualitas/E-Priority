package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import id.co.qualitas.epriority.model.TripsResponse;
import id.co.qualitas.epriority.model.TripRequest;
import id.co.qualitas.epriority.model.WSMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OngoingTripFragment extends BaseFragment {
    private List<TripsResponse> mList = new ArrayList<>();
    OnGoingTripAdapter adapter;
    private boolean arrival = true;
    private FragmentOngoingTripCustomerBinding binding;
    int offset;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOngoingTripCustomerBinding.inflate(inflater, container, false);
        initAdapter();

        if (Helper.getItemParam(Constants.TYPE_TAB) != null) {
            String type = (String) Helper.getItemParam(Constants.TYPE_TAB);
            binding.txtTitle.setText("Ongoing " + type.toUpperCase() + " trip");
            arrival = type.equals(Constants.ARRIVAL);
        }
        offset = 0;
        getOnGoingCustomerTrips();//first
        setRefreshLayout();

        binding.backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }

    private void initAdapter() {
        linearLayout = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(linearLayout);
        adapter = new OnGoingTripAdapter(OngoingTripFragment.this, mList, (header, pos) -> {
        });
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        binding.loadingDataBottom.relativeProgress.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });
    }

    public void itemCount() {
        visibleItemCount = linearLayout.getChildCount();
        totalItemCount = linearLayout.getItemCount();
        pastVisiblesItems = linearLayout.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                binding.loadingDataBottom.relativeProgress.setVisibility(View.VISIBLE);
                offset = totalItemCount;
                getOnGoingCustomerTrips();//paging
            }
        }
    }

    private void setRefreshLayout() {
        binding.swipeLayout.setColorSchemeResources(R.color.textPending,
                R.color.badgeUpcoming,
                R.color.textUpcoming,
                R.color.badgePending);
        binding.swipeLayout.setOnRefreshListener(() -> {
            binding.swipeLayout.setRefreshing(false);
            offset = 0;
            binding.progressBar.setVisibility(View.VISIBLE);
            getOnGoingCustomerTrips();//setRefreshLayout
        });
    }

    public void getOnGoingCustomerTrips() {
        binding.progressBar.setVisibility(View.VISIBLE);
        apiInterface = RetrofitAPIClient.getClientWithToken().create(APIInterface.class);
        TripRequest tripRequest = new TripRequest();
        tripRequest.setLimit(Integer.parseInt(Constants.DEFAULT_LIMIT));
        tripRequest.setOffset(offset);
        tripRequest.setTrip_type((arrival ? Constants.ARRIVAL : Constants.DEPARTURE));
        Call<WSMessage> httpRequest = apiInterface.getOnGoingCustomerTrips(tripRequest);
        httpRequest.enqueue(new Callback<WSMessage>() {
            @Override
            public void onResponse(Call<WSMessage> call, Response<WSMessage> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    WSMessage result = response.body();
                    if (result != null) {
                        if (result.getIdMessage() == 1) {
                            String jsonInString = new Gson().toJson(result.getResult());
                            Type listType = new TypeToken<ArrayList<TripsResponse>>() {
                            }.getType();
                            List<TripsResponse> tempList = new Gson().fromJson(jsonInString, listType);
                            if (Helper.isNotEmptyOrNull(tempList)) {
                                if (offset == 0) {
                                    mList = new ArrayList<>();
                                }
                                mList.addAll(tempList);
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