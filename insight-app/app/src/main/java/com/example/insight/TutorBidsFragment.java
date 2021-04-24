package com.example.insight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorBidsFragment extends Fragment implements View.OnClickListener {

    public TutorBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_bids, container, false);

        Button buttonViewCounterBidStatus = view.findViewById(R.id.buttonViewCounterBidStatus);
        buttonViewCounterBidStatus.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonViewCounterBidStatus:
                navigateChat();
                break;
        }
    }

    // Navigate to Chat if tutor wants to chat with student to negotiate
    private void navigateChat(){
        NavDirections navAction = TutorBidsFragmentDirections.actionTutorBidsFragmentToChatFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
