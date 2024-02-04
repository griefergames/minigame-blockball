plugins {
    kotlin("jvm")
    id("net.linguica.maven-settings") version "0.5"
}

group = "net.griefergames"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "gg-repo"
        setUrl("https://artifactory.griefergames.dev/artifactory/gg-repo")
    }
    maven {
        name = "paper-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(ggcloud.kotlin.stdlib)
    compileOnly(ggcloud.kotlin.coroutines.core)

    compileOnly(ggcloud.minigame.shared)
    compileOnly(ggcloud.minigame.server)

    compileOnly(ggcloud.paper.api)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    processResources {
        expand("version" to project.version)
    }
}