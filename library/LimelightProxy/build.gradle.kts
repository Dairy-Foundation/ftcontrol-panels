val pluginNamespace = "com.bylazar.limelightproxy"
val pluginVersion = "1.0.4"

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.bylazar.svelte-assets")
    id("dev.frozenmilk.publish") version "0.0.5"
    id("dev.frozenmilk.doc") version "0.0.5"
    id("dev.frozenmilk.build-meta-data") version "0.0.2"
}

svelteAssets {
    assetsPath = assetPathForPlugin(pluginNamespace)
}

dairyPublishing {
    gitDir = file("..")
}

version = "${dairyPublishing.version}+$pluginVersion"

meta {
    packagePath = pluginNamespace
    name = "LimelightProxy"
    registerField("name", "String", "\"$pluginNamespace\"")
    registerField("clean", "Boolean") { "${dairyPublishing.clean}" }
    registerField("gitRef", "String") { "\"$version\"" }
    registerField("snapshot", "Boolean") { "${dairyPublishing.snapshot}" }
    registerField("version", "String") { "\"${dairyPublishing.version}\"" }
}

android {
    namespace = pluginNamespace

    defaultConfig {
        compileSdk = 34
        minSdk = 24
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {}
    }
}

dependencies {
    compileOnly("org.firstinspires.ftc:Inspection:11.0.0")
    compileOnly("org.firstinspires.ftc:Blocks:11.0.0")
    compileOnly("org.firstinspires.ftc:RobotCore:11.0.0")
    compileOnly("org.firstinspires.ftc:RobotServer:11.0.0")
    compileOnly("org.firstinspires.ftc:OnBotJava:11.0.0")
    compileOnly("org.firstinspires.ftc:Hardware:11.0.0")
    compileOnly("org.firstinspires.ftc:FtcCommon:11.0.0")
    compileOnly("org.firstinspires.ftc:Vision:11.0.0")

    compileOnly(project(":Panels"))

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = pluginNamespace.substringBeforeLast('.') + ".sloth"
                artifactId = pluginNamespace.substringAfterLast('.')

                artifact(dairyDoc.dokkaJavadocJar)
                artifact(dairyDoc.dokkaHtmlJar)

                pom {
                    description.set("Panels Limelight Proxy Plugin")
                    name.set("Panels Limelight Proxy")
                    url.set("https://panels.bylazar.com")

                    developers {
                        developer {
                            id.set("lazar")
                            name.set("Lazar Dragos George")
                            email.set("hi@bylazar.com")
                        }
                    }
                }
            }
        }
    }
}
