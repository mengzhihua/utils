FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/utils.jar /app/utils.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/utils.jar"]
