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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.accountprogress.data.AccountProgressResultData;
import net.netconomy.jiraassistant.accountprogress.services.AccountProgressService;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.AccountBaseData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class AccountProgressDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountProgressDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    AccountProgressService accountProgressService;

    @Autowired
    BasicDataService basicDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    FileOutput fileOutput;

    private AccountProgressResultData getAccountProgress(String accountIdentifiersString, Boolean identifiedByIDs,
            String andClause) throws ConfigurationException {

        AccountProgressResultData accountProgressResultData;
        ClientCredentials credentials;
        List<String> accountIdentifiers;

        credentials = configuration.getClientCredentials();

        accountIdentifiers = basicDataService.splitString(accountIdentifiersString, ",");

        accountProgressResultData = accountProgressService.calculateAccountProgress(credentials, accountIdentifiers,
                identifiedByIDs, andClause);

        return accountProgressResultData;

    }

    public String getAccountProgressJSON(String accountIdentifiers, Boolean identifiedByIDs, String andClause) {

        AccountProgressResultData accountProgressResultData;

        try {

            accountProgressResultData = getAccountProgress(accountIdentifiers, identifiedByIDs, andClause);

            return jsonAnswerWrapper.wrapObjectForJSONOutput(accountProgressResultData);

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    @Async
    public Future<Path> createAccountProgressJSONFile(Path tmpFile, String accountIdentifiers, Boolean identifiedByIDs,
            String andClause) throws ConfigurationException {

        AccountProgressResultData accountProgressResultData;

        accountProgressResultData = getAccountProgress(accountIdentifiers, identifiedByIDs, andClause);

        fileOutput.writeObjectAsJsonToFile(accountProgressResultData, tmpFile, null);

        return new AsyncResult<Path>(tmpFile);

    }

    public String getAccountProgressDummy() {

        AccountProgressResultData accountProgressResultDataDummy = new AccountProgressResultData();
        AccountBaseData accountBaseData1 = new AccountBaseData();
        AccountBaseData accountBaseData2 = new AccountBaseData();

        accountBaseData1.setId(42);
        accountBaseData1.setKey("testkey");
        accountBaseData1.setName("Test Account 1");
        accountBaseData1.setLeadUserName("testuser");
        accountBaseData1.setLeadName("Mr Test");
        accountBaseData1.setStatus("OPEN");
        accountBaseData1.setCustomerShortName("TST");
        accountBaseData1.setCustomerFullName("Test Kunden AG");
        accountBaseData1.setCategoryShortName("ALI");
        accountBaseData1.setCategoryLongName("Aliquot Billable");

        accountBaseData2.setId(45);
        accountBaseData2.setKey("34587");
        accountBaseData2.setName("Test Account 2");
        accountBaseData2.setLeadUserName("testuser");
        accountBaseData2.setLeadName("Mr Test");
        accountBaseData2.setStatus("OPEN");
        accountBaseData2.setCustomerShortName("TST");
        accountBaseData2.setCustomerFullName("Test Kunden AG");
        accountBaseData2.setCategoryShortName("DIR");
        accountBaseData2.setCategoryLongName("Directly Billable");

        accountProgressResultDataDummy.addAccountBaseData(accountBaseData1);
        accountProgressResultDataDummy.addAccountBaseData(accountBaseData2);
        accountProgressResultDataDummy.setAccountProgressDate(DateTime.now().toString());
        accountProgressResultDataDummy.setAndClause("type=Arbeitspaket");
        accountProgressResultDataDummy.setSpentTimeOnOpenIssues(1240.0);
        accountProgressResultDataDummy.setSpentTimeOnFinishedIssues(3403.0);
        accountProgressResultDataDummy.setSpentTimeOnClosedIssues(23.0);

        return jsonAnswerWrapper.wrapObjectForJSONOutput(accountProgressResultDataDummy);

    }

}
