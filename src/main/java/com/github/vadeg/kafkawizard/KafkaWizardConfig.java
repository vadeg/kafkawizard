package com.github.vadeg.kafkawizard;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

import java.util.Map;

public final class KafkaWizardConfig {

    public static final String REPORTER_NAME = "com.github.vadeg.kafkawizard.KafkawizardReporter";
    public static final String METRIC_REGISTRY_NAME_CONFIG = "kafkawizard.metrics.registry.name";
    public static final String METRIC_PREFIX_CONFIG = "kafkawizard.metrics.prefix";

    public static final ConfigDef CONFIG = new ConfigDef()
            .define(METRIC_REGISTRY_NAME_CONFIG, Type.STRING, null, Importance.LOW,
                    "Metric Registry name where to register metrics")
            .define(METRIC_PREFIX_CONFIG, Type.STRING, "kafka", Importance.LOW, "Metric prefix");


    private KafkaWizardConfig() {
    }

    /**
     * Created a user-friendly configuration representation.
     *
     * @param properties configuration properties
     * @return configuration
     */
    static String toFormattedString(final Map<String, Object> properties) {
        StringBuilder builder = new StringBuilder("KafkaWizardConfig values:\n");
        properties.forEach((k, v) -> {
            String prop = String.format("\t%s = %s\n", k, v);
            builder.append(prop);
        });
        return builder.toString();
    }
}
