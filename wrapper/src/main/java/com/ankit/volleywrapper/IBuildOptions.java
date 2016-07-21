package com.ankit.volleywrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;



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

        IBuildOptions callback(IRequestListener val);

        IBuildOptions asJsonObject(@NonNull IRequestListener<JSONObject> val);

        IBuildOptions asGsonObject(@NonNull GsonModelListener<?> val);

        IBuildOptions asString(@NonNull IRequestListener<String> val);

        IBuildOptions params(@Nullable JSONObject val);

        IBuildOptions cacheTime( long time);

        IBuildOptions addHeader(@NonNull String key,@NonNull String value);

        IBuildOptions addParam(@NonNull String key,@NonNull String value);

        IBuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional);

        IBuildOptions networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional);
}
