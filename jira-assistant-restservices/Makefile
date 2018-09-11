debug:
	gradle --refresh-dependencies clean build
	docker build -t jassistant .
	docker run --rm \
		-v $(PWD)/JiraAssistant.properties:/usr/local/tomcat/conf/JiraAssistant.properties \
		-p 8080:8080 \
		-p 8000:8000 \
		-e "JPDA_ADDRESS=8000" \
		-e "JPDA_TRANSPORT=dt_socket" \
		jassistant catalina.sh jpda run
