FROM openjdk:8-jre-slim

MAINTAINER liboware@gmail.com

ENV PARAMS=""

ENV TZ=PRC

ENV NACOS_ADDRESS="172.17.0.3"

ENV JAVA_OPTS="-Xms256m -Xmx256m -Xmn128m -Xloggc:./gc/server_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD target/assemble-apiGateway.jar /assemble-apiGateway.jar

EXPOSE 8001

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /assemble-apiGateway.jar $PARAMS"]