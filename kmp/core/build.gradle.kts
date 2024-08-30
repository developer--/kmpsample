import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    println("System.getenv(PUBLISHING) = ${System.getenv("PUBLISHING")}")
    val isPublishLibraryBuild =
        System.getenv("GITHUB_WORKFLOW_REF")?.contains("KMMBridge-publish") == true
    if (isPublishLibraryBuild) { // comment that line to enable local publishing  ./gradlew spmDevBuild -PspmBuildTargets=ios_simulator_arm64
        alias(libs.plugins.kotlin.serialization)
        alias(libs.plugins.sqlDelight)
        alias(libs.plugins.skie)
        println("CI build: core")
    } else {
        println("local build: core")
        id(libs.plugins.kotlin.serialization.get().pluginId).apply(true)
        id(libs.plugins.sqlDelight.get().pluginId).apply(true)
        id(libs.plugins.skie.get().pluginId)
    }
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.kmmbridge)
    `maven-publish`
}

group = "io.example.kmmbridge"

version = "1.5.0"
addGithubPackagesRepository()
kmmbridge {
    mavenPublishArtifacts()
    spm()
}

skie {
    build {
        enableSwiftLibraryEvolution.set(true)
    }
}

val autoVersion = project.property(
    if (project.hasProperty("AUTO_VERSION")) {
        "AUTO_VERSION"
    } else {
        "LIBRARY_VERSION"
    }
) as String

subprojects {
    val GROUP: String by project
    group = GROUP
    version = autoVersion
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "core"
            isStatic = true
            binaryOption("bundleId", "core")
        }
    }

    targets.filterIsInstance<KotlinNativeTarget>().forEach {
        it.binaries.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach { lib ->
                lib.isStatic = false
                lib.linkerOpts.add("-lsqlite3")
            }
        val coroutinesVersion = "1.8.1"
        val ktorVersion = "2.3.7"
        val serializationVersion = "1.6.2"
        val koinVersion = "3.5.6"
        val sqlDelight = "2.0.1"
        sourceSets {
            commonMain {
                dependencies {
                    implementation("io.ktor:ktor-client-core:$ktorVersion")
                    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                    implementation("io.ktor:ktor-client-logging:$ktorVersion")
                    implementation("io.ktor:ktor-client-mock:$ktorVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                    implementation("io.ktor:ktor-client-cio:$ktorVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

                    implementation("app.cash.sqldelight:runtime:$sqlDelight")
                    // SqlDelight extension
                    implementation("app.cash.sqldelight:coroutines-extensions:$sqlDelight")
                    implementation("app.cash.sqldelight:primitive-adapters:$sqlDelight")
                    implementation("io.insert-koin:koin-core:$koinVersion")
                    //shared preferences
                    implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")
                    implementation("com.russhwolf:multiplatform-settings-test:0.9")

                    // date time
                    implementation(libs.kotlinx.dateTime)
                }
            }
            commonTest {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                    implementation("io.insert-koin:koin-test:$koinVersion")
                    implementation("io.mockk:mockk-common:1.12.5")
                }
            }
            androidMain {
                dependencies {
                    implementation("androidx.core:core-ktx:1.12.0")
                    implementation("io.ktor:ktor-client-android:$ktorVersion")
                    implementation("io.ktor:ktor-client-logging:$ktorVersion")
                    implementation("app.cash.sqldelight:android-driver:$sqlDelight")
                    implementation("app.cash.sqldelight:sqlite-driver:$sqlDelight")
                }
            }

            val androidUnitTest by getting {
                dependencies {
                    implementation(kotlin("test-junit"))
                    implementation("junit:junit:4.13.2")
                }
            }

            iosMain {
                dependencies {
                    implementation("io.ktor:ktor-client-ios:$ktorVersion")
                    implementation("app.cash.sqldelight:native-driver:$sqlDelight")
                }
            }
        }
    }
}


android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    namespace = "com.example.kmp.core.android"
}


sqldelight {
    databases {
        create("CoreDb") {
            packageName.set("com.example.kmp.core")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}
