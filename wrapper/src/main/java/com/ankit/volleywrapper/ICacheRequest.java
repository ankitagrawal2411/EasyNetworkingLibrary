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
     void makeJsonRequest(Context context, int method, String URL, JSONObject jsonObject,
                          HashMap<String, String> header, IRequestListener<JSONObject> jsonRequestFinishedListener, RetryPolicy retryPolicy, String reqTAG, final int memoryPolicy, final int networkPolicy);
     void makeStringRequest(final Context context, int method, final String URL, String jsonObject, final HashMap<String, String> header, final IRequestListener<String> jsonRequestFinishedListener, final RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy);
}
