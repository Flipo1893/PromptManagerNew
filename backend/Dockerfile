# --- BUILD STAGE ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# pom.xml zuerst kopieren (so cached Docker dependencies)
COPY pom.xml .

# Dependencies laden (so wird build schneller)
RUN mvn dependency:go-offline

# Jetzt den Rest des Codes kopieren
COPY src ./src

# Das JAR bauen
RUN mvn clean package -DskipTests

# --- RUNTIME STAGE ---
FROM eclipse-temurin:21-jre

WORKDIR /app

# JAR aus dem Build Stage kopieren
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
