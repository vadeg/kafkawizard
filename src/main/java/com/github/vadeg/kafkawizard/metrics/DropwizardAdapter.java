package com.github.vadeg.kafkawizard.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.github.vadeg.kafkawizard.metrics.DropwizardAdapterConfig.METRIC_REGISTRY_NAME_CONFIG;

public final class DropwizardAdapter implements MetricsReporter {

    private static final Logger LOG = LoggerFactory.getLogger(DropwizardAdapter.class);
    private MetricRegistry metricRegistry;

    @Override
    public void init(final List<KafkaMetric> metrics) {
        metrics.forEach(this::metricChange);
    }

    @Override
    public void metricChange(final KafkaMetric metric) {
        final String name = MetricNameBuilder.build(metric.metricName());
        metricRegistry.gauge(name, () -> metric::metricValue);
        LOG.debug("Metric {} registered", name);
    }

    @Override
    public void metricRemoval(final KafkaMetric metric) {
        final String name = MetricNameBuilder.build(metric.metricName());
        metricRegistry.remove(name);
        LOG.debug("Metric {} unregistered", name);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(final Map<String, ?> configs) {
        Map<String, Object> properties = DropwizardAdapterConfig.CONFIG.parse(configs);
        String registryName = (String) properties.get(METRIC_REGISTRY_NAME_CONFIG);
        if (registryName == null) {
            metricRegistry = SharedMetricRegistries.getDefault();
            LOG.info("Using default metric registry");
        } else {
            metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
            LOG.info("Using \"{}\" metric registry", registryName);
        }
    }
}
