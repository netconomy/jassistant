FROM tomcat:9-jdk11
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY ./build/libs/jira-assistant-restservices*-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
