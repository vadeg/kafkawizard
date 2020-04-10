package com.github.vadeg.kafkawizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.apache.kafka.common.metrics.KafkaMetric;
import org.apache.kafka.common.metrics.MetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.vadeg.kafkawizard.KafkaWizardConfig.METRIC_PREFIX_CONFIG;
import static com.github.vadeg.kafkawizard.KafkaWizardConfig.METRIC_REGISTRY_NAME_CONFIG;

public final class KafkawizardReporter implements MetricsReporter {

    private static final Logger LOG = LoggerFactory.getLogger(KafkawizardReporter.class);
    private MetricRegistry metricRegistry;
    private MetricNameBuilder metricNameBuilder;
    private Set<String> registeredMetrics = ConcurrentHashMap.newKeySet();

    @Override
    public void init(final List<KafkaMetric> metrics) {
        metrics.forEach(this::metricChange);
    }

    @Override
    public void metricChange(final KafkaMetric metric) {
        final String name = metricNameBuilder.build(metric.metricName());
        metricRegistry.gauge(name, () -> metric::metricValue);
        registeredMetrics.add(name);
        LOG.debug("Metric {} registered", name);
    }

    @Override
    public void metricRemoval(final KafkaMetric metric) {
        final String name = metricNameBuilder.build(metric.metricName());
        metricRegistry.remove(name);
        registeredMetrics.remove(name);
        LOG.debug("Metric {} unregistered", name);
    }

    @Override
    public void close() {
        for (String metricName : registeredMetrics) {
            metricRegistry.remove(metricName);
        }
        registeredMetrics.clear();
    }

    @Override
    public void configure(final Map<String, ?> configs) {
        Map<String, Object> properties = KafkaWizardConfig.CONFIG.parse(configs);
        String registryName = (String) properties.get(METRIC_REGISTRY_NAME_CONFIG);
        LOG.info("{}", KafkaWizardConfig.toFormattedString(properties));

        if (registryName == null) {
            LOG.debug("No metric registry name provided. Fallback to default");
            try {
                metricRegistry = SharedMetricRegistries.getDefault();
            } catch (IllegalStateException e) {
                String error = MessageFormat.format(
                        "No default MetricRegistry provided. "
                                + "Set MetricRegistry name using {0} property or set a default MetricRegistry "
                                + "using SharedMetricRegistries",
                        METRIC_REGISTRY_NAME_CONFIG);
                throw new IllegalStateException(error, e);
            }
        } else {
            metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
        }

        String prefix = (String) properties.get(METRIC_PREFIX_CONFIG);
        if (prefix == null) {
            this.metricNameBuilder = MetricNameBuilder.noPrefix();
        } else {
            this.metricNameBuilder = MetricNameBuilder.withPrefix(prefix);
        }
    }
}
