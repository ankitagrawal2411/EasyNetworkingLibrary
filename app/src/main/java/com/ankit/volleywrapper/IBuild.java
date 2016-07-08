package com.ankit.volleywrapper;

import com.android.volley.RetryPolicy;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16.
 */
public interface IBuild {
        RequestBuilder build();

        IBuild withShouldCache(boolean val);

        IBuild withRetryPolicy(RetryPolicy val);

        IBuild withReqTAG(String val);

        IBuild withRequestHeader(HashMap<String, String> val);

        IBuild withIFcRequestListener(IRequestListener<JSONObject> val);

        IBuild withJsonObject(JSONObject val);

        IBuild withRequestUrl(String val);

        IBuild withPostRequest(JSONObject val);

        IBuild withGetRequest();

        IBuild withMethod(int val);
}
