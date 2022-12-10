plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("io.ktor.plugin") version "2.1.3"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
}

group = "com.phoenix"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-auth-jvm:2.1.3")
    implementation("io.ktor:ktor-server-core-jvm:2.1.3")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.1.3")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.1.3")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.1.3")
    implementation("io.ktor:ktor-server-netty-jvm:2.1.3")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.1.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.1.3")

    implementation("org.litote.kmongo:kmongo:4.7.2")
    implementation("org.litote.kmongo:kmongo-coroutine:4.7.2")
    implementation("commons-codec:commons-codec:1.15")
}