/**
 *
 */
package com.ankit.volleywrapper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class RequestHandler implements IRequest {

	private static RequestHandler instance;

	private  RequestQueue mRequestQueue;

	public static final int timeout = 20000;
	public static final int RETRY = 1;

	private static final String TAG = "RequestHandler";

	/**
	 * Returns the instance of this singleton.
	 */
	protected static RequestHandler getInstance(Context context) {
		if (instance == null) {
			instance = new RequestHandler(context);
		}
		return instance;
	}


	public RequestHandler(Context context){
		mRequestQueue = Volley.newRequestQueue(context);

	}



	@Override
	public void makeJsonRequest(final int method, final String requestUrl,
                                final JSONObject jsonObject,
                                final IRequestListener<JSONObject> iRequestListener,
                                final HashMap<String, String> requestHeader,
                                RetryPolicy retryPolicy, final String reqTAG) {
         if(retryPolicy==null) {
			 retryPolicy= new DefaultRetryPolicy(timeout,
					 RETRY,
					 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		 }

		Log.d(TAG, reqTAG + " request Url: " + requestUrl);
		Log.d(TAG, reqTAG + " request Json Params: " + jsonObject);
		Log.d(TAG, reqTAG + " request Header: " + requestHeader);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,
				requestUrl, jsonObject, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {

						Log.d(TAG, "onResponse jsonObject: "
								+ jsonObject);

						if (jsonObject != null) {

							iRequestListener.onRequestSuccess(jsonObject);

						} else {

							iRequestListener.onRequestErrorCode(null);

						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						String error = "";
						if(volleyError!=null){
							error = volleyError.getMessage();
						}
						Log.v(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
						iRequestListener.onRequestErrorCode(volleyError);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				if (requestHeader != null) {
					return requestHeader;
				} else {
					return super.getHeaders();
				}

			}
			@Override
			protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

				if(response!=null) {
					JSONObject json =iRequestListener.onNetworkResponse(response);
					return Response.success(
							json, HttpHeaderParser.parseCacheHeaders(response));
				}
				else{
					return Response.error(new ParseError());
				}

			}
		};

		jsonObjectRequest.setRetryPolicy(retryPolicy);
		jsonObjectRequest.setShouldCache(false);

		if (reqTAG!=null && reqTAG.trim().length()>0) {
			Log.d("RequestHandler", "Tag is:" + reqTAG);
			addToRequestQueue(jsonObjectRequest, reqTAG);

		} else {
			Log.d("RequestHandler", "Tag is:" + RequestHandler.TAG);

			addToRequestQueue(jsonObjectRequest, RequestHandler.TAG);
		}
	}

	@Override
	public void makeStringRequest(final int method, final String url,
                                  final String stringParams,
                                  final IRequestListener<String> iRequestListener,
                                  final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {

		RetryPolicy retry = new DefaultRetryPolicy(timeout,
				RETRY,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

		/*if (retryPolicy != null) {
			retry = retryPolicy;
		}*/

		Log.d(TAG, reqTAG + " request Url: " + url);
		Log.d(TAG, reqTAG + " request String Params: " + stringParams);
		Log.d(TAG, reqTAG + " request Header: " + requestHeader);

		StringRequest objStringRequest = new StringRequest(method, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {

						Log.d(TAG, "onResponse String response: " + response);

						if (response != null) {

							iRequestListener.onRequestSuccess(response);
						} else {
							iRequestListener.onRequestErrorCode(null);
						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
							iRequestListener.onRequestErrorCode(volleyError);
						String error = "";
						if(volleyError!=null){
							error = volleyError.getMessage();
						}

						Log.v(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				if (stringParams != null) {
					HashMap<String, String> mParams = new HashMap<>();
					mParams.put("key", stringParams);
					return mParams;
				} else {
					return super.getParams();
				}
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {

				if (requestHeader != null) {
					return requestHeader;
				} else {
					return super.getHeaders();
				}
			}
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {

					if(response!=null) {
						String json =iRequestListener.onNetworkResponse(response);
						return Response.success(
								json, HttpHeaderParser.parseCacheHeaders(response));
					}
				else{
					return Response.error(new ParseError());
				}

			}
		};

		objStringRequest.setRetryPolicy(retry);
		objStringRequest.setShouldCache(false);
		
		
		if (reqTAG!=null && reqTAG.trim().length()>0) {
			Log.d(TAG, "Tag is:" + reqTAG);
			addToRequestQueue(objStringRequest, reqTAG);

		} else {
			Log.d(TAG, "Tag is:" + RequestHandler.TAG);

			addToRequestQueue(objStringRequest, RequestHandler.TAG);
		}
		
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		// req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		Log.d(TAG, "addToRequestQueue");
		req.setTag(tag);
		mRequestQueue.add(req);
	}


	public void cancelPendingRequests(String tag) {
		if (mRequestQueue != null) {
			Log.d(TAG, "Cancelling  request tag ::" + tag);
			Log.d(TAG, "Rquest Queue : " + mRequestQueue);
			mRequestQueue.cancelAll(tag);
			Log.d(TAG, "Rquest Queue : " + mRequestQueue);
		}
	}



}
