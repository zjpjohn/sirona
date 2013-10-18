/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.monitoring.graphite;

import org.apache.commons.monitoring.Role;
import org.apache.commons.monitoring.configuration.Configuration;
import org.apache.commons.monitoring.gauges.Gauge;
import org.apache.commons.monitoring.store.GaugeDataStore;
import org.apache.commons.monitoring.store.GaugeValuesRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphiteGaugeDataStore implements GaugeDataStore {
    private static final Logger LOGGER = Logger.getLogger(GraphiteGaugeDataStore.class.getName());

    private static final String GAUGE_PREFIX = "gauge-";
    private final Graphite graphite = Configuration.findOrCreateInstance(GraphiteBuilder.class).build();

    @Override
    public void addToGauge(final Gauge gauge, final long time, final double value) {
        try {
            graphite.simplePush(GAUGE_PREFIX + gauge.role().getName(), value, time);
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public Map<Long, Double> getGaugeValues(final GaugeValuesRequest gaugeValuesRequest) {
        return Collections.emptyMap(); // when using graphite we expect the user to use Graphite to render metrics
    }

    @Override
    public void createOrNoopGauge(Role role) {
        // no-op
    }
}