/**
 * 
 */
package com.ankit.volleywrapper;

import com.android.volley.VolleyError;

/**
 * @author ankit.agrawal
 *
 */
public interface IRequestListener<T> {

     Object onRequestSuccess(T response);

     void onParseSuccess(Object response);

     void onRequestErrorCode(VolleyError volleyError);

}
