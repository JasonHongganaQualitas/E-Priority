package com.example.e_priority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_priority.R;

public class ChatDetailFragment extends Fragment {
    View view;
    RecyclerView chatDetailRV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_detail, container, false);

        initialize();
        initAdapter();

        return view;
    }

    private void initAdapter() {

    }

    private void initialize() {
        chatDetailRV = view.findViewById(R.id.chatDetailRV);
    }
}