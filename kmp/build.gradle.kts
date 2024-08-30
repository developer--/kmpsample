plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.skie) apply false
    // uncomment & comment 20, 21 lines in order to run iOS tests. and comment line 17 & 18
//     alias(libs.plugins.kotlin.serialization) apply false
//     alias(libs.plugins.sqlDelight) apply false
    val isPublishLibraryBuild = System.getenv("GITHUB_WORKFLOW_REF")?.contains("KMMBridge-publish") == true
    if (isPublishLibraryBuild) {
        println("CI build is running")
        alias(libs.plugins.sqlDelight) apply false
        alias(libs.plugins.kotlin.serialization) apply false
    } else {
        println("local build is running")
        id(libs.plugins.sqlDelight.get().pluginId).apply(false)
        id(libs.plugins.kotlin.serialization.get().pluginId).apply(false)
    }
    alias(libs.plugins.kmmbridge) apply false
}