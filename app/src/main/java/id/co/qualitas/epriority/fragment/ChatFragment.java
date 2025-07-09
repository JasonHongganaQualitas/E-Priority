package id.co.qualitas.epriority.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.qualitas.epriority.R;

public class ChatFragment extends Fragment {
    View view;
    RecyclerView chatRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        initialize();
        iniAdapter();

        return view;
    }

    private void iniAdapter() {
        chatRV.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initialize() {
        chatRV = view.findViewById(R.id.chatRV);
    }
}