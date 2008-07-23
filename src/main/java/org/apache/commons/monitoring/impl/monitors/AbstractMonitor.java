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

package org.apache.commons.monitoring.impl.monitors;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.monitoring.Counter;
import org.apache.commons.monitoring.Gauge;
import org.apache.commons.monitoring.Metric;
import org.apache.commons.monitoring.Monitor;
import org.apache.commons.monitoring.Role;

/**
 * Abstract {@link Monitor} implementation with implementation for base methods
 *
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public abstract class AbstractMonitor implements Monitor
{

    @SuppressWarnings("unchecked")
    private final ConcurrentMap<Role, Metric> metrics;
    private final Key key;

    @SuppressWarnings("unchecked")
    public AbstractMonitor( Key key )
    {
        super();
        this.key = key;
        this.metrics = new ConcurrentHashMap<Role, Metric>();
    }

    /**
     * {@inheritDoc}
     */
    public final Key getKey()
    {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    public final Metric getMetric( String role )
    {
        return metrics.get( role );
    }

    @SuppressWarnings("unchecked")
    public final <T extends Metric> T getMetric( Role<T> role )
    {
        return (T) metrics.get( role );
    }

    @SuppressWarnings("unchecked")
    public final Collection<Role> getRoles()
    {
        return Collections.unmodifiableCollection( metrics.keySet() );
    }

    public final Collection<Metric> getMetrics()
    {
        return Collections.unmodifiableCollection( metrics.values() );
    }

    /**
     * Register a new Metric in the monitor
     * 
     * @param metric Metric instance to get registered
     * @return a previously registered Metric if existed, or <code>null</code> if the metric has been successfully
     * registered
     */
    @SuppressWarnings("unchecked")
    protected <T extends Metric> T register( T metric )
    {
        metric.setMonitor( this );
        return (T) metrics.putIfAbsent( metric.getRole(), metric );
    }

    /**
     * {@inheritDoc}
     */
    public void reset()
    {
        for ( Metric metric : metrics.values() )
        {
            metric.reset();
        }
    }

    public Counter getCounter( Role<Counter> role )
    {
        return getMetric( role );
    }

    @SuppressWarnings("unchecked")
    public Counter getCounter( String role )
    {
        return getCounter( (Role<Counter>) Role.getRole( role ) );
    }

    public Gauge getGauge( Role<Gauge> role )
    {
        return getMetric( role );
    }

    @SuppressWarnings("unchecked")
    public Gauge getGauge( String role )
    {
        return getGauge( (Role<Gauge>) Role.getRole( role ) );
    }

}