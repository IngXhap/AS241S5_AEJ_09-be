# Stage 1: Build con tu Maven personalizado
FROM robertoespinoza/as241s5_aej_09-be:maven-base AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run con tu Java personalizado
FROM robertoespinoza/as241s5_aej_09-be:jre-base
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]