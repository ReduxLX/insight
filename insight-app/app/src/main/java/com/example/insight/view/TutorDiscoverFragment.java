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
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorDiscoverFragment extends Fragment implements View.OnClickListener {
    public TutorDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_discover, container, false);

        Button buttonViewStudentBid = root.findViewById(R.id.buttonViewStudentBid);
        buttonViewStudentBid.setOnClickListener(this);

        getAllBids();

        return root;
    }

    // Intercept and handles fragment click events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonViewStudentBid:
                navigate();
                break;
        }
    }

    // Navigate to TutorViewBid fragment after tutor clicks on a student's bid to see more details
    private void navigate(){
        // TODO: Replace this with the selected card bid's id
        String viewBidId = "33c196cf-2208-4c22-9774-dac5cfc66347";
        NavDirections navAction = TutorDiscoverFragmentDirections.actionTutorDiscoverFragmentToTutorViewBidFragment(viewBidId);
        NavHostFragment.findNavController(this).navigate(navAction);
    }

    private void getAllBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "TutorDiscoverFragment: "+"Get All Bids Success");
                JSONArray bids = (JSONArray) response;
                try{
                    for (int i=0 ; i < bids.length(); i++) {
                        JSONObject bidObj = bids.getJSONObject(i);
                        BidModel bid = new BidModel(bidObj);
                        // TODO: Use bid getters to display info in card
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("print", "TutorDiscoverFragment: "+"Get All Bids Failed "+message);
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
