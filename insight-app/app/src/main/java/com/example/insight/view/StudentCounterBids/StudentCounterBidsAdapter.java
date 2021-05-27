package com.example.insight.view.StudentCounterBids;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.model.JWTModel;
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
 * Adapter class used to populate the Counter bids recycler view
 * which is responsible for displaying any counter bids the student has received
 */
public class StudentCounterBidsAdapter extends RecyclerView.Adapter<StudentCounterBidsAdapter.CounterBidViewHolder> {

    private Context context;
    private NavController navController;
    private SharedPreferences prefs;

    private BidModel bid;
    private ArrayList<TutorBidModel> tutorBidsArray = new ArrayList<>();

    StudentCounterBidsAdapter(Context ctx, NavController navController, BidModel bidModel){
        context = ctx;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        this.navController = navController;

        bid = bidModel;
        JSONArray tutorBids = bidModel.getAdditionalInfo().getTutorBids();
        try {
            for (int j=0; j < tutorBids.length(); j++){
                JSONObject tutorBidObj = tutorBids.getJSONObject(j);
                TutorBidModel tutorBid = new TutorBidModel(tutorBidObj);
                tutorBidsArray.add(tutorBid);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public CounterBidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_student_counter_bid, parent,false);
        
        return new CounterBidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterBidViewHolder holder, int position) {
        final TutorBidModel tutorBid = tutorBidsArray.get(position);
        BidOfferModel tutorOffer = tutorBid.getTutorOffer();

       holder.name.setText(tutorBid.getTutor().getFullName());
       holder.rate.setText(tutorOffer.getRateStr());
       holder.hoursPerLesson.setText(tutorOffer.getHoursPerLessonStr());
       holder.lessonsPerWeek.setText(tutorOffer.getLessonsPerWeekStr());
       holder.freeLessons.setText(tutorOffer.getFreeClassesStr());
       holder.contractDuration.setText(tutorOffer.getContractDurationMonthsStr());

       holder.button_message.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               NavDirections navAction = StudentCounterBidsFragmentDirections.actionStudentCounterBidsFragmentToChatFragment(
                       bid.getId(), tutorBid.getTutor().getFullName(), tutorBid.getTutor().getId());
               navController.navigate(navAction);
           }
       });

       holder.button_sign_contract.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Make a confirmation pop-up window
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure you want to sign this contract?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createContract(tutorBid);
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorBidsArray.size();
    }

    static class CounterBidViewHolder extends RecyclerView.ViewHolder {
        TextView name, rate, hoursPerLesson, lessonsPerWeek, freeLessons, contractDuration;
        Button button_message, button_sign_contract;

        CounterBidViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_scb);
            rate = itemView.findViewById(R.id.tv_rate_scb);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_scb);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_scb);
            freeLessons = itemView.findViewById(R.id.tv_free_scb);
            contractDuration = itemView.findViewById(R.id.tv_contract_duration_scb);
            button_message = itemView.findViewById(R.id.button_message);
            button_sign_contract = itemView.findViewById(R.id.button_sign_contract);
        }
    }

    // Gets expiry date in ISO8601 format given the contract duration in months
    private String getExpiryDate(int months){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, months);
        Date expiryTime = cal.getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String expiryDate = ISO8601.format(expiryTime);
        return expiryDate;
    }

    private void createContract(TutorBidModel tutorBid){
        JSONObject jsonBody = new JSONObject();
        // Get current and expiry dates
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);
        // Constructing the contract JSON Object
        try{
            BidOfferModel tutorOffer = tutorBid.getTutorOffer();
            String jwt = prefs.getString("jwt", null);
            JWTModel jwtModel = new JWTModel(jwt);

            jsonBody.put("firstPartyId", jwtModel.getId());
            jsonBody.put("secondPartyId", tutorBid.getTutor().getId());
            jsonBody.put("subjectId", bid.getSubject().getId());
            jsonBody.put("dateCreated", currentDate);
            jsonBody.put("expiryDate", getExpiryDate(tutorBid.getTutorOffer().getContractDurationMonths()));
            jsonBody.put("paymentInfo", new JSONObject());
            jsonBody.put("lessonInfo", new JSONObject());

            JSONObject additionalInfo = new JSONObject();
            JSONObject contractTerms = new JSONObject();
            contractTerms.put("rate", tutorOffer.getRate());
            contractTerms.put("isRateHourly", tutorOffer.isRateHourly());
            contractTerms.put("isRateWeekly", tutorOffer.isRateWeekly());
            contractTerms.put("lessonsPerWeek", tutorOffer.getLessonsPerWeek());
            contractTerms.put("hoursPerLesson", tutorOffer.getHoursPerLesson());
            contractTerms.put("freeClasses", tutorOffer.getFreeClasses());
            contractTerms.put("competency", bid.getAdditionalInfo().getStudentOffer().getCompetency());
            contractTerms.put("contractDurationMonths", tutorOffer.getContractDurationMonths());

            additionalInfo.put("contractTerms", contractTerms);

            jsonBody.put("additionalInfo", additionalInfo);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "StudentCounterBidsFragment: " +"Create Contract Success");
                    Toast.makeText(context, "New Contract Formed", Toast.LENGTH_SHORT).show();
                    // After signing the contract, close the bid
                    closeBid();
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "StudentCounterBidsFragment: " +"Create Contract Failed "+message);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            };

            Log.i("print", "Form Contract: "+jsonBody.toString());

            VolleyUtils.makeJsonObjectRequest(
                    context,
                    "contract",
                    Request.Method.POST,
                    jsonBody,
                    listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void closeBid(){
        JSONObject jsonBody = new JSONObject();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String currentDate = ISO8601.format(currentTime);

        try{
            jsonBody.put("dateClosedDown", currentDate);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "StudentCounterBidsFragment: "+"Bid Closed Success "+bid.getId());
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "StudentCounterBidsFragment: "+"Bid ClosedFailed "+message);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            };

            VolleyUtils.makeJsonObjectRequest(
                    context,
                    "bid/"+bid.getId()+"/close-down",
                    Request.Method.POST,
                    jsonBody,
                    listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
