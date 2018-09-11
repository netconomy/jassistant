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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Future;

import com.atlassian.jira.rest.client.api.RestClientException;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanResultData;
import net.netconomy.jiraassistant.kanbananalysis.services.KanbanResultService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class KanbanAnalysisDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanbanAnalysisDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    KanbanResultService kanbanResultService;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    private KanbanResultData getKanbanAnalysis(String givenBacklogDefinition, String startDateString,
            String endDateString, String lightAnalysis, String relevantProjects, String excludedStatusString,
            String excludedTypesString, String andClause, Boolean definedByFilter, String withAltEstimations) throws ConfigurationException {

        KanbanResultData kanbanResultData;
        List<String> wantedFields;
        ClientCredentials credentials;
        DateTime startDate;
        DateTime endDate;
        Boolean lightAnalysisBool;
        Boolean withAltEstimationsBool;
        List<String> projectsList;
        List<String> relevantProjectsList;
        List<String> excludedStatusList;
        List<String> excludedTypesList;
        DateTimeZone timeZone;

        credentials = configuration.getClientCredentials();
        wantedFields = configuration.getIssueConfiguration().getWantedFields();

        timeZone = DateTimeZone.forID(credentials.getTimeZone());

        DateTimeZone.setDefault(timeZone);

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        lightAnalysisBool = Boolean.valueOf(lightAnalysis);
        withAltEstimationsBool = Boolean.valueOf(withAltEstimations);

        if (definedByFilter) {
            relevantProjectsList = basicDataService.splitString(relevantProjects, ",");

            kanbanResultData = kanbanResultService.calculateKanbanResultBasedOnFilter(credentials,
                    givenBacklogDefinition, startDate, endDate, wantedFields, lightAnalysisBool, relevantProjectsList, withAltEstimationsBool);
        } else {
            projectsList = basicDataService.splitString(givenBacklogDefinition, ",");
            excludedStatusList = basicDataService.splitString(excludedStatusString, ",");
            excludedTypesList = basicDataService.splitString(excludedTypesString, ",");

            kanbanResultData = kanbanResultService.calculateKanbanResultBasedOnProjects(credentials, projectsList,
                    startDate, endDate, excludedStatusList, excludedTypesList, andClause, wantedFields,
                    lightAnalysisBool, withAltEstimationsBool);
        }

        return kanbanResultData;

    }

    public String getKanbanAnalysisJSON(String givenBacklogDefinition, String startDateString, String endDateString,
            String lightAnalysis, String relevantProjects, String excludedStatusString, String excludedTypesString,
            String andClause, Boolean definedByFilter, String withAltEstimations) {

        KanbanResultData kanbanResultData;

        try {

            kanbanResultData = getKanbanAnalysis(givenBacklogDefinition, startDateString, endDateString, lightAnalysis,
                    relevantProjects, excludedStatusString, excludedTypesString, andClause, definedByFilter, withAltEstimations);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(kanbanResultData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }
    }

    @Async
    public Future<Path> getKanbanAnalysisJSONFile(Path tmpFile, String givenBacklogDefinition, String startDateString,
            String endDateString, String lightAnalysis, String relevantProjects, String excludedStatusString,
            String excludedTypesString, String andClause, Boolean definedByFilter, String withAltEstimations) throws ConfigurationException {

        KanbanResultData kanbanResultData;

        kanbanResultData = getKanbanAnalysis(givenBacklogDefinition, startDateString, endDateString, lightAnalysis,
                relevantProjects, excludedStatusString, excludedTypesString, andClause, definedByFilter, withAltEstimations);

        fileOutput.writeObjectAsJsonToFile(kanbanResultData, tmpFile, null);

        return new AsyncResult<Path>(tmpFile);

    }

}
