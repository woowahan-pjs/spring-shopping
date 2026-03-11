plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("io.cucumber:cucumber-spring:7.20.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.20.1")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("org.junit.platform:junit-platform-suite-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.platform:junit-platform-suite-engine")
    pitest("it.mulders.stryker:pit-dashboard-reporter:0.2.1")
}

repositories {
    mavenCentral()
}

spotless {
    java {
        target("src/**/*.java")
        eclipse("4.26").configFile(rootProject.file("google-style.xml"))
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required = true
        csv.required = false
    }
}

pitest {
    junit5PluginVersion = "1.2.1"
    targetClasses = setOf("shopping.*")
    targetTests = setOf("shopping.*")
    excludedClasses = setOf(
        "shopping.Application",
        "shopping.*.dto.*",
        "shopping.*.controller.*",
        "shopping.*.*Configuration",
        "shopping.*.*Repository",
        "shopping.health.*"
    )
    mutators = setOf("DEFAULTS")
    outputFormats = setOf("HTML")
    threads = Runtime.getRuntime().availableProcessors()
    timestampedReports = false
}

tasks.named("asciidoctor", org.asciidoctor.gradle.jvm.AsciidoctorTask::class) {
    dependsOn("test")
    inputs.dir(file("build/generated-snippets"))
    baseDirFollowsSourceDir()
    sources {
        include("*.adoc")
    }
    attributes(mapOf("snippets" to file("build/generated-snippets").absolutePath))
}

tasks.named("bootJar") {
    dependsOn("asciidoctor")
}
