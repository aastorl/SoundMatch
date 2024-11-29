import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.soundmatch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.soundmatch"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${properties.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${properties.getProperty("SPOTIFY_CLIENT_SECRET")}\"")
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
        // To enable the use of java.time api's with minSdk < 26
        // coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        // kotlinCompilerExtensionVersion ("1.4.3")
    }
    packagingOptions {
        resources {
           excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
          //  includeAndroidResources = true
        }
    }
}

dependencies {
    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Java.time support for api < 26
    coreLibraryDesugaring (libs.desugar.jdk.libs)
    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    // Navigation
    implementation (libs.androidx.navigation.compose)
    //accompanist
    implementation (libs.accompanist.placeholder.material)
    // image handling
    implementation (libs.coil.compose)
    // lottie compose
    implementation (libs.lottie.compose)
    // testing
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    //noinspection GradleDependency
    // androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$compose_version")
    testImplementation (libs.robolectric)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)

    // retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.jackson)
    // Jackson support for kotlin types
    implementation (libs.jackson.module.kotlin)

    // hilt
    implementation (libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.compiler)


    //exoplayer
    implementation (libs.google.exoplayer.core)
    // for PlayerNotificationManager
    implementation (libs.google.exoplayer.ui)

    // paging
    implementation (libs.androidx.paging.runtime)
    implementation (libs.androidx.paging.compose)

    // color extraction
    implementation (libs.androidx.palette.ktx)

    // mdc components for view
    // This dependency is not needed once the min lines param
    // for Text composable becomes available in compose
    // This parameter is needed in the home screen
    implementation (libs.material)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}
