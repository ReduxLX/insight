package com.example.insight.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.JWTModel;
import com.example.insight.model.SubjectModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Fragment which hosts the form where students can post bids
 */
public class StudentDiscoverFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences prefs;

    private EditText etRate;
    private Spinner spinnerSubject, spinnerCompetency, spinnerHoursPerLesson,
                    spinnerLessonsPerWeek, spinnerContractDuration;
    private RadioGroup radioGroupRate, radioGroupBidType;

    private ArrayList<SubjectModel> subjectArray = new ArrayList<>();
    private String[] competencies = {"Select competency level", "Level 1 - Beginner", "Level 2 - Beginner", "Level 3 - Beginner",
            "Level 4 - Novice", "Level 5 - Novice", "Level 6 - Novice",
            "Level 7 - Intermediate", "Level 8 - Intermediate", "Level 9 - Expert",
            "Level 10 - Expert"};

    private int[] competenciesValue = {0,1,2,3,4,5,6,7,8,9,10};
    private String[] hoursPerLesson = {"Hours", "1 hour", "2 hours", "3 hours", "4 hours", "5 hours"};

    private int[] hoursPerLessonValue = {0,1,2,3,4,5};
    private String[] lessonsPerWeek = {"Lessons", "1 lesson", "2 lessons", "3 lessons", "4 lessons", "5 lessons"};
    private int[] lessonsPerWeekValue = {0,1,2,3,4,5};

    private String[] contractDuration = {"3 months", "6 months", "12 months", "24 months"};
    private int[] contractDurationValue = {3,6,12,24};

    // Default radio button settings
    private boolean isHourlyRate = true;
    private boolean isTypeOpenBid = true;

    public StudentDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_discover, container, false);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        etRate = root.findViewById(R.id.pt_rate_sd);
        spinnerSubject = root.findViewById(R.id.sp_subject_sd);
        spinnerCompetency = root.findViewById(R.id.sp_competency_sd);
        spinnerHoursPerLesson = root.findViewById(R.id.sp_hoursPerLesson_sd);
        spinnerLessonsPerWeek = root.findViewById(R.id.sp_lessonsPerWeek_sd);
        spinnerContractDuration = root.findViewById(R.id.sp_contract_duration_sd);
        Button buttonPostBid = root.findViewById(R.id.buttonPostBid);
        buttonPostBid.setOnClickListener(this);
        // Fetch subjects using GET HTTP call
        getSubjects();

        // Populate subject dropdown menu
        String[] subjects = {"Loading..."};
        initializeSpinner(new ArrayList<>(Arrays.asList(subjects)), spinnerSubject, 0);
        // Populate competency dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(competencies)), spinnerCompetency, 0);
        // Populate hours per lesson dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(hoursPerLesson)), spinnerHoursPerLesson, 0);
        // Populate lessons per week dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(lessonsPerWeek)), spinnerLessonsPerWeek, 0);
        // Populate contract duration dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(contractDuration)), spinnerContractDuration, 1);

        // Set click listeners for radio group rate
        radioGroupRate = root.findViewById(R.id.rg_rate);
        radioGroupRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_hourly_sd:
                        isHourlyRate = true;
                        break;
                    case R.id.rb_weekly_sd:
                        isHourlyRate = false;
                        break;
                }
            }
        });

        // Set click listeners for radio group rate
        radioGroupBidType = root.findViewById(R.id.rg_bid_type);
        radioGroupBidType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_open_sd:
                        isTypeOpenBid = true;
                        break;
                    case R.id.rb_close_sd:
                        isTypeOpenBid = false;
                        break;
                }
            }
        });

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

    private void initializeSpinner(ArrayList<String> arrayList, Spinner spinner, int startIndex){
        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(startIndex);
    }

    // Navigate to StudentDiscover fragment after student posts a bid
    private void navigate(){
        NavDirections navAction = DiscoverFragmentDirections.actionDiscoverFragmentToBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    // Gets expiry date in ISO8601 format given the contract duration in months
    private String getExpiryDate(int months){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, months);
        Date expiryTime = cal.getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String expiryDate = ISO8601.format(expiryTime);
        return expiryDate;
    }

    // TODO: Replace placeholders with form input
    private void postBid(){
        JSONObject jsonBody = new JSONObject();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String datePosted = ISO8601.format(currentTime);
        int subjectIndex = spinnerSubject.getSelectedItemPosition(),
            competencyIndex = spinnerCompetency.getSelectedItemPosition(),
            lessonsPerWeekIndex = spinnerLessonsPerWeek.getSelectedItemPosition(),
            hoursPerLessonIndex = spinnerHoursPerLesson.getSelectedItemPosition(),
            contractDurationIndex = spinnerContractDuration.getSelectedItemPosition();
        if(subjectIndex == 0){
            Toast.makeText(getActivity(), "Please select a subject", Toast.LENGTH_SHORT).show();
            return;
        }
        if(competencyIndex == 0){
            Toast.makeText(getActivity(), "Please select a competency level", Toast.LENGTH_SHORT).show();
            return;
        }
        String rateStr = etRate.getText().toString();
        if(TextUtils.isEmpty(rateStr)){
            Toast.makeText(getActivity(), "Please enter the rate amount", Toast.LENGTH_SHORT).show();
            return;
        }
        if(lessonsPerWeekIndex == 0){
            Toast.makeText(getActivity(), "Please select the number of lessons per week", Toast.LENGTH_SHORT).show();
            return;
        }
        if(hoursPerLessonIndex == 0){
            Toast.makeText(getActivity(), "Please select the hours per lesson", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            String bidType = isTypeOpenBid? "open": "close";
            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);
            jsonBody.put("type", bidType);
            jsonBody.put("initiatorId", jwtModel.getId());
            jsonBody.put("dateCreated", datePosted);
            jsonBody.put("subjectId", subjectArray.get(subjectIndex-1).getId());

            JSONObject additionalInfo = new JSONObject();

            JSONObject studentOffer = new JSONObject();
            studentOffer.put("competency", competenciesValue[competencyIndex]);
            studentOffer.put("rate", Integer.parseInt(rateStr));
            studentOffer.put("isRateHourly", isHourlyRate);
            studentOffer.put("isRateWeekly", !isHourlyRate);
            studentOffer.put("lessonsPerWeek", lessonsPerWeekValue[lessonsPerWeekIndex]);
            studentOffer.put("hoursPerLesson", hoursPerLessonValue[hoursPerLessonIndex]);
            studentOffer.put("contractDurationMonths", contractDurationValue[contractDurationIndex]);
            additionalInfo.put("studentOffer", studentOffer);

            additionalInfo.put("tutorBids", new JSONArray());
            additionalInfo.put("expiryDate", getExpiryTime(isTypeOpenBid));
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
            Log.i("print", "Post Bid JSON"+ jsonBody.toString());

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
                ArrayList<String> subjectNameArray = new ArrayList<>();
                subjectNameArray.add("Select a subject");
                try{
                    // Loop through all subjects and add them to subjectArray
                    for (int i=0; i < subjects.length(); i++) {
                        JSONObject subjectObj = subjects.getJSONObject(i);
                        SubjectModel subject = new SubjectModel(subjectObj);
                        subjectArray.add(subject);
                        subjectNameArray.add(subject.getName());
                    }
                    // Re-initialize subject dropdown menu with fetched subjects
                    initializeSpinner(subjectNameArray, spinnerSubject, 0);
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
