plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.airei.app.phc.attendance"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.airei.app.phc.attendance"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "my_palm_attendance-($versionName)")
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
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Splash
    implementation(libs.androidx.core.splashscreen)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // WorkManager
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // Hilt extension for AndroidX (this enables @ApplicationContext and more)
    implementation(libs.androidx.hilt.common)
    // Add KSP compiler for androidx.hilt (required for Hilt + WorkManager)
    ksp(libs.androidx.hilt.compiler)
    // Crypto
    implementation(libs.androidx.security.crypto)
    // Lottie
    implementation(libs.lottie)
    // Gson
    implementation(libs.gson)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // OkHttp (for logging network requests)
    implementation(libs.logging.interceptor)

    // CameraX
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    // MLKit
    implementation(libs.face.detection)
    implementation(libs.play.services.mlkit.face.detection)
    // Tensorflow-Lite
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite)

    // swipe refresh layout
    implementation(libs.androidx.swiperefreshlayout)
    //Room
    // Room DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}