# ---------- build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR gerado pelo build
COPY --from=build /app/target/*.jar /app/app.jar

# Variáveis de ambiente padrão do Render
ENV JAVA_TOOL_OPTIONS="-Xmx350m"
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

# Expõe a porta para o Render detectar
EXPOSE 8080

# Inicia a aplicação
CMD ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dserver.port=${PORT} -jar /app/app.jar"]
