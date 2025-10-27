# Etapa de construcción
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
# Descargar dependencias primero para aprovechar la caché de Docker
RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src/ /app/src/

# Construir la aplicación
RUN mvn package -DskipTests

# Etapa de producción
FROM eclipse-temurin:17-jre-alpine

# Variables de entorno
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

# Crear usuario no root para mayor seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar /app/application.jar

# Puerto expuesto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/application.jar"]
