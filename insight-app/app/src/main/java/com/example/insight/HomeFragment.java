package com.example.insight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tvUsername;
    private static final String myApiKey = "CdmgdLhhGK7qbkhNbttJrdmpWtR7Pz";
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupVolley(view);
        return view;
    }

    private void setupVolley(View view){
        RequestQueue queue = VolleyController.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        tvUsername = view.findViewById(R.id.tvUsername);
        String url = "https://fit3077.com/api/v1/contract";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for (int i=0 ; i < response.length(); i++) {
                                JSONObject contract = response.getJSONObject(i);
                                String contractId = contract.getString("id");
                                Log.i("console_log", contractId);
                                tvUsername.append(contractId +"\n\n");
                            }} catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", myApiKey);
                return params;
            }
        };

        queue.add(request);
    }
}
