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
public class StudentCounterBidsFragment extends Fragment implements View.OnClickListener {

    public StudentCounterBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_counter_bids, container, false);

        Button buttonChat = view.findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(this);
        return view;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChat:
                navigate();
                break;
        }
    }

    // Navigate to ChatFragment (chat with selected tutor to discuss their offer bid)
    private void navigate(){
        NavDirections navAction = StudentCounterBidsFragmentDirections.actionStudentCounterBidsFragmentToChatFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
