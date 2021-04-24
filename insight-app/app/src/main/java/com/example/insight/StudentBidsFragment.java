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
public class StudentBidsFragment extends Fragment implements View.OnClickListener {

    public StudentBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_bids, container, false);

        Button buttonViewBid = view.findViewById(R.id.buttonViewBid);
        buttonViewBid.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonViewBid:
                navigate();
                break;
        }
    }

    // Navigate to StudentCounterBidsFragment (view the selected bid's counter bids by tutors)
    private void navigate(){
        NavDirections navAction = StudentBidsFragmentDirections.actionStudentBidsFragmentToStudentCounterBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
