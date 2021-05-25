package com.example.insight.view.TutorBids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Bid.BidModel;
import com.example.insight.model.Bid.BidOfferModel;
import com.example.insight.model.Bid.TutorBidModel;
import com.example.insight.view.BidsFragmentDirections;

import java.util.ArrayList;

/**
 * Adapter class used to populate the tutor bids recycler view
 * which is responsible for displaying the bids where the tutor is involved in (sent a counter bid)
 */
public class TutorBidsAdapter extends RecyclerView.Adapter<TutorBidsAdapter.TutorBidsViewHolder> {

    private Context context;
    private NavController navController;

    private ArrayList<BidModel> bidArray;
    private ArrayList<TutorBidModel> tutorBidArray;

    // bidArray contains bids that tutor is involved in (send a bid)
    TutorBidsAdapter(Context ctx, NavController navController, ArrayList<BidModel> bidArray,
                     ArrayList<TutorBidModel> tutorBidArray){
        context = ctx;
        this.navController = navController;
        this.bidArray = bidArray;
        this.tutorBidArray = tutorBidArray;
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
        final TutorBidModel tutorBid = tutorBidArray.get(position);
        BidOfferModel tutorOffer = tutorBid.getTutorOffer();
        holder.name.setText(studentBid.getInitiator().getFullName());
        holder.subject.setText(studentBid.getSubjectAndCompetencyStr());
        holder.rate.setText(tutorOffer.getRateStr());
        holder.freeClasses.setText(tutorOffer.getFreeClassesStr());
        holder.hoursPerLesson.setText(tutorOffer.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(tutorOffer.getLessonsPerWeekStr());

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

    @Override
    public int getItemCount() {
        return tutorBidArray.size();
    }

    static class TutorBidsViewHolder extends RecyclerView.ViewHolder {
        TextView subject, name, rate, hoursPerLesson, lessonsPerWeek, freeClasses;
        Button button_message;

        TutorBidsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_tb);
            subject = itemView.findViewById(R.id.tv_subject_tb);
            rate = itemView.findViewById(R.id.tv_rate_tb);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_tb);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_tb);
            freeClasses = itemView.findViewById(R.id.tv_free_tb);
            button_message = itemView.findViewById(R.id.button_message_tb);
        }
    }
}
