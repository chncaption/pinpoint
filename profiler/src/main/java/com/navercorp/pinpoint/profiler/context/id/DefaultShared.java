/*
 * Copyright 2017 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler.context.id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultShared implements Shared {
    private static final Logger logger = LogManager.getLogger(DefaultShared.class);

    private static final AtomicReferenceFieldUpdater<DefaultShared, String> END_POINT_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(DefaultShared.class, String.class, "endPoint");

    private static final AtomicReferenceFieldUpdater<DefaultShared, String> RPC_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(DefaultShared.class, String.class, "rpc");

    private static final AtomicReferenceFieldUpdater<DefaultShared, String> URL_TEMPLATE_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(DefaultShared.class, String.class, "uriTemplate");

    private volatile int errorCode;
    private volatile byte loggingInfo;

    @SuppressWarnings("unused")
    private volatile String endPoint;

    @SuppressWarnings("unused")
    private volatile String rpc;

    private volatile long threadId;

    private volatile int statusCode;

    private volatile String uriTemplate = null;

    @Override
    public void maskErrorCode(int errorCode) {
//        synchronized (this) {
////            TODO Refactor bit masking rule
//            this.errorCode |= errorCode;
//        }
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
//        synchronized (this) {
//            return errorCode;
//        }
        return errorCode;
    }

    @Override
    public void setLoggingInfo(byte loggingInfo) {
        this.loggingInfo = loggingInfo;

    }

    @Override
    public byte getLoggingInfo() {
        return loggingInfo;
    }

    @Override
    public void setEndPoint(String endPoint) {
        final boolean updated = END_POINT_UPDATER.compareAndSet(this, null, endPoint);
        if (!updated) {
            // for debug
            logger.debug("already set EndPoint {}", endPoint);
        }
    }

    @Override
    public String getEndPoint() {
        return END_POINT_UPDATER.get(this);
    }

    @Override
    public void setRpcName(String rpc) {
        final boolean updated = RPC_UPDATER.compareAndSet(this, null, rpc);
        if (!updated) {
            // for debug
            logger.debug("already set Rpc {}", rpc);
        }
    }

    @Override
    public String getRpcName() {
        return RPC_UPDATER.get(this);
    }

    @Override
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    @Override
    public long getThreadId() {
        return threadId;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public boolean setUriTemplate(String uriTemplate) {
        return URL_TEMPLATE_UPDATER.compareAndSet(this, null, uriTemplate);
    }

    @Override
    public boolean setUriTemplate(String uriTemplate, boolean force) {
        if (force) {
            URL_TEMPLATE_UPDATER.set(this, uriTemplate);
            return true;
        } else {
            return setUriTemplate(uriTemplate);
        }
    }

    @Override
    public String getUriTemplate() {
        return uriTemplate;
    }
}