FROM centos

RUN yum install -y java-11-openjdk-devel

VOLUME /tmp
ADD /target/rest-demo-0.0.1-SNAPSHOT.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom --spring.datasource.password=${MYSQL_PASS}","-jar","/myapp.jar"]
