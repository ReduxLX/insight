package com.example.insight.view.Home;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Contract.ContractModel;
import com.example.insight.model.Contract.ContractTermsModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Adapter class used to populate the contracts recycler view in the home screen
 * which is responsible for displaying the user's ongoing contracts
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context context;
    private SharedPreferences prefs;

    private ArrayList<ContractModel> contractArray = new ArrayList<>();

    HomeAdapter(Context ctx, JSONArray contracts){
        context = ctx;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        try {
            for (int j=0; j < contracts.length(); j++){
                JSONObject contractObj = contracts.getJSONObject(j);
                ContractModel contract = new ContractModel(contractObj);

                String jwt = prefs.getString("jwt", null);
                JWTModel jwtModel = new JWTModel(jwt);
                String userId = jwtModel.getId();

                String firstPartyId = contract.getFirstParty().getId();
                String secondPartyId = contract.getSecondParty().getId();
                // Add contracts that involve the current user
                if(userId.equals(firstPartyId) || userId.equals(secondPartyId)){
                    contractArray.add(contract);
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_layout_home, parent,false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final ContractModel contract = contractArray.get(position);
        ContractTermsModel contractTerms = contract.getAdditionalInfo().getContractTerms();

        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String userId = jwtModel.getId();
        UserModel otherParty;
        String firstPartyId = contract.getFirstParty().getId();

        if(firstPartyId.equals(userId)){
            otherParty = contract.getSecondParty();
        }else{
            otherParty = contract.getFirstParty();
        }

        holder.subject.setText(contract.getSubjectAndCompetencyStr());
        holder.name.setText(otherParty.getFullName());
        holder.rate.setText(contractTerms.getRateStr());
        holder.hoursPerLesson.setText(contractTerms.getHoursPerLessonStr());
        holder.lessonsPerWeek.setText(contractTerms.getLessonsPerWeekStr());
        holder.expiryDate.setText(contract.getExpiryDateStr());
        holder.competencyCircle.setImageResource(contractTerms.getCompetencyResource());
    }

    @Override
    public int getItemCount() {
        return contractArray.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView subject, name, rate, hoursPerLesson, lessonsPerWeek, freeLessons, expiryDate;
        ImageView competencyCircle;

        HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.tv_subject_home);
            name = itemView.findViewById(R.id.tv_user_home);
            rate = itemView.findViewById(R.id.tv_rate_home);
            hoursPerLesson = itemView.findViewById(R.id.tv_duration_home);
            lessonsPerWeek = itemView.findViewById(R.id.tv_schedule_home);
            freeLessons = itemView.findViewById(R.id.tv_free_home);
            expiryDate = itemView.findViewById(R.id.tv_expiry_home);
            competencyCircle = itemView.findViewById(R.id.icon_level_home);

        }
    }
}
