

val npmInstall by tasks.registering(Exec::class) {
    group = "build"
    description = "Install dependencies using npm"
    workingDir = file(".")
    commandLine("npm", "install")
}

val npmBuild by tasks.registering(Exec::class) {
    dependsOn(npmInstall)
    group = "build"
    description = "Build the React application using npm"
    workingDir = file(".")
    commandLine("npm", "run", "build")
}

tasks.named("assemble") {
    dependsOn(npmBuild)
}