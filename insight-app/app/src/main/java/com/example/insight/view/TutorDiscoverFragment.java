package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.insight.R;


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
        View root = inflater.inflate(R.layout.fragment_tutor_discover, container, false);

        Button buttonViewStudentBid = root.findViewById(R.id.buttonViewStudentBid);
        buttonViewStudentBid.setOnClickListener(this);
        return root;
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
        // TODO: Replace this with the selected card bid's id
        String viewBidId = "33c196cf-2208-4c22-9774-dac5cfc66347";
        NavDirections navAction = TutorDiscoverFragmentDirections.actionTutorDiscoverFragmentToTutorViewBidFragment(viewBidId);
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
