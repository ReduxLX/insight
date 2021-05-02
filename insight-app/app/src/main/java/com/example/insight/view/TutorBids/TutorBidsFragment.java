package com.example.insight.view.TutorBids;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.model.JWTModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorBidsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavController navController;
    private SharedPreferences prefs;

    public TutorBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_bids, container, false);

        recyclerView = root.findViewById(R.id.rv_tutor_bids);
        navController = NavHostFragment.findNavController(this);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        getTutorBids();

        return root;
    }

    private void getTutorBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "TutorBidsFragment: " +"Get Tutor Bids Success");
                ArrayList<BidModel> bidArray = new ArrayList<>();
                ArrayList<TutorBidModel> tutorBidArray = new ArrayList<>();
                JSONArray bids = (JSONArray) response;
                try{
                    // Loop through all student bids
                    for (int i=0; i < bids.length(); i++) {
                        JSONObject bidObj = bids.getJSONObject(i);
                        BidModel bid = new BidModel(bidObj);
                        JSONArray tutorBids = bid.getAdditionalInfo().getTutorBids();
                        // For each student bids, loop through each tutor bids
                        for (int j=0; j < tutorBids.length(); j++){
                            JSONObject tutorBidObj = tutorBids.getJSONObject(j);
                            TutorBidModel tutorBid = new TutorBidModel(tutorBidObj);
                            // Check if tutor has posted a bid (involved in this student's bid)
                            // If yes then add the current bid model object to the array list
                            String jwt = prefs.getString("jwt", null);
                            JWTModel jwtModel = new JWTModel(jwt);
                            String userId = jwtModel.getId();

                            // Filter for non-closed bids with tutor
                            if(userId.equals(tutorBid.getTutor().getId()) && bid.getDateClosedDown().equals("null")){
                                // TODO: Use bidModel getters to create cards
                                bidArray.add(bid);
                                tutorBidArray.add(tutorBid);
                            }
                        }
                    }

                    TutorBidsAdapter adapter = new TutorBidsAdapter(getActivity(), navController, bidArray, tutorBidArray);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
