package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentBidsFragment extends Fragment implements View.OnClickListener {
    // TODO: Replace this with real user's id
    private String userId = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";

    public StudentBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_bids, container, false);

        Button buttonViewBid = root.findViewById(R.id.buttonViewBid);
        buttonViewBid.setOnClickListener(this);
        return root;
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

    private void getBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray bids = (JSONArray) response;
                try{
                    for (int i=0 ; i < bids.length(); i++) {
                        JSONObject bid = bids.getJSONObject(i);
                        JSONObject initiator = bid.getJSONObject("initiator");
                        String initiatorId = initiator.getString("id");

                        // Filter bids with student id only
                        if(initiatorId.equals(userId)){
                            JSONObject additionalInfo = bid.getJSONObject("additionalInfo");
                            int competency = additionalInfo.getInt("competency");
                            int rate = additionalInfo.getInt("rate");
                            int hoursPerLesson = additionalInfo.getInt("hoursPerLesson");
                            int lessonsPerWeek = additionalInfo.getInt("lessonsPerWeek");
                            boolean isRateHourly = additionalInfo.getBoolean("isRateHourly");
                            boolean isRateWeekly = additionalInfo.getBoolean("isRateWeekly");
                            JSONArray tutorBids = additionalInfo.getJSONArray("tutorBids");
                            // TODO: Display all the above info in each card and length of tutorBids

                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity(),
            "bid",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }
}
