package com.ankit.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 14/7/16.
 */
public class Request  {
    private int method = Method.POST;
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

    protected Request(Builder builder) {
        method = builder.method;
        requestUrl = builder.requestUrl;
        jsonObject = builder.jsonObject;
        baseResponseListener = builder.baseResponseListener;
        retryPolicy = builder.retryPolicy;
        reqTAG = builder.reqTAG;
        memoryPolicy = builder.memoryPolicy;
        networkPolicy = builder.networkPolicy;
        cacheTime = builder.cacheTime;
        mRequestType = builder.mRequestType;
        mHeaders = builder.mHeaders;
        mClass = builder.mClass;
        mCancel = builder.mCancel;
        mLogLevel = builder.mLogLevel;
    }
    private void sendRequest(Context context) {
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
    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Request copy) {
        Builder builder = new Builder();
        builder.method = copy.method;
        builder.requestUrl = copy.requestUrl;
        builder.jsonObject = copy.jsonObject;
        builder.baseResponseListener = copy.baseResponseListener;
        builder.retryPolicy = copy.retryPolicy;
        builder.reqTAG = copy.reqTAG;
        builder.memoryPolicy = copy.memoryPolicy;
        builder.networkPolicy = copy.networkPolicy;
        builder.cacheTime = copy.cacheTime;
        builder.mRequestType = copy.mRequestType;
        builder.mHeaders = copy.mHeaders;
        builder.mClass = copy.mClass;
        builder.mCancel = copy.mCancel;
        builder.mLogLevel = copy.mLogLevel;
        return builder;
    }

    /**
     * Supported request methods.
     */
    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }
    public interface IBuildUrl {

        IBuildTag url(@NonNull String val);
    }
    public interface IBuildOptions {
        Request build();

        IBuildOptions memoryCache(boolean val);

        IBuildOptions diskCache(boolean val);

        IBuildOptions retryPolicy(@Nullable RetryPolicy val);

        void send(@NonNull Context context);

        IBuildOptions headers(HashMap<String, String> val);

        IBuildOptions asJsonObject(@NonNull ResponseListener<JSONObject,?> val);

        <F> IBuildOptions asClass(@NonNull Class<F> mClass,@NonNull BaseResponseListener<JSONObject,F> val);

        IBuildOptions asString(@NonNull ResponseListener<String,?> val);

        IBuildOptions params(@Nullable JSONObject val);

        IBuildOptions cacheTime( long time);

        IBuildOptions cancel();

        IBuildOptions logLevel( int level);

        IBuildOptions addHeader(@NonNull String key,@NonNull String value);

        IBuildOptions memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional);

        IBuildOptions networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional);
    }

    public interface IBuildRequestType {


        IBuildUrl post(@Nullable JSONObject val);

        IBuildUrl get();

        IBuildUrl method(int val);

        IBuildRequestType invalidate(Context context,String tag);

        IBuildRequestType clearCache(Context context);
    }
    public interface IBuildTag {

        IBuildOptions tag(@NonNull String val);
    }
    /**
     * {@code Request} builder static inner class.
     */
    public static final class Builder implements IBuildRequestType{
        private int method= Method.GET;
        private String requestUrl;
        private JSONObject jsonObject;
        private BaseResponseListener<?, ?> baseResponseListener;
        private RetryPolicy retryPolicy;
        private String reqTAG;
        private int memoryPolicy;
        private int networkPolicy;
        private long cacheTime;
        private int mRequestType=-1;
        private HashMap<String, String> mHeaders;
        private Class mClass;
        private  boolean mCancel=false;
        private int mLogLevel= LogLevel.NO_LEVEL;
        public Builder() {
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
            public IBuildOptions networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional) {
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
            public IBuildOptions memoryCache(boolean val) {
                if (val) {
                    memoryPolicy(MemoryPolicy.CACHE, MemoryPolicy.STORE);
                } else {
                    memoryPolicy = 0;
                }
                return this;
            }

            @Override
            public IBuildOptions diskCache(boolean val) {
                if (val) {
                    networkPolicy(NetworkPolicy.CACHE, NetworkPolicy.STORE);
                } else {
                    networkPolicy = 0;
                }
                return this;
            }

            @Override
            public IBuildOptions addHeader(@NonNull String key, @NonNull String value) {
                if (mHeaders == null) {
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
            public IBuildOptions retryPolicy(@Nullable RetryPolicy val) {
                retryPolicy = val;
                return this;
            }

            /**
             * Returns a {@code RequestBuilder} built from the parameters previously set.
             */
            @Override
            public void send(@NonNull Context context) {
                build().sendRequest(context);

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


            @Override
            public IBuildOptions asJsonObject(@NonNull ResponseListener<JSONObject, ?> val) {
                if (mRequestType != -1) {
                    throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                            "asClass() method is allowed ");
                }
                mRequestType = JSON;
                baseResponseListener = val;
                return this;
            }


            @Override
            public <F> IBuildOptions asClass(@NonNull Class<F> aClass, @NonNull
            BaseResponseListener<JSONObject, F> val) {
                if (mRequestType != -1) {
                    throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                            "asClass() method is allowed ");
                }
                baseResponseListener = val;
                mRequestType = JSON;
                mClass = aClass;
                return this;
            }

            @Override
            public IBuildOptions asString(@NonNull ResponseListener<String, ?> val) {
                if (mRequestType != -1) {
                    throw new IllegalArgumentException("only one of asJsonObject() asString() or " +
                            "asClass() method is allowed ");
                }
                mRequestType = STRING;
                baseResponseListener = val;
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
                cacheTime = time;
                return this;
            }

            @Override
            public IBuildOptions cancel() {
                mCancel = true;
                return this;
            }

            @Override
            public IBuildOptions logLevel(int level) {
                mLogLevel = level;
                return this;
            }

            /**
             * Returns a {@code RequestBuilder} built from the parameters previously set.
             *
             * @return a {@code RequestBuilder} built with parameters of this {@code RequestBuilder.Builder}
             */
            @Override
            public Request build() {
                return new Request(Builder.this);
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

        @Override
        public IBuildRequestType invalidate(Context context,String tag) {
            CacheRequestManager.getInstance(context).invalidateCacheResponse(tag);
            CacheRequestHandler.getInstance().invalidateCacheResponse(tag);
            return this;
        }

        @Override
        public IBuildRequestType clearCache(Context context) {
            CacheRequestManager.getInstance(context).clearCache();
            CacheRequestHandler.getInstance().clearCache();
            return this;
        }


    }
}
