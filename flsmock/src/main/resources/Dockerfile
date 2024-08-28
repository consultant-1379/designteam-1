FROM armdocker.rnd.ericsson.se/proj-am/sles/sles-openjdk11:3.29.0-10
VOLUME /tmp
WORKDIR /root/dataingress/filehandler/flsmock
ENV PORT 9778
EXPOSE 9778
ARG JAR_FILE=flsmock-0.0.1-SNAPSHOT.jar
COPY flsmock-0.0.1-SNAPSHOT.jar /root/dataingress/filehandler/flsmock/flsmock-0.0.1-SNAPSHOT.jar
ENTRYPOINT exec java $JAVA_OPTS -jar flsmock-0.0.1-SNAPSHOT.jar