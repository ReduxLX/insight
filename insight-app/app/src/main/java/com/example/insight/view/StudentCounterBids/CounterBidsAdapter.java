package com.example.insight.view.StudentCounterBids;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;

public class CounterBidsAdapter extends RecyclerView.Adapter<CounterBidsAdapter.CounterBidViewHolder> {

    private String[] namesArray;
    private int[] ratesArray;
    private int[] hoursPerLessonArray;
    private Context context;
    

    public CounterBidsAdapter(Context ctx, String[] names, int[] rates, int[] hoursPerLesson){
        context = ctx;
        namesArray = names;
        ratesArray = rates;
        hoursPerLessonArray = hoursPerLesson;
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
       holder.name.setText(namesArray[position]);
       holder.rate.setText(String.valueOf(ratesArray[position]));
       holder.hoursPerLesson.setText(String.valueOf(hoursPerLessonArray[position]));
    }

    @Override
    public int getItemCount() {
        return namesArray.length;
    }

    public class CounterBidViewHolder extends RecyclerView.ViewHolder {
        TextView name, rate, hoursPerLesson;
        public CounterBidViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_tcb);
            rate = itemView.findViewById(R.id.tv_rate_tvb);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_tcb);

        }
    }
}
