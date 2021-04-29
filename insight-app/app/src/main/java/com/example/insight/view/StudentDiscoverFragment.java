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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentDiscoverFragment extends Fragment implements View.OnClickListener {
    // TODO: Replace this with real user's id
    private String userId = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";

    public StudentDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_discover, container, false);

        Button buttonPostBid = root.findViewById(R.id.buttonPostBid);
        buttonPostBid.setOnClickListener(this);
        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPostBid:
                postBid();
                break;
        }
    }

    // Navigate to StudentDiscover fragment after student posts a bid
    private void navigate(){
        NavDirections navAction = StudentDiscoverFragmentDirections.actionStudentDiscoverFragmentToStudentBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    // TODO: Implement UI and get the json body data from UI instead of placeholders
    private void postBid(){
        JSONObject jsonBody = new JSONObject();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String datePosted = ISO8601.format(currentTime);

        try{
            jsonBody.put("type", "Open");
            jsonBody.put("initiatorId", userId);
            jsonBody.put("dateCreated", datePosted);
            jsonBody.put("subjectId", "7a95ef32-6808-4035-a6c5-a77a73c9d741");

            JSONObject additionalInfo = new JSONObject();
            additionalInfo.put("competency", 5);
            additionalInfo.put("rate", 20);
            additionalInfo.put("isRateHourly", true);
            additionalInfo.put("isRateWeekly", false);
            additionalInfo.put("lessonsPerWeek", 2);
            additionalInfo.put("hoursPerLesson", 3);
            additionalInfo.put("tutorBids", new JSONArray());
            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getActivity(), "Bid Posted", Toast.LENGTH_SHORT).show();
                    navigate();
                }
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            Log.i("console_log", jsonBody.toString());
            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "bid",
                Request.Method.POST,
                jsonBody,
                listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
