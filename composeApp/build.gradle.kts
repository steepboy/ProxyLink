import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.selenium)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.jsoup)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "me.yiski.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.AppImage, TargetFormat.Exe, TargetFormat.Deb)
            packageName = rootProject.name
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("src/desktopMain/resources/icons/link-logo-windows.ico"))
            }

            linux {
                iconFile.set(project.file("src/desktopMain/resources/icons/link-logo-linux.png"))
            }
        }
    }
}
