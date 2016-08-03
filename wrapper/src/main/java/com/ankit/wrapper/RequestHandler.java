package com.ankit.wrapper;


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
        mRetryPolicy = new DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,MAX_RETRIES,
                DEFAULT_BACKOFF_MULT);
    }
    public RequestHandler(RetryPolicy retryPolicy) {
        mRetryPolicy = retryPolicy;
    }
    /**
     * Created by ankitagrawal on 6/7/16. yay
     */
    protected interface IRequest<T> {

        void onRequestSuccess(T response);

        void onRequestErrorCode(int errorCode);


    }

    protected abstract boolean canHandleRequest(String url, int method);

    protected abstract void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject,
                                            IRequest<Response<JSONObject>> onJsonRequestFinishedListener,
                                            HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);


    protected abstract void makeStringRequest(int method, String url, String stringParams,
                                              IRequest<Response<String>> onStringRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);
/*    protected abstract <T> void makeRequest(int method, String url, String stringParams,
                                              IRequest<Response<T>> onStringRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);*/
    protected abstract void cancelPendingRequests(String tag) ;

    protected abstract void cancelAllRequests() ;
}