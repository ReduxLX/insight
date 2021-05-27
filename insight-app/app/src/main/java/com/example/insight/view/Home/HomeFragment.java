package com.example.insight.view.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.insight.MainActivity;
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
import java.util.Calendar;
import java.util.Date;


/**
 * Fragment class for the Home screen
 */
public class HomeFragment extends Fragment {
    private SharedPreferences prefs;
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
        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);
        navController = NavHostFragment.findNavController(this);
        viewPager = root.findViewById(R.id.view_pager_home);

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
                int closeToExpiryContracts = 0;
                contractArray.clear();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, 1);
                String jwt = prefs.getString("jwt", null);
                JWTModel jwtModel = new JWTModel(jwt);
                String userId = jwtModel.getId();

                Date dateAfterOneMonth = cal.getTime();
                Log.i("print", "Date After One Month: "+ dateAfterOneMonth.toString());
                try {
                    for (int j=0; j < contracts.length(); j++){
                        JSONObject contractObj = contracts.getJSONObject(j);
                        ContractModel contract = new ContractModel(contractObj);
                        contractArray.add(contract);
                        String firstPartyId = contract.getFirstParty().getId();
                        String secondPartyId = contract.getSecondParty().getId();
                        boolean isUserInvolved = firstPartyId.equals(userId) || secondPartyId.equals(userId);
                        if(isUserInvolved && dateAfterOneMonth.after(contract.getExpiryDateObj())){
                            Log.i("print", "Close Expiry: "+ contract.getExpiryDateObj().toString());
                            closeToExpiryContracts++;
                        }
                    }
                } catch (JSONException e){ e.printStackTrace(); }
                // Show dialog if there is 1 or more close to expire contracts
                MainActivity mainActivity = (MainActivity)getActivity();
                if(!mainActivity.hasShowedLoginWarning() && closeToExpiryContracts > 0){
                    showWarning();
                    mainActivity.setShowedLoginWarning(true);
                }
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

    public void showWarning(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("You have one or more contracts that are about to expire in 1 month");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

}
