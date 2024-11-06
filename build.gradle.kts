plugins {
    kotlin("jvm") version "2.0.21"
    id("org.springframework.boot") version "3.3.5" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    kotlin("plugin.spring") version "2.1.0-RC" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
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
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
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
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        runtimeOnly("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project(":core") {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
}

project(":frontend") {
    apply(plugin = "com.github.node-gradle.node")

    node {
        version.set("18.17.1")
        download.set(true)
        workDir.set(file("${project.buildDir}/nodejs"))
        npmWorkDir.set(file("${project.buildDir}/npm"))
        nodeProjectDir.set(file("${project.buildDir}/"))
    }

    tasks.named("build") {
        dependsOn("npmInstall")
    }
}
