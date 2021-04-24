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
public class StudentDiscoverFragment extends Fragment implements View.OnClickListener {

    public StudentDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_discover, container, false);

        Button buttonPostBid = view.findViewById(R.id.buttonPostBid);
        buttonPostBid.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPostBid:
                navigate();
                break;
        }
    }

    // Navigate to StudentDiscover fragment after student posts a bid
    private void navigate(){
        NavDirections navAction = StudentDiscoverFragmentDirections.actionStudentDiscoverFragmentToStudentBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
