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
import com.example.insight.model.SubjectModel;
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

    // TODO: Replace placeholders with form input
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

            JSONObject studentOffer = new JSONObject();
            studentOffer.put("competency", 5);
            studentOffer.put("rate", 20);
            studentOffer.put("isRateHourly", true);
            studentOffer.put("isRateWeekly", false);
            studentOffer.put("lessonsPerWeek", 2);
            studentOffer.put("hoursPerLesson", 3);
            additionalInfo.put("studentOffer", studentOffer);

            additionalInfo.put("tutorBids", new JSONArray());
            additionalInfo.put("expiryDate", getExpiryTime(false));
            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "StudentDiscoverFragment: "+"Post Bid Success");
                    Toast.makeText(getActivity(), "Bid Posted", Toast.LENGTH_SHORT).show();
                    navigate();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "StudentDiscoverFragment: " +"Post Bid Failed "+message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

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

    private String getExpiryTime(boolean isOpenBidding){
        Calendar expiryTime = Calendar.getInstance();
        if(isOpenBidding){
            expiryTime.add(Calendar.MINUTE, 30);
        }else{
            expiryTime.add(Calendar.DATE, 7);
        }
        Date expiryDate = expiryTime.getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        return ISO8601.format(expiryDate);
    }

    private void getSubjects(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "StudentDiscoverFragment: "+"Get Subjects Success");
                JSONArray subjects = (JSONArray) response;
                try{
                    // Loop through all subjects
                    for (int i=0; i < subjects.length(); i++) {
                        JSONObject subjectObj = subjects.getJSONObject(i);
                        SubjectModel subject = new SubjectModel(subjectObj);
                        // TODO: Show all subjects in a dropdown menu
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "StudentDiscoverFragment: "+"Get Subjects Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity(),
            "subject",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }
}
