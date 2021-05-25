package com.example.insight.view.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.insight.R;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeActiveFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_active, container, false);
        recyclerView = root.findViewById(R.id.rv_home_active);

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
