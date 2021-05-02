package com.example.insight.view.TutorViewBid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.JWTModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Fragment class for the tutor view bid page where tutors
 * can review the student bid's details and either buy out or send a counter offer
 */
public class TutorViewBidFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences prefs;

    private TextView tvTimer, tvSubject, tvRate, tvDuration, tvSchedule;
    private Button buttonBuyoutBid, buttonSendCounterBid;
    private EditText etRate;
    private Spinner spinnerHoursPerLesson, spinnerLessonsPerWeek, spinnerFreeClass;
    private RadioGroup radioGroupRate;

    private String currentBidId;
    private BidModel currentBid;

    private int[] competenciesValue = {0,1,2,3,4,5,6,7,8,9,10};
    private String[] hoursPerLesson = {"Hours", "1 hour", "2 hours", "3 hours", "4 hours",
            "5 hours"};
    private int[] hoursPerLessonValue = {0,1,2,3,4,5};
    private String[] lessonsPerWeek = {"Lessons", "1 lesson", "2 lessons", "3 lessons",
            "4 lessons", "5 lessons"};
    private int[] lessonsPerWeekValue = {0,1,2,3,4,5};
    private String[] freeClasses = {"0 free class", "1 free class", "2 free classes",
            "3 free classes", "4 free classes", "5 free classes"};
    private int[] freeClassesValue = {0,1,2,3,4,5};

    private boolean isRateHourly = true;

    public TutorViewBidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_view_bid, container, false);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        // Get viewed bid id from navigation params
        TutorViewBidFragmentArgs navArgs = TutorViewBidFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getBidId();

        // Get all the views from layout
        tvTimer = root.findViewById(R.id.tv_timer_tvb);
        tvSubject = root.findViewById(R.id.tv_subject_tvb);
        tvRate = root.findViewById(R.id.tv_rate_tvb);
        tvDuration = root.findViewById(R.id.tv_duration_tvb);
        tvSchedule = root.findViewById(R.id.tv_schedule_tvb);
        buttonBuyoutBid = root.findViewById(R.id.button_accept_tvb);
        buttonSendCounterBid = root.findViewById(R.id.button_send_tvb);
        buttonBuyoutBid.setOnClickListener(this);
        buttonSendCounterBid.setOnClickListener(this);
        etRate = root.findViewById(R.id.pt_rate_tvb);
        spinnerLessonsPerWeek = root.findViewById(R.id.sp_schedule_tvb);
        spinnerHoursPerLesson = root.findViewById(R.id.sp_duration_tvb);
        spinnerFreeClass = root.findViewById(R.id.sp_free_class_tvb);
        radioGroupRate = root.findViewById(R.id.rg_rate_tvg);

        // Populate spinners
        initializeSpinner(new ArrayList<>(Arrays.asList(hoursPerLesson)), spinnerHoursPerLesson);
        initializeSpinner(new ArrayList<>(Arrays.asList(lessonsPerWeek)), spinnerLessonsPerWeek);
        initializeSpinner(new ArrayList<>(Arrays.asList(freeClasses)), spinnerFreeClass);

        // Set click listeners for radio group rate
        radioGroupRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_hourly_tvb:
                        isRateHourly = true;
                        break;
                    case R.id.rb_weekly_tvb:
                        isRateHourly = false;
                        break;
                }
            }
        });

        // Get current bid's JSON Object and store in viewedBid
        getCurrentBid();

        return root;
    }

    /**
     * Helper method to initialize the contents of the dropdown menu
     */
    private void initializeSpinner(ArrayList<String> arrayList, Spinner spinner){
        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_accept_tvb:
                buyoutBid();
                break;
            case R.id.button_send_tvb:
                postTutorBid();
                break;
        }
    }

    // Navigate to Home fragment if tutor buys out the bid (show newly formed contract)
    private void navigateHome(){
        NavDirections navAction = TutorViewBidFragmentDirections.actionTutorViewBidFragmentToHomeFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    // Navigate to TutorBids fragment if tutor sends their own bid (show status of sent bid)
    private void navigateTutorBids(){
        NavDirections navAction = TutorViewBidFragmentDirections.actionTutorViewBidFragmentToBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    /**
     * Get current viewed bid's details
     */
    private void getCurrentBid(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "TutorViewBidFragment: "+"Get Current Bid Success");
                JSONObject bidObj = (JSONObject) response;
                currentBid = new BidModel(bidObj);
                BidOfferModel studentOffer = currentBid.getAdditionalInfo().getStudentOffer();
                tvSubject.setText(currentBid.getSubject().getName());
                tvRate.setText(studentOffer.getRateStr());
                tvSchedule.setText(studentOffer.getLessonsPerWeekStr());
                tvDuration.setText(studentOffer.getHoursPerLessonStr());
                showTimer(currentBid.getAdditionalInfo().toString());
            }
            @Override
            public void onError(String message) {
                Log.i("print", "TutorViewBidFragment: "+"Get Current Bid Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJsonObjectRequest(
            getActivity(),
            "bid/"+currentBidId,
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }

    /**
     * Creates and append's a tutorBid object inside student bid's additionalInfo -> tutorBids JSONArray
     * If tutorBids JSONArray does not exist then create it
     */
    private void postTutorBid(){
        JSONObject jsonBody = new JSONObject();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);
        // Precondition: Validate inputs
        int lessonsPerWeekIndex = spinnerLessonsPerWeek.getSelectedItemPosition(),
            hoursPerLessonIndex = spinnerHoursPerLesson.getSelectedItemPosition(),
            freeClassIndex = spinnerFreeClass.getSelectedItemPosition();

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
            // Clone tutorBids JSONArray to prevent duplicates being appended
            JSONArray tutorBids = new JSONArray(currentBid.getAdditionalInfo().getTutorBids().toString());

            // Step 1: Use the additionalInfo from current bid as a starting template
            JSONObject additionalInfo = currentBid.getAdditionalInfo().parseIntoJSON();

            // Step 2: Construct new tutorBid object
            JSONObject tutorBid = new JSONObject();
            tutorBid.put("dateCreated", currentDate);

            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);
            JSONObject tutor = new JSONObject();
            tutor.put("id", jwtModel.getId());
            tutor.put("givenName", jwtModel.getGivenName());
            tutor.put("familyName", jwtModel.getFamilyName());
            tutor.put("userName", jwtModel.getUsername());
            tutor.put("isStudent", jwtModel.isStudent());
            tutor.put("isTutor", jwtModel.isTutor());
            tutorBid.put("tutor", tutor);

            JSONObject tutorOffer = new JSONObject();
            tutorOffer.put("rate", Integer.parseInt(rateStr));
            tutorOffer.put("isRateHourly", isRateHourly);
            tutorOffer.put("isRateWeekly", !isRateHourly);
            tutorOffer.put("lessonsPerWeek", lessonsPerWeekValue[lessonsPerWeekIndex]);
            tutorOffer.put("hoursPerLesson", hoursPerLessonValue[hoursPerLessonIndex]);
            tutorOffer.put("freeClasses", freeClassesValue[freeClassIndex]);
            tutorBid.put("tutorOffer", tutorOffer);

            // Step 3: Add tutorBid object to tutorBids JSONArray and include this in additionalInfo
            // IMPORTANT: This assumes that tutorBids exist in viewedBid class
//            JSONArray tutorBids = currentBid.getAdditionalInfo().getTutorBids();
            tutorBids.put(tutorBid);
            additionalInfo.put("tutorBids", tutorBids);
            additionalInfo.put("expiryDate", currentBid.getAdditionalInfo().getExpiryDate());

            // Step 4: Put the additionalInfo into json body to be send in request
            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "TutorViewBidFragment: "+"Post Tutor Bid Success");
                    Toast.makeText(getActivity(), "Tutor Bid Posted", Toast.LENGTH_SHORT).show();
                    navigateTutorBids();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "TutorViewBidFragment: "+"Post Tutor Bid Failed "+message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            Log.i("print", "TutorViewBidFragment: "+"Post Tutor Bid JSON "+jsonBody);
            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "bid/"+currentBidId,
                Request.Method.PATCH,
                jsonBody,
                listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and append's a tutorBid object inside student bid's additionalInfo -> tutorBids JSONArray
     * If tutorBids JSONArray does not exist then create it
     */
    private void buyoutBid(){
        JSONObject jsonBody = new JSONObject();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);

        try{
            jsonBody.put("dateClosedDown", currentDate);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "TutorViewBidFragment: "+"Buyout Bid Success");
                    Toast.makeText(getActivity(), "Bid bought out", Toast.LENGTH_SHORT).show();
                    // Form contract and navigate to home page to show the newly formed contract
                    createContract();
                    // Close the student bid

                    navigateHome();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "TutorViewBidFragment: "+"Buyout Bid Failed "+message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "bid/"+currentBidId+"/close-down",
                Request.Method.POST,
                jsonBody,
                listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Forms a new contract between tutor and student after tutor buys out the bid
     */
    private void createContract(){
        JSONObject jsonBody = new JSONObject();

        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date nextYearTime = cal.getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);
        String expiryDate = ISO8601.format(nextYearTime);

        try{
            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);
            BidOfferModel studentOffer = currentBid.getAdditionalInfo().getStudentOffer();

            jsonBody.put("firstPartyId", currentBid.getInitiator().getId());
            jsonBody.put("secondPartyId", jwtModel.getId());
            jsonBody.put("subjectId", currentBid.getSubject().getId());
            jsonBody.put("dateCreated", currentDate);
            jsonBody.put("expiryDate", expiryDate);
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());
            jsonBody.put("additionalInfo", new JSONObject());

            JSONObject additionalInfo = new JSONObject();
            JSONObject contractTerms = new JSONObject();
            contractTerms.put("rate", studentOffer.getRate());
            contractTerms.put("isRateHourly", studentOffer.isRateHourly());
            contractTerms.put("isRateWeekly", studentOffer.isRateWeekly());
            contractTerms.put("lessonsPerWeek", studentOffer.getLessonsPerWeek());
            contractTerms.put("hoursPerLesson", studentOffer.getHoursPerLesson());
            contractTerms.put("freeClasses", 0);
            contractTerms.put("competency", studentOffer.getCompetency());

            additionalInfo.put("contractTerms", contractTerms);

            jsonBody.put("additionalInfo", new JSONObject());

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "TutorViewBidFragment: " +"Create Contract Success");
                    Toast.makeText(getActivity(), "New Contract Formed", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "TutorViewBidFragment: " +"Create Contract Failed "+message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };
            Log.i("print", "Form Contract: "+jsonBody.toString());
//            VolleyUtils.makeJsonObjectRequest(
//                getActivity(),
//                "contract",
//                Request.Method.POST,
//                jsonBody,
//                listener
//            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTimer(String expiryDateStr) {
        Log.i("print", "Expiry time now : " + expiryDateStr);
        Calendar expiryDate = Calendar.getInstance();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

        try{
            Date expiryDateObj = ISO8601.parse(expiryDateStr);
            assert expiryDateObj != null;
            expiryDate.setTime(expiryDateObj);
        }catch (ParseException e){
            e.printStackTrace();
        }

        Calendar currentTime = Calendar.getInstance();
        Log.i("print", "Current time now : " + currentTime.getTime());
        Log.i("print", "Expiry time now : " + expiryDate.getTime());
        long difference = expiryDate.getTimeInMillis() - currentTime.getTimeInMillis();

        new CountDownTimer(difference, 1000) {
            @Override
            public void onTick(long diff) {
                long days = diff / (24 * 60 * 60 * 1000);
                int weeks = (int) (days / 7);
                long day = days - (weeks * 7);
                diff = diff - (days * (24 * 60 * 60 * 1000));

                long hours = diff / (60 * 60 * 1000);
                diff = diff - (hours * (60 * 60 * 1000));

                long minutes = diff / (60 * 1000);
                diff = diff - (minutes * (60 * 1000));
                long seconds = diff / 1000;
                String timeLeftStr;

                if(days == 0 && hours < 1){
                    timeLeftStr = String.format(Locale.getDefault(),
                            "Closes in %02d minutes and %02d seconds", minutes, seconds);
                }else{
                    timeLeftStr = String.format(Locale.getDefault(),
                            "Closes in %02d days and %02d hours", days, hours);
                }
                Log.i("consoleLog", "Time Difference: " + timeLeftStr);
                tvTimer.setText(timeLeftStr);
            }

            @Override
            public void onFinish() {
                /*clearing all fields and displaying countdown finished message          */
                tvTimer.setText("Count down completed");
            }
        }.start();
    }

}
