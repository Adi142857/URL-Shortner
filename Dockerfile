FROM eclipse-temurin:17

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

EXPOSE 8082

CMD ["java", "-jar", "target/url-shortner-0.0.1-SNAPSHOT.jar"]
