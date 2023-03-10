import org.lwjgl.lwjgl
import org.lwjgl.Lwjgl.Module.*

plugins {
    id("java")
    id("org.lwjgl.plugin") version "0.0.30"
    id("io.freefair.lombok") version "6.6.3"
}

group = "com.chaottic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("io.netty:netty-all:4.1.89.Final")
    implementation("it.unimi.dsi:fastutil:8.5.11")
    implementation("org.joml:joml:1.10.5")
    implementation("org.jetbrains:annotations:23.0.0")

    lwjgl {
        implementation(core, glfw, opengl, stb)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}