# Etapa 1: Construcción (Build)
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copiamos el wrapper de Maven y el archivo de dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargamos las dependencias
RUN ./mvnw dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el .jar generado
COPY --from=build /app/target/*.jar app.jar

# Render asigna el puerto mediante la variable de entorno PORT.
# Exponemos 8081 por defecto para desarrollo local.
EXPOSE 8081

# Le decimos a Spring Boot que arranque en el puerto que Render asigne (${PORT}),
# o en el 8081 si no encuentra esa variable (como en tu entorno local).
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8081}"]