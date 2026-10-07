plugins {
    alias(libs.plugins.kotlinJvmPlugin)
    alias(libs.plugins.kotlinSerializationPlugin)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}