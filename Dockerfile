FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts google-style.xml ./
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies || true
COPY src src
RUN ./gradlew --no-daemon bootJar -x test -x asciidoctor

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.minikube.internal:3306/shopping
ENV SPRING_DATA_MONGODB_URI=mongodb://host.minikube.internal:27017/shopping
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
