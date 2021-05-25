package com.example.insight.view.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insight.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContractFragment extends Fragment {
    private HomeContractAdapter adapter;
    private int contractType;

    public HomeContractFragment(HomeContractAdapter adapter, int contractType){
        this.adapter = adapter;
        this.contractType = contractType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(getLayout(), container, false);
        RecyclerView recyclerView = root.findViewById(getRecyclerView());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    public int getLayout(){
        switch(contractType){
            case 1:
                return R.layout.fragment_home_pending;
            case 2:
                return R.layout.fragment_home_expired;
            default:
                return R.layout.fragment_home_active;
        }
    }

    public int getRecyclerView(){
        switch(contractType){
            case 1:
                return R.id.rv_home_pending;
            case 2:
                return R.id.rv_home_expired;
            default:
                return R.id.rv_home_active;
        }
    }
}
