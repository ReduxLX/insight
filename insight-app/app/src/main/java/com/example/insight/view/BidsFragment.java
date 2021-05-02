package com.example.insight.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insight.R;
import com.example.insight.model.JWTModel;
import com.example.insight.view.StudentBids.StudentBidsFragment;
import com.example.insight.view.TutorBids.TutorBidsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BidsFragment extends Fragment {

    public BidsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bids, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(jwtModel.isTutor()){
            // Replace whatever is in the fragment_container view with this fragment
            transaction.replace(R.id.fragment_container_bids, TutorBidsFragment.class, null);
        }else{
            transaction.replace(R.id.fragment_container_bids, StudentBidsFragment.class, null);
        }
        transaction.setReorderingAllowed(false);
        transaction.commit();

        return root;
    }
}
