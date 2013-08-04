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
package org.apache.commons.monitoring.reporting.web.plugin.jvm;

import org.apache.commons.monitoring.reporting.web.handler.HandlerRendererAdapter;
import org.apache.commons.monitoring.reporting.web.plugin.jvm.gauges.CPUGauge;
import org.apache.commons.monitoring.reporting.web.plugin.jvm.gauges.UsedMemoryGauge;
import org.apache.commons.monitoring.reporting.web.template.MapBuilder;
import org.apache.commons.monitoring.repositories.Repository;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;

public class JVMHandler extends HandlerRendererAdapter {
    protected String getTemplate() {
        return "jvm/jvm.vm";
    }

    protected Map<String,?> getVariables() {
        final long now = System.currentTimeMillis();
        final OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        return new MapBuilder<String, Object>()
            .set("architecture", os.getArch())
            .set("name", os.getName())
            .set("version", os.getVersion())
            .set("numberProcessor", os.getAvailableProcessors())
            .set("cpu", Repository.INSTANCE.getGaugeValues(0, now, CPUGauge.CPU))
            .set("memory", Repository.INSTANCE.getGaugeValues(0, now, UsedMemoryGauge.USED_MEMORY))
            .build();
    }
}