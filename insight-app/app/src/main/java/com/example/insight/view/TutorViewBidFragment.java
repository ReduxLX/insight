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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Bid.BidAdditionalInfoModel;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorViewBidFragment extends Fragment implements View.OnClickListener {
    private TextView tvBidId;
    private String currentBidId;
    private BidModel currentBid;

    public TutorViewBidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_view_bid, container, false);

        // Get viewed bid id from navigation params
        TutorViewBidFragmentArgs navArgs = TutorViewBidFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getBidId();

        Button buttonBuyoutBid = root.findViewById(R.id.buttonBuyoutBid);
        Button buttonSendCounterBid = root.findViewById(R.id.buttonSendCounterBid);
        tvBidId = root.findViewById(R.id.tvBidId_TutorViewBid);
        buttonBuyoutBid.setOnClickListener(this);
        buttonSendCounterBid.setOnClickListener(this);

        // Get current bid's JSON Object and store in viewedBid
        getCurrentBid();

        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBuyoutBid:
                // TODO: Uncomment this during final implementation
                buyoutBid();
                break;
            case R.id.buttonSendCounterBid:
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
                // TODO: Render bid information on the UI
                tvBidId.setText(currentBid.getId());
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

        try{
            // Step 1: Use the additionalInfo from current bid as a starting template
            JSONObject additionalInfo = currentBid.getAdditionalInfo().parseIntoJSON();

            // Step 2: Construct new tutorBid object
            JSONObject tutorBid = new JSONObject();
            tutorBid.put("dateCreated", currentDate);

            // TODO: Replace all placeholders with tutor's details (if possible reuse tutor's JSON Object)
            JSONObject tutor = new JSONObject();
            tutor.put("id", "984e7871-ed81-4f75-9524-3d1870788b1f");
            tutor.put("givenName", "James");
            tutor.put("familyName", "Coleman");
            tutor.put("userName", "jcool123");
            tutor.put("isStudent", false);
            tutor.put("isTutor", true);
            tutorBid.put("tutor", tutor);

            // TODO: Replace placeholder data with form inputs
            JSONObject tutorOffer = new JSONObject();
            tutorOffer.put("rate", 80);
            tutorOffer.put("isRateHourly", false);
            tutorOffer.put("isRateWeekly", true);
            tutorOffer.put("lessonsPerWeek", 2);
            tutorOffer.put("hoursPerLesson", 4);
            tutorOffer.put("freeClasses", 2);
            tutorBid.put("tutorOffer", tutorOffer);

            // Step 3: Add tutorBid object to tutorBids JSONArray and include this in additionalInfo
            // IMPORTANT: This assumes that tutorBids exist in viewedBid class
            JSONArray tutorBids = currentBid.getAdditionalInfo().getTutorBids();
            tutorBids.put(tutorBid);
            additionalInfo.put("tutorBids", tutorBids);

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
//            VolleyUtils.makeJsonObjectRequest(
//                getActivity(),
//                "bid/"+currentBidId,
//                Request.Method.PATCH,
//                jsonBody,
//                listener
//            );
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
            // TODO: Replace this with current user's Id
            String tutorId = "984e7871-ed81-4f75-9524-3d1870788b1f";
            // TODO: Replace secondPartyId with tutor's actual id
            jsonBody.put("firstPartyId", currentBid.getInitiator().getId());
            jsonBody.put("secondPartyId", tutorId);
            jsonBody.put("subjectId", currentBid.getSubject().getId());
            jsonBody.put("dateCreated", currentDate);
            jsonBody.put("expiryDate", expiryDate);
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());
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
            Log.i("print", jsonBody.toString());
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

}
