package com.ankitagrawal.wrapper;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 14/7/16.
 */
public abstract class RequestHandler {
    protected static final int CONNECT_TIMEOUT_MILLIS = 10000;
    protected static final int MAX_RETRIES = 2;
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    protected static final String TAG="RequestHandler" ;
    protected RetryPolicy mRetryPolicy;
    public RequestHandler() {
        this(new DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }
    public RequestHandler(RetryPolicy retryPolicy) {
        if(mRetryPolicy!=null) {
            mRetryPolicy = retryPolicy;
        }else{
            mRetryPolicy = new DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,MAX_RETRIES,
                    DEFAULT_BACKOFF_MULT);
        }
    }
    /**
     * Created by ankitagrawal on 6/7/16. yay
     */
    protected interface IRequest<T> {

        void onRequestSuccess(T response);

        void onRequestErrorCode(int errorCode);


    }

    protected abstract boolean canHandleRequest(String url, int method);

    protected abstract void makeJsonRequest(int method, String requestUrl, JSONObject params,
                                            IRequest<Response<JSONObject>> onRequestFinishedListener,
                                            HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);


    protected abstract void makeStringRequest(int method, String requestUrl, String params,
                                              IRequest<Response<String>> onRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);

    protected abstract void makeJsonArrayRequest(int method, String requestUrl, JSONObject params,
                                              IRequest<Response<JSONArray>> onRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);
/*    protected abstract <T> void makeRequest(int method, String url, String stringParams,
                                              IRequest<Response<T>> onStringRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);*/
    protected abstract void cancelPendingRequests(String tag) ;

    protected abstract void cancelAllRequests() ;
}