![Build & Test](https://github.com/vadeg/kafkawizard-metrics/workflows/Build%20&%20Test/badge.svg?branch=master)
![Maven Central](https://img.shields.io/maven-central/v/com.github.vadeg/kafkawizard)

# Kafka clients metrics collector

A tool which collects producer and consumer Kafka metrics and expose them using [Dropwizard](https://metrics.dropwizard.io/4.1.2/index.html) metric library.

# Features

* Expose Kafka consumer and producer metrics using Dropwizard
* Supports default and custom `MetricRegistry`
* Group metrics by `client.id` property by default

# Installation

Check latest version in [Maven Central](https://search.maven.org/search?q=g:com.github.vadeg%20AND%20a:kafkawizard). 

## Maven
```xml
<dependency>
  <groupId>com.github.vadeg</groupId>
  <artifactId>kafkawizard</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Gradle 
### Groovy DSL
```
implementation 'com.github.vadeg:kafkawizard:0.0.1'
```

### Kotlin DSL
```
implementation("com.github.vadeg:kafkawizard:0.0.1")
```

## Scala
```sbt
libraryDependencies += "com.github.vadeg" % "kafkawizard" % "0.0.1"
```

# Usage

Add the following property to Kafka producer or consumer configuration:
```java
props.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, KafkaWizardConfig.REPORTER_NAME);
```

## Example
```java
// Configure MetricRegistry and reporter
MetricRegistry metricRegistry = SharedMetricRegistries.setDefault("default");
ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
reporter.start(10, TimeUnit.SECONDS);

// Configure Kafka producer
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put(ProducerConfig.METRIC_REPORTER_CLASSES_CONFIG, KafkaWizardConfig.REPORTER_NAME);

Producer producer = new KafkaProducer<>(props);
// some producer logic
```

### Output
```shell script
KafkaWizardConfig values:
	kafkawizard.metrics.registry.name = null
	kafkawizard.metrics.prefix = kafka

No metric registry name provided. Fallback to default
Kafka version: 2.3.0
[Producer clientId=producer-1] Cluster ID: Y4ZFWccXRG6J199AxxG2sQ
-- Gauges ----------------------------------------------------------------------
kafka.app-info.producer-1.commit-id
             value = fc1aaa116b661c8a
kafka.app-info.producer-1.start-time-ms
             value = 1586519482483
kafka.app-info.producer-1.version
             value = 2.3.0
kafka.kafka-metrics-count.producer-1.count
             value = 102.0
```

# Configuration
| Key | Description |
|-----| ------------|
| `kafkawizard.metrics.registry.name` | Name of the registry. If not specified the reported will try to get a default from `SharedMetricRegistries` |
| `kafkawizard.metrics.prefix` | Metrics prefix. Default prefix is `kafka` |
  