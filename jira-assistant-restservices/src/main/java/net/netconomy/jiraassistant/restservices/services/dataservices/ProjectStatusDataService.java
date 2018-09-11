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
package net.netconomy.jiraassistant.restservices.services.dataservices;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Future;

import com.atlassian.jira.rest.client.api.RestClientException;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.projectstatus.data.ProjectStatusData;
import net.netconomy.jiraassistant.projectstatus.services.ProjectStatusService;
import net.netconomy.jiraassistant.projectstatus.services.ProjectStatusToCSVService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class ProjectStatusDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectStatusDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    ProjectStatusService projectStatusService;

    @Autowired
    ProjectStatusToCSVService projectStatusToCSVService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    private ProjectStatusData getProjectStatusData(String projects, String groupBy, String excludedTypesString,
            String andClause) throws ConfigurationException {

        ProjectStatusData projectStatusData;
        ClientCredentials credentials;
        List<String> projectList;
        List<String> excludedTypes;

        credentials = configuration.getClientCredentials();

        projectList = basicDataService.splitString(projects, ",");
        excludedTypes = basicDataService.splitString(excludedTypesString, ",");

        projectStatusData = projectStatusService.analyseProjects(credentials, projectList, groupBy, excludedTypes,
                andClause);

        return projectStatusData;

    }

    public String getProjectStatusDataJSON(String projects, String groupBy, String excludedTypes, String andClause) {

        ProjectStatusData projectStatusData;

        try {
            projectStatusData = getProjectStatusData(projects, groupBy, excludedTypes, andClause);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(projectStatusData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createProjectStatusDataCSVFile(Path tmpFile, String projects, String groupBy, String excludedTypes,
            String andClause) throws ConfigurationException {

        ProjectStatusData projectStatusData;
        CSVTable csvTable;

        projectStatusData = getProjectStatusData(projects, groupBy, excludedTypes, andClause);

        csvTable = projectStatusToCSVService.generateCSVTableFromProjectStatus(projectStatusData);

        fileOutput.writeCSVTableToFile(csvTable, tmpFile, null, null);

        return new AsyncResult<Path>(tmpFile);

    }

}
