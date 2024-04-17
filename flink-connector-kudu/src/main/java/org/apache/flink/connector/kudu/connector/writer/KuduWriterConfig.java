/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.connector.kudu.connector.writer;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.connector.kudu.sink.KuduSink;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.kudu.client.AsyncKuduClient;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.flink.util.Preconditions.checkNotNull;
import static org.apache.kudu.client.SessionConfiguration.FlushMode;

/** Configuration used by {@link KuduSink}. Specifies connection and other necessary properties. */
@PublicEvolving
public class KuduWriterConfig implements Serializable {

    private final String masters;
    private final FlushMode flushMode;
    private final long operationTimeoutMillis;
    private final int maxBufferSize;
    private final int flushIntervalMillis;
    private final boolean ignoreNotFound;
    private final boolean ignoreDuplicate;

    private KuduWriterConfig(
            String masters,
            FlushMode flushMode,
            long operationTimeoutMillis,
            int maxBufferSize,
            int flushIntervalMillis,
            boolean ignoreNotFound,
            boolean ignoreDuplicate) {

        this.masters = checkNotNull(masters, "Kudu masters cannot be null");
        this.flushMode = checkNotNull(flushMode, "Kudu flush mode cannot be null");
        this.operationTimeoutMillis = operationTimeoutMillis;
        this.maxBufferSize = maxBufferSize;
        this.flushIntervalMillis = flushIntervalMillis;
        this.ignoreNotFound = ignoreNotFound;
        this.ignoreDuplicate = ignoreDuplicate;
    }

    public String getMasters() {
        return masters;
    }

    public FlushMode getFlushMode() {
        return flushMode;
    }

    public long getOperationTimeoutMillis() {
        return operationTimeoutMillis;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public int getFlushIntervalMillis() {
        return flushIntervalMillis;
    }

    public boolean isIgnoreNotFound() {
        return ignoreNotFound;
    }

    public boolean isIgnoreDuplicate() {
        return ignoreDuplicate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("masters", masters)
                .append("flushMode", flushMode)
                .toString();
    }

    /** Builder for the {@link KuduWriterConfig}. */
    public static class Builder {
        private String masters;
        private FlushMode flushMode = FlushMode.AUTO_FLUSH_BACKGROUND;
        // Reference from AsyncKuduClientBuilder defaultOperationTimeoutMs.
        private long operationTimeoutMillis = AsyncKuduClient.DEFAULT_OPERATION_TIMEOUT_MS;
        // Reference from AsyncKuduSession mutationBufferMaxOps 1000.
        private int maxBufferSize = 1000;
        // Reference from AsyncKuduSession flushIntervalMillis 1000.
        private int flushIntervalMillis = 1000;
        // Reference from AsyncKuduSession ignoreAllNotFoundRows false.
        private boolean ignoreNotFound = false;
        // Reference from AsyncKuduSession ignoreAllDuplicateRows false.
        private boolean ignoreDuplicate = false;

        private Builder(String masters) {
            this.masters = masters;
        }

        public static Builder setMasters(String masters) {
            return new Builder(masters);
        }

        public Builder setConsistency(FlushMode flushMode) {
            this.flushMode = flushMode;
            return this;
        }

        public Builder setEventualConsistency() {
            return setConsistency(FlushMode.AUTO_FLUSH_BACKGROUND);
        }

        public Builder setStrongConsistency() {
            return setConsistency(FlushMode.AUTO_FLUSH_SYNC);
        }

        public Builder setMaxBufferSize(int maxBufferSize) {
            this.maxBufferSize = maxBufferSize;
            return this;
        }

        public Builder setFlushIntervalMillis(int flushIntervalMillis) {
            this.flushIntervalMillis = flushIntervalMillis;
            return this;
        }

        public Builder setOperationTimeoutMillis(long operationTimeoutMillis) {
            this.operationTimeoutMillis = operationTimeoutMillis;
            return this;
        }

        public Builder setIgnoreNotFound(boolean ignoreNotFound) {
            this.ignoreNotFound = ignoreNotFound;
            return this;
        }

        public Builder setIgnoreDuplicate(boolean ignoreDuplicate) {
            this.ignoreDuplicate = ignoreDuplicate;
            return this;
        }

        public KuduWriterConfig build() {
            return new KuduWriterConfig(
                    masters,
                    flushMode,
                    operationTimeoutMillis,
                    maxBufferSize,
                    flushIntervalMillis,
                    ignoreNotFound,
                    ignoreDuplicate);
        }

        @Override
        public int hashCode() {
            int result =
                    Objects.hash(
                            masters,
                            flushMode,
                            operationTimeoutMillis,
                            maxBufferSize,
                            flushIntervalMillis,
                            ignoreNotFound,
                            ignoreDuplicate);
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Builder that = (Builder) o;
            return Objects.equals(masters, that.masters)
                    && Objects.equals(flushMode, that.flushMode)
                    && Objects.equals(operationTimeoutMillis, that.operationTimeoutMillis)
                    && Objects.equals(maxBufferSize, that.maxBufferSize)
                    && Objects.equals(flushIntervalMillis, that.flushIntervalMillis)
                    && Objects.equals(ignoreNotFound, that.ignoreNotFound)
                    && Objects.equals(ignoreDuplicate, that.ignoreDuplicate);
        }
    }
}
