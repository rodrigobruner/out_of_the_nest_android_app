plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}
//def MAPS_API_KEY = project.findProperty("MAPS_API_KEY") ?: ""
val MAPS_API_KEY: String = project.findProperty("MAPS_API_KEY") as? String ?: ""
android {
    namespace = "app.outofthenest"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.outofthenest"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        buildConfigField "String", "MAPS_API_KEY", "\"${MAPS_API_KEY}\""
        buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.maps)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.location)
    implementation(libs.places)
    implementation(libs.logging.interceptor)
    implementation(libs.android.maps.utils)
    implementation(libs.google.maps.services)
    implementation(libs.slf4j.simple)
    implementation(libs.firebase.messaging)
    implementation(libs.glide)
    implementation(libs.google.places)
    annotationProcessor(libs.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}