package com.github.vadeg.kafkawizard;

import com.codahale.metrics.MetricRegistry;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.MetricName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class MetricNameBuilder {

    private String prefix;

    private MetricNameBuilder(final String prefix) {
        this.prefix = prefix;
    }

    static MetricNameBuilder withPrefix(final String prefix) {
        return new MetricNameBuilder(prefix);
    }

    static MetricNameBuilder noPrefix() {
        return new MetricNameBuilder(null);
    }

    private static String makeTagString(final Map<String, String> tags) {
        List<String> values = new ArrayList<>();
        if (tags.containsKey(CommonClientConfigs.CLIENT_ID_CONFIG)) {
            values.add(tags.remove(CommonClientConfigs.CLIENT_ID_CONFIG));
        }
        values.addAll(tags.values());
        return String.join(".", values);
    }

    /**
     * Builds a <code>String</code> metric name from {@link MetricName} using <code>group.tags.name</code> format.
     *
     * @param metricName metric name from Kafka
     * @return metric name as single string
     */
    public String build(final MetricName metricName) {
        if (metricName == null) {
            throw new IllegalArgumentException("'metricName' argument is null");
        }
        final String tagString = makeTagString(metricName.tags());
        if (prefix != null) {
            return MetricRegistry.name(prefix, metricName.group(), tagString, metricName.name());
        }
        return MetricRegistry.name(metricName.group(), tagString, metricName.name());
    }

}
