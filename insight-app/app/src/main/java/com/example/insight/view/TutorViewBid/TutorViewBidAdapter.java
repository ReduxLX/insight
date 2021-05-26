package com.example.insight.view.TutorViewBid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.model.JWTModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Adapter class used to populate the tutor view bid recycler view
 * which is responsible for displaying other tutors who have sent a counter bid for this bid
 */
public class TutorViewBidAdapter extends RecyclerView.Adapter<TutorViewBidAdapter.TutorViewBidViewHolder> {

    private Context context;
    private SharedPreferences prefs;

    private ArrayList<TutorBidModel> tutorBidsArray = new ArrayList<>();

    TutorViewBidAdapter(Context ctx, BidModel bidModel){
        context = ctx;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        JSONArray tutorBids = bidModel.getAdditionalInfo().getTutorBids();
        try {
            for (int j=tutorBids.length()-1; j >= 0; j--){
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
    public TutorViewBidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_other_counterbids, parent,false);

        return new TutorViewBidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewBidViewHolder holder, int position) {
        final TutorBidModel tutorBid = tutorBidsArray.get(position);
        BidOfferModel tutorOffer = tutorBid.getTutorOffer();

        holder.name.setText(tutorBid.getTutor().getFullName());
        holder.rate.setText(tutorOffer.getRateStr());
        holder.hoursPerLesson.setText(tutorOffer.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(tutorOffer.getLessonsPerWeekStr());
        holder.freeLessons.setText(tutorOffer.getFreeClassesStr());
        holder.contractDuration.setText(tutorOffer.getContractDurationMonthsStr());

        // Highlight the current user's bid in red
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String userId = jwtModel.getId();
        if(userId.equals(tutorBid.getTutor().getId())){
            holder.name.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return tutorBidsArray.size();
    }

    static class TutorViewBidViewHolder extends RecyclerView.ViewHolder {
        TextView name, rate, hoursPerLesson, lessonsPerWeek, freeLessons, contractDuration;

        TutorViewBidViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_oc);
            rate = itemView.findViewById(R.id.tv_rate_oc);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_oc);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_oc);
            freeLessons = itemView.findViewById(R.id.tv_free_oc);
            contractDuration = itemView.findViewById(R.id.tv_contract_duration_oc);
        }
    }
}
