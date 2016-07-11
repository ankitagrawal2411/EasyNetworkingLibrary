package com.ankit.volleywrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IBuildOptions {
        IBuildOptions build();

        IBuildOptions cache(boolean val);

        IBuildOptions retryPolicy(@Nullable RetryPolicy val);


        void send(@NonNull Context context);

        IBuildOptions headers(HashMap<String, String> val);

        IBuildOptions listener(IRequestListener<JSONObject> val);

        IBuildOptions jsonObject(@Nullable JSONObject val);


        IBuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional);

        IBuildOptions networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional);
}
