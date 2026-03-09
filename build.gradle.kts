plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "12.0.1"
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"
extra["springCloudVersion"] = "2025.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

fun openInBrowser(reportFile: java.io.File) {
    if (!reportFile.exists()) {
        throw GradleException("Report not found: ${reportFile.absolutePath}")
    }

    val osName = System.getProperty("os.name").lowercase()
    val command = when {
        osName.contains("mac") -> listOf("open", reportFile.absolutePath)
        osName.contains("win") -> listOf("cmd", "/c", "start", "", reportFile.absolutePath)
        else -> listOf("xdg-open", reportFile.absolutePath)
    }

    val process = ProcessBuilder(command)
            .inheritIO()
            .start()
    val exitCode = process.waitFor()
    if (exitCode != 0) {
        throw GradleException("Failed to open report in browser: ${reportFile.absolutePath}")
    }
}

val e2eTest by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
    runtimeClasspath += output + compileClasspath
}

configurations[e2eTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[e2eTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
configurations[e2eTest.compileOnlyConfigurationName].extendsFrom(configurations.testCompileOnly.get())
configurations[e2eTest.annotationProcessorConfigurationName].extendsFrom(configurations.testAnnotationProcessor.get())

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

val e2eTestTask = tasks.register<Test>("e2eTest") {
    description = "실HTTP E2E 테스트를 실행한다."
    group = "verification"
    testClassesDirs = e2eTest.output.classesDirs
    classpath = e2eTest.runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
    reports {
        html.required.set(true)
        junitXml.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/tests/e2eTest"))
        junitXml.outputLocation.set(layout.buildDirectory.dir("test-results/e2eTest"))
    }
}

tasks.register("openCoverageReport") {
    description = "JaCoCo HTML 리포트를 브라우저로 연다."
    group = "verification"
    dependsOn(tasks.jacocoTestReport)
    doLast {
        openInBrowser(layout.buildDirectory.file("reports/jacoco/test/html/index.html").get().asFile)
    }
}

tasks.register("openE2eReport") {
    description = "E2E 테스트 HTML 리포트를 브라우저로 연다."
    group = "verification"
    dependsOn(e2eTestTask)
    doLast {
        openInBrowser(layout.buildDirectory.file("reports/tests/e2eTest/index.html").get().asFile)
    }
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            element = "BUNDLE"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.85".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.75".toBigDecimal()
            }
        }

        listOf(
                "shopping/auth*",
                "shopping/member*",
                "shopping/product*",
                "shopping/wish*"
        ).forEach { packagePattern ->
            rule {
                element = "PACKAGE"
                includes = listOf(packagePattern)
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.95".toBigDecimal()
                }
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.90".toBigDecimal()
                }
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
