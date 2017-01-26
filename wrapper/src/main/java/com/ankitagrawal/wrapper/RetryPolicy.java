/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ankitagrawal.wrapper;


/**
 * Retry policy for a request.
 */
public interface RetryPolicy {

    /**
     * Returns the current timeout (used for logging).
     */
     int getCurrentTimeout();

    /**
     * Returns the total retry count (used for logging).
     */
     int getRetryCount();

    /**
     * Returns the total retry count (used for logging).
     */
    void setCurrentTimeout(int timeOut);
    /**
     * Returns the backoff multiplier for the policy.
     */
     float getBackoffMultiplier();

}
