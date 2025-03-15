plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kwos.smokecolumnapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kwos.smokecolumnapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.2"

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
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    //implementation(libs.geodesy)
    implementation("org.gavaghan:geodesy:1.1.3")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.exifinterface:exifinterface:1.4.0")
    implementation("com.drewnoakes:metadata-extractor:2.18.0")
    implementation("org.apache.tika:tika-core:3.0.0")
    implementation("com.adobe.xmp:xmpcore:6.1.11")
    implementation("org.apache.commons:commons-io:1.3.2")
    implementation("org.jdom:jdom2:2.0.6")
    implementation(files("libs/PhotoView-2.3.0.jar"))
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    //implementation("com.github.chrisbanes:photoview:2.1.4")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}