/**
 * 
 */
package com.ankit.volleywrapper;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequest<T> {

	void onRequestSuccess(T response);

	void onNetworkResponse(NetworkResponse response);

	void onRequestErrorCode(int errorCode);


}
