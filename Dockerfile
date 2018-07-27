FROM java:8-alpine
RUN apk update && apk add tzdata
RUN cp "/usr/share/zoneinfo/Asia/Shanghai" /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
VOLUME /tmp
VOLUME /usr/app/logs
WORKDIR /usr/app
ADD ./target/adzuki-admin-0.0.1-SNAPSHOT.jar /usr/app/app.war
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar"]