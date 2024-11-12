
node {
    version.set("23.1.0")
    yarnVersion.set("1.22.22")
    download.set(true)
    workDir.set(file("${project.buildDir}/nodejs"))
    yarnWorkDir.set(file("${project.buildDir}/yarn"))
    nodeProjectDir.set(file("${project.projectDir}"))
}

val yarnInstall by tasks.registering(Exec::class) {
    group = "build"
    description = "Install dependencies using Yarn"
    workingDir = file(".")
    commandLine("yarn", "install")
}

val yarnBuild by tasks.registering(Exec::class) {
    dependsOn(yarnInstall)
    group = "build"
    description = "Build the React application using Yarn"
    workingDir = file(".")
    commandLine("yarn", "build")
}

tasks.named("assemble") {
    dependsOn(yarnBuild)
}