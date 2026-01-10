val pluginNamespace = "com.bylazar.panels"
val pluginVersion = "1.0.5"

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.bylazar.svelte-assets")
    id("dev.frozenmilk.publish") version "0.0.5"
    id("dev.frozenmilk.doc") version "0.0.5"
    id("dev.frozenmilk.build-meta-data") version "0.0.2"
}

svelteAssets {
    assetsPath = "web"
    buildDirPath = "build"
}

dairyPublishing {
    gitDir = file("..")
}

version = "${dairyPublishing.version}+$pluginVersion"

meta {
    packagePath = pluginNamespace
    name = "Panels"
    registerField("name", "String", "\"$pluginNamespace\"")
    registerField("clean", "Boolean") { "${dairyPublishing.clean}" }
    registerField("gitRef", "String") { "\"${dairyPublishing.gitRef}\"" }
    registerField("snapshot", "Boolean") { "${dairyPublishing.snapshot}" }
    registerField("version", "String") { "\"$version\"" }
}

android {
    namespace = "com.bylazar.panels"

    defaultConfig {
        compileSdk = 34
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
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

    publishing {
        singleVariant("release") {}
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    compileOnly("org.firstinspires.ftc:Inspection:11.0.0")
    compileOnly("org.firstinspires.ftc:Blocks:11.0.0")
    compileOnly("org.firstinspires.ftc:RobotCore:11.0.0")
    compileOnly("org.firstinspires.ftc:RobotServer:11.0.0")
    compileOnly("org.firstinspires.ftc:OnBotJava:11.0.0")
    compileOnly("org.firstinspires.ftc:Hardware:11.0.0")
    compileOnly("org.firstinspires.ftc:FtcCommon:11.0.0")
    compileOnly("org.firstinspires.ftc:Vision:11.0.0")

    implementation("org.nanohttpd:nanohttpd-websocket:2.3.1") {
        exclude(module = "nanohttpd")
    }

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")

    implementation("org.tukaani:xz:1.9")
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
                    description.set("All in one toolbox dashboard for FTC.")
                    name.set("Panels")
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
