package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;

public class ChoosePackageFragment extends BaseFragment {
    View view;
    RecyclerView agentRV;
    AgentAdapter adapter;
    TextView btnReview;
    ImageView backBtn, detTransfer, detLounge, detFlight, detFastLane, detBaggage, transferImg, loungeImg, flightImg, fastLaneImg, baggageImg;
    CheckBox transferCB, loungeCB, flightCB, fastLaneCB, baggageCB;
    LinearLayout transferDetLL, transferLL, loungeLL, loungeDetLL, flightLL, flightDetLL, fastLaneLL, fastLaneDetLL, baggageLL, baggageDetLL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_package, container, false);

        initialize();
        initAdapter();

        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        transferCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                transferDetLL.setVisibility(View.VISIBLE);
                detTransfer.setImageResource(R.drawable.ic_arrow_up_blue);
                transferLL.setBackgroundResource(R.drawable.bg_blue_card);
                transferImg.setImageResource(R.drawable.ic_vehicle);
                transferImg.setBackgroundResource(R.color.blue2);
            }
            else {
                transferDetLL.setVisibility(View.GONE);
                detTransfer.setImageResource(R.drawable.ic_arrow_down_gray);
                transferLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
                transferImg.setImageResource(R.drawable.ic_vehicle_gray);
                transferImg.setBackgroundResource(R.color.gray3);
            }
        });

        loungeCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                loungeDetLL.setVisibility(View.VISIBLE);
                detLounge.setImageResource(R.drawable.ic_arrow_up_blue);
                loungeLL.setBackgroundResource(R.drawable.bg_blue_card);
                loungeImg.setImageResource(R.drawable.ic_couch);
                loungeImg.setBackgroundResource(R.color.blue2);
            }
            else {
                loungeDetLL.setVisibility(View.GONE);
                detLounge.setImageResource(R.drawable.ic_arrow_down_gray);
                loungeLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
                loungeImg.setImageResource(R.drawable.ic_couch_gray);
                loungeImg.setBackgroundResource(R.color.gray3);
            }
        });

        flightCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                flightDetLL.setVisibility(View.VISIBLE);
                detFlight.setImageResource(R.drawable.ic_arrow_up_blue);
                flightLL.setBackgroundResource(R.drawable.bg_blue_card);
                flightImg.setImageResource(R.drawable.ic_airplane);
                flightImg.setBackgroundResource(R.color.blue2);
            }
            else {
                flightDetLL.setVisibility(View.GONE);
                detFlight.setImageResource(R.drawable.ic_arrow_down_gray);
                flightLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
                flightImg.setImageResource(R.drawable.ic_airplane_gray);
                flightImg.setBackgroundResource(R.color.gray3);
            }
        });

        fastLaneCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                fastLaneDetLL.setVisibility(View.VISIBLE);
                detFastLane.setImageResource(R.drawable.ic_arrow_up_blue);
                fastLaneLL.setBackgroundResource(R.drawable.bg_blue_card);
                fastLaneImg.setImageResource(R.drawable.ic_fastlane);
                fastLaneImg.setBackgroundResource(R.color.blue2);
            }
            else {
                fastLaneDetLL.setVisibility(View.GONE);
                detFastLane.setImageResource(R.drawable.ic_arrow_down_gray);
                fastLaneLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
                fastLaneImg.setImageResource(R.drawable.ic_fastlane_gray);
                fastLaneImg.setBackgroundResource(R.color.gray3);
            }
        });

        baggageCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                baggageDetLL.setVisibility(View.VISIBLE);
                detBaggage.setImageResource(R.drawable.ic_arrow_up_blue);
                baggageLL.setBackgroundResource(R.drawable.bg_blue_card);
                baggageImg.setImageResource(R.drawable.ic_baggage);
                baggageImg.setBackgroundResource(R.color.blue2);
            }
            else {
                baggageLL.setVisibility(View.GONE);
                detBaggage.setImageResource(R.drawable.ic_arrow_down_gray);
                baggageLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
                baggageImg.setImageResource(R.drawable.ic_baggage_gray);
                baggageImg.setBackgroundResource(R.color.gray3);
            }
        });

        btnReview.setOnClickListener(v -> {
            ReviewBookingFragment fragment = new ReviewBookingFragment();
            getParentFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(null).commit();
        });

        return view;
    }

    private void initAdapter() {
        agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter();
        agentRV.setAdapter(adapter);
    }

    private void initialize() {
        backBtn = view.findViewById(R.id.backBtn);
        agentRV = view.findViewById(R.id.agentRV);
        detTransfer = view.findViewById(R.id.detTransfer);
        detLounge = view.findViewById(R.id.detLounge);
        detFlight = view.findViewById(R.id.detFlight);
        detFastLane = view.findViewById(R.id.detFastLane);
        detBaggage = view.findViewById(R.id.detBaggage);
        transferCB = view.findViewById(R.id.transferCB);
        loungeCB = view.findViewById(R.id.loungeCB);
        flightCB = view.findViewById(R.id.flightCB);
        fastLaneCB = view.findViewById(R.id.fastLaneCB);
        baggageCB = view.findViewById(R.id.baggageCB);
        transferDetLL = view.findViewById(R.id.transferDetLL);
        transferLL = view.findViewById(R.id.transferLL);
        loungeLL = view.findViewById(R.id.loungeLL);
        loungeDetLL = view.findViewById(R.id.loungeDetLL);
        flightLL = view.findViewById(R.id.flightLL);
        flightDetLL = view.findViewById(R.id.flightDetLL);
        fastLaneLL = view.findViewById(R.id.fastLaneLL);
        fastLaneDetLL = view.findViewById(R.id.fastLaneDetLL);
        baggageLL = view.findViewById(R.id.baggageLL);
        baggageDetLL = view.findViewById(R.id.baggageDetLL);
        transferImg = view.findViewById(R.id.transferImg);
        loungeImg = view.findViewById(R.id.loungeImg);
        flightImg = view.findViewById(R.id.flightImg);
        fastLaneImg = view.findViewById(R.id.fastLaneImg);
        baggageImg = view.findViewById(R.id.baggageImg);
        btnReview = view.findViewById(R.id.btnReview);
    }
}