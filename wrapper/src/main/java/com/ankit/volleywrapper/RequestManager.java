package com.ankit.volleywrapper;



import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 14/7/16.
 */
public abstract class RequestManager {

    public abstract void init(Context context);
    public RequestManager(Context context){

    }
    protected abstract void  makeJsonRequest(int method, String requestUrl, JSONObject jsonObject,
                                             IRequestListener<JSONObject> onJsonRequestFinishedListener,
                                             HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);

    protected abstract void makeStringRequest(int method, String url, String stringParams,
                           IRequestListener<String> onStringRequestFinishedListener,
                           HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);
}
