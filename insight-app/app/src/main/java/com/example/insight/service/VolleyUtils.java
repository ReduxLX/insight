package com.example.insight.service;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for making HTTP API calls
 */
public class VolleyUtils {
    private static final String API_KEY = "CdmgdLhhGK7qbkhNbttJrdmpWtR7Pz";
    private static final String BASE_URL = "https://fit3077.com/api/v1/";

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
                    listener.onError(error.toString());
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
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
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
                    listener.onError(error.toString());
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
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
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
