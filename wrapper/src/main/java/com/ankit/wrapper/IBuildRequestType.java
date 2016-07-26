package com.ankit.wrapper;

import android.support.annotation.Nullable;

import org.json.JSONObject;

/**
 * Created by ankitagrawal on 11/7/16.
 */
public interface IBuildRequestType {


    IBuildUrl post(@Nullable JSONObject val);

    IBuildUrl get();

    IBuildUrl method(int val);


}
