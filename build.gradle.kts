plugins {
    id("java")
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
//    kotlin("plugin.jpa") version "1.9.24"
//    kotlin("jvm") version "1.9.24"
//    kotlin("plugin.spring") version "1.9.24"
//    kotlin("kapt") version "1.9.24" // 추가: Kotlin KAPT 플러그인
}

group = "camp.nextstep.edu"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JWT and Lombok
    implementation ("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation ("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation ("io.jsonwebtoken:jjwt-jackson:0.11.2")
//    implementation("io.jsonwebtoken:jjwt:0.9.1")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // google guava
    implementation("com.google.guava:guava:33.2.1-jre")

    testImplementation("io.rest-assured:rest-assured:5.4.0")

    // spring security core
    implementation("org.springframework.security:spring-security-core:6.3.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.2")
    }
}


//kotlin {
//    compilerOptions {
//        freeCompilerArgs.addAll("-Xjsr305=strict")
//    }
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
