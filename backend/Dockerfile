FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

EXPOSE 8081

CMD ["java", "-jar", "target/*.jar"]
