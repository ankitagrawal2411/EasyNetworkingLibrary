/**
 * 
 */
package com.ankit.wrapper;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequest<T> {

	void onRequestSuccess(T response);

	void onRequestErrorCode(int errorCode);


}
