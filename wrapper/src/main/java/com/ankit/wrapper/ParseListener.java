package com.ankit.wrapper;

/**
 * Created by ankitagrawal on 26/7/16.
 */
public interface ParseListener<T> {
    <J> J onParse(T response);
}
