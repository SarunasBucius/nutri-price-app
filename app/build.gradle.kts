import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.parcelize)
    id("com.apollographql.apollo") version "4.1.1"
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

apollo {
    service("service") {
        packageName.set("com.github.sarunasbucius.nutriprice.graphql")
    }
}

android {
    namespace = "com.github.sarunasbucius.nutriprice"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.sarunasbucius.nutriprice"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", localProperties.getProperty("API_URL"))
        }

        debug {
            buildConfigField("String", "API_URL", localProperties.getProperty("API_URL"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.retrofit.bom))
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.retrofitBundle)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)

    implementation(libs.apollo.runtime)
}