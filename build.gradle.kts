plugins {
    kotlin("jvm") version "2.0.21"
    id("org.springframework.boot") version "3.3.5" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    kotlin("plugin.spring") version "2.1.0-RC" apply false
    id("com.github.node-gradle.node") version "7.1.0"
}

allprojects {
    group = "com.jmfg.certs"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.named("compileKotlin", org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java) {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project(":backend") {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-spring")

    dependencies {
        implementation(kotlin("stdlib"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        runtimeOnly("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project(":core") {
    dependencies {
        implementation(kotlin("stdlib"))
    }
}

project(":frontend") {
    apply(plugin = "com.github.node-gradle.node")

    node {
        version.set("22.11.0")
        download.set(true)
        workDir.set(file("${project.layout.buildDirectory}/nodejs"))
        npmWorkDir.set(file("${project.layout.buildDirectory}/npm"))
        nodeProjectDir.set(file("${project.layout.buildDirectory}/"))
    }

    tasks.named("build") {
        dependsOn("npmInstall")
    }
}
