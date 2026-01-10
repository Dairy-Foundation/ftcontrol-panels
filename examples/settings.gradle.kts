pluginManagement {
    includeBuild("../library/plugin-svelte-assets")
    repositories {
        gradlePluginPortal()
        maven("https://repo.dairy.foundation/releases")
    }
}

rootProject.name = "examples"

val modules = listOf(
    "OpModeControl",
    "ExamplePlugin",
    "Telemetry",
    "Configurables",
    "Themes",
    "Capture",
    "LimelightProxy",
    "Field",
    "Gamepad",
    "Docs",
    "Battery",
    "FullPanels",
    "Panels",
    "Utils",
    "Pinger",
    "Graph",
    "Lights",
    "CameraStream"
)

modules.forEach { name ->
    include(":$name")
    project(":$name").projectDir = file("../library/$name")
}

include(":FtcRobotController")
include(":TeamCode")