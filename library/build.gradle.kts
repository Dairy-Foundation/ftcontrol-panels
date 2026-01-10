/**
 * Top-level build file for ftc_app project.
 *
 * It is extraordinarily rare that you will ever need to edit this file.
 */

buildscript {
    val kotlin_version by extra("2.1.20")

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        // Note for FTC Teams: Do not modify this yourself.
        classpath("com.android.tools.build:gradle:8.7.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

// This is now required because aapt2 has to be downloaded from the
// google() repository beginning with version 3.2 of the Android Gradle Plugin
allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

repositories {
    mavenCentral()
}


fun makePublishAllTask(repository: String) = tasks.register("publishAllReleasePublicationsTo$repository") {
    group = "Publishing"
    description = "publish all release publications except ExamplePlugin to $repository"
    subprojects.forEach { project ->
        if (project.name == "ExamplePlugin") return@forEach
        if (project.name == "TeamCode") return@forEach
        if (project.name == "FtcRobotController") return@forEach
        if (project.name == "plugin-svelte-assets") return@forEach
        dependsOn(project.tasks.getByName("publishReleasePublicationTo$repository"))
    }
}

makePublishAllTask("MavenLocal")
makePublishAllTask("DairyRepository")
