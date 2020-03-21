package com.github.vadeg.kafkawizard.metrics;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public final class DropwizardAdapterConfig {

    public static final String METRIC_REGISTRY_NAME_CONFIG = "kafkawizard.metric.registry.name";
    public static final String METRIC_PREFIX_CONFIG = "kafkawizard.metric.prefix";
    private static final String METRIC_REGISTRY_NAME_DOC = "Metric Registry name where to register metrics";
    private static final String METRIC_PREFIX_DOC = "Prefix to prepend to a metric name";

    public static final ConfigDef CONFIG = new ConfigDef()
            .define(METRIC_REGISTRY_NAME_CONFIG, Type.STRING, Importance.LOW, METRIC_REGISTRY_NAME_DOC)
            .define(METRIC_PREFIX_CONFIG, Type.STRING, Importance.LOW, METRIC_PREFIX_DOC);


    private DropwizardAdapterConfig() {
    }
}
