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

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JiraBasicRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraBasicRestService.class);

    @Autowired
    private JiraRestUrlBuilder jiraRestUrlBuilder;

    @Autowired
    private RestConnector restConnector;

    public JSONArray getAllPriorities(ClientCredentials clientCredentials) {

        JSONArray jsonArray;
        String restUrl = jiraRestUrlBuilder.constructPriorityUrl(clientCredentials);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieving all Priorities from Server using the URL: '{}'.", restUrl);
        }

        jsonArray = restConnector.getJsonArrayFromRest(clientCredentials, restUrl);

        return jsonArray;

    }

}
