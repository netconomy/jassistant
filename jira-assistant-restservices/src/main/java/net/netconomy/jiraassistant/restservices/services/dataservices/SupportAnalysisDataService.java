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
import java.util.concurrent.Future;

import net.netconomy.jiraassistant.base.output.ZipFileOutput;

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
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;
import net.netconomy.jiraassistant.supportanalysis.data.SupportAnalysisData;
import net.netconomy.jiraassistant.supportanalysis.services.SupportAnalysisService;

@Service
public class SupportAnalysisDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportAnalysisDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    SupportAnalysisService supportAnalysisService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    ZipFileOutput zipFileOutput;

    private SupportAnalysisData getSupportAnalysisData(String projectsString, String startDateString,
            String endDateString, String issueTypesString, String andClause, Boolean analysisByMonth)
            throws ConfigurationException {

        SupportAnalysisData supportAnalysisData;
        ClientCredentials credentials;
        DateTime startDate;
        DateTime endDate;
        DateTime month;
        DateTimeZone timeZone;
        List<String> projects;
        List<String> issueTypes;

        credentials = configuration.getClientCredentials();

        timeZone = DateTimeZone.forID(credentials.getTimeZone());

        DateTimeZone.setDefault(timeZone);

        projects = basicDataService.splitString(projectsString, ",");
        issueTypes = basicDataService.splitString(issueTypesString, ",");

        if (analysisByMonth) {
            month = DateTime.parse(startDateString);

            supportAnalysisData = supportAnalysisService.calculateSupportAnalysis(credentials, projects, month,
                    issueTypes, andClause);

            return supportAnalysisData;
        } else {
            startDate = DateTime.parse(startDateString);
            endDate = DateTime.parse(endDateString);

            supportAnalysisData = supportAnalysisService.calculateSupportAnalysis(credentials, projects, startDate,
                    endDate, issueTypes, andClause);

            return supportAnalysisData;
        }

    }

    public String getSupportAnalysisJSON(String projects, String issueTypesString, String startDateString,
            String endDateString, String andClause, Boolean analysisByMonth) {

        SupportAnalysisData supportAnalysisData;

        try {
            supportAnalysisData = getSupportAnalysisData(projects, startDateString, endDateString, issueTypesString,
                    andClause, analysisByMonth);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(supportAnalysisData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createSupportAnalysisJSONFile(Path tmpFile, String projects, String issueTypesString,
            String startDateString, String endDateString, String andClause, Boolean analysisByMonth) throws ConfigurationException {

        SupportAnalysisData supportAnalysisData;

        supportAnalysisData = getSupportAnalysisData(projects, startDateString, endDateString, issueTypesString,
                    andClause, analysisByMonth);

        fileOutput.writeObjectAsJsonToFile(supportAnalysisData, tmpFile, null);

        return new AsyncResult<Path>(tmpFile);

    }

    @Async
    public Future<Path> createSupportAnalysisZipFile(Path tmpZipFile, String projectsString, String issueTypesString,
             String startDateString, String endDateString, String andClause, Boolean analysisByMonth)
        throws IOException, ConfigurationException {

        SupportAnalysisData supportAnalysisData;
        String fileEnding = ".json";
        List<String> projects;
        Path tempDirectory = Files.createTempDirectory("supportAnalysis");
        Path tempFile;
        Map<String, Path> jsonFiles = new HashMap<>();

        try {

            projects = basicDataService.splitString(projectsString, ",");

            for(String currentProject : projects) {

                supportAnalysisData = getSupportAnalysisData(currentProject, startDateString, endDateString,
                        issueTypesString, andClause, analysisByMonth);

                tempFile = Files.createTempFile(tempDirectory, "supportAnalysis_" + currentProject,
                        fileEnding);

                fileOutput.writeObjectAsJsonToFile(supportAnalysisData, tempFile, null);

                jsonFiles.put("supportAnalysis_" + currentProject + fileEnding, tempFile);

            }

            zipFileOutput.createZipFile(tmpZipFile, jsonFiles);

            return new AsyncResult<Path>(tmpZipFile);

        } finally {
            basicDataService.deleteDirectory(tempDirectory);
        }

    }

}
