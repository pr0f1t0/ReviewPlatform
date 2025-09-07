plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
    id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.36"
}

group = "com.pr0f1t"
version = "0.0.1"
description = "Educational purposes project - restaurant review platform"

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

val lombokVersion = "1.18.36"
val mapStructVersion = "1.6.3"
val lombokMapstructBindingVersion = "0.2.0"
val minioVersion = "8.5.17"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("io.minio:minio:$minioVersion")

	// Lombok
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")

	// MapStruct
	implementation("org.mapstruct:mapstruct:$mapStructVersion")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

	// Lombok-MapStruct binding
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
	useJUnitPlatform()
}
