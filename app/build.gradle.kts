plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.khush.devicemapper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.khush.devicemapper"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildFeatures {
        dataBinding = true
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Using the version from your catalog
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit and Gson for API handling
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    // Or the latest compatible version


    // Coroutines for async calls
    //noinspection UseTomlInstead
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    //noinspection UseTomlInstead
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Google Play Services Location
    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-location:21.3.0") // Explicit version

    // Room components

    //noinspection GradleDependency, UseTomlInstead
    implementation("androidx.room:room-runtime:2.6.1")
    //noinspection UseTomlInstead,  GradleDependency
    annotationProcessor("androidx.room:room-compiler:2.6.1") // Or kapt if not using KSP
    // Optional - Kotlin Extensions and Coroutines support for Room
    //noinspection UseTomlInstead,  GradleDependency
    implementation("androidx.room:room-ktx:2.7.2")

    //noinspection UseTomlInstead, GradleDependency
    implementation("com.google.android.material:material:1.11.0")
    //noinspection UseTomlInstead
    implementation("com.google.android.gms:play-services-location")

    //noinspection UseTomlInstead,GradleDependency
    implementation("androidx.paging:paging-runtime-ktx:3.1.1") // Or the latest version

}
