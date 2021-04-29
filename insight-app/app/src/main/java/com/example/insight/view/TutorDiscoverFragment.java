package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
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
                JSONArray bids = (JSONArray) response;
                try{
                    for (int i=0 ; i < bids.length(); i++) {
                        JSONObject bid = bids.getJSONObject(i);
                        JSONObject initiator = bid.getJSONObject("initiator");
                        String initiatorId = initiator.getString("id");
                        String givenName = initiator.getString("givenName");
                        String familyName = initiator.getString("familyName");

                        JSONObject additionalInfo = bid.getJSONObject("additionalInfo");
                        int competency = additionalInfo.getInt("competency");
                        int rate = additionalInfo.getInt("rate");
                        int hoursPerLesson = additionalInfo.getInt("hoursPerLesson");
                        int lessonsPerWeek = additionalInfo.getInt("lessonsPerWeek");
                        boolean isRateHourly = additionalInfo.getBoolean("isRateHourly");
                        boolean isRateWeekly = additionalInfo.getBoolean("isRateWeekly");
                        JSONArray tutorBids = additionalInfo.getJSONArray("tutorBids");
                        // TODO: Display all the above info in each card
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
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
