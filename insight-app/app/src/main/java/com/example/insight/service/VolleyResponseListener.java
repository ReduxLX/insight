package com.example.insight.service;

public interface VolleyResponseListener {
    void onResponse(Object response);

    void onError(String message);
}
