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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraIssueRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraIssueRestService.class);

    @Autowired
    private JiraRestUrlBuilder jiraRestUrlBuilder;

    @Autowired
    private RestConnector restConnector;

    public JSONArray getWorklogForIssue(ClientCredentials clientCredentials, String issueKey) {

        JSONObject jsonObject;
        String restUrl = jiraRestUrlBuilder.constructWorklogsUrl(clientCredentials, issueKey);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieving Worklog for Issue {} using the URL: '{}'.", issueKey, restUrl);
        }

        try {

            jsonObject = restConnector.getJsonFromRest(clientCredentials, restUrl);

            return (JSONArray) jsonObject.get("worklogs");

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while converting Worklogs JSON", e);
        }

    }

    public JSONObject getIssue(ClientCredentials clientCredentials, String issueKey) {

        JSONObject jsonObject;
        String restUrl = jiraRestUrlBuilder.constructIssueUrl(clientCredentials, issueKey);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieving Issue {} using the URL: '{}'.", issueKey, restUrl);
        }

        jsonObject = restConnector.getJsonFromRest(clientCredentials, restUrl);

        return jsonObject;

    }

    public ResponseEntity<String> updateIssue(ClientCredentials clientCredentials, String issueKey, JSONObject updateBody) {

        ResponseEntity<String> response;
        String restUrl = jiraRestUrlBuilder.constructIssueUrl(clientCredentials, issueKey);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating Issue {} using the URL: '{}' and the Body: '{}'.", issueKey, restUrl, updateBody);
        }

        response = restConnector.putJsonToRest(clientCredentials, restUrl, updateBody);

        return response;

    }

}
