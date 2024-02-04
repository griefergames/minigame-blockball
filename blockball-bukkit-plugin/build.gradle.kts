import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.*
import java.io.*

plugins {
    id("com.github.johnrengelman.shadow") version ("7.0.0")
}

tasks.withType<Jar> {
    archiveName = "${baseName}-${version}-raw.${extension}"
}

/**
 * Include all blockball-api, blockball-bukkit-api and exclude debugging classes.
 */
tasks.withType<ShadowJar> {
    dependsOn("jar")
    archiveName = "${baseName}-${version}-shadowjar.${extension}"
    exclude("DebugProbesKt.bin")
    exclude("module-info.class")
}

/**
 * Create all plugin jar files.
 */
tasks.register("pluginJars") {
    dependsOn("pluginJarLatest")
    dependsOn("pluginJarLegacy")
}

/**
 * Create legacy plugin jar file.
 */
tasks.register("relocateLegacyPluginJar", ShadowJar::class.java) {
    dependsOn("shadowJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("shadowJar") as Jar).archiveName)))
    archiveName = "${baseName}-${version}-legacy-relocate.${extension}"
    relocate("kotlin", "com.github.shynixn.blockball.lib.kotlin")
    relocate("kotlinx", "com.github.shynixn.blockball.lib.kotlinx")
    relocate("org.intellij", "com.github.shynixn.blockball.lib.org.intelli")
    relocate("org.jetbrains", "com.github.shynixn.blockball.lib.org.jetbrains")
    relocate("org.bstats", "com.github.shynixn.blockball.lib.org.bstats")
    relocate("javax.inject", "com.github.shynixn.blockball.lib.javax.inject")
    relocate("javax.annotation", "com.github.shynixn.blockball.lib.javax.annotation")
    relocate("org.checkerframework", "com.github.shynixn.blockball.lib.org.checkerframework")
    relocate("org.aopalliance", "com.github.shynixn.blockball.lib.org.aopalliance")
    relocate("org.slf4j", "com.github.shynixn.blockball.lib.org.slf4j")
    relocate("com.github.shynixn.mccoroutine", "com.github.shynixn.blockball.lib.com.github.shynixn.mccoroutine")
    relocate("com.google", "com.github.shynixn.blockball.lib.com.google")
    relocate("com.zaxxer", "com.github.shynixn.blockball.lib.com.zaxxer")
    relocate("org.apache", "com.github.shynixn.blockball.lib.org.apache")
    relocate("com.github.shynixn.mcutils", "com.github.shynixn.blockball.lib.com.github.shynixn.mcutils")
    relocate("com.github.shynixn.mccoroutine", "com.github.shynixn.blockball.lib.com.github.shynixn.mccoroutine")

    exclude("plugin.yml")
    rename("plugin-legacy.yml", "plugin.yml")
}

/**
 * Create legacy plugin jar file.
 */
tasks.register("pluginJarLegacy", ShadowJar::class.java) {
    dependsOn("relocateLegacyPluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocateLegacyPluginJar") as Jar).archiveName)))
    archiveName = "${baseName}-${version}-legacy.${extension}"
    // destinationDir = File("C:\\temp\\plugins")
    exclude("kotlin/**")
    exclude("kotlinx/**")
    exclude("org/**")
    exclude("javax/**")
    exclude("com/google/**")
    exclude("com/github/shynixn/mcutils/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("plugin-legacy.yml")
}


/**
 * Create legacy plugin jar file.
 */
tasks.register("relocatePluginJar", ShadowJar::class.java) {
    dependsOn("shadowJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("shadowJar") as Jar).archiveName)))
    archiveName = "${baseName}-${version}-relocate.${extension}"
    relocate("org.bstats", "com.github.shynixn.blockball.lib.org.bstats")
    relocate("com.github.shynixn.mcutils", "com.github.shynixn.blockball.lib.com.github.shynixn.mcutils")
}

/**
 * Create latest plugin jar file.
 */
tasks.register("pluginJarLatest", ShadowJar::class.java) {
    dependsOn("relocatePluginJar")
    from(zipTree(File("./build/libs/" + (tasks.getByName("relocatePluginJar") as Jar).archiveName)))
    archiveName = "${baseName}-${version}-latest.${extension}"
    // destinationDir = File("C:\\temp\\plugins")

    exclude("com/github/shynixn/mcutils/**")
    exclude("com/github/shynixn/mccoroutine/**")
    exclude("org/**")
    exclude("kotlin/**")
    exclude("kotlinx/**")
    exclude("javax/**")
    exclude("com/google/**")
    exclude("plugin-legacy.yml")
}


repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://shynixn.github.io/m2/repository/mcutils")
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
    implementation(project(":blockball-api"))

    implementation("com.github.shynixn.mcutils:common:1.0.48")
    implementation("com.github.shynixn.mcutils:packet:1.0.65")

    implementation("com.github.shynixn.org.bstats:bstats-bukkit:1.7")
    implementation("org.slf4j:slf4j-jdk14:1.7.25")
    implementation("com.google.inject:guice:5.0.1")
    implementation("commons-io:commons-io:2.6")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.13.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    compileOnly("me.clip:placeholderapi:2.9.2")
    compileOnly("net.milkbowlvault:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot:1.16.4-R0.1-SNAPSHOT")
    compileOnly(ggcloud.)

    testImplementation("org.xerial:sqlite-jdbc:3.23.1")
    testImplementation("org.spigotmc:spigot:1.16.4-R0.1-SNAPSHOT")
}

tasks.register("languageFile") {
    val kotlinSrcFolder = project.sourceSets.toList()[0].allJava.srcDirs.first { e -> e.endsWith("java") }
    val languageKotlinFile = kotlinSrcFolder.resolve("com/github/shynixn/blockball/BlockBallLanguage.kt")
    val resourceFile = kotlinSrcFolder.parentFile.resolve("resources").resolve("lang").resolve("en_us.properties")
    val bundle = FileInputStream(resourceFile).use { stream ->
        PropertyResourceBundle(stream)
    }

    val contents = ArrayList<String>()
    contents.add("package com.github.shynixn.blockball")
    contents.add("")
    contents.add("object BlockBallLanguage {")
    for (key in bundle.keys) {
        val value = bundle.getString(key)
        contents.add("  /** $value **/")
        contents.add("  var ${key} : String = \"$value\"")
        contents.add("")
    }
    contents.removeLast()
    contents.add("}")

    languageKotlinFile.printWriter().use { out ->
        for (line in contents) {
            out.println(line)
        }
    }
}
