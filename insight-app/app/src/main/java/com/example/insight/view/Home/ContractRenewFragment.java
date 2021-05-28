package com.example.insight.view.Home;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.MainActivity;
import com.example.insight.R;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.Competency.CompetencyModel;
import com.example.insight.model.Contract.ContractModel;
import com.example.insight.model.Contract.ContractTermsModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.User.UserModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;
import com.example.insight.view.TutorViewBid.TutorViewBidFragmentDirections;

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

    private EditText etSubject, etCompetency, etRate;
    private Spinner spTutor, spHoursPerLesson, spLessonsPerWeek, spContractDuration;
    private RadioGroup radioGroupRate;
    private RadioButton rb_hourly, rb_weekly;

    // Contract to be renewed
    private String contractId;
    private ContractModel currentContract;

    private ArrayList<CompetencyModel> competenciesArray = new ArrayList<>();
    private String[] hoursPerLesson = {"Hours", "1 hour", "2 hours", "3 hours", "4 hours", "5 hours"};
    private int[] hoursPerLessonValue = {0,1,2,3,4,5};
    private String[] lessonsPerWeek = {"Lessons", "1 lesson", "2 lessons", "3 lessons", "4 lessons", "5 lessons"};
    private int[] lessonsPerWeekValue = {0,1,2,3,4,5};
    private String[] contractDuration = {"3 months", "6 months", "12 months", "24 months"};
    private int[] contractDurationValue = {3,6,12,24};

    // Default radio button settings
    private boolean isHourlyRate = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_contract_renew, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Contract Renewal Page");
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        etSubject = root.findViewById(R.id.et_subject_cr);
        etCompetency = root.findViewById(R.id.et_competency_cr);
        etRate = root.findViewById(R.id.et_rate_cr);
        spTutor = root.findViewById(R.id.sp_tutor_cr);
        spHoursPerLesson = root.findViewById(R.id.sp_hours_cr);
        spLessonsPerWeek = root.findViewById(R.id.sp_lessons_cr);
        spContractDuration = root.findViewById(R.id.sp_contract_duration_cr);
        radioGroupRate = root.findViewById(R.id.rg_rate_cr);
        rb_hourly = root.findViewById(R.id.rb_hourly_cr);
        rb_weekly = root.findViewById(R.id.rb_weekly_cr);
        Button buttonRenewContract = root.findViewById(R.id.button_renew_cr);
        buttonRenewContract.setOnClickListener(this);

        // Get contractId from navigation params
        // Get current bid id from navigation params
        ContractRenewFragmentArgs navArgs = ContractRenewFragmentArgs.fromBundle(getArguments());
        contractId = navArgs.getContractId();

        // Fetch expired contract details & tutors
        getContract();


        // Populate tutors, hours, lessons and contract duration dropdown menus
        String[] competencies = {"Loading Tutors..."};
        initializeSpinner(new ArrayList<>(Arrays.asList(competencies)), spTutor);
        initializeSpinner(new ArrayList<>(Arrays.asList(hoursPerLesson)), spHoursPerLesson);
        initializeSpinner(new ArrayList<>(Arrays.asList(lessonsPerWeek)), spLessonsPerWeek);
        initializeSpinner(new ArrayList<>(Arrays.asList(contractDuration)), spContractDuration);

        // Set click listeners for radio group rate
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
                renewContract();
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
        int competencyIndex = spTutor.getSelectedItemPosition(),
            lessonsPerWeekIndex = spLessonsPerWeek.getSelectedItemPosition(),
            hoursPerLessonIndex = spHoursPerLesson.getSelectedItemPosition(),
            contractDurationIndex = spContractDuration.getSelectedItemPosition();

        String rateStr = etRate.getText().toString();
        if(TextUtils.isEmpty(rateStr)){
            Toast.makeText(getActivity(), "Please enter a rate amount", Toast.LENGTH_SHORT).show();
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
            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);

            jsonBody.put("firstPartyId", jwtModel.getId());
            jsonBody.put("secondPartyId", competenciesArray.get(competencyIndex).getOwner().getId());
            jsonBody.put("subjectId", currentContract.getSubject().getId());
            jsonBody.put("dateCreated", currentDate);
            jsonBody.put("expiryDate", getExpiryDate(contractDurationValue[contractDurationIndex]));
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());

            JSONObject additionalInfo = new JSONObject();
            JSONObject contractTerms = new JSONObject();
            contractTerms.put("rate", etRate.getText().toString());
            contractTerms.put("isRateHourly", rb_hourly.isChecked());
            contractTerms.put("isRateWeekly", rb_weekly.isChecked());
            contractTerms.put("lessonsPerWeek", lessonsPerWeekValue[lessonsPerWeekIndex]);
            contractTerms.put("hoursPerLesson", hoursPerLessonValue[hoursPerLessonIndex]);
            contractTerms.put("freeClasses", 0);
            contractTerms.put("competency", currentContract.getAdditionalInfo().getContractTerms().getCompetency());
            contractTerms.put("contractDurationMonths", contractDurationValue[contractDurationIndex]);

            additionalInfo.put("contractTerms", contractTerms);
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

            // Navigate home
            NavDirections navAction = ContractRenewFragmentDirections.actionContractRenewFragmentToHomeFragment();
            NavHostFragment.findNavController(this).navigate(navAction);
            Log.i("print", "Renew Contract "+ jsonBody.toString());

            VolleyUtils.makeJSONObjectRequest(
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

    // Fetch competencies and filter those that belong to qualified tutors (2+ level)
    private void getQualifiedTutors(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "ContractRenewFragment: "+"Get Competencies Success");
                JSONArray competencies = (JSONArray) response;
                // Tutor details store name & competency in string format Ex: James Brown (7)
                ArrayList<String> tutorDetailsArray = new ArrayList<>();

                try{
                    // Loop through all competencies and add those that are:
                    // 1. Matching contract's original subject
                    // 2. Belongs to a tutor and not a student
                    // 3. Has level +2 above contract's competency
                    int originalTutorIndex = 0;
                    for (int i=0; i < competencies.length(); i++) {
                        JSONObject competencyObj = competencies.getJSONObject(i);
                        CompetencyModel competency = new CompetencyModel(competencyObj);
                        boolean matchSubject = competency.getSubject().getId().equals(
                                currentContract.getSubject().getId());
                        boolean belongsToTutor = competency.getOwner().isTutor();
                        boolean isQualified = competency.getLevel() >=
                                currentContract.getAdditionalInfo().getContractTerms().getCompetency() + 2;
                        if(matchSubject && belongsToTutor && isQualified){
                            competenciesArray.add(competency);
                            String dropdownTxt = competency.getOwner().getFullName() + " (Level "+
                                    competency.getLevelStr()+")";
                            tutorDetailsArray.add(dropdownTxt);
                            // Just in case tutor is listed as first party
                            boolean belongsToFirstParty = competency.getOwner().getId()
                                    .equals(currentContract.getSecondParty().getId());
                            boolean belongsToSecondParty = competency.getOwner().getId()
                                    .equals(currentContract.getSecondParty().getId());
                            if(belongsToFirstParty || belongsToSecondParty){
                                originalTutorIndex = i;
                            }
                        }
                    }
                    selectOriginalTutor(originalTutorIndex);
                    // Re-initialize the tutor dropdown menu with their name & competency
                    initializeSpinner(tutorDetailsArray, spTutor);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "ContractRenewFragment: "+"Get Competencies Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
                getActivity(),
                "competency",
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
                currentContract = new ContractModel(contractObj);
                getQualifiedTutors();
                fillContractTerms();
            }
            @Override
            public void onError(String message) {
                Log.i("print", "ContractRenewFragment: "+"Get Contract "+contractId+" Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONObjectRequest(
                getActivity(),
                "contract/"+contractId,
                Request.Method.GET,
                new JSONObject(),
                listener
        );
    }

    // Pre-fills the renewal form with the old contract's terms
    private void fillContractTerms(){
        ContractTermsModel contractTerms = currentContract.getAdditionalInfo().getContractTerms();
        etSubject.setText(currentContract.getSubject().getName());
        etCompetency.setText(contractTerms.getCompetencyDetails());
        etRate.setText(String.valueOf(contractTerms.getRate()));
        if(contractTerms.isRateHourly()){
            radioGroupRate.check(R.id.rb_hourly_cr);
        }else if(contractTerms.isRateWeekly()){
            radioGroupRate.check(R.id.rb_weekly_cr);
        }

        spLessonsPerWeek.setSelection(contractTerms.getLessonsPerWeekDropdownIndex());
        spHoursPerLesson.setSelection(contractTerms.getHoursPerLessonDropdownIndex());
        spContractDuration.setSelection(contractTerms.getContractDurationDropdownIndex());
    }

    // Selects the original tutor as default tutor in the dropdown spinner
    private void selectOriginalTutor(int originalIndex){
        spTutor.setSelection(originalIndex);
    }
}
