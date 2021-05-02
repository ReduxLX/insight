package com.example.insight.view.StudentBids;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Fragment class for the Student Bids screen
 */
public class StudentBidsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavController navController;

    public StudentBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_student_bids, container, false);

        recyclerView = root.findViewById(R.id.rv_student_bids);
        navController = NavHostFragment.findNavController(this);

        // Render current student's bids as cards
        getStudentBids();

        return root;
    }

    private void getStudentBids(){
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                Log.i("print", "StudentBidsFragment: "+"Get Student Bids Success");
                JSONArray bids = (JSONArray) response;
                StudentBidsAdapter adapter = new StudentBidsAdapter(getActivity(), navController, bids);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            @Override
            public void onError(String message) {
                Log.i("print", "StudentBidsFragment: "+"Get Student Bids Failed "+message);
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
