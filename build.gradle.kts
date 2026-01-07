plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "com.meo"
version = "0.1.2"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // SQL formatter library
    implementation("com.github.vertical-blank:sql-formatter:2.0.4")

    intellijPlatform {
        // Specify the IntelliJ IDE version used for building and running the plugin
        intellijIdeaCommunity("2023.3.8")

        // Bundled plugins
        bundledPlugin("com.intellij.java")

        // Plugin verifier
        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
}

intellijPlatform {
    pluginVerification {
        ides {
            ide("IC-2023.3.8")
            ide("IC-2022.2.5")
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    // Patch plugin.xml with build information
    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("253.*")
    }

    // Sign the plugin
    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    // Publish the plugin
    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
