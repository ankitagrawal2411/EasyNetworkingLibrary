/**
 * 
 */
package com.ankit.volleywrapper;

import android.content.Context;

import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface ICacheRequest {
     void makeJsonPostRequest(Context context, String URL, JSONObject jsonObject, HashMap<String, String> header, IRequestListener<JSONObject> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
     void makeJsonGetRequest(Context context, String URL, IRequestListener<JSONObject> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
     void makeStringGetRequest(Context context, String URL, IRequestListener<String> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
}
