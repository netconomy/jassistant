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

import com.atlassian.jira.rest.client.api.RestClientException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.AccountBaseData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.restclient.JiraTempoAccountsRestService;
import net.netconomy.jiraassistant.base.services.AccountBaseDataService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.restservices.data.TempoAccountDataCollection;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class TempoAccountInformationDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempoAccountInformationDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    JiraTempoAccountsRestService jiraTempoAccountsRestService;

    @Autowired
    AccountBaseDataService accountBaseDataService;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    private TempoAccountDataCollection getAllAccounts(ClientCredentials clientCredentials) {

        TempoAccountDataCollection accountCollection = new TempoAccountDataCollection();
        AccountBaseData accountBaseData;
        JSONArray accounts = jiraTempoAccountsRestService.getAllAccounts(clientCredentials);
        JSONObject currentAccount;

        for (int i = 0; i < accounts.length(); i++) {
            currentAccount = accounts.optJSONObject(i);

            if (currentAccount != null) {

                accountBaseData = accountBaseDataService.parseAccountBaseData(currentAccount);

                accountCollection.addAccountBaseData(accountBaseData);

            }

        }

        return accountCollection;

    }

    /**
     * Returns all not archived Accounts.
     * 
     * @return
     */
    public String getAllAccountsJSON() {

        ClientCredentials credentials;

        try {

            credentials = configuration.getClientCredentials();

            return jsonAnswerWrapper.wrapObjectForJSONOutput(getAllAccounts(credentials));

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

}
