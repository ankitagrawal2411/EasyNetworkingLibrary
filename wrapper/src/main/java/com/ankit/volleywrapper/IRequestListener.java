/**
 * 
 */
package com.ankit.volleywrapper;


import com.android.volley.VolleyError;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequestListener<T> {

     Object onRequestSuccess(T response);

     void onParseSuccess(Object response);

     T onNetworkResponse(NetworkResponse response);

     void onRequestErrorCode(VolleyError volleyError);

}
