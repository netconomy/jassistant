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
package net.netconomy.jiraassistant.base.services;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.AccountBaseData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.restclient.JiraTempoAccountsRestService;

@Service
public class AccountBaseDataService {

    @Autowired
    private JiraTempoAccountsRestService jiraTempoAccountsRestService;

    public AccountBaseData parseAccountBaseData(JSONObject jsonAccountInfo) {

        AccountBaseData accountBaseData = new AccountBaseData();

        JSONObject leadObject;
        JSONObject customerObject;
        JSONObject categoryObject;

        accountBaseData.setId(jsonAccountInfo.optInt("id"));
        accountBaseData.setKey(jsonAccountInfo.optString("key"));
        accountBaseData.setName(jsonAccountInfo.optString("name"));
        accountBaseData.setStatus(jsonAccountInfo.optString("status"));

        leadObject = jsonAccountInfo.optJSONObject("lead");
        customerObject = jsonAccountInfo.optJSONObject("customer");
        categoryObject = jsonAccountInfo.optJSONObject("category");

        if (leadObject != null) {
            accountBaseData.setLeadUserName(leadObject.optString("username"));
            accountBaseData.setLeadName(leadObject.optString("displayName"));
        }

        if (customerObject != null) {
            accountBaseData.setCustomerShortName(customerObject.optString("key"));
            accountBaseData.setCustomerFullName(customerObject.optString("name"));
        }

        if (categoryObject != null) {
            accountBaseData.setCategoryShortName(categoryObject.optString("key"));
            accountBaseData.setCategoryLongName(categoryObject.optString("name"));
        }

        return accountBaseData;

    }

    public AccountBaseData getAccountBaseData(ClientCredentials credentials, String accountKey) {

        JSONObject jsonAccountInfo = jiraTempoAccountsRestService.getSingleAccountByKey(credentials, accountKey);

        return parseAccountBaseData(jsonAccountInfo);

    }

    public AccountBaseData getAccountBaseData(ClientCredentials credentials, Integer accountID) {

        JSONObject jsonAccountInfo = jiraTempoAccountsRestService.getSingleAccountByID(credentials, accountID);

        return parseAccountBaseData(jsonAccountInfo);

    }

}
