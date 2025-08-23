# ================================================
# 1. Build Stage
# ================================================
FROM eclipse-temurin:17-jdk-alpine AS build

# Install required packages for build
RUN apk add --no-cache \
    bash curl

# Build optimization için layer caching
WORKDIR /build

# 1. Copy gradle wrapper and build files from parent directory
COPY ../gradlew .
COPY ../gradle ./gradle/
COPY ../build.gradle .
COPY ../settings.gradle .
COPY ../dependencies.gradle .
COPY ../gradle.properties* ./

# 2. Give execute permission to gradle wrapper
RUN chmod +x gradlew

# 3. Copy AuthMicroservice specific build file
COPY build.gradle ./AuthMicroservice/

# 4. Download dependencies (cache layer)
RUN ./gradlew :AuthMicroservice:dependencies --no-daemon || true

# 5. Copy AuthMicroservice source code and build
COPY src ./AuthMicroservice/src
RUN ./gradlew :AuthMicroservice:clean :AuthMicroservice:bootJar --no-daemon -x test && \
    # Standardize JAR file name
    mv AuthMicroservice/build/libs/*.jar AuthMicroservice/build/libs/app.jar

# ================================================
# 2. Runtime Stage
# ================================================
FROM eclipse-temurin:17.0.10_7-jre-alpine

# Install required packages
RUN apk add --no-cache \
    curl \
    tzdata \
    dumb-init \
    && rm -rf /var/cache/apk/*

# Application directory
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /build/AuthMicroservice/build/libs/app.jar ./app.jar

# Create non-root user and set permissions
RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port (AuthMicroservice typically runs on 8888)
EXPOSE 8888

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Health check for AuthMicroservice
HEALTHCHECK --interval=30s --timeout=15s --start-period=120s --retries=5 \
    CMD curl --fail --silent --show-error --connect-timeout 10 http://localhost:8888/actuator/health || exit 1

# Use dumb-init for signal handling
ENTRYPOINT ["dumb-init", "--"]

# JVM optimizations and application startup
CMD ["java", \
    # Container support
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    # Garbage Collection optimization
    "-XX:+UseG1GC", \
    "-XX:G1HeapRegionSize=16m", \
    "-XX:+UseStringDeduplication", \
    "-XX:+OptimizeStringConcat", \
    # Startup optimization
    "-XX:+TieredCompilation", \
    "-XX:TieredStopAtLevel=1", \
    # Network optimization
    "-Djava.net.preferIPv4Stack=true", \
    "-Djava.awt.headless=true", \
    # Security
    "-Djava.security.egd=file:/dev/./urandom", \
    # Port configuration (AuthMicroservice specific)
    "-Dserver.port=${PORT:-8888}", \
    # Graceful shutdown
    "-Dspring.lifecycle.timeout-per-shutdown-phase=30s", \
    # JAR file
    "-jar", "app.jar"]