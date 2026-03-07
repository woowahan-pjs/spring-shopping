plugins {
    `java-library`
    id("com.diffplug.spotless") version "6.25.0"
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}