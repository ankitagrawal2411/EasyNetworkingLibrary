package com.ankit.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.ankit.wrapper.interfaces.Builder;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class RequestBuilder implements Builder.BuildRequestType {
    private int method = Request.Method.POST;
    private String requestUrl;
    private JSONObject jsonObject;
    private BaseResponseListener<?,?> baseResponseListener;
    private RetryPolicy retryPolicy;
    private String reqTAG;
    private int memoryPolicy=0;
    private int networkPolicy=0;
    private long cacheTime;
    private int mRequestType=-1;
    private static final int JSON = 2;
    private static final int STRING = 1;
    private HashMap<String, String> mHeaders;
    private  Class mClass;
    private  boolean mCancel=false;
    private int mLogLevel= LogLevel.NO_LEVEL;
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
        baseResponseListener = builder.baseResponseListener;
        mHeaders = builder.mHeaders;
        retryPolicy = builder.retryPolicy;
        reqTAG = builder.reqTAG;
        memoryPolicy = builder.memoryPolicy;
        networkPolicy = builder.networkPolicy;
    }

    public  RequestBuilder cloneRequest() {
        RequestBuilder builder = new RequestBuilder();
        builder.memoryPolicy = this.memoryPolicy;
        builder.networkPolicy = this.networkPolicy;
        builder.reqTAG = this.reqTAG;
        builder.retryPolicy = this.retryPolicy;
        builder.mHeaders = this.mHeaders;
        builder.baseResponseListener = this.baseResponseListener;
        builder.jsonObject = this.jsonObject;
        builder.requestUrl = this.requestUrl;
        builder.method = this.method;
        return builder;
    }
  private Builder.BuildOptions buildOptions = new Builder.BuildOptions() {
      /**
       * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
       * options using the varargs parameter.
       */
      @Override
      public Builder.BuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional) {
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
      public Builder.BuildOptions networkPolicy(@NonNull NetworkPolicy policy,@NonNull NetworkPolicy... additional) {
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
      public Builder.BuildOptions memoryCache(boolean val) {
          if(val) {
              memoryPolicy(MemoryPolicy.CACHE, MemoryPolicy.STORE);
          }else{
              memoryPolicy=0;
          }
          return this;
      }

      @Override
      public Builder.BuildOptions diskCache(boolean val) {
          if(val) {
              networkPolicy(NetworkPolicy.CACHE, NetworkPolicy.STORE);
          }else{
              networkPolicy=0;
          }
          return this;
      }

      @Override
      public Builder.BuildOptions addHeader(@NonNull String key,@NonNull String value) {
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
      public Builder.BuildOptions retryPolicy(@Nullable RetryPolicy val) {
          retryPolicy = val;
          return this;
      }

      /**
       * Returns a {@code RequestBuilder} built from the parameters previously set.
       *
       */
      @Override
      public void send(@NonNull Context context) {
          if(requestUrl==null){
              throw new NullPointerException("url cannot be null");
          }
          if(reqTAG==null){
              throw new NullPointerException("reqTag cannot be null");
          }
          if(mClass!=null && baseResponseListener instanceof ResponseListener){
              throw new IllegalArgumentException("wrong interface registered, should be BaseResponseListener and not ResponseListener");
          }
          if(mRequestType==JSON) {
              CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                      jsonObject, mHeaders, retryPolicy, reqTAG, memoryPolicy,
                      networkPolicy, cacheTime, baseResponseListener,mLogLevel,mCancel, mClass);
          }else if(mRequestType==STRING){
              CacheRequestHandler.getInstance().makeStringRequest(context, method, requestUrl,
                      jsonObject!=null?jsonObject.toString():null, mHeaders, retryPolicy, reqTAG,
                      memoryPolicy,
                      networkPolicy, cacheTime, baseResponseListener,mLogLevel,mCancel,mClass);
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
      public Builder.BuildOptions headers(@Nullable HashMap<String, String> val) {
          mHeaders = val;
          return this;
      }



      @Override
      public Builder.BuildOptions asJsonObject(@NonNull ResponseListener<JSONObject,?> val) {
          if(mRequestType!=-1){
              throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                      "asClass() method is allowed ");
          }
          mRequestType = JSON;
          baseResponseListener =val;
          return this;
      }


      @Override
      public <F> Builder.BuildOptions asClass(@NonNull Class<F> aClass, @NonNull
      ParsedResponseListener<JSONObject,F> val) {
          if(mRequestType!=-1){
              throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                      "asClass() method is allowed ");
          }
          baseResponseListener =val;
          mRequestType = JSON;
          mClass = aClass;
          return this;
      }

      @Override
      public Builder.BuildOptions asString(@NonNull ResponseListener<String,?> val) {
          if(mRequestType!=-1){
              throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                      "asClass() method is allowed ");
          }
          mRequestType = STRING;
          baseResponseListener =val;
          return this;
      }

      /**
       * Sets the {@code jsonObject} and returns a reference to {@code IIFcRequestListener}
       *
       * @param val the {@code jsonObject} to set
       * @return a reference to this Builder
       */
      @Override
      public Builder.BuildOptions params(@Nullable JSONObject val) {
          jsonObject = val;
          return this;
      }

      @Override
      public Builder.BuildOptions cacheTime(long time) {
          cacheTime = time;
          return this;
      }

      @Override
      public Builder.BuildOptions cancel() {
          mCancel = true;
          return this;
      }

      @Override
      public Builder.BuildOptions logLevel(int level) {
          mLogLevel = level;
          return this;
      }

      /**
       * Returns a {@code RequestBuilder} built from the parameters previously set.
       *
       * @return a {@code RequestBuilder} built with parameters of this {@code RequestBuilder.Builder}
       */
      @Override
      public Builder.BuildOptions build() {
          return this;
      }
    };
    private Builder.BuildUrl buildUrl = new Builder.BuildUrl() {
        /**
         * Sets the {@code requestUrl} and returns a reference to {@code IJsonObject}
         *
         * @param val the {@code requestUrl} to set
         * @return a reference to this Builder
         */
        @Override
        public Builder.BuildTag url(@NonNull String val) {
            requestUrl = val;
            return buildTag;
        }
    };
    private Builder.BuildTag buildTag = new Builder.BuildTag() {
        /**
         * Sets the {@code reqTAG} and returns a reference to {@code IShouldCache}
         *
         * @param val the {@code reqTAG} to set
         * @return a reference to this Builder
         */
        @Override
        public Builder.BuildOptions tag(@NonNull String val) {
            reqTAG = val;
            return buildOptions;
        }
    } ;

    @Override
    public Builder.BuildUrl post(@Nullable JSONObject val) {
        jsonObject = val;
        method(Request.Method.POST);
        return buildUrl;
    }

    @Override
    public Builder.BuildUrl get() {
        method(Request.Method.GET);
        return buildUrl;
    }

    /**
     * Sets the {@code method} and returns a reference to {@code IRequestUrl}
     *
     * @param val the {@code method} to set
     * @return a reference to this Builder
     */
    @Override
    public Builder.BuildUrl method(int val) {
        method = val;
        return buildUrl;
    }

    @Override
    public void invalidate(Context context,String tag) {
        CacheRequestManager.getInstance(context).invalidateCacheResponse(tag);
        CacheRequestHandler.getInstance().invalidateCacheResponse(tag);
    }

    @Override
    public void clearCache(Context context) {
        CacheRequestManager.getInstance(context).clearCache();
        CacheRequestHandler.getInstance().clearCache();
    }


}





