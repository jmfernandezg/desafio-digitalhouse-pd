plugins {
    base
}

tasks.register<Exec>("npmBuild") {
    dependsOn("npmInstall")
    commandLine("npm", "run", "build")
    workingDir("${project.projectDir}")
}

tasks.named("assemble") {
    dependsOn("npmBuild")
}