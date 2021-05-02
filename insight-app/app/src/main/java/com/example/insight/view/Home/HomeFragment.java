package com.example.insight.view.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.model.Contract.ContractModel;
import com.example.insight.model.JWTModel;
import com.example.insight.model.SubjectModel;
import com.example.insight.model.UserModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;
import com.example.insight.view.StudentBids.StudentBidsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Fragment class for the Home screen
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private SharedPreferences prefs;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.rv_home);
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String userFullName = jwtModel.getFullName();
        TextView tvName = root.findViewById(R.id.tv_name_home);
        tvName.setText(userFullName);

        getContracts();

        return root;
    }

    private void getContracts(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "HomeFragment: "+"Get Contracts Success");
                JSONArray contracts = (JSONArray) response;
                HomeAdapter adapter = new HomeAdapter(getActivity(), contracts);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
            @Override
            public void onError(String message) {
                Log.i("print", "HomeFragment: "+"Get Contracts Failed "+message);
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
