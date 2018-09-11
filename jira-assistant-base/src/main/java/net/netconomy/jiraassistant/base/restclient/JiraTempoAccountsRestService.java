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
package net.netconomy.jiraassistant.base.restclient;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraTempoAccountsRestService {

    private static final String BASEURL = "{serverUrl}rest/tempo-accounts/latest/";

    private static final String ALLACCOUNTSURL = "account";

    private static final String WITHARCHIVEDACCOUNTSURL = "account?skipArchived=false";

    private static final String SINGLEACCOUNTURLKEY = "account/key/{key}";

    private static final String SINGLEACCOUNTURLID = "account/{id}";

    @Autowired
    RestConnector restConnector;

    /**
     * Gets an JSON Array of all existing Accounts, archived Accounts will not be part of the Array
     * 
     * @param clientCredentials
     * @return
     */
    public JSONArray getAllAccounts(ClientCredentials clientCredentials) {

        JSONArray jsonArray;
        String restUrl = BASEURL + ALLACCOUNTSURL;

        restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

        jsonArray = restConnector.getJsonArrayFromRest(clientCredentials, restUrl);

        return jsonArray;

    }

    /**
     * Gets an JSON Array of all Accounts including the archived ones
     * 
     * @param clientCredentials
     * @return
     */
    public JSONArray getAllAccountsWithArchived(ClientCredentials clientCredentials) {

        JSONArray jsonArray;
        String restUrl = BASEURL + WITHARCHIVEDACCOUNTSURL;

        restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

        jsonArray = restConnector.getJsonArrayFromRest(clientCredentials, restUrl);

        return jsonArray;

    }

    /**
     * Gets the Information of a single Account
     * 
     * @param clientCredentials
     * @param accountKey
     * @return
     */
    public JSONObject getSingleAccountByKey(ClientCredentials clientCredentials, String accountKey) {

        String restUrl = BASEURL + SINGLEACCOUNTURLKEY;

        restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

        restUrl = restUrl.replace("{key}", accountKey);

        return restConnector.getJsonFromRest(clientCredentials, restUrl);

    }

    /**
     * Gets the Information of a single Account
     * 
     * @param clientCredentials
     * @param accountID
     * @return
     */
    public JSONObject getSingleAccountByID(ClientCredentials clientCredentials, Integer accountID) {

        String restUrl = BASEURL + SINGLEACCOUNTURLID;

        restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

        restUrl = restUrl.replace("{id}", accountID.toString());

        return restConnector.getJsonFromRest(clientCredentials, restUrl);

    }

}
