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
public class StudentCounterBidsFragment extends Fragment implements View.OnClickListener {

    public StudentCounterBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_counter_bids, container, false);

        Button buttonChat = root.findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(this);
        return root;
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
        // TODO: Replace placeholder with selected bid's id
        NavDirections navAction = StudentCounterBidsFragmentDirections.actionStudentCounterBidsFragmentToChatFragment(
                "bc06e9ad-5d20-4dce-a176-a6ac73b26b35");
        NavHostFragment.findNavController(this).navigate(navAction);
    }
}
