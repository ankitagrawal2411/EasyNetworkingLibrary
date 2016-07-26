package com.ankit.wrapper;

import java.util.ArrayList;

/**
 * Created by ankitagrawal on 19/7/16.
 */
public class GlobalRequest {
    private ArrayList<RequestManager> mRequestManager;
    private ArrayList<Converter> mConverters;

    private GlobalRequest(Builder builder) {
        mRequestManager = builder.mRequestManager;
        mConverters = builder.mConverters;
        CacheRequestHandler.getInstance().setRequestManagers(mRequestManager);
        CacheRequestHandler.getInstance().setConverters(mConverters);
    }

    public static Builder newBuilder() {
        return new Builder();
    }



    /**
     * {@code GlobalRequestBuilder} builder static inner class.
     */
    public static final class Builder {
        private ArrayList<RequestManager> mRequestManager;
        private ArrayList<Converter> mConverters;

        private Builder() {
        }

        /**
         * Sets the {@code mRequestManager} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mRequestManager} to set
         * @return a reference to this Builder
         */
        public Builder setRequestManager(RequestManager val) {
            if(mRequestManager==null){
                mRequestManager = new ArrayList<>(2);
            }
            mRequestManager.clear();
            mRequestManager.add(val);
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
        public Builder addRequestManager(RequestManager requestManager) {
            if(mRequestManager==null){
                mRequestManager = new ArrayList<>(2);
            }
            this.mRequestManager.add(requestManager);
            return this;
        }
        public Builder setRequestManagers(ArrayList<RequestManager> requestManager) {
            if(mRequestManager==null){
                mRequestManager = new ArrayList<>(2);
            }
            this.mRequestManager = requestManager;
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
