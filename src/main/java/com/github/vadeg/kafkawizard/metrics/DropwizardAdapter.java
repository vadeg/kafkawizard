package com.github.vadeg.kafkawizard.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.github.vadeg.kafkawizard.metrics.DropwizardAdapterConfig.METRIC_REGISTRY_NAME_CONFIG;

public class DropwizardAdapter implements MetricsReporter {

    private static final Logger LOG = LoggerFactory.getLogger(DropwizardAdapter.class);
    private MetricRegistry metricRegistry;
    private final Set<String> registeredMetrics = new ConcurrentSkipListSet<>();

    @Override
    public void init(List<KafkaMetric> metrics) {
        metrics.forEach(m -> System.out.println("Init metric: " + m.metricName()));
    }

    @Override
    public void metricChange(KafkaMetric metric) {
        System.out.println("Metric change: " + metric.metricName());
    }

    @Override
    public void metricRemoval(KafkaMetric metric) {
        System.out.println("Metric remove: " + metric.metricName());
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        Map<String, Object> properties = DropwizardAdapterConfig.CONFIG.parse(configs);
        String registryName = (String) properties.get(METRIC_REGISTRY_NAME_CONFIG);
        if (registryName == null) {
            metricRegistry = SharedMetricRegistries.getDefault();
            LOG.info("Using default metric registry");
        } else {
            metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
            LOG.info("Using \"{}\" metric registry", registryName);
        }


/*        configs.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });*/
    }
}
