FROM anapsix/alpine-java:8_server-jre_unlimited
VOLUME /tmp
VOLUME /data
ADD build/libs/shop-supplier-*.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
