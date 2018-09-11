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
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;
import net.netconomy.jiraassistant.sprintforecast.data.Forecast;
import net.netconomy.jiraassistant.sprintforecast.services.ForecastToCSVService;
import net.netconomy.jiraassistant.sprintforecast.services.SprintForecastService;

@Service
public class SprintForecastDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintForecastDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    SprintForecastService forecastService;

    @Autowired
    ForecastToCSVService forecastToCSVService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    private Forecast getSprintForecast(String givenBacklogDefinition, String andClause, String forecastedVelocity,
            String numberOfSprints, Boolean definedByFilter) throws ConfigurationException {

        Forecast forecast;
        List<String> wantedFields;
        ClientCredentials credentials;
        List<String> projects;

        credentials = configuration.getClientCredentials();
        wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (definedByFilter) {
            forecast = forecastService.generateForecastBasedOnFilter(credentials, givenBacklogDefinition,
                    forecastedVelocity, numberOfSprints, wantedFields);
        } else {
            projects = basicDataService.splitString(givenBacklogDefinition, ",");

            forecast = forecastService.generateForecastBasedOnProjects(credentials, projects, andClause,
                    forecastedVelocity, numberOfSprints, wantedFields);
        }

        return forecast;

    }

    public String getSprintForecastJSON(String givenBacklogDefinition, String andClause, String forecastedVelocity,
            String numberOfSprints, Boolean definedByFilter) {

        Forecast forecast;

        try {

            forecast = getSprintForecast(givenBacklogDefinition, andClause, forecastedVelocity, numberOfSprints,
                    definedByFilter);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(forecast);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }
    }

    @Async
    public Future<Path> createForecastCSVFile(Path tmpFile, String givenBacklogDefinition, String andClause,
            String forecastedVelocity, String numberOfSprints, Boolean definedByFilter) throws ConfigurationException {

        Forecast forecast;
        CSVTable csvTable;

        forecast = getSprintForecast(givenBacklogDefinition, andClause, forecastedVelocity, numberOfSprints,
                    definedByFilter);

        csvTable = forecastToCSVService.generateCSVTableFromForecast(forecast);

        fileOutput.writeCSVTableToFile(csvTable, tmpFile, null, null);

        return new AsyncResult<Path>(tmpFile);

    }

}
