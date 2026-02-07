# Step 1: Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Runtime stage
FROM public.ecr.aws/lambda/java:21
WORKDIR /var/task

# JAR ko copy karo
COPY --from=builder /app/target/TherapistMS-0.0.1-SNAPSHOT.jar app.jar

# JAR ko extract (unzip) karo seedha /var/task mein
# Ye step BOOT-INF se classes nikal kar bahar le aayega
RUN jar -xf app.jar && rm app.jar

# Handler ka path
CMD [ "com.therapistms.StreamLambdaHandler::handleRequest" ]