package com.example.insight.view.Home;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insight.R;
import com.example.insight.model.Contract.ContractModel;
import com.example.insight.model.Contract.ContractTermsModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.UserModel;
import com.example.insight.view.DiscoverFragmentDirections;

import java.util.ArrayList;

/**
 * Adapter class used to populate the contracts recycler view in the home screen
 * which is responsible for displaying the user's ongoing contracts
 */
public class HomeContractAdapter extends RecyclerView.Adapter<HomeContractAdapter.HomeViewHolder> {

    private Context context;
    private SharedPreferences prefs;
    private int contractType;
    private NavController navController;

    private ArrayList<ContractModel> contractArray = new ArrayList<>();

    public HomeContractAdapter(Context ctx, NavController navController, ArrayList<ContractModel> contracts, int contractType){
        context = ctx;
        prefs = ctx.getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        this.navController = navController;
        this.contractType = contractType;

        updateData(contracts);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_contract, parent,false);

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

        // Pending Contract button -> Signs contract
        if(contractType == 1){
            holder.buttonAction.setVisibility(View.VISIBLE);
            holder.buttonAction.setText(R.string.sign_contract);
            holder.buttonAction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
//                    createContract(tutorBid);
                    Toast.makeText(context, "Pending Action", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(contractType == 2){
            holder.buttonAction.setVisibility(View.VISIBLE);
            holder.buttonAction.setText(R.string.renew_contract);
            holder.buttonAction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i("console", "HomeActiveAdapter: Contract Id "+contract.getId());
                    NavDirections navAction = HomeFragmentDirections.actionHomeFragmentToContractRenewFragment(
                            contract.getId()
                    );
                    navController.navigate(navAction);
                    Toast.makeText(context, "Expired Action", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return contractArray.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView subject, name, rate, hoursPerLesson, lessonsPerWeek, freeLessons, expiryDate;
        ImageView competencyCircle;
        Button buttonAction;

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
            buttonAction = itemView.findViewById(R.id.button_action_home);
        }
    }

    public void updateData(ArrayList<ContractModel> contracts){
        for (ContractModel contract: contracts){
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
        notifyDataSetChanged();
    }

}
