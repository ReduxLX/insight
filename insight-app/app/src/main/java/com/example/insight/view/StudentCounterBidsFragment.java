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
import com.example.insight.model.BidModel;
import com.example.insight.model.TutorBidModel;
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
public class StudentCounterBidsFragment extends Fragment implements View.OnClickListener {
    private String currentBidId;

    public StudentCounterBidsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_counter_bids, container, false);

        // Get current bid id from navigation params
        StudentCounterBidsFragmentArgs navArgs = StudentCounterBidsFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getBidId();

        Button buttonChat = root.findViewById(R.id.buttonChat);
        buttonChat.setOnClickListener(this);

        getCounterBids();
        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChat:
                navigate();
                break;
        }
    }

    // Navigate to ChatFragment (chat with selected tutor to discuss their offer bid)
    private void navigate(){
        // TODO: Replace placeholder with selected bid's id
        NavDirections navAction = StudentCounterBidsFragmentDirections.actionStudentCounterBidsFragmentToChatFragment(
                "bc06e9ad-5d20-4dce-a176-a6ac73b26b35");
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    /**
     * Display all tutor counter bids made for the bid
     */
    private void getCounterBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONObject bid = (JSONObject) response;
                BidModel bidModel = new BidModel(bid);
                JSONArray tutorBids = bidModel.getTutorBids();
                try{
                    for(int i=0; i < tutorBids.length(); i++){
                        TutorBidModel tutorBidModel = new TutorBidModel(tutorBids.getJSONObject(i));
                        // TODO: Render data from tutorBidModel into each card
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
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
            String userId = "ecc52cc1-a3e4-4037-a80f-62d3799645f4";
            String tutorId = "984e7871-ed81-4f75-9524-3d1870788b1f";
            String subjectId = "";
            // TODO: Replace firstPartyId, secondPartyId and SubjectId
            jsonBody.put("firstPartyId", userId);
            jsonBody.put("secondPartyId", tutorId);
            jsonBody.put("subjectId", subjectId);
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
