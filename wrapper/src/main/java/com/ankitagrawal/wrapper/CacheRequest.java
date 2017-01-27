/**
 * 
 */
package com.ankitagrawal.wrapper;

import android.content.Context;



import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
 interface CacheRequest {
    <T,F> void makeJsonRequest(Context context,Client client, int method, String URL, JSONObject jsonObject,
                             HashMap<String, String> header, RetryPolicy retryPolicy, String reqTAG, final int memoryPolicy, final int networkPolicy, long cacheTime, BaseResponseListener<T, F> responseListener, int logLevel, boolean mCancel, Class<F> aClass);
    <T,F> void makeJsonArrayRequest(Context context,Client client, int method, String URL, JSONObject jsonObject,
                               HashMap<String, String> header, RetryPolicy retryPolicy, String reqTAG, final int memoryPolicy, final int networkPolicy, long cacheTime, BaseResponseListener<T, F> responseListener, int logLevel, boolean mCancel, Class<F> aClass);
    <T,F>  void makeStringRequest(final Context context,Client client, int method, final String URL, JSONObject jsonObject, final HashMap<String, String> header, final RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, long cacheTime, BaseResponseListener<T, F> jsonRequestFinishedListener, int logLevel, boolean cancel, final Class<F> aClass);
 /*   <T,F>  void makeRequest(final Context context, int method, final String URL, String jsonObject, final HashMap<String, String> header, final RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy,long cacheTime, BaseResponseListener<T,F> jsonRequestFinishedListener,final Class<F> aClass);*/

}
