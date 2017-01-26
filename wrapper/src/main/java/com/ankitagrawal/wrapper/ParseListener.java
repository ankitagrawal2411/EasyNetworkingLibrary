package com.ankitagrawal.wrapper;

/**
 * Created by ankitagrawal on 26/7/16.
 */
public interface ParseListener<T> {
    <J> J onParse(T response);
}
