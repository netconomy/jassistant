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
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataLight;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;
import net.netconomy.jiraassistant.sprintanalysis.data.AdditionalDefectBugStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.IssueStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.MultipleSprintAnalysisData;
import net.netconomy.jiraassistant.sprintanalysis.data.SprintResultData;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatisticsPartData;
import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisService;
import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisToCSVService;
import net.netconomy.jiraassistant.sprintanalysis.services.SprintResultService;

@Service
public class SprintAnalysisDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintAnalysisDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    SprintResultService sprintResultService;

    @Autowired
    MultipleSprintAnalysisService multipleSprintAnalysisService;

    @Autowired
    MultipleSprintAnalysisToCSVService multipleAnalysisToCSVService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    private SprintResultData getSprintAnalysis(String sprintIdentifier, Boolean identifiedById, String lightAnalysis,
            String relevantProjects) throws ConfigurationException {

        SprintResultData sprintResult;
        List<String> wantedFields;
        ClientCredentials credentials;
        Boolean lightAnalysisBool;
        List<String> relevantProjectsList;

        credentials = configuration.getClientCredentials();
        wantedFields = configuration.getIssueConfiguration().getWantedFields();

        relevantProjectsList = basicDataService.splitString(relevantProjects, ",");

        lightAnalysisBool = Boolean.valueOf(lightAnalysis);

        sprintResult = sprintResultService.calculateSprintResult(credentials, sprintIdentifier, identifiedById,
                wantedFields, lightAnalysisBool, relevantProjectsList);

        return sprintResult;

    }

    private MultipleSprintAnalysisData getMultipleSprintAnalysis(String sprintIdentifiers, String relevantProjects)
            throws ConfigurationException {

        MultipleSprintAnalysisData multipleSprintAnalysisData;
        List<String> wantedFields;
        ClientCredentials credentials;
        List<String> relevantProjectsList;
        List<String> sprintIDList;

        credentials = configuration.getClientCredentials();
        wantedFields = configuration.getIssueConfiguration().getWantedFields();

        relevantProjectsList = basicDataService.splitString(relevantProjects, ",");

        sprintIDList = basicDataService.splitString(sprintIdentifiers, ",");

        multipleSprintAnalysisData = multipleSprintAnalysisService.generateMultipleSprintAnalysisDataFromIDList(
                sprintIDList, credentials, wantedFields, relevantProjectsList);

        return multipleSprintAnalysisData;

    }

    public String getSprintAnalysisJSON(String sprintIdentifier, Boolean identifiedById, String lightAnalysis,
            String relevantProjects) {

        SprintResultData sprintResult;

        try {

            sprintResult = getSprintAnalysis(sprintIdentifier, identifiedById, lightAnalysis, relevantProjects);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(sprintResult);

        } catch (JiraAssistantException | RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createSprintAnalysisJSONFile(Path tmpFile, String sprintIdentifier, Boolean identifiedById,
            String lightAnalysis, String relevantProjects) throws ConfigurationException {

        SprintResultData sprintResult;

        sprintResult = getSprintAnalysis(sprintIdentifier, identifiedById, lightAnalysis, relevantProjects);

        fileOutput.writeObjectAsJsonToFile(sprintResult, tmpFile, null);

        return new AsyncResult<Path>(tmpFile);

    }

    @Async
    public Future<Path> createMultipleSprintAnalysisCSVFile(Path tmpFile, String sprintIdentifiers, String relevantProjects)
        throws ConfigurationException {

        MultipleSprintAnalysisData multipleSprintAnalysisData;
        CSVTable csvTable;

        multipleSprintAnalysisData = getMultipleSprintAnalysis(sprintIdentifiers, relevantProjects);

        csvTable = multipleAnalysisToCSVService
                    .generateCSVTableFromMultipleSprintAnalysis(multipleSprintAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, tmpFile, null, null);

        return new AsyncResult<Path>(tmpFile);

    }

    public String getSprintAnalysisDummy() {

        SprintResultData sprintResultDataDummy = new SprintResultData();
        SprintData sprintDataDummy = new SprintData();
        IssueStatistics issueStatisticsDummy = new IssueStatistics();
        FlaggingStatistics flaggingStatisticsDummy = new FlaggingStatistics();

        try {

            sprintDataDummy.setId(123);
            sprintDataDummy.setRapidViewId(1234);
            sprintDataDummy.setName("testName");
            sprintDataDummy.setState("testState");
            sprintDataDummy.setStartDate("2015-01-01");
            sprintDataDummy.setEndDate("2015-02-02");
            sprintDataDummy.setCompleteDate("2015-03-03");

            flaggingStatisticsDummy.setIssueFlaggingStatistics(new FlaggingStatisticsPartData());
            flaggingStatisticsDummy.setSubIssueFlaggingStatistics(new FlaggingStatisticsPartData());

            issueStatisticsDummy.setFlaggingStatistics(flaggingStatisticsDummy);

            sprintResultDataDummy.setSprintData(sprintDataDummy);
            sprintResultDataDummy.setStoryStatistics(issueStatisticsDummy);
            sprintResultDataDummy.setDefectBugStatistics(issueStatisticsDummy);
            sprintResultDataDummy.setTaskStatistics(issueStatisticsDummy);
            sprintResultDataDummy.setFlaggingStatistics(flaggingStatisticsDummy);
            sprintResultDataDummy.setAdditionalDefectBugStatistics(new AdditionalDefectBugStatistics());
            sprintResultDataDummy.setSprintDataDelta(new SprintDataDelta(sprintDataDummy));
            sprintResultDataDummy.setSprintDataLight(new SprintDataLight(sprintDataDummy));

            return jsonAnswerWrapper.wrapObjectForJSONOutput(sprintResultDataDummy);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }
    }

}
