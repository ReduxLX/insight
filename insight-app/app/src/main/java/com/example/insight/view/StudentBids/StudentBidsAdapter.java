package com.example.insight.view.StudentBids;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.JWTModel;
import com.example.insight.view.BidsFragmentDirections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter class used to populate the student bids recycler view
 * which is responsible for displaying student's active bids
 */
public class StudentBidsAdapter extends RecyclerView.Adapter<StudentBidsAdapter.StudentBidsViewHolder> {

    private Context context;
    private NavController navController;

    private ArrayList<BidModel> bidArray = new ArrayList<>();

    StudentBidsAdapter(Context ctx, NavController navController, JSONArray bids){
        context = ctx;
        this.navController = navController;
        SharedPreferences prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        try {
            for (int j=0; j < bids.length(); j++){
                JSONObject bidObj = bids.getJSONObject(j);
                BidModel bid = new BidModel(bidObj);
                Log.i("print", "date: "+bid.getDateClosedDown());
                if(bid.getInitiator().getId().equals(jwtModel.getId()) &&
                        bid.getDateClosedDown().equals("null"))
                bidArray.add(bid);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public StudentBidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_student_bids, parent,false);

        return new StudentBidsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentBidsViewHolder holder, int position) {
        final BidModel bid = bidArray.get(position);
        BidOfferModel studentOffer = bid.getAdditionalInfo().getStudentOffer();

        holder.subject.setText(bid.getSubjectAndCompetencyStr());
        holder.totalTutorBids.setText(bid.getAdditionalInfo().getTutorBidsStr());
        holder.rate.setText(studentOffer.getRateStr());
        holder.hoursPerLesson.setText(studentOffer.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(studentOffer.getLessonsPerWeekStr());

        holder.constraint_sb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("print", "WHYYYY "+bid.getId());
                NavDirections navAction = BidsFragmentDirections.actionBidsFragmentToStudentCounterBidsFragment(
                        bid.getId());
                navController.navigate(navAction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bidArray.size();
    }

    static class StudentBidsViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraint_sb;
        TextView subject, rate, hoursPerLesson, lessonsPerWeek, totalTutorBids;

        StudentBidsViewHolder(@NonNull View itemView) {
            super(itemView);
            constraint_sb = itemView.findViewById(R.id.constraint_sb);
            subject = itemView.findViewById(R.id.tv_subject_sb);
            rate = itemView.findViewById(R.id.tv_rate_sb);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_sb);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_sb);
            totalTutorBids = itemView.findViewById(R.id.tv_total_tutor_bids_sb);

        }
    }
}
