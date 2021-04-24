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
public class TutorViewBidFragment extends Fragment implements View.OnClickListener {

    public TutorViewBidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_view_bid, container, false);

        Button buttonBuyoutBid = view.findViewById(R.id.buttonBuyoutBid);
        Button buttonSendCounterBid = view.findViewById(R.id.buttonSendCounterBid);
        buttonBuyoutBid.setOnClickListener(this);
        buttonSendCounterBid.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBuyoutBid:
                navigateHome();
                break;
            case R.id.buttonSendCounterBid:
                navigateTutorBids();
                break;
        }
    }

    // Navigate to Home fragment if tutor buys out the bid (show newly formed contract)
    private void navigateHome(){
        NavDirections navAction = TutorViewBidFragmentDirections.actionTutorViewBidFragmentToHomeFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    // Navigate to TutorBids fragment if tutor sends their own bid (show status of sent bid)
    private void navigateTutorBids(){
        NavDirections navAction = TutorViewBidFragmentDirections.actionTutorViewBidFragmentToTutorBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }


}
