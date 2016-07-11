/**
 * 
 */
package com.ankit.volleywrapper;


import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequest {

	  void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject,
						   IRequestListener<JSONObject> onJsonRequestFinishedListener,
						   HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);

	     void makeStringRequest(int method, String url, String stringParams,
								IRequestListener<String> onStringRequestFinishedListener,
								HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);


}
