package com.ankitagrawal.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankitagrawal on 19/7/16.
 */
public class GlobalRequest {

    private GlobalRequest(Builder builder) {
        ArrayList<Client> mClient = builder.mClient;
        ArrayList<Converter> mConverters = builder.mConverters;
        CacheClient.getInstance().setRetryPolicy(builder.retryPolicy);
        CacheClient.getInstance().setNetworkPolicy(builder.networkPolicy);
        CacheClient.getInstance().setMemoryPolicy(builder.memoryPolicy);
        CacheClient.getInstance().setHeaders(builder.mHeaders);
        CacheClient.getInstance().setClients(mClient);
        CacheClient.getInstance().setConverters(mConverters);
        CacheClient.getInstance().setBaseUrl(builder.baseUrl);
        CacheClient.getInstance().setSingleRequestMode(builder.singleMode);
        Logger.getInstance().setLevel(builder.mLogLevel);
        Logger.getInstance().setDisabledLogs(builder.disableAllLogs);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * {@code GlobalRequestBuilder} builder static inner class.
     */
    public static final class Builder {
        private ArrayList<Client> mClient;
        private ArrayList<Converter> mConverters;
        private HashMap<String, String> mHeaders;
        private String baseUrl;
        private RetryPolicy retryPolicy;
        private int memoryPolicy;
        private int networkPolicy;
        private boolean singleMode=true;
        private int mLogLevel=LogLevel.NO_LEVEL;
        private boolean disableAllLogs=false;
        public Builder() {

        }

        /**
         * Specifies the {@link MemoryPolicy} to use for this request. You may specify additional policy
         * options using the varargs parameter.
         */
        public Builder memoryPolicy(@NonNull MemoryPolicy policy, @NonNull MemoryPolicy...
                additional) {
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

        public Builder baseUrl(@NonNull String url) {
            baseUrl = url;
            return this;
        }

        public Builder addHeader(@NonNull String key, @NonNull String value) {
            if (mHeaders == null) {
                mHeaders = new HashMap<>();
            }
            mHeaders.put(key, value);
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
        public Builder logLevel(int level) {
            mLogLevel = level;
            return this;
        }
        public Builder singleRequest(boolean mode) {
            singleMode = mode;
            return this;
        }
        public Builder disableAllLogs(boolean disableAllLogs) {
            this.disableAllLogs = disableAllLogs;
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
        public Builder networkPolicy(@NonNull NetworkPolicy policy, @NonNull NetworkPolicy... additional) {
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
        public Builder client(Client val) {
            if (mClient == null) {
                mClient = new ArrayList<>(2);
            }
            mClient.clear();
            mClient.add(val);
            return this;
        }

        public Builder converters(ArrayList<Converter> converters) {
            if (mConverters == null) {
                mConverters = new ArrayList<>(2);
            }
            mConverters.clear();
            this.mConverters = converters;
            return this;
        }

        public Builder converter(Converter converters) {
            if (mConverters == null) {
                mConverters = new ArrayList<>(2);
            }
            mConverters.clear();
            this.mConverters.add(converters);
            return this;
        }

        public Builder addConverter(Converter converters) {
            if (mConverters == null) {
                mConverters = new ArrayList<>(2);
            }
            this.mConverters.add(converters);
            return this;
        }

        public Builder addClient(Client client) {
            if (mClient == null) {
                mClient = new ArrayList<>(2);
            }
            this.mClient.add(client);
            return this;
        }

        public Builder client(ArrayList<Client> client) {
            if (mClient == null) {
                mClient = new ArrayList<>(2);
            }
            this.mClient = client;
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
