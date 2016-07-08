/**
 * 
 */
package com.ankit.volleywrapper;

import com.android.volley.VolleyError;

/**
 * @author suyash.bhagwat
 *
 */
public interface IRequestListener<T> {

     Object onRequestSuccess(T response);

     void onParseSuccess(Object response);

     void onRequestErrorCode(VolleyError volleyError);

}
