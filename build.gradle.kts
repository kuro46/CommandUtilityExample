
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "5.1.0"
    eclipse
    java
    maven
}

group = "com.github.kuro46"
version = "0.2.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.bukkit", "bukkit", "1.12.2-R0.1-SNAPSHOT")
    implementation("com.github.kuro46", "CommandUtility", "v0.2.0")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", "1.3.41")
}

tasks.withType<Jar> {
    archiveFileName.set("CommandUtilityExample.jar")
}

tasks.withType<ProcessResources> {
    filter { it.replace("\$version", version.toString()) }
}

val relocationPrefix = "com.github.kuro46.commandutilityexample.libs."

tasks.withType<ShadowJar> {
    minimize()
    relocatePackage("arrow")
    relocatePackage("kotlin")
    relocatePackage("org.jetbrains.annotations")
    relocatePackage("com.github.kuro46.commandutility.")
}

fun ShadowJar.relocatePackage(target: String) {
    relocate(target, "$relocationPrefix$target")
}
