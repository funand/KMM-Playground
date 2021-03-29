import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("com.android.library")
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}
kotlin {
    android()
    val sdkName: String? = System.getenv("SDK_NAME")
    val isiOSDevice = sdkName.orEmpty().startsWith("iphoneos")
    if (isiOSDevice) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }
    sourceSets {
        val ktor_version = "1.5.2"
        val sqldelight = "1.4.3"
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")
                implementation("com.squareup.sqldelight:runtime:$sqldelight")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                implementation("io.ktor:ktor-client-android:$ktor_version")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.2")
                implementation("com.squareup.sqldelight:android-driver:$sqldelight")

            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting {
            dependencies{
                implementation("io.ktor:ktor-client-ios:$ktor_version")
                implementation("com.squareup.sqldelight:native-driver:$sqldelight")
            }
        }
        val iosTest by getting

    }
}

sqldelight {
    database("RecipeDatabase") {
        packageName = "com.example.kmmplayground.cache"
        sourceFolders = listOf("sqldelight")
    }
}