package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.ContractModel;
import com.example.insight.model.SubjectModel;
import com.example.insight.model.UserModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tvUsername;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tvUsername = root.findViewById(R.id.tvUsername);

        getContracts();

        return root;
    }

    private void getContracts(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray contracts = (JSONArray) response;
                try{
                    for (int i=0 ; i < contracts.length(); i++) {
                        JSONObject contract = contracts.getJSONObject(i);
                        ContractModel contractModel = new ContractModel(contract);
                        UserModel firstParty = contractModel.getFirstParty();
                        UserModel secondParty = contractModel.getSecondParty();
                        SubjectModel subject = contractModel.getSubject();

                        // Filter contracts that have userID
                        // TODO: Replace placeholder userId
                        String userId = "1ed84243-50ac-437e-872e-39dbce04c5a4";
                        if(userId.equals(firstParty.getId()) || userId.equals(secondParty.getId())){
                            // TODO: Render each card using data in contractModel
                            tvUsername.append(contractModel.getId()+"\n\n");
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity(),
            "contract",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }
}
