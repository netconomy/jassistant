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
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.billing.data.BillingData;
import net.netconomy.jiraassistant.billing.services.BillingDataToCSVService;
import net.netconomy.jiraassistant.billing.services.BillingService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class BillingDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    BillingService billingService;

    @Autowired
    BillingDataToCSVService billingDataToCSVService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    BasicDataService basicDataService;

    private BillingData getBillingData(String identifiersString, String startDateString, String endDateString,
            String hoursInAPersonDayString, String issueTypesString, String andClause, Boolean identifiedByProjects,
            String linksToListString, String additionalAccountsString, Boolean analysisByMonth, String additionalFieldsString)
            throws ConfigurationException {

        BillingData billingData;
        ClientCredentials credentials;
        Double hoursInAPersonDay;
        List<String> identifiers;
        List<String> issueTypes;
        List<String> linksToList;
        List<String> additionalAccounts;
        List<String> additionalFields;
        DateTime month;
        DateTime startDate;
        DateTime endDate;
        DateTimeZone timeZone;

        credentials = configuration.getClientCredentials();

        hoursInAPersonDay = Double.valueOf(hoursInAPersonDayString.replace(',', '.'));
        identifiers = basicDataService.splitString(identifiersString, ",");
        issueTypes = basicDataService.splitString(issueTypesString, ",");
        linksToList = basicDataService.splitString(linksToListString, ",");
        additionalAccounts = basicDataService.splitString(additionalAccountsString, ",");
        additionalFields = basicDataService.splitString(additionalFieldsString, ",");

        timeZone = DateTimeZone.forID(credentials.getTimeZone());

        DateTimeZone.setDefault(timeZone);

        if (analysisByMonth) {
            month = DateTime.parse(startDateString);

            billingData = billingService.generateBillingData(credentials, identifiers, month, hoursInAPersonDay,
                    issueTypes, andClause, identifiedByProjects, linksToList, additionalAccounts, additionalFields);

            return billingData;
        } else {
            startDate = DateTime.parse(startDateString);
            endDate = DateTime.parse(endDateString);

            billingData = billingService.generateBillingData(credentials, identifiers, startDate, endDate,
                    hoursInAPersonDay, issueTypes, andClause, identifiedByProjects, linksToList, additionalAccounts,
                    additionalFields);

            return billingData;
        }

    }

    public String getBillingDataJSON(String identifiersString, String startDateString, String endDateString,
            String hoursInAPersonDayString, String issueTypesString, String andClause, Boolean identifiedByProjects,
            String linksToListString, String additionalAccountsString, Boolean analysisByMonth, String additionalFieldsString) {

        BillingData billingData;

        try {

            billingData = getBillingData(identifiersString, startDateString, endDateString, hoursInAPersonDayString,
                    issueTypesString, andClause, identifiedByProjects, linksToListString, additionalAccountsString,
                    analysisByMonth, additionalFieldsString);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(billingData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createBillingDataCSVFile(Path tmpFile, String identifiersString, String startDateString,
            String endDateString, String hoursInAPersonDayString, String issueTypesString, String andClause,
            Boolean identifiedByProjects, String linksToListString, String additionalAccountsString,
            Boolean analysisByMonth, String additionalFieldsString) throws ConfigurationException {

        BillingData billingData;
        CSVTable csvTable;
        List<String> additionalFields;

        billingData = getBillingData(identifiersString, startDateString, endDateString, hoursInAPersonDayString,
                issueTypesString, andClause, identifiedByProjects, linksToListString, additionalAccountsString,
                analysisByMonth, additionalFieldsString);

        additionalFields = basicDataService.splitString(additionalFieldsString, ",");

        csvTable = billingDataToCSVService.generateCSVTableFromBillingData(billingData, additionalFields);

        fileOutput.writeCSVTableToFile(csvTable, tmpFile, null, null);

        return new AsyncResult<Path>(tmpFile);

    }

}
