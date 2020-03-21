plugins {
    java
    checkstyle
}

group = "com.github.vadeg"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.kafka:kafka_2.12:2.3.0")
    implementation("io.dropwizard.metrics:metrics-core:4.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.1")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.2")
}

tasks.test {
    useJUnitPlatform()
}
