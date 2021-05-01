package com.example.insight.view.TutorDiscover;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.view.DiscoverFragmentDirections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TutorDiscoverAdapter extends RecyclerView.Adapter<TutorDiscoverAdapter.TutorDiscoverViewHolder> {

    private Context context;
    private NavController navController;

    private ArrayList<BidModel> bidArray = new ArrayList<>();

    TutorDiscoverAdapter(Context ctx, NavController navController, JSONArray bids){
        context = ctx;
        this.navController = navController;

        try {
            for (int j=0; j < bids.length(); j++){
                JSONObject bidObj = bids.getJSONObject(j);
                BidModel bid = new BidModel(bidObj);
                bidArray.add(bid);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public TutorDiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_layout_tutor_discover, parent,false);

        return new TutorDiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorDiscoverViewHolder holder, int position) {
        final BidModel bid = bidArray.get(position);
        BidOfferModel studentOffer = bid.getAdditionalInfo().getStudentOffer();

        holder.subject.setText(bid.getSubject().getName());
        holder.competency.setText(String.valueOf(studentOffer.getCompetencyStr()));
        holder.name.setText(bid.getInitiator().getFullName());
        holder.rate.setText(studentOffer.getRateStr());
        holder.hoursPerLesson.setText(studentOffer.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(studentOffer.getLessonsPerWeekStr());
        holder.competencyCircle.setImageResource(studentOffer.getCompetencyResource());

        holder.constraint_td.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavDirections navAction = DiscoverFragmentDirections.actionDiscoverFragmentToTutorViewBidFragment(
                        bid.getId());
                navController.navigate(navAction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bidArray.size();
    }

    static class TutorDiscoverViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraint_td;
        TextView subject, competency, name, rate, hoursPerLesson, lessonsPerWeek;
        ImageView competencyCircle;

        TutorDiscoverViewHolder(@NonNull View itemView) {
            super(itemView);
            constraint_td = itemView.findViewById(R.id.constraint_tutor_discovery);
            subject = itemView.findViewById(R.id.tv_subject_td);
            name = itemView.findViewById(R.id.tv_user_td);
            competency = itemView.findViewById(R.id.tv_level_td);
            competencyCircle = itemView.findViewById(R.id.icon_level_td);
            rate = itemView.findViewById(R.id.tv_rate_td);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_td);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_td);

        }
    }
}
