package com.ankit.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.ankit.wrapper.interfaces.Builder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class RequestBuilder implements Builder.IBuildRequestType {
    private int method = Request.Method.POST;
    private String requestUrl;
    private JSONObject jsonObject;
    private IParsedResponseListener iParsedResponseListener;
    private RetryPolicy retryPolicy;
    private String reqTAG;
    private int memoryPolicy=0;
    private int networkPolicy=0;
    private long cacheTime;
    private int mRequestType=-1;
    private static final int JSON = 1;
    private static final int STRING = 0;
    private HashMap<String, String> mHeaders;
    private  Class<?> mClass;
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
        iParsedResponseListener = builder.iParsedResponseListener;
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
        builder.iParsedResponseListener = copy.iParsedResponseListener;
        builder.jsonObject = copy.jsonObject;
        builder.requestUrl = copy.requestUrl;
        builder.method = copy.method;
        return builder;
    }

  private Builder.IBuildOptions iBuildOptions = new Builder.IBuildOptions() {
      /**
       * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
       * options using the varargs parameter.
       */
      @Override
      public Builder.IBuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional) {
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
      public Builder.IBuildOptions networkPolicy(@NonNull NetworkPolicy policy,@NonNull NetworkPolicy... additional) {
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
      public Builder.IBuildOptions cache(boolean val) {
          if(val) {
              networkPolicy(NetworkPolicy.CACHE, NetworkPolicy.STORE);
          }
          return this;
      }
      @Override
      public Builder.IBuildOptions addHeader(@NonNull String key,@NonNull String value) {
          if(mHeaders==null){
              mHeaders = new HashMap<>();
          }
          mHeaders.put(key, value);
          return this;
      }

      /**
       * Sets the {@code retryPolicy} and returns a reference to {@code IReqTAG}
       *
       * @param val the {@code retryPolicy} to set
       * @return a reference to this Builder
       */
      @Override
      public Builder.IBuildOptions retryPolicy(@Nullable RetryPolicy val) {
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
          if(mClass!=null && iParsedResponseListener instanceof IResponseListener){
              throw new IllegalArgumentException("wrong interface registered, should be IParsedResponseListener and not IResponseListener");
          }
          if(mRequestType==JSON) {
              CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                      jsonObject, mHeaders, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime, iParsedResponseListener, mClass);
          }else if(mRequestType==STRING){
              CacheRequestHandler.getInstance().makeStringRequest(context, method, requestUrl,
                      jsonObject.toString(), mHeaders, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime, iParsedResponseListener,mClass);
          }else{
              throw new IllegalArgumentException("no request type set please set it using as...()" +
                      "method of Request Builder");
          }
      }

      /**
       * Sets the {@code requestHeader} and returns a reference to {@code IRetryPolicy}
       *
       * @param val the {@code requestHeader} to set
       * @return a reference to this Builder
       */
      @Override
      public Builder.IBuildOptions headers(@Nullable HashMap<String, String> val) {
          mHeaders = val;
          return this;
      }



      @Override
      public Builder.IBuildOptions asJsonObject(@NonNull IResponseListener<JSONObject,?> val) {
          mRequestType = JSON;
          iParsedResponseListener =val;
          return this;
      }


      @Override
      public Builder.IBuildOptions asClass(@NonNull Class aClass, @NonNull IParsedResponseListener<?,?> val) {
          iParsedResponseListener =val;
          mRequestType = JSON;
          mClass = aClass;
          return this;
      }

      @Override
      public Builder.IBuildOptions asString(@NonNull IResponseListener<String,?> val) {
          mRequestType = STRING;
          iParsedResponseListener =val;
          return this;
      }

      /**
       * Sets the {@code jsonObject} and returns a reference to {@code IIFcRequestListener}
       *
       * @param val the {@code jsonObject} to set
       * @return a reference to this Builder
       */
      @Override
      public Builder.IBuildOptions params(@Nullable JSONObject val) {
          jsonObject = val;
          return this;
      }

      @Override
      public Builder.IBuildOptions cacheTime(long time) {
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
      public Builder.IBuildOptions build() {
          return this;
      }
    };
    private Builder.IBuildUrl iBuildUrl= new Builder.IBuildUrl() {
        /**
         * Sets the {@code requestUrl} and returns a reference to {@code IJsonObject}
         *
         * @param val the {@code requestUrl} to set
         * @return a reference to this Builder
         */
        @Override
        public Builder.IBuildTag url(@NonNull String val) {
            requestUrl = val;
            return iBuildTag;
        }
    };
    private Builder.IBuildTag iBuildTag= new Builder.IBuildTag() {
        /**
         * Sets the {@code reqTAG} and returns a reference to {@code IShouldCache}
         *
         * @param val the {@code reqTAG} to set
         * @return a reference to this Builder
         */
        @Override
        public Builder.IBuildOptions tag(@NonNull String val) {
            reqTAG = val;
            return iBuildOptions;
        }
    } ;

    @Override
    public Builder.IBuildUrl post(@Nullable JSONObject val) {
        jsonObject = val;
        method(Request.Method.POST);
        return iBuildUrl;
    }

    @Override
    public Builder.IBuildUrl get() {
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
    public Builder.IBuildUrl method(int val) {
        method = val;
        return iBuildUrl;
    }

    @Override
    public Builder.IBuildUrl invalidate(Context context,String tag) {
        CacheRequestManager.getInstance(context).invalidateCacheResponse(tag);
        return iBuildUrl;
    }

    @Override
    public Builder.IBuildUrl clearCache(Context context) {
        CacheRequestManager.getInstance(context).clearCache();
        return iBuildUrl;
    }


}





