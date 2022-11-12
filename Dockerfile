FROM gradle:jdk17-alpine
COPY . /tmp

WORKDIR /tmp
RUN gradle build --no-daemon
EXPOSE 10081
ENTRYPOINT ["java", "-jar","/tmp/build/libs/ReactiveService-0.0.1-SNAPSHOT.jar"]