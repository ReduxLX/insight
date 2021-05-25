package com.example.insight.view.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insight.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeExpiredFragment extends Fragment {

    public HomeExpiredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_expired, container, false);
    }
}
