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

import com.navercorp.pinpoint.bootstrap.context.TraceId;

import java.util.Objects;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface TraceRoot {

    TraceId getTraceId();

    long getLocalTransactionId();

    long getTraceStartTime();

    Shared getShared();

    static TraceRoot remote(TraceId traceId, String agentId, long traceStartTime, long localTransactionId) {
        return new RemoteTraceRoot(traceId, agentId, traceStartTime, localTransactionId);
    }

    static TraceRoot local(String agentId, long traceStartTime, long localTransactionId) {
        Objects.requireNonNull(agentId, "agentId");
        return new LocalTraceRoot(agentId, traceStartTime, localTransactionId);
    }
}
