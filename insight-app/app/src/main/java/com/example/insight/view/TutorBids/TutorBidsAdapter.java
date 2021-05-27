package com.example.insight.view.TutorBids;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.User.UserModel;
import com.example.insight.view.BidsFragment;
import com.example.insight.view.BidsFragmentDirections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter class used to populate the tutor bids recycler view
 * which is responsible for displaying the bids where the tutor is involved in (sent a counter bid)
 */
public class TutorBidsAdapter extends RecyclerView.Adapter<TutorBidsAdapter.TutorBidsViewHolder> {

    private Context context;
    private NavController navController;
    private SharedPreferences prefs;

    private ArrayList<BidModel> bidArray = new ArrayList<>();
    private ArrayList<BidOfferModel> tutorOfferArray = new ArrayList<>();
    private ArrayList<Boolean> involvementArray = new ArrayList<>();

    // bidArray contains bids that tutor is involved in (send a bid)
    TutorBidsAdapter(Context ctx, NavController navController, JSONArray bids, UserModel currentTutor){
        context = ctx;
        this.navController = navController;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        try{
            // Loop through all student bids
            for (int i=0; i < bids.length(); i++) {
                JSONObject bidObj = bids.getJSONObject(i);
                BidModel bid = new BidModel(bidObj);
                JSONArray tutorBids = bid.getAdditionalInfo().getTutorBids();
                boolean isInvolved = false;
                // For each student bids, loop through each tutor bids
                for (int j=0; j < tutorBids.length(); j++){
                    JSONObject tutorBidObj = tutorBids.getJSONObject(j);
                    TutorBidModel tutorBid = new TutorBidModel(tutorBidObj);
                    boolean isNotClosed = bid.getDateClosedDown().equals("null");
                    isInvolved = tutorBid.getTutor().getId().equals(currentTutor.getId());
                    // Filter for tutor's non-closed bids and add tutor's offers
                    if(isNotClosed && isInvolved){
                        bidArray.add(bid);
                        tutorOfferArray.add(tutorBid.getTutorOffer());
                        involvementArray.add(true);
                        break;
                    }
                }
                // IF bookmarked AND NOT involved, add the student's offer instead
                boolean isBidBookmarked = currentTutor.getAdditionalInfo().searchBid(bid.getId());
                if(isBidBookmarked && !isInvolved){
                    bidArray.add(bid);
                    tutorOfferArray.add(bid.getAdditionalInfo().getStudentOffer());
                    involvementArray.add(false);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public TutorBidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_tutor_bids, parent,false);

        return new TutorBidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorBidsViewHolder holder, int position) {
        final BidModel studentBid = bidArray.get(position);
        final BidOfferModel tutorOffer = tutorOfferArray.get(position);
        boolean isInvolved = involvementArray.get(position);

        holder.name.setText(studentBid.getInitiator().getFullName());
        holder.totalTutorBids.setText(studentBid.getAdditionalInfo().getTutorBidsStr());
        holder.subject.setText(studentBid.getSubjectAndCompetencyStr());
        holder.rate.setText(tutorOffer.getRateStr());
        holder.freeClasses.setText(tutorOffer.getFreeClassesStr());
        holder.hoursPerLesson.setText(tutorOffer.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(tutorOffer.getLessonsPerWeekStr());
        holder.contractDuration.setText(tutorOffer.getContractDurationMonthsStr());

        if(!isInvolved){
            holder.button_message.setVisibility(View.GONE);
            holder.button_view_bid.setText("View Bid");
        }else{
            holder.button_message.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    NavDirections navAction = BidsFragmentDirections.actionBidsFragmentToChatFragment(
                            studentBid.getId(),
                            studentBid.getInitiator().getFullName(),
                            studentBid.getInitiator().getId());
                    navController.navigate(navAction);
                }
            });
        }

        holder.button_view_bid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavDirections navAction = BidsFragmentDirections.actionBidsFragmentToTutorViewBidFragment(studentBid.getId());
                navController.navigate(navAction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorOfferArray.size();
    }

    static class TutorBidsViewHolder extends RecyclerView.ViewHolder {
        TextView subject, name, rate, hoursPerLesson, lessonsPerWeek, freeClasses, contractDuration, totalTutorBids;
        Button button_message, button_view_bid;

        TutorBidsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_tb);
            totalTutorBids = itemView.findViewById(R.id.tv_total_tutor_bids_tb);
            subject = itemView.findViewById(R.id.tv_subject_tb);
            rate = itemView.findViewById(R.id.tv_rate_tb);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_tb);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_tb);
            freeClasses = itemView.findViewById(R.id.tv_free_tb);
            contractDuration = itemView.findViewById(R.id.tv_contract_duration_tb);
            button_message = itemView.findViewById(R.id.button_message_tb);
            button_view_bid = itemView.findViewById(R.id.button_view_bid_tb);
        }
    }
}
