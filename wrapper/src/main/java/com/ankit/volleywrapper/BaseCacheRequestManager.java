package com.ankit.volleywrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class BaseCacheRequestManager {
    public SharedPreferences mSharedPreferences;
    private static BaseCacheRequestManager mInstance;

    public BaseCacheRequestManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static BaseCacheRequestManager getInstance(Context context){
        if(mInstance==null){
            mInstance = new BaseCacheRequestManager(context);
        }
        return mInstance;
    }

    public  void cacheResponse(String url,JSONObject response) {
        if(response!=null) {
            savePreference(url, response.toString());
        }
    }
    public  void cacheResponse(String url,String response) {
        if(response!=null) {
            savePreference(url, response);
        }
    }
    @SuppressLint("CommitPrefEdits")
    public  void invalidateCacheResponse(String url) {
        Log.e("preference", "remove " + url + " ");
        if(mSharedPreferences.contains(url)) {
            mSharedPreferences.edit().remove(url).commit();
        }
    }

    private  void savePreference(String url, String response) {
        if(response!=null) {
            Log.e("preference", "save " + url + " " + response);
            mSharedPreferences.edit().putString(url, response).apply();
        }else{
            Log.e("preference", "save " + url + " " + null);
            mSharedPreferences.edit().putString(url, null).apply();
        }

    }
    private String loadPreference(String url) {
        Log.e("preference","load "+url + " "+mSharedPreferences.getString(url, null));
       return mSharedPreferences.getString(url, null);
    }
    public String getCacheResponse(String url) {
        String obj = loadPreference(url);
        if(TextUtils.isEmpty(obj)){
            return null;
        }
        try {
            JSONObject jsonObject1 = new JSONObject(obj);
            long cacheTime =jsonObject1.optLong("timestamp");
            int hours =jsonObject1.optInt("hours");
           // if(cacheTime+hours<System.currentTimeMillis()) {
                return jsonObject1.optString("response");
          /*  }else{
                invalidateCacheResponse(url);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("CommitPrefEdits")
    public void clearCache() {
        mSharedPreferences.edit().clear().commit();
    }


    public void cacheResponse(ICache.CacheEntry cacheEntry) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("response",cacheEntry.getData());
            jsonObject.put("timestamp",cacheEntry.getTimeStampMillis());
            jsonObject.put("hours",cacheEntry.getCacheDuration());
            savePreference(cacheEntry.getUrl(), jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
