plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("com.mysql:mysql-connector-j:8.0.33")
    implementation ("org.apache.logging.log4j:log4j-core:2.12.1")
}

tasks.test {
    useJUnitPlatform()
}