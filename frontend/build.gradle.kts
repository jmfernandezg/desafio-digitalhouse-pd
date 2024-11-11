
node {
    version.set("23.1.0")
    yarnVersion.set("1.22.22")
    download.set(true)
    workDir.set(file("${project.buildDir}/nodejs"))
    yarnWorkDir.set(file("${project.buildDir}/yarn"))
    nodeProjectDir.set(file("${project.projectDir}"))
}

tasks.register<Delete>("cleanDist") {
    delete(fileTree("dist").matching {
        include("**/*")
    })
}

tasks.register<com.github.gradle.node.yarn.task.YarnTask>("yarnInstall") {
    args.set(listOf("install"))
    inputs.files("package.json", "yarn.lock")
    outputs.dir("node_modules")
}

tasks.register<com.github.gradle.node.yarn.task.YarnTask>("yarnBuild") {
    dependsOn("yarnInstall")
    args.set(listOf("build"))
    inputs.files("package.json", "yarn.lock")
    inputs.dir("src")
    outputs.dir("build")
}

tasks.register<com.github.gradle.node.yarn.task.YarnTask>("yarnTest") {
    dependsOn("yarnInstall")
    args.set(listOf("test", "--watchAll=false"))
    inputs.files("package.json", "yarn.lock")
    inputs.dir("src")
}

tasks.register<com.github.gradle.node.yarn.task.YarnTask>("yarnLint") {
    dependsOn("yarnInstall")
    args.set(listOf("lint"))
    inputs.files("package.json", "yarn.lock")
    inputs.dir("src")
}

tasks.named("clean") {
    dependsOn("cleanDist")
}

tasks.named("check") {
    dependsOn("yarnTest", "yarnLint")
}

tasks.named("assemble") {
    dependsOn("yarnBuild")
}