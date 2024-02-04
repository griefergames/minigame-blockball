pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if(requested.id.namespace == "net.griefergames.ggframe.gradle") {
                useModule("ggframe-gradle-plugin:${requested.version}")
            }
        }
    }
    repositories {
        maven {
            url = uri("https://artifactory.griefergames.dev/artifactory/gg-repo")
        }
        maven("https://repo.papermc.io/repository/maven-public/")
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        maven {
            name = "gg-repo"
            url = uri("https://artifactory.griefergames.dev/artifactory/gg-repo")
        }
        gradlePluginPortal()
    }
    versionCatalogs {
        create("ggcloud") {
            from("net.griefergames.ggcloud:cloud-dependency-catalog:1.20")
        }
    }
}
rootProject.name = "blockball-root"
include("blockball-api")
include("blockball-bukkit-plugin")
include("blockball-tools")
