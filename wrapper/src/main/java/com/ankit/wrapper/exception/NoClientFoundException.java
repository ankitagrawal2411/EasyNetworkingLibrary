package com.ankit.wrapper.exception;

/**
 * Created by ankitagrawal on 1/26/17.
 */
public class NoClientFoundException extends IllegalStateException {

    public NoClientFoundException(String msg) {
        super(msg);
    }
}
