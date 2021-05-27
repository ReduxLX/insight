package com.example.insight.view.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

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
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;
import com.example.insight.view.Chat.ChatFragmentArgs;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Fragment class for the Home screen
 */
public class HomeFragment extends Fragment {
    private NavController navController;
    private ViewPager viewPager;

    private HomeContractAdapter activeAdapter;
    private HomeContractAdapter pendingAdapter;
    private HomeContractAdapter expiredAdapter;
    private ArrayList<ContractModel> contractArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = root.findViewById(R.id.view_pager_home);
        navController = NavHostFragment.findNavController(this);

        // Get the current user's name from jwt
        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String userFullName = jwtModel.getFullName();
        TextView tvName = root.findViewById(R.id.tv_name_home);
        tvName.setText(userFullName);

        // Initialize Recycler view adapters for the 3 tab fragments
        HomeSectionsPagerAdapter pagerAdapter = new HomeSectionsPagerAdapter(getChildFragmentManager());

        activeAdapter = new HomeContractAdapter(getActivity(), navController, contractArray, 0, HomeFragment.this);
        pagerAdapter.addFragment(new HomeContractFragment(activeAdapter, 0), "Active");

        pendingAdapter = new HomeContractAdapter(getActivity(), navController, contractArray, 1, HomeFragment.this);
        pagerAdapter.addFragment(new HomeContractFragment(pendingAdapter, 1), "Pending");

        expiredAdapter = new HomeContractAdapter(getActivity(), navController, contractArray, 2, HomeFragment.this);
        pagerAdapter.addFragment(new HomeContractFragment(expiredAdapter, 2), "Expired");


        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs_home);
        tabs.setupWithViewPager(viewPager);

        // Get contracts
        getContracts();

        return root;
    }

    public void getContracts(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray contracts = (JSONArray) response;
                contractArray.clear();
                try {
                    for (int j=0; j < contracts.length(); j++){
                        JSONObject contractObj = contracts.getJSONObject(j);
                        ContractModel contract = new ContractModel(contractObj);
                        contractArray.add(contract);
                    }
                } catch (JSONException e){ e.printStackTrace(); }

                // Update the contractArray in each tab
                activeAdapter.updateData(contractArray);
                pendingAdapter.updateData(contractArray);
                expiredAdapter.updateData(contractArray);
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
