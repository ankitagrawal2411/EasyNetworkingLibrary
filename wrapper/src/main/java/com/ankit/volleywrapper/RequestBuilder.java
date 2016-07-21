package com.ankit.volleywrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class RequestBuilder implements IBuildRequestType {
    private int method = Request.Method.POST;
    private String requestUrl;
    private JSONObject jsonObject;
    private IRequestListener iRequestListener;
    private RetryPolicy retryPolicy;
    private String reqTAG;
    private int memoryPolicy;
    private int networkPolicy;
    private long cacheTime;
    private int mRequestType;
    private GsonModelListener<?> gsonModelListener;
    private static final int JSON = 1;
    private static final int GSON = 2;
    private static final int STRING = 0;
    private HashMap<String, String> mHeaders;
    private Map<String, String> mParams = new HashMap<>();
    public RequestBuilder(String requestUrl, String reqTAG) {
        this.requestUrl = requestUrl;
        this.reqTAG = reqTAG;
    }

    public RequestBuilder() {

    }
    public RequestBuilder(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    public RequestBuilder(RequestBuilder builder) {
        method = builder.method;
        requestUrl = builder.requestUrl;
        jsonObject = builder.jsonObject;
        iRequestListener = builder.iRequestListener;
        mHeaders = builder.mHeaders;
        retryPolicy = builder.retryPolicy;
        reqTAG = builder.reqTAG;
        memoryPolicy = builder.memoryPolicy;
        networkPolicy = builder.networkPolicy;
    }

    public static RequestBuilder newBuilder(RequestBuilder copy) {
        RequestBuilder builder = new RequestBuilder();
        builder.memoryPolicy = copy.memoryPolicy;
        builder.networkPolicy = copy.networkPolicy;
        builder.reqTAG = copy.reqTAG;
        builder.retryPolicy = copy.retryPolicy;
        builder.mHeaders = copy.mHeaders;
        builder.iRequestListener = copy.iRequestListener;
        builder.jsonObject = copy.jsonObject;
        builder.requestUrl = copy.requestUrl;
        builder.method = copy.method;
        return builder;
    }

  private IBuildOptions iBuildOptions = new IBuildOptions() {
      /**
       * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
       * options using the varargs parameter.
       */
      @Override
      public IBuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional) {
          memoryPolicy |= policy.index;
          if (additional.length > 0) {
              for (MemoryPolicy memoryPolicy1 : additional) {
                  if (memoryPolicy1 == null) {
                      throw new IllegalArgumentException("Memory policy cannot be null.");
                  }
                 memoryPolicy |= memoryPolicy1.index;
              }
          }
          return this;
      }

      /**
       * Specifies the {@link NetworkPolicy} to use for this request. You may specify additional policy
       * options using the varargs parameter.
       */
      @Override
      public IBuildOptions networkPolicy(@NonNull NetworkPolicy policy,@NonNull NetworkPolicy... additional) {
         networkPolicy |= policy.index;
          if (additional.length > 0) {
              for (NetworkPolicy networkPolicy1 : additional) {
                  if (networkPolicy1 == null) {
                      throw new IllegalArgumentException("Network policy cannot be null.");
                  }
                 networkPolicy |= networkPolicy1.index;
              }
          }
          return this;
      }
      /**
       * Sets the {@code shouldCache} and returns a reference to {@code IBuild}
       *
       * @param val the {@code shouldCache} to set
       * @return a reference to this Builder
       */
      @Override
      public IBuildOptions cache(boolean val) {
          if(val) {
              networkPolicy(NetworkPolicy.CACHE, NetworkPolicy.STORE);
          }
          return this;
      }
      @Override
      public IBuildOptions addHeader(@NonNull String key,@NonNull String value) {
          if(mHeaders==null){
              mHeaders = new HashMap<>();
          }
          mHeaders.put(key, value);
          return this;
      }

      public IBuildOptions addParam(@NonNull String key,@NonNull String value) {
          if(mParams==null){
              mParams = new HashMap<>();
          }
          mParams.put(key, value);
          return this;
      }

      /**
       * Sets the {@code retryPolicy} and returns a reference to {@code IReqTAG}
       *
       * @param val the {@code retryPolicy} to set
       * @return a reference to this Builder
       */
      @Override
      public IBuildOptions retryPolicy(@Nullable RetryPolicy val) {
          retryPolicy = val;
          return this;
      }

      /**
       * Returns a {@code RequestBuilder} built from the parameters previously set.
       *
       * @return a {@code RequestBuilder} built with parameters of this {@code RequestBuilder.Builder}
       */
      @Override
      public void send(@NonNull Context context) {
          if(requestUrl==null){
              throw new NullPointerException("url cannot be null");
          }
          if(reqTAG==null){
              throw new NullPointerException("reqTag cannot be null");
          }

          if(mParams!=null){
              Map<?, ?> contentsTyped = (Map<?, ?>) mParams;
              for (Map.Entry<?, ?> entry : contentsTyped.entrySet()) {
            /*
             * Deviate from the original by checking that keys are non-null and
             * of the proper type. (We still defer validating the values).
             */
                  String key = (String) entry.getKey();
                  if (key == null) {
                      throw new NullPointerException("key == null");
                  }
                  try {
                      jsonObject.put(key, entry.getValue());
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          }
          if(mRequestType==JSON) {
              CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                      jsonObject, mHeaders, iRequestListener, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime, gsonModelListener);
          }else if(mRequestType==STRING){
              CacheRequestHandler.getInstance().makeStringRequest(context, method, requestUrl,
                      jsonObject.toString(), mHeaders, iRequestListener, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime);
          }else if(mRequestType==GSON){
              CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                      jsonObject, mHeaders, iRequestListener, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime,gsonModelListener);
          }
      }

      /**
       * Sets the {@code requestHeader} and returns a reference to {@code IRetryPolicy}
       *
       * @param val the {@code requestHeader} to set
       * @return a reference to this Builder
       */
      @Override
      public IBuildOptions headers(@Nullable HashMap<String, String> val) {
          mHeaders = val;
          return this;
      }

      /**
       * Sets the {@code iFcRequestListener} and returns a reference to {@code IRequestHeader}
       *
       * @param val the {@code iFcRequestListener} to set
       * @return a reference to this Builder
       */
      @Deprecated
      @Override
      public IBuildOptions callback(@NonNull IRequestListener val) {
          iRequestListener = val;
          return this;
      }

      @Override
      public IBuildOptions asJsonObject(@NonNull IRequestListener<JSONObject> val) {
          mRequestType = JSON;
          iRequestListener =val;
          return this;
      }

      @Override
      public IBuildOptions asGsonObject(@NonNull GsonModelListener<?> val) {
          mRequestType = GSON;
          gsonModelListener =val;
          return this;
      }

      @Override
      public IBuildOptions asString(@NonNull IRequestListener<String> val) {
          mRequestType = STRING;
          iRequestListener =val;
          return this;
      }

      /**
       * Sets the {@code jsonObject} and returns a reference to {@code IIFcRequestListener}
       *
       * @param val the {@code jsonObject} to set
       * @return a reference to this Builder
       */
      @Override
      public IBuildOptions params(@Nullable JSONObject val) {
          jsonObject = val;
          return this;
      }

      @Override
      public IBuildOptions cacheTime(long time) {
          cache(true);
          cacheTime = time;
          return this;
      }

      /**
       * Returns a {@code RequestBuilder} built from the parameters previously set.
       *
       * @return a {@code RequestBuilder} built with parameters of this {@code RequestBuilder.Builder}
       */
      @Override
      public IBuildOptions build() {
          return this;
      }
    };
    private IBuildUrl iBuildUrl= new IBuildUrl() {
        /**
         * Sets the {@code requestUrl} and returns a reference to {@code IJsonObject}
         *
         * @param val the {@code requestUrl} to set
         * @return a reference to this Builder
         */
        @Override
        public IBuildTag url(@NonNull String val) {
            requestUrl = val;
            return iBuildTag;
        }
    };
    private IBuildTag iBuildTag= new IBuildTag() {
        /**
         * Sets the {@code reqTAG} and returns a reference to {@code IShouldCache}
         *
         * @param val the {@code reqTAG} to set
         * @return a reference to this Builder
         */
        @Override
        public IBuildOptions tag(@NonNull String val) {
            reqTAG = val;
            return iBuildOptions;
        }
    } ;

    @Override
    public IBuildUrl post(@Nullable JSONObject val) {
        jsonObject = val;
        method(Request.Method.POST);
        return iBuildUrl;
    }

    @Override
    public IBuildUrl get() {
        method(Request.Method.GET);
        return iBuildUrl;
    }

    /**
     * Sets the {@code method} and returns a reference to {@code IRequestUrl}
     *
     * @param val the {@code method} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuildUrl method(int val) {
        method = val;
        return iBuildUrl;
    }





}



