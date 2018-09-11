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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.output.ZipFileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatistics;
import net.netconomy.jiraassistant.estimationstatistics.services.EstimationStatisticsService;
import net.netconomy.jiraassistant.estimationstatistics.services.EstimationStatisticsToCSVService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class EstimationStatisticsDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstimationStatisticsDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    EstimationStatisticsService estimationStatisticsService;

    @Autowired
    EstimationStatisticsToCSVService estimationStatisticsToCSVService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    ZipFileOutput zipFileOutput;

    EstimationStatistics getEstimationStatistics(String projectsString, String issueTypesString,
            String startDateString, String endDateString, String altEstimationsString, String andClause) throws ConfigurationException {

        EstimationStatistics estimationStatistics;
        ClientCredentials credentials;
        List<String> projects;
        List<String> issueTypes;
        DateTime startDate;
        DateTime endDate;
        DateTimeZone timeZone;
        Boolean altEstimations = false;

        credentials = configuration.getClientCredentials();

        timeZone = DateTimeZone.forID(credentials.getTimeZone());

        DateTimeZone.setDefault(timeZone);

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        if(altEstimationsString != null && !altEstimationsString.isEmpty()) {
            altEstimations = Boolean.parseBoolean(altEstimationsString);
        }

        projects = basicDataService.splitString(projectsString, ",");
        issueTypes = basicDataService.splitString(issueTypesString, ",");

        estimationStatistics = estimationStatisticsService.calculateEstimationStatistics(credentials, projects,
                issueTypes, startDate, endDate, altEstimations, andClause);

        return estimationStatistics;

    }

    public String getEstimationStatisticsJSON(String projects, String issueTypesString, String startDateString,
            String endDateString, String altEstimationsString, String andClause) {

        EstimationStatistics estimationStatistics;
        
        try {
            estimationStatistics = getEstimationStatistics(projects, issueTypesString, startDateString, endDateString,
                    altEstimationsString, andClause);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(estimationStatistics);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createEstimationStatisticsZipFile(Path tmpZipFile, String projects, String issueTypesString,
        String startDateString, String endDateString, String altEstimationsString, String andClause)
        throws ConfigurationException, IOException {

        String fileName;
        String fileEnding = ".csv";
        EstimationStatistics estimationStatistics;
        Map<String, CSVTable> csvTables;
        Path tempDirectory = Files.createTempDirectory("estimationStatistics");;
        Path tempFile;
        Map<String, Path> csvFiles = new HashMap<>();

        try {

            estimationStatistics = getEstimationStatistics(projects, issueTypesString, startDateString, endDateString,
                altEstimationsString, andClause);

            csvTables = estimationStatisticsToCSVService
                .generateCSVTablesFromEstimationStatistics(estimationStatistics);

            for (Entry<String, CSVTable> current : csvTables.entrySet()) {

                fileName = projects.replace(',', '_') + "_" + current.getKey() + fileEnding;

                tempFile = Files.createTempFile(tempDirectory, "estimationStatistics_" + projects.replace(',', '_'),
                    fileEnding);

                fileOutput.writeCSVTableToFile(current.getValue(), tempFile, null, null);

                csvFiles.put(fileName, tempFile);

            }

            zipFileOutput.createZipFile(tmpZipFile, csvFiles);

            return new AsyncResult<Path>(tmpZipFile);

        } finally {
            basicDataService.deleteDirectory(tempDirectory);
        }

    }

}
