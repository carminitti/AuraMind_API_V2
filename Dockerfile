# ---------- build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
# Render injeta a porta em $PORT
ENV JAVA_TOOL_OPTIONS="-Xmx350m"
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
