package com.example.insight.view.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.insight.model.SubjectModel;
import com.example.insight.model.UserModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;
import com.example.insight.view.StudentBids.StudentBidsAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Fragment class for the Home screen
 */
public class HomeFragment extends Fragment {
    private SharedPreferences prefs;
    private ViewPager viewPager;
    private TabLayout tabs;

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

        prefs = getActivity().getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        String jwt = prefs.getString("jwt", null);
        JWTModel jwtModel = new JWTModel(jwt);
        String userFullName = jwtModel.getFullName();
        TextView tvName = root.findViewById(R.id.tv_name_home);
        tvName.setText(userFullName);

        HomeSectionsPagerAdapter pagerAdapter = new HomeSectionsPagerAdapter(getActivity().getSupportFragmentManager());
        pagerAdapter.addFragment(new HomeActiveFragment(), "Active");
        pagerAdapter.addFragment(new HomePendingFragment(), "Pending");
        pagerAdapter.addFragment(new HomeExpiredFragment(), "Expired");
        viewPager.setAdapter(pagerAdapter);

        tabs = root.findViewById(R.id.tabs_home);
        tabs.setupWithViewPager(viewPager);

        return root;
    }

}
