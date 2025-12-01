FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /workspace/target/bajaj-finserv-hiring-challenge.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]