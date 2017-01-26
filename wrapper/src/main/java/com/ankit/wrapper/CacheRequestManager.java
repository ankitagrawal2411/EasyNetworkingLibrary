package com.ankit.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
  class CacheRequestManager {
    public SharedPreferences mSharedPreferences;
    private static CacheRequestManager mInstance;
    private static final String TAG="cache";

    public CacheRequestManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static CacheRequestManager getInstance(Context context){
        if(mInstance==null){
            mInstance = new CacheRequestManager(context);
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
        Logger.getInstance().d(TAG, "remove " + url + " ");
        if(mSharedPreferences.contains(url)) {
            mSharedPreferences.edit().remove(url).commit();
        }
    }

    private  void savePreference(String url, String response) {
        if(response!=null) {
            Logger.getInstance().d(TAG, "save " + url + " " + response);
            mSharedPreferences.edit().putString(url, response).apply();
        }else{
            Logger.getInstance().d(TAG, "save " + url + " " + null);
            mSharedPreferences.edit().putString(url, null).apply();
        }

    }
    private String loadPreference(String url) {
        Logger.getInstance().d(TAG, "load " + url + " " + mSharedPreferences.getString
                (url, null));
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
            long hours =jsonObject1.optLong("hours");  Logger.getInstance().d("cache valid",(cacheTime+hours> SystemClock.elapsedRealtime())+" time");

            if(cacheTime+hours> SystemClock.elapsedRealtime() || hours==0) {
                return jsonObject1.optString("response");
            }else{
                invalidateCacheResponse(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    @SuppressLint("CommitPrefEdits")
    public void clearCache() {
        mSharedPreferences.edit().clear().commit();
    }


    public void cacheResponse(Cache.CacheEntry cacheEntry) {
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
