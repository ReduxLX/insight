package com.example.insight.view.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.insight.model.Contract.ContractModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.User.UserModel;
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
 * Fragment which lets student renew their expired contracts
 */
public class ContractRenewFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences prefs;

    private EditText etRate;
    private Spinner spTutor, spHoursPerLesson, spLessonsPerWeek, spContractDuration;
    private RadioGroup radioGroupRate;

    // Contract to be renewed
    private String contractId;
    private ContractModel contract;

    private ArrayList<UserModel> tutorArray = new ArrayList<>();
    private String[] hoursPerLesson = {"Hours", "1 hour", "2 hours", "3 hours", "4 hours", "5 hours"};
    private int[] hoursPerLessonValue = {0,1,2,3,4,5};
    private String[] lessonsPerWeek = {"Lessons", "1 lesson", "2 lessons", "3 lessons", "4 lessons", "5 lessons"};
    private int[] lessonsPerWeekValue = {0,1,2,3,4,5};
    private String[] contractDuration = {"6 months", "3 months", "12 months", "24 months"};
    private int[] contractDurationValue = {6,3,12,24};

    // Default radio button settings
    private boolean isHourlyRate = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contract_renew, container, false);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        etRate = root.findViewById(R.id.et_rate_cr);
        spTutor = root.findViewById(R.id.sp_tutor_cr);
        spHoursPerLesson = root.findViewById(R.id.sp_hours_cr);
        spLessonsPerWeek = root.findViewById(R.id.sp_lessons_cr);
        spContractDuration = root.findViewById(R.id.sp_contract_duration_cr);
        Button buttonRenewContract = root.findViewById(R.id.button_renew_cr);
        buttonRenewContract.setOnClickListener(this);

        // Get contractId from navigation params
        // Get current bid id from navigation params
        ContractRenewFragmentArgs navArgs = ContractRenewFragmentArgs.fromBundle(getArguments());
        contractId = navArgs.getContractId();

        // Fetch expired contract details & tutors
        getContract();
        getTutors();

        // Populate the tutors dropdown menu with valid tutors
        String[] subjects = {"Loading Tutors..."};
        initializeSpinner(new ArrayList<>(Arrays.asList(subjects)), spTutor);
        // Populate hours per lesson dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(hoursPerLesson)), spHoursPerLesson);
        // Populate lessons per week dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(lessonsPerWeek)), spLessonsPerWeek);
        // Populate contract duration dropdown menu
        initializeSpinner(new ArrayList<>(Arrays.asList(contractDuration)), spContractDuration);

        // Set click listeners for radio group rate
        radioGroupRate = root.findViewById(R.id.rg_rate_cr);
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

        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_renew_cr:
//                renewContract();
                break;
        }
    }

    private void initializeSpinner(ArrayList<String> arrayList, Spinner spinner){
        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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


    private void renewContract(){
        JSONObject jsonBody = new JSONObject();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);
        int tutorIndex = spTutor.getSelectedItemPosition(),
            lessonsPerWeekIndex = spLessonsPerWeek.getSelectedItemPosition(),
            hoursPerLessonIndex = spHoursPerLesson.getSelectedItemPosition(),
            contractDurationIndex = spContractDuration.getSelectedItemPosition();

        String rateStr = etRate.getText().toString();
        if(TextUtils.isEmpty(rateStr)){
            Toast.makeText(getActivity(), "Please enter the rate amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);

            jsonBody.put("firstPartyId", jwtModel.getId());
            jsonBody.put("secondPartyId", tutorArray.get(tutorIndex).getId());
            // TODO: Set subjectId based on passed in data
            jsonBody.put("subjectId", "Physics");
            jsonBody.put("dateCreated", currentDate);
            // TODO: Set expiry date using contract duration (make a method)
            jsonBody.put("expiryDate", getExpiryDate(contractDurationValue[contractDurationIndex]));
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());

            JSONObject additionalInfo = new JSONObject();
            JSONObject contractTerms = new JSONObject();
            contractTerms.put("rate", etRate.getText().toString());
            contractTerms.put("isRateHourly", isHourlyRate);
            contractTerms.put("isRateWeekly", !isHourlyRate);
            contractTerms.put("lessonsPerWeek", lessonsPerWeekValue[lessonsPerWeekIndex]);
            contractTerms.put("hoursPerLesson", hoursPerLessonValue[hoursPerLessonIndex]);
            contractTerms.put("freeClasses", 0);
            //TODO: Set Competency based on passed in data
            contractTerms.put("competency", 7);

            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "ContractRenewFragment: "+"Renew Contract Success");
                    Toast.makeText(getActivity(), "Renewal request sent", Toast.LENGTH_SHORT).show();
//                    navigate();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "ContractRenewFragment: " +"Renew Contract Failed "+message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };
            Log.i("print", "Renew Contract JSON"+ jsonBody.toString());

            VolleyUtils.makeJsonObjectRequest(
                    getActivity(),
                    "contract",
                    Request.Method.POST,
                    jsonBody,
                    listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Fetch tutors and filter based on subject competency
    private void getTutors(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "ContractRenewFragment: "+"Get Tutors Success");
                JSONArray users = (JSONArray) response;
                // Tutor details store name & competency in string format Ex: James Brown (7)
                ArrayList<String> tutorDetailsArray = new ArrayList<>();

                try{
                    // Loop through all subjects and add them to subjectArray
                    for (int i=0; i < users.length(); i++) {
                        JSONObject userObj = users.getJSONObject(i);
                        UserModel user = new UserModel(userObj);
                        if(user.isTutor()){ tutorArray.add(user); }
                    }
                    // Re-initialize the tutor dropdown menu with their name & competency
                    initializeSpinner(tutorDetailsArray, spTutor);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "ContractRenewFragment: "+"Get Tutors Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
                getActivity(),
                "user",
                Request.Method.GET,
                new JSONObject(),
                listener
        );
    }

    private void getContract(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "ContractRenewFragment: "+"Get Contract Success");
                JSONObject contractObj = (JSONObject) response;
                contract = new ContractModel(contractObj);
                Log.i("print", "ContractRenewFragment: "+contractObj.toString());
            }
            @Override
            public void onError(String message) {
                Log.i("print", "ContractRenewFragment: "+"Get Contract "+contractId+" Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
                getActivity(),
                "contract/"+contractId,
                Request.Method.GET,
                new JSONObject(),
                listener
        );
    }
}
