package com.ankitagrawal.converters;

import com.ankitagrawal.wrapper.Converter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public class GsonConverter implements Converter {
    Gson gson = new Gson();
    @Override
    public <J> J convert(Object value, final Class<J> aClass) throws IOException {

        if(value instanceof  JSONObject){
            return gson.fromJson(value.toString(), aClass);
        }else  if(value instanceof  JSONArray){
        Type listType = new TypeToken<ArrayList<J>>(){}.getType();
        return gson.fromJson(value.toString(), listType);
        }else{
            return gson.fromJson((String)value, aClass);
        }

    }

    @Override
    public boolean canConvert(Object value) {
        if(value instanceof JSONObject){
            return true;
        }else if(value instanceof String) {
            try {
                new JSONObject((String) value);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                new JSONArray((String) value);
                return true;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
        return false;
    }
}
