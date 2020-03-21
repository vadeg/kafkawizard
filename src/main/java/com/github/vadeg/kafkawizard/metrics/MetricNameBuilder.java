package com.github.vadeg.kafkawizard.metrics;

import com.codahale.metrics.MetricRegistry;
import org.apache.kafka.common.MetricName;

interface MetricNameBuilder {

    /**
     * Builds a <code>String</code> metric name from {@link MetricName} using <code>group.tags.name</code> format.
     *
     * @param metricName metric name from Kafka
     * @return metric name as single string
     */
    static String build(MetricName metricName) {
        if (metricName == null) {
            throw new IllegalArgumentException("'metricName' argument is null");
        }
        String tags = String.join(".", metricName.tags().values());
        return MetricRegistry.name(metricName.group(), tags, metricName.name());
    }

}
