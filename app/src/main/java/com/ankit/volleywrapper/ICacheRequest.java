/**
 * 
 */
package com.ankit.volleywrapper;

import android.content.Context;

import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author darshan.zite
 *
 */
public interface ICacheRequest {
    public void makeJsonPostRequest(Context context, String URL, JSONObject jsonObject, HashMap<String, String> header, IRequestListener<JSONObject> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
    public void makeJsonGetRequest(Context context, String URL, IRequestListener<JSONObject> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
    public void makeStringGetRequest(Context context, String URL, IRequestListener<String> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG);
}
