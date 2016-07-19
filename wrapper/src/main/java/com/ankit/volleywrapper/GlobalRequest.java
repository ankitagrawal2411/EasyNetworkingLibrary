package com.ankit.volleywrapper;

/**
 * Created by ankitagrawal on 19/7/16.
 */
public class GlobalRequest {


    private GlobalRequest(Builder builder) {
        CacheRequestHandler.getInstance().setRequestManager(builder.mRequestManager);
    }

    public static Builder newBuilder() {
        return new Builder();
    }



    /**
     * {@code GlobalRequestBuilder} builder static inner class.
     */
    public static final class Builder {
        private RequestManager mRequestManager;

        private Builder() {
        }

        /**
         * Sets the {@code mRequestManager} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mRequestManager} to set
         * @return a reference to this Builder
         */
        public Builder setRequestManager(RequestManager val) {
            mRequestManager = val;
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
