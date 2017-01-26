package com.ankit.wrapper.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankit.wrapper.MemoryPolicy;
import com.ankit.wrapper.NetworkPolicy;
import com.ankit.wrapper.ParsedResponseListener;
import com.ankit.wrapper.ResponseListener;
import com.ankit.wrapper.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 1/8/16.
 */

public class Builder {
    public interface BuildUrl {

        BuildTag url(@NonNull String val);
    }
    public interface BuildOptions {
        BuildOptions build();

        BuildOptions memoryCache(boolean val);

        BuildOptions diskCache(boolean val);

        BuildOptions retryPolicy(@Nullable RetryPolicy val);

        void send(@NonNull Context context);

        BuildOptions headers(HashMap<String, String> val);

        BuildOptions asJsonObject(@NonNull ResponseListener<JSONObject,?> val);

        <F> BuildOptions asClass(@NonNull Class<F> mClass,@NonNull ParsedResponseListener<JSONObject,F> val);

        BuildOptions asString(@NonNull ResponseListener<String,?> val);

        BuildOptions params(@Nullable JSONObject val);

        BuildOptions cacheTime( long time);

        BuildOptions cancel();

        BuildOptions logLevel( int level);

        BuildOptions addHeader(@NonNull String key,@NonNull String value);

        BuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional);

        BuildOptions networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional);
    }

    public interface BuildRequestType {


        BuildUrl post(@Nullable JSONObject val);

        BuildUrl get();

        BuildUrl method(int val);

        void invalidate(Context context,String tag);

        void clearCache(Context context);
    }
    public interface BuildTag {

        BuildOptions tag(@NonNull String val);
    }
}
