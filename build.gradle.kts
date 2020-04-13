plugins {
    java
    checkstyle
    `maven-publish`
    signing
}

group = "com.github.vadeg"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
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

publishing {
    publications {
        create<MavenPublication>("kafkaLib") {
            from(components["java"])
            pom {
                description.set("Library to publish metrics from Kafka producer or consumer using Dropwizard metrics.")
                url.set("https://github.com/vadeg/kafkawizard-metrics")
                name.set("Kafka client integration with Dropwizard")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("vadeg")
                        name.set("Vadim")
                        email.set("5587834+vadeg@users.noreply.github.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/vadeg/kafkawizard-metrics.git")
                    developerConnection.set("scm:git:ssh://github.com:vadeg/kafkawizard-metrics.git")
                    url.set("http://github.com/vadeg/kafkawizard-metrics/tree/master")
                }
            }
        }
    }

    repositories {
        maven {
            name = "LocalRepo"
            url = uri("file://$buildDir/repo")
        }
        mavenCentral {
            name = "CentralSnapshot"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
            credentials {
                username = System.getenv("OSS_USER")
                password = System.getenv("OSS_PASSWORD")
            }
            mavenContent {
                snapshotsOnly()
            }
        }
        mavenCentral {
            name = "CentralRelease"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = System.getenv("OSS_USER")
                password = System.getenv("OSS_PASSWORD")
            }
            mavenContent {
                releasesOnly()
            }
        }
    }
}

signing {
    sign(publishing.publications["kafkaLib"])
}