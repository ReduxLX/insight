package com.example.insight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.insight.model.JWTModel;
import com.example.insight.service.VolleyResponseListener;
import com.example.insight.service.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences prefs;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("com.example.insight", Context.MODE_PRIVATE);

        checkIfLoggedIn();

        editTextUsername = findViewById(R.id.pt_username_login);
        editTextPassword = findViewById(R.id.pt_password_login);
        buttonLogin = findViewById(R.id.button_login);

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    login();
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                login();
            }
        });
    }

    // Check if JWT exists in SharedPreferences AND if it has expired
    private void checkIfLoggedIn(){
        String jwt = prefs.getString("jwt", null);
        Log.i("print", "Login Activity: "+"Shared pref JWT "+ jwt);
        if(!TextUtils.isEmpty(jwt)){
            JWTModel jwtModel = new JWTModel(jwt);
            Log.i("print", "Login Activity: "+"JWT Expired? "+ jwtModel.isJWTExpired());
            if(!jwtModel.isJWTExpired()){

                Log.i("print", "Login Activity: "+jwtModel.getUserRole()+" logged in, redirect to Main activity");
                redirectToMainActivity();
            }
        }
    }

    private void redirectToMainActivity(){
        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
        // Start the MainActivity and finish the current activity to prevent going back
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: Tutor: iamthewei, kevink Student: mbrown123
    private void login(){
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        Log.i("print", "LoginActivity: "+username+ " "+password);
        if(TextUtils.isEmpty(username)){
            Toast.makeText(getApplicationContext(), "Please enter your username",
                    Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Please enter your password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("userName", username);
            jsonBody.put("password", password);

            VolleyResponseListener listener = new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    Log.i("print", "Login Activity: "+"Login Success");
                    JSONObject loginResponse = (JSONObject) response;
                    try{
                        // Get jwt string from response and store it in SharedPreferences
                        String jwt = loginResponse.getString("jwt");
                        prefs.edit().putString("jwt", jwt).apply();

                        redirectToMainActivity();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(String message) {
                    Log.i("print", "Login Activity: "+"Login Failed "+message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            };

            VolleyUtils.makeJsonObjectRequest(
                this,
                "user/login?jwt=true",
                Request.Method.POST,
                jsonBody,
                listener
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
