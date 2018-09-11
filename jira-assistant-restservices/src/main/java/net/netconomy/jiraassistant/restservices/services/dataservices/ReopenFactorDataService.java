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
import net.netconomy.jiraassistant.reopenfactor.data.ReopenFactorData;
import net.netconomy.jiraassistant.reopenfactor.services.ReopenFactorService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class ReopenFactorDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReopenFactorDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    ReopenFactorService reopenFactorService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    private ReopenFactorData getReopenFactor(String projectNames, String startDateString, String endDateString,
            String andClause, String thresholdString) throws ConfigurationException {

        ReopenFactorData reopenFactorData;
        ClientCredentials credentials;
        Integer threshold;
        DateTime startDate;
        DateTime endDate;
        DateTimeZone timeZone;
        List<String> projects;

        credentials = configuration.getClientCredentials();

        if (thresholdString != null && !thresholdString.isEmpty()) {
            threshold = Integer.parseInt(thresholdString);
        } else {
            threshold = null;
        }

        timeZone = DateTimeZone.forID(credentials.getTimeZone());

        DateTimeZone.setDefault(timeZone);

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        projects = basicDataService.splitString(projectNames, ",");

        reopenFactorData = reopenFactorService.calculateReopenFactor(credentials, projects, startDate, endDate,
                andClause, threshold);

        return reopenFactorData;
    }

    public String getReopenFactorJSON(String projectNames, String startDateString, String endDateString,
            String andClause, String thresholdString) {

        ReopenFactorData reopenFactorData;

        try {

            reopenFactorData = getReopenFactor(projectNames, startDateString, endDateString, andClause, thresholdString);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(reopenFactorData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }
    }

    @Async
    public Future<Path> createReopenFactorJSONFile(Path tmpFile, String projectNames, String startDateString,
            String endDateString, String andClause, String thresholdString) throws ConfigurationException {

        ReopenFactorData reopenFactorData;

        reopenFactorData = getReopenFactor(projectNames, startDateString, endDateString, andClause, thresholdString);

        fileOutput.writeObjectAsJsonToFile(reopenFactorData, tmpFile, null);

        return new AsyncResult<Path>(tmpFile);
    }

}
