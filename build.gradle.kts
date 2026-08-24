plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // El plugin de Hilt arrastra un JavaPoet antiguo que rompe a Room
        // (NoSuchMethodError en ClassName.canonicalName). Forzamos la version
        // que ambos necesitan.
        classpath("com.squareup:javapoet:1.13.0")
    }
}
