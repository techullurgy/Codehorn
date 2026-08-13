import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.kotlinJvmPlugin)
    alias(libs.plugins.springPlugin)
    alias(libs.plugins.springBootPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
}

dependencies {
    implementation(project(":common"))

    implementation(libs.spring.boot.starter)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)

    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.kotlin.reactor.extensions)
}

tasks.withType<BootJar>().configureEach {
    enabled = false
}