# ---------- Stage 1: Build the application ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies (cached unless pom changes)
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot application
RUN mvn -B package -DskipTests


# ---------- Stage 2: Runtime image ----------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]