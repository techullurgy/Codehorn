plugins {
    alias(libs.plugins.kotlinJvmPlugin)
    alias(libs.plugins.springPlugin)
    alias(libs.plugins.springBootPlugin)
    alias(libs.plugins.springDependencyManagementPlugin)
}

dependencies {
    implementation(project(":common"))
    implementation(libs.bundles.webflux.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.spring.boot.starter.webflux.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.mongodb)

    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.cloud.starter.consul.discovery)
}