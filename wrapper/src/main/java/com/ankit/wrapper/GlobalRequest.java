package com.ankit.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankitagrawal on 19/7/16.
 */
public class GlobalRequest {

    private GlobalRequest(Builder builder) {
        ArrayList<RequestHandler> mRequestHandler = builder.mRequestHandler;
        ArrayList<Converter> mConverters = builder.mConverters;
        CacheRequestHandler.getInstance().setRetryPolicy(builder.retryPolicy);
        CacheRequestHandler.getInstance().setNetworkPolicy(builder.networkPolicy);
        CacheRequestHandler.getInstance().setMemoryPolicy(builder.memoryPolicy);
        CacheRequestHandler.getInstance().setHeaders(builder.mHeaders);
        CacheRequestHandler.getInstance().setRequestHandlers(mRequestHandler);
        CacheRequestHandler.getInstance().setConverters(mConverters);
    }

    public static Builder newBuilder() {
        return new Builder();
    }



    /**
     * {@code GlobalRequestBuilder} builder static inner class.
     */
    public static final class Builder {
        private ArrayList<RequestHandler> mRequestHandler;
        private ArrayList<Converter> mConverters;
        private HashMap<String, String> mHeaders;
        private String baseUrl;
        private RetryPolicy retryPolicy;
        private int memoryPolicy;
        private int networkPolicy;
        private Builder() {
        }
        /**
         * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
         * options using the varargs parameter.
         */
        public Builder memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy... additional) {
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
        public Builder addHeader(@NonNull String key,@NonNull String value) {
            if(mHeaders==null){
                mHeaders = new HashMap<>();
            }
            mHeaders.put(key, value);
            return this;
        }
        /**
         * Sets the {@code requestUrl} and returns a reference to {@code IJsonObject}
         *
         * @param val the {@code requestUrl} to set
         * @return a reference to this Builder
         */
        public Builder url(@NonNull String val) {
            baseUrl = val;
            return this;
        }
        /**
         * Sets the {@code requestHeader} and returns a reference to {@code IRetryPolicy}
         *
         * @param val the {@code requestHeader} to set
         * @return a reference to this Builder
         */
        public Builder headers(@Nullable HashMap<String, String> val) {
            mHeaders = val;
            return this;
        }
        /**
         * Sets the {@code retryPolicy} and returns a reference to {@code IReqTAG}
         *
         * @param val the {@code retryPolicy} to set
         * @return a reference to this Builder
         */
        public Builder retryPolicy(@Nullable RetryPolicy val) {
            retryPolicy = val;
            return this;
        }
        /**
         * Specifies the {@link NetworkPolicy} to use for this request. You may specify additional policy
         * options using the varargs parameter.
         */
        public Builder networkPolicy(@NonNull NetworkPolicy policy,@NonNull NetworkPolicy... additional) {
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
         * Sets the {@code mRequestManager} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mRequestManager} to set
         * @return a reference to this Builder
         */
        public Builder setRequestManager(RequestHandler val) {
            if(mRequestHandler ==null){
                mRequestHandler = new ArrayList<>(2);
            }
            mRequestHandler.clear();
            mRequestHandler.add(val);
            return this;
        }
        public Builder setConverters(ArrayList<Converter> converters) {
            if(mConverters==null){
                mConverters = new ArrayList<>(2);
            }
            mConverters.clear();
            this.mConverters=converters;
            return this;
        }
        public Builder setConverter(Converter converters) {
            if(mConverters==null){
                mConverters = new ArrayList<>(2);
            }
            mConverters.clear();
            this.mConverters.add(converters);
            return this;
        }
        public Builder addConverter(Converter converters) {
            if(mConverters==null){
                mConverters = new ArrayList<>(2);
            }
            this.mConverters.add(converters);
            return this;
        }
        public Builder addRequestManager(RequestHandler requestHandler) {
            if(mRequestHandler ==null){
                mRequestHandler = new ArrayList<>(2);
            }
            this.mRequestHandler.add(requestHandler);
            return this;
        }
        public Builder setRequestManagers(ArrayList<RequestHandler> requestHandler) {
            if(mRequestHandler ==null){
                mRequestHandler = new ArrayList<>(2);
            }
            this.mRequestHandler = requestHandler;
            return this;
        }
        /**
         * Returns a {@code GlobalRequestBuilder} built from the parameters previously set.
         *
         * @return a {@code GlobalRequestBuilder} built with parameters of this {@code GlobalRequestBuilder.Builder}
         */
        public GlobalRequest build() {
            return new GlobalRequest(this);
        }
    }
}
