/**
*  Copyright 2018 The JASSISTANT Authors.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
**/
package net.netconomy.jiraassistant.projectsetup.services;

import java.util.Scanner;

import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectSetupConfiguration;
import net.netconomy.jiraassistant.base.restclient.RestConnector;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.projectsetup.data.NewProjectConfiguration;

@Service
public class ProjectSetupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSetupService.class);

    @Autowired
    private RestConnector restConnector;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    private JsonBodyCreatorService jsonBodyCreatorService;

    public void createTestProject() {
        
        ClientCredentials clientCredentials = new ClientCredentials();
        String restUrl;
        JSONObject body;
        ResponseEntity<String> response;
        Scanner scanner = new Scanner(System.in);
        String userName = "";
        String password = "";
        ProjectSetupConfiguration projectTypeConfiguration;
        NewProjectConfiguration newProjectConfiguration = new NewProjectConfiguration();
        
        try {

            projectTypeConfiguration = configuration.getProjectSetupConfiguration();

            newProjectConfiguration.setKey("EX");
            newProjectConfiguration.setName("Example");
            newProjectConfiguration.setDescription("Example Project description");
            newProjectConfiguration.setLead("mweilbuchner");
            newProjectConfiguration.setUrl("http://atlassian.com");

            body = jsonBodyCreatorService
                    .createJsonBodyForNewProject(projectTypeConfiguration, newProjectConfiguration);

            System.out.println("Username: ");
            userName = scanner.nextLine();
            System.out.println("Password: ");
            password = String.copyValueOf(System.console().readPassword());

            clientCredentials.setJiraUri("https://jira-test.local.netconomy.net/");

            clientCredentials.setUserName(userName);
            clientCredentials.setPassword(password);

            restUrl = "https://jira-test.local.netconomy.net/rest/api/latest/project";

            response = restConnector.postJsonToRest(clientCredentials, restUrl, body);

            LOGGER.info("Responce was: " + response.toString());

        } catch (JSONException e) {
            LOGGER.error("There was an Exception during the Project Creation", e);
        } catch (ConfigurationException e) {
            LOGGER.error("There was an Exception during the Project Creation", e);
        } finally {
            scanner.close();
        }
        
    }

}
