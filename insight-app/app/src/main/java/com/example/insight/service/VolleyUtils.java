package com.example.insight.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for making HTTP API calls
 */
public class VolleyUtils {
    private static final String API_KEY = "hdHJkzGjJhkDwn7Rpm6pmHMpL9PCzB";
    private static final String BASE_URL = "https://fit3077.com/api/v2/";

    /**
     * Helper method to create and add a new JSONObjectRequest instance to the Volley Queue
     * Use this method if the expected HTTP response is a JSON Object
     */
    public static void makeJsonObjectRequest(Context context, String urlEndpoint,  int requestType,
                                             JSONObject body, final VolleyResponseListener listener) {
        final String requestBody = body.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            requestType,
            BASE_URL + urlEndpoint,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.onResponse(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMsg = "Unknown error";
                    // Get response body and encode in UTF-8
                    if(error.networkResponse != null && error.networkResponse.data!=null) {
                        String response = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            errorMsg = responseJSON.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onError(errorMsg);
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", API_KEY);
                return params;
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        // Add request to VolleyController Queue
        VolleyController.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Helper method to create and add a new JSONArrayRequest instance to the Volley Queue
     * Use this method if the expected HTTP response is a JSON Array of Objects
     */
    public static void makeJSONArrayRequest(Context context, String urlEndpoint, int requestType,
        JSONObject body, final VolleyResponseListener listener) {
        final String requestBody = body.toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            requestType,
            BASE_URL + urlEndpoint,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    listener.onResponse(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String body = "Unknown error";
                    // Get response body and encode in UTF-8
                    if(error.networkResponse != null && error.networkResponse.data!=null) {
                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    }
                    listener.onError(body);
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", API_KEY);
                return params;
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }


        };

        // Add request to VolleyController Queue
        VolleyController.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }
}
