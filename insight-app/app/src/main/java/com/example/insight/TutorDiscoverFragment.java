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
public class TutorDiscoverFragment extends Fragment implements View.OnClickListener {

    public TutorDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_discover, container, false);

        Button buttonViewStudentBid = view.findViewById(R.id.buttonViewStudentBid);
        buttonViewStudentBid.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonViewStudentBid:
                navigate();
                break;
        }
    }

    // Navigate to TutorViewBid fragment after tutor clicks on a student's bid to see more details
    private void navigate(){
        NavDirections navAction = TutorDiscoverFragmentDirections.actionTutorDiscoverFragmentToTutorViewBidFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
