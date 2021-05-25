package com.example.insight.view.StudentCounterBids;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment class for the Student Counter bids screen
 */
public class StudentCounterBidsFragment extends Fragment {
    private String currentBidId;
    private RecyclerView recyclerView;
    private NavController navController;

    TextView tvCountdownTimer;

    public StudentCounterBidsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_counter_bids, container, false);

        recyclerView = root.findViewById(R.id.rv_counter_bids);
        navController = NavHostFragment.findNavController(this);

        // Get current bid id from navigation params
        StudentCounterBidsFragmentArgs navArgs = StudentCounterBidsFragmentArgs.fromBundle(getArguments());
        currentBidId = navArgs.getBidId();

        tvCountdownTimer = root.findViewById(R.id.tv_countdown_timer_scb);

        getCounterBids();
        return root;
    }

    /**
     * Display all tutor counter bids made for the bid
     */
    private void getCounterBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "StudentCounterBidsFragment: "+"Get Counter Bids Success for bid "+currentBidId);
                JSONObject bidObj = (JSONObject) response;
                BidModel bid = new BidModel(bidObj);
                showTimer(bid.getAdditionalInfo().getExpiryDate());
                StudentCounterBidsAdapter adapter = new StudentCounterBidsAdapter(getActivity(), navController, bid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
            @Override
            public void onError(String message) {
                Log.i("print", "StudentCounterBidsFragment: "+"Get Counter Bids Failed "+message);
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
                tvCountdownTimer.setText(timeLeftStr);
            }

            @Override
            public void onFinish() {
                /*clearing all fields and displaying countdown finished message          */
                tvCountdownTimer.setText("Count down completed");
            }
        }.start();
    }
}
