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
import com.example.insight.model.BidModel;
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
public class TutorViewBidFragment extends Fragment implements View.OnClickListener {
    private TextView tvBidId;
    // TODO: Replace this with current user's Id
    private String tutorId = "984e7871-ed81-4f75-9524-3d1870788b1f";
    private String viewedBidId;
    private BidModel viewedBid;

    public TutorViewBidFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_view_bid, container, false);

        // Get viewed bid id from navigation params
        TutorViewBidFragmentArgs navArgs = TutorViewBidFragmentArgs.fromBundle(getArguments());
        viewedBidId = navArgs.getBidId();

        Button buttonBuyoutBid = root.findViewById(R.id.buttonBuyoutBid);
        Button buttonSendCounterBid = root.findViewById(R.id.buttonSendCounterBid);
        tvBidId = root.findViewById(R.id.tvBidId_TutorViewBid);
        buttonBuyoutBid.setOnClickListener(this);
        buttonSendCounterBid.setOnClickListener(this);

        // Get current bid's JSON Object and store in viewedBid
        getViewedBid();

        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBuyoutBid:
                // TODO: Uncomment this during final implementation
//                buyoutBid();
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
        NavDirections navAction = TutorViewBidFragmentDirections.actionTutorViewBidFragmentToTutorBidsFragment();
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    /**
     * Get current viewed bid's details
     */
    private void getViewedBid(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONObject bid = (JSONObject) response;
                viewedBid = new BidModel(bid);
                // TODO: Render bid information on the UI
                tvBidId.setText(viewedBid.getId());
            }
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJsonObjectRequest(
            getActivity(),
            "bid/"+viewedBidId,
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
        // Use the current bid's additionalInfo object as the JSON body
        JSONObject additionalInfo = viewedBid.getAdditionalInfo();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);

        try{
            // Step 1: Construct new tutorBid object
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

            // Step 2: Add tutorBid object to tutorBids JSONArray and include this in additionalInfo
            // IMPORTANT: This assumes that tutorBids exist in viewedBid class
            JSONArray tutorBids = viewedBid.getTutorBids();
            tutorBids.put(tutorBid);
            additionalInfo.put("tutorBids", tutorBids);

            // Step 2: Create jsonBody with additionalInfo
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getActivity(), "Tutor Bid Posted", Toast.LENGTH_SHORT).show();
                    navigateTutorBids();
                }
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "bid/"+viewedBidId,
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
        JSONObject jsonBody = viewedBid.getAdditionalInfo();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);

        try{
            jsonBody.put("dateClosedDown", currentDate);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getActivity(), "Bid bought out", Toast.LENGTH_SHORT).show();
                    // Form contract and navigate to home page to show the newly formed contract
                    createContract();
                    navigateHome();
                }
                @Override
                public void onError(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            };

            VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "bid/"+viewedBidId+"/close-down",
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
            // TODO: Replace secondPartyId with tutor's actual id
            jsonBody.put("firstPartyId", viewedBid.getStudentId());
            jsonBody.put("secondPartyId", tutorId);
            jsonBody.put("subjectId", viewedBid.getSubjectId());
            jsonBody.put("dateCreated", currentDate);
            jsonBody.put("expiryDate", expiryDate);
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());
            jsonBody.put("additionalInfo", new JSONObject());

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Toast.makeText(getActivity(), "New Contract Formed", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(String message) {
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
