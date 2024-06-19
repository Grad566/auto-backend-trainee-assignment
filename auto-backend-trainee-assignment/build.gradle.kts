plugins {
    id("java")
    checkstyle
    jacoco
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("io.javalin:javalin:6.1.3")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.224")
    compileOnly("org.projectlombok:lombok:1.18.32")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("gg.jte:jte:3.1.12")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("io.javalin:javalin-bundle:6.1.3")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.mockito:mockito-core:4.6.1")
    testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:4.3.0")
    testImplementation("io.github.bonigarcia:webdrivermanager:5.2.3")
    implementation(platform("com.konghq:unirest-java-bom:4.3.0"))
    implementation("com.konghq:unirest-java-core")
    implementation("com.konghq:unirest-modules-gson")
    implementation("com.konghq:unirest-modules-jackson")
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation ("org.jsoup:jsoup:1.17.2")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

application {
    mainClass = "App"
}