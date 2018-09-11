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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraAgileRestService {

    private static final String BASEURL = "{serverUrl}rest/greenhopper/latest/";

    private static final String SPRINTREPORTURL = "rapid/charts/sprintreport?rapidViewId={rapidViewId}&sprintId={sprintId}";

    private static final String RAPIDVIEWSURL = "rapidview";

    private static final String RAPIDVIEWSLISTURL = "rapidviews/list";

    private static final String SPRINTLISTURL = "sprintquery/{rapidViewId}";

    @Autowired
    RestConnector restConnector;

    /**
     * Gets the Sprint Report Information from the REST API of the JIRA Agile Plugin
     * 
     * @param clientCredentials
     * @param rapidViewId
     * @param sprintId
     * @return
     * @throws JSONException
     */
    public JSONObject getSprintReport(ClientCredentials clientCredentials, Integer rapidViewId, Integer sprintId) {

        String restUrl = BASEURL + SPRINTREPORTURL;

        restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

        restUrl = restUrl.replace("{rapidViewId}", rapidViewId.toString());

        restUrl = restUrl.replace("{sprintId}", sprintId.toString());

        return restConnector.getJsonFromRest(clientCredentials, restUrl);

    }

    /**
     * Get all RapidViews from the REST API of the JIRA Agile Plugin, but only with id, name, canEdit and
     * sprintSupportEnabled
     * 
     * @param clientCredentials
     * @return
     */
    public JSONArray getAllRapidViewsLight(ClientCredentials clientCredentials) {

        JSONObject jsonObject;
        String restUrl = BASEURL + RAPIDVIEWSURL;

        try {

            restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

            jsonObject = restConnector.getJsonFromRest(clientCredentials, restUrl);

            return (JSONArray) jsonObject.get("views");

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while converting Sprints JSON", e);
        }

    }

    /**
     * Get all RapidViews from the REST API of the JIRA Agile Plugin, with all available Information
     * 
     * @param clientCredentials
     * @return
     */
    public JSONArray getAllRapidViews(ClientCredentials clientCredentials) {

        JSONObject jsonObject;
        String restUrl = BASEURL + RAPIDVIEWSLISTURL;

        try {

            restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

            jsonObject = restConnector.getJsonFromRest(clientCredentials, restUrl);

            return (JSONArray) jsonObject.get("views");

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while converting Sprints JSON", e);
        }

    }

    /**
     * Get all Sprints For the given Board from the REST API of the JIRA Agile Plugin
     * 
     * @param clientCredentials
     * @param rapidViewId
     * @return
     */
    public JSONArray getAllSprintsForBoard(ClientCredentials clientCredentials, Integer rapidViewId) {

        JSONObject jsonObject;
        String restUrl = BASEURL + SPRINTLISTURL;

        try {

            restUrl = restUrl.replace("{serverUrl}", clientCredentials.getJiraUri());

            restUrl = restUrl.replace("{rapidViewId}", rapidViewId.toString());

            jsonObject = restConnector.getJsonFromRest(clientCredentials, restUrl);

            return (JSONArray) jsonObject.get("sprints");

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while converting Sprints JSON", e);
        }

    }

}
