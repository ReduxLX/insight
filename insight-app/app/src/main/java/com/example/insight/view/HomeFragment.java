package com.example.insight.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.insight.R;
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
        getUser("9bf9d775-8c70-4b26-ad1c-4120c2abf446");
//        addUser("Alfons", "Fernaldy", "redux",
////                "password", true, false);
//        deleteUser("13dc3d5d-ff5c-4d1e-935b-e771db845b41");
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
                        String contractId = contract.getString("id");
                        Log.i("console_log", contractId);
                        tvUsername.append(contractId +"\n\n");
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("console_log", message);
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity().getApplicationContext(),
            "contract",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }

    private void getUser(String userId){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONObject singleUser = (JSONObject) response;
                Log.i("console_log", singleUser.toString());
                try{
                    tvUsername.setText(singleUser.getString("givenName"));
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                Log.i("console_log", message);
            }
        };
        VolleyUtils.makeJsonObjectRequest(
            getActivity().getApplicationContext(),
            "user/"+userId,
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }

    private void addUser(String givenName, String familyName, String userName, String password,
                         Boolean isStudent, Boolean isTutor){
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("givenName", givenName);
            jsonBody.put("familyName", familyName);
            jsonBody.put("userName", userName);
            jsonBody.put("password", password);
            jsonBody.put("isStudent", isStudent);
            jsonBody.put("isTutor", isTutor);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("console_log", "User added successfully");
            }
            @Override
            public void onError(String message) {
                Log.i("console_log", message);
            }
        };

        VolleyUtils.makeJsonObjectRequest(
            getActivity().getApplicationContext(),
            "user",
            Request.Method.POST,
            jsonBody,
            listener
        );
    }

    private void deleteUser(String userId){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("console_log", "User deleted successfully");
            }
            @Override
            public void onError(String message) {
                Log.i("console_log", message);
            }
        };
        VolleyUtils.makeJsonObjectRequest(
                getActivity().getApplicationContext(),
                "user/"+userId,
                Request.Method.DELETE,
                new JSONObject(),
                listener
        );
    }
}
