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

import id.co.qualitas.epriority.R;
import id.co.qualitas.epriority.adapter.AgentAdapter;

public class ChoosePackageFragment extends Fragment {
    View view;
    RecyclerView agentRV;
    AgentAdapter adapter;
    ImageView detTransfer, detLounge, detFlight, detFastLane, detBaggage;
    CheckBox transferCB, loungeCB, flightCB, fastLaneCB, baggageCB;
    LinearLayout transferDetLL, transferLL, loungeLL, loungeDetLL, flightLL, flightDetLL, fastLaneLL, fastLaneDetLL, baggageLL, baggageDetLL;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_package, container, false);

        initialize();
        initAdapter();


        transferCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                transferDetLL.setVisibility(View.VISIBLE);
                detTransfer.setImageResource(R.drawable.ic_arrow_up_blue);
                transferLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                transferDetLL.setVisibility(View.GONE);
                detTransfer.setImageResource(R.drawable.ic_arrow_down_gray);
                transferLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });

        loungeCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                loungeDetLL.setVisibility(View.VISIBLE);
                detLounge.setImageResource(R.drawable.ic_arrow_up_blue);
                loungeLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                loungeDetLL.setVisibility(View.GONE);
                detLounge.setImageResource(R.drawable.ic_arrow_down_gray);
                loungeLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });

        flightCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                flightDetLL.setVisibility(View.VISIBLE);
                detFlight.setImageResource(R.drawable.ic_arrow_up_blue);
                flightLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                flightDetLL.setVisibility(View.GONE);
                detFlight.setImageResource(R.drawable.ic_arrow_down_gray);
                flightLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });

        fastLaneCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                fastLaneDetLL.setVisibility(View.VISIBLE);
                detFastLane.setImageResource(R.drawable.ic_arrow_up_blue);
                fastLaneLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                fastLaneDetLL.setVisibility(View.GONE);
                detFastLane.setImageResource(R.drawable.ic_arrow_down_gray);
                fastLaneLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });

        baggageCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                baggageDetLL.setVisibility(View.VISIBLE);
                detBaggage.setImageResource(R.drawable.ic_arrow_up_blue);
                baggageLL.setBackgroundResource(R.drawable.bg_blue_card);
            }
            else {
                baggageLL.setVisibility(View.GONE);
                detBaggage.setImageResource(R.drawable.ic_arrow_down_gray);
                baggageLL.setBackgroundResource(R.drawable.bg_rounded_border_gray);
            }
        });

        return view;
    }

    private void initAdapter() {
        agentRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentAdapter();
        agentRV.setAdapter(adapter);
    }

    private void initialize() {
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
    }
}