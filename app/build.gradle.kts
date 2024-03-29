plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.manish.hotelbookingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.manish.hotelbookingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "EMAIL_SEND_PASS_KEY",
            "${project.properties["email_send_pass_key"]}"
        )
        buildConfigField(
            "String",
            "EMAIL_SEND_EMAIL",
            "${project.properties["email_send_email"]}"
        )
        buildConfigField(
            "String",
            "HOTELS_API_BASE_URL",
            "${project.properties["base_url"]}"
        )
        buildConfigField(
            "String",
            "X_API_KEY",
            "${project.properties["x_api_key"]}"
        )
        buildConfigField(
            "String",
            "X_API_HOST",
            "${project.properties["x_api_host"]}"
        )

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val glideVersion = "4.16.0"
    val daggerVersion = "2.48"
    val retrofitVersion = "2.9.0"
    val roomVersion = "2.6.1"
    val coroutineVersion = "1.7.3"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Auth - Facebook
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    // Auth - Google
    implementation("com.google.android.gms:play-services-auth:20.5.0")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Preference
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    // Dagger
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")

    // Progress Bar
    implementation("com.airbnb.android:lottie:6.3.0")

    // Email Client
    implementation("com.github.1902shubh:SendMail:1.0.0")

    // Otp
    implementation("com.github.aabhasr1:OtpView:v1.1.2-ktx")

    // Round Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Retrofit
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Room
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}