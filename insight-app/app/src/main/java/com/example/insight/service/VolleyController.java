package com.example.insight.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Singleton class which encapsulates Volley's RequestQueue and other Volley functionality
 * Used to ensure that only one RequestQueue is used throughout the app's lifespan
 */
public class VolleyController {
    private static VolleyController instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private VolleyController(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyController getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyController(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
