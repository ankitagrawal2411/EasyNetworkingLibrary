package com.ankit.wrapper;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public interface IResponseListener<T,F> extends IParsedResponseListener<T,F> {

        F onRequestSuccess(T response);

}
