package com.example.frontend;

import com.android.volley.VolleyError;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response);
    void onError(VolleyError error);
}
