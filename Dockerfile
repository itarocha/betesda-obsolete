FROM adoptopenjdk/openjdk8:latest
ADD target/betesda.war betesda.war
EXPOSE 8088
ENTRYPOINT [ "java", "-jar" "/betesda.war" ]