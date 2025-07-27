plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt") // Add this line
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") // Add this line
}

android {
    namespace = "com.example.localexplorer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.localexplorer"
        minSdk = 24
        targetSdk = 36
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3") // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3") // If you prefer LiveData over StateFlow

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0-beta02") // Latest stable Navigation Compose

    // Retrofit (for RESTful API calls)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // For JSON parsing with Gson

    // Coil (for image loading from URLs in Compose)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Room Database (for local persistence)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.7.2") // KAPT for annotation processing
    implementation("androidx.room:room-ktx:2.6.1") // Kotlin Coroutine support for Room

    // Google Maps Compose (for map integration)
    implementation("com.google.maps.android:maps-compose:4.4.0") // Latest stable Maps Compose
    implementation("com.google.android.gms:play-services-maps:18.2.0") // Base Maps SDK

    // Location Services (for getting user's current location)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}