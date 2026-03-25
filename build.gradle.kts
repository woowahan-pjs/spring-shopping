plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    // Flyway 마이그레이션을 Gradle 태스크로 직접 실행하기 위해 추가한다.
    id("org.flywaydb.flyway") version "12.0.1"
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"
extra["springCloudVersion"] = "2025.0.1"

java {
    toolchain {
        // 로컬과 CI에서 동일한 JDK 버전으로 빌드되도록 Java 21을 고정한다.
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

// 단위/통합 테스트와 분리된 실HTTP E2E 테스트 전용 소스셋을 추가한다.
val e2eTest by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output + configurations.testRuntimeClasspath.get()
    runtimeClasspath += output + compileClasspath
}

// e2eTest도 test 소스셋과 동일한 테스트 의존성 구성을 상속받도록 맞춘다.
configurations[e2eTest.implementationConfigurationName].extendsFrom(configurations.testImplementation.get())
configurations[e2eTest.runtimeOnlyConfigurationName].extendsFrom(configurations.testRuntimeOnly.get())
configurations[e2eTest.compileOnlyConfigurationName].extendsFrom(configurations.testCompileOnly.get())
configurations[e2eTest.annotationProcessorConfigurationName].extendsFrom(configurations.testAnnotationProcessor.get())

dependencies {
    // Spring Boot 애플리케이션의 웹/뷰/검증/JPA 기본 기능을 구성한다.
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // 비밀번호 해싱 등 보안 유틸리티만 가볍게 사용하기 위해 crypto 모듈만 추가한다.
    implementation("org.springframework.security:spring-security-crypto")

    // JWT는 컴파일 시 API에 의존하고, 구현체와 Jackson 직렬화 지원은 런타임에만 로드한다.
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // Flyway가 애플리케이션 실행 시 MySQL 스키마 마이그레이션을 수행할 수 있게 한다.
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // 개발/테스트와 운영에서 서로 다른 데이터베이스 드라이버를 런타임에 선택한다.
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Lombok은 컴파일 단계에서만 필요하므로 소스셋별로 annotation processor와 함께 선언한다.
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // 테스트 실행과 IDE/빌드 툴 연동에 필요한 기본 테스트 라이브러리를 묶는다.
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    // 기본 테스트가 끝나면 즉시 커버리지 리포트를 생성해 로컬 확인과 CI 아티팩트 수집을 쉽게 한다.
    finalizedBy(tasks.jacocoTestReport)
}

// e2eTest 소스셋을 실제 Gradle verification 태스크처럼 실행할 수 있도록 별도 Test 태스크를 만든다.
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
    // check 이전에 사람이 읽을 HTML 리포트와 CI가 수집할 XML 리포트를 모두 남긴다.
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {
    // 전체 품질 하한선을 먼저 확인하고, 핵심 도메인 패키지는 더 엄격한 기준으로 검증한다.
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

        // 핵심 비즈니스 패키지는 회귀 영향이 크므로 패키지 단위 커버리지를 더 높게 유지한다.
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
    // 표준 check에 커버리지 기준 검증을 연결해 CI에서 누락 없이 실패시키도록 한다.
    dependsOn(tasks.jacocoTestCoverageVerification)
}
