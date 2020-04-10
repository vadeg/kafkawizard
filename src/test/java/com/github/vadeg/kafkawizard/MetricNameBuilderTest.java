package com.github.vadeg.kafkawizard;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.metrics.Metrics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public final class MetricNameBuilderTest {

    static Stream<Arguments> generateMetricNames() {
        Metrics metrics = new Metrics();
        return Stream.of(
                arguments(metrics.metricName("metric-1", "group-1"), "group-1.metric-1"),
                arguments(
                        metrics.metricName(
                                "metric-1",
                                "group-1",
                                Map.ofEntries(entry("key-1", "tag-1"))
                        ),
                        "group-1.tag-1.metric-1"
                ),
                arguments(
                        metrics.metricName(
                                "metric-1",
                                "group-1",
                                Map.ofEntries(entry(CommonClientConfigs.CLIENT_ID_CONFIG, "tag-1"),
                                              entry("key-2", "tag-2"))
                        ),
                        "group-1.tag-1.tag-2.metric-1"
                )
        );
    }

    @SafeVarargs
    private static Map<String, String> orderedTags(final Entry<String, String>... entries) {
        Map<String, String> map = new LinkedHashMap<>();
        Arrays.stream(entries).forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return map;
    }

    @ParameterizedTest
    @MethodSource("generateMetricNames")
    void createMetricNameWithoutPrefix(final MetricName metricName, final String expected) {
        Assertions.assertEquals(expected, MetricNameBuilder.noPrefix().build(metricName));
    }

    @Test
    void createMetricNameWith() {
        MetricNameBuilder nameBuilder = MetricNameBuilder.withPrefix("prefix");
        Metrics metrics = new Metrics();
        MetricName metricName = metrics.metricName("metric-1", "group-1");
        Assertions.assertEquals("prefix.group-1.metric-1", nameBuilder.build(metricName));
    }

    @Test
    void passNullInBuilder() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> MetricNameBuilder.noPrefix().build(null));
    }
}
