plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "test-tasks"
include("liquids")
include("fileshare")
include("weatherservice")
