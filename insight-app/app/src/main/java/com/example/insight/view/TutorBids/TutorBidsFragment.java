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
import com.example.insight.model.User.UserModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Fragment class for the Tutor bids screen where tutors can view and message student bids
 */
public class TutorBidsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavController navController;
    private SharedPreferences prefs;

    private UserModel currentTutor;

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

        getUserBidBookmarks();

        return root;
    }

    private void getUserBidBookmarks(){
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);

        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONObject userObj = (JSONObject) response;
                currentTutor = new UserModel(userObj);
                // Get Tutor's Involved bids after knowing their bookmarked bids
                getTutorBids();
            }
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJsonObjectRequest(
                getActivity(),
                "user/"+jwtModel.getId(),
                Request.Method.GET,
                new JSONObject(),
                listener
        );
    }

    private void getTutorBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "TutorBidsFragment: " +"Get Tutor Bids Success");
                JSONArray bids = (JSONArray) response;
                TutorBidsAdapter adapter = new TutorBidsAdapter(getActivity(), navController, bids, currentTutor);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
