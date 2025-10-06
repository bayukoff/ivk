plugins {
    id("java")
    id("application")
}

group = "ru.coolz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application{
    mainClass.set("ru.coolz.fileshare.FileShareMain")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.eclipse.jetty:jetty-server:12.1.1")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.1.1")
    implementation("ch.qos.logback:logback-core:1.5.19")
    implementation("ch.qos.logback:logback-classic:1.5.19")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}