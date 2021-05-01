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
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorBidsFragment extends Fragment implements View.OnClickListener {

    // TODO: Replace this with current user's Id
    private String userId = "984e7871-ed81-4f75-9524-3d1870788b1f";

    public TutorBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_bids, container, false);

        Button buttonViewCounterBidStatus = root.findViewById(R.id.buttonViewCounterBidStatus);
        buttonViewCounterBidStatus.setOnClickListener(this);

        getTutorBids();

        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonViewCounterBidStatus:
                navigateChat();
                break;
        }
    }

    // Navigate to Chat if tutor wants to chat with student to negotiate
    private void navigateChat(){
        // TODO: Replace placeholder with selected bid's id
        NavDirections navAction = BidsFragmentDirections.actionBidsFragmentToChatFragment(
                "bc06e9ad-5d20-4dce-a176-a6ac73b26b35");
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    private void getTutorBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "TutorBidsFragment: " +"Get Tutor Bids Success");
                JSONArray bids = (JSONArray) response;
                try{
                    // Loop through all student bids
                    for (int i=0; i < bids.length(); i++) {
                        JSONObject bidObj = bids.getJSONObject(i);
                        BidModel bid = new BidModel(bidObj);
                        JSONArray tutorBids = bid.getAdditionalInfo().getTutorBids();
                        // For each student bids, loop through each tutorBid
                        for (int j=0; j < tutorBids.length(); j++){
                            JSONObject tutorBidObj = tutorBids.getJSONObject(j);
                            TutorBidModel tutorBid = new TutorBidModel(tutorBidObj);
                            // Check if tutor has posted a bid (involved in this student's bid)
                            if(userId.equals(tutorBid.getTutor().getId())){
                                // TODO: Use bidModel getters to create cards
                                Log.i("print", "TutorBidsFragment: "+"Matching Bid found "+bid.getId());
                                break;
                            }
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "TutorBidsFragment: " +"Get Tutor Bids Failed "+message);
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
