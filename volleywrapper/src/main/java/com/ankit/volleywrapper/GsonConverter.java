package com.ankit.volleywrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public class GsonConverter<F,T> implements Converter<F,T> {
    @Override
    public T convert(F value) throws IOException {
        return null;
    }

    @Override
    public boolean canConvert(F value) {
        if(value instanceof JSONObject){
            return true;
        }else if(value instanceof String) {
            try {
                 new JSONObject((String)value);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
