package com.example.insight.view.TutorDiscover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
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
import com.example.insight.view.DiscoverFragmentDirections;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Fragment class for the Tutor Discovery screen where tutors can discover students
 */
public class TutorDiscoverFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavController navController;

    public TutorDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tutor_discover, container, false);

        recyclerView = root.findViewById(R.id.rv_tutor_discover);
        navController = NavHostFragment.findNavController(this);

        getAllBids();

        return root;
    }

    private void getAllBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {

                JSONArray bids = (JSONArray) response;
                Log.i("print", "TutorDiscoverFragment: "+"Get All Bids Success");
                TutorDiscoverAdapter adapter = new TutorDiscoverAdapter(getActivity(),
                        navController, bids);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            @Override
            public void onError(String message) {
                Log.i("print", "TutorDiscoverFragment: "+"Get All Bids Failed "+message);
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        };

        VolleyUtils.makeJSONArrayRequest(
            getActivity(),
            "bid",
            Request.Method.GET,
            new JSONObject(),
            listener
        );
    }
}
