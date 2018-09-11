FROM tomcat:8-jre8-alpine

RUN apk --update add ca-certificates
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY ./build/libs/jira-assistant-restservices*-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
