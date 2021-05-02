package com.example.insight.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.insight.LoginActivity;
import com.example.insight.MainActivity;
import com.example.insight.R;
import com.example.insight.model.JWTModel;


/**
 * Fragment which is used for students to update their personal details and to log out
 * Currently unfinished
 */
public class ProfileFragment extends Fragment {
    private SharedPreferences prefs;

    private Button buttonSignOut;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        buttonSignOut = root.findViewById(R.id.buttonSignout);

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().remove("jwt").apply();
                Toast.makeText(getActivity(), "Sign out successful", Toast.LENGTH_SHORT).show();
                // Start the MainActivity and finish the current activity to prevent going back
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return root;
    }
}
