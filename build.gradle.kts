plugins {
	java
	id("org.springframework.boot") version "3.4.0-RC1"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.pablog"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
