package com.github.vadeg.kafkawizard.metrics;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class DropwizardAdapterConfig {

    public static ConfigDef CONFIG;

    public static String METRIC_REGISTRY_NAME_CONFIG = "kafkawizard.metric.registry.name";
    public static String METRIC_PREFIX_CONFIG = "kafkawizard.metric.prefix";
    private static String METRIC_REGISTRY_NAME_DOC = "Metric Registry name where to register metrics";
    private static String METRIC_PREFIX_DOC = "Prefix to prepend to a metric name";

    static {
        CONFIG = new ConfigDef()
                .define(METRIC_REGISTRY_NAME_CONFIG, Type.STRING, Importance.LOW, METRIC_REGISTRY_NAME_DOC)
                .define(METRIC_PREFIX_CONFIG, Type.STRING, Importance.LOW, METRIC_PREFIX_DOC);
    }

    public static void main(String[] args) {
        System.out.println(CONFIG.toRst());
    }
}
