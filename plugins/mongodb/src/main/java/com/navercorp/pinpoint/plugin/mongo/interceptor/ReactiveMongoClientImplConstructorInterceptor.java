/*
 * Copyright 2022 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.plugin.mongo.interceptor;

import com.mongodb.MongoClientSettings;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.common.util.ArrayArgumentUtils;
import com.navercorp.pinpoint.plugin.mongo.HostListAccessor;
import com.navercorp.pinpoint.plugin.mongo.MongoUtil;

import java.util.List;

public class ReactiveMongoClientImplConstructorInterceptor implements AroundInterceptor {
    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    public ReactiveMongoClientImplConstructorInterceptor() {
    }

    @Override
    public void before(Object target, Object[] args) {
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        if (throwable != null) {
            return;
        }

        if (Boolean.FALSE == (target instanceof HostListAccessor)) {
            if (isDebug) {
                logger.debug("Unexpected target. The target is not a HostListAccessor implementation. target={}", target);
            }
            return;
        }

        try {
            // 4.2 or later
            final MongoClientSettings mongoClientSettings = ArrayArgumentUtils.getArgument(args, 0, MongoClientSettings.class);
            if (mongoClientSettings == null) {
                if (isDebug) {
                    logger.debug("Unexpected argument. arg0 is not a MongoClientSettings class. args={}", args);
                }
                return;
            }

            final List<String> hostList = MongoUtil.getHostList(mongoClientSettings);
            ((HostListAccessor) target)._$PINPOINT$_setHostList(hostList);
            if (isDebug) {
                logger.debug("Set hostList={}", hostList);
            }
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("AFTER error. Caused:{}", th.getMessage(), th);
            }
        }
    }
}
