pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "focus-study"

include(
    ":app",
    ":courseapp",
    ":core:common",
    ":core:model",
    ":core:database",
    ":core:data",
    ":core:ui",
    ":core:testing",
    ":feature:onboarding",
    ":feature:home",
    ":feature:task",
    ":feature:focus",
    ":feature:stats",
    ":feature:advice",
    ":feature:settings",
    ":worker"
)
