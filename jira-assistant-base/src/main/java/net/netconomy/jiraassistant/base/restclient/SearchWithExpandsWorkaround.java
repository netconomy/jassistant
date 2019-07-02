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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

/**
 * This Class contains a Workaround, that accesses JIRA REST API directly,
 * 
 * @author mweilbuchner
 *
 */
@Service
public class SearchWithExpandsWorkaround {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchWithExpandsWorkaround.class);

    private static final Integer MAXRESULTS = 50;

    @Autowired
    private JiraRestUrlBuilder jiraRestUrlBuilder;

    @Autowired
    private RestConnector restConnector;

    @Autowired
    private JsonToIssueConverter jsonToIssueConverter;

    /**
     * Get Issues a Number of Issues (MAXRESULTS) fitting the given query from the Rest API and add them to the given
     * Issue List. The total Number of Issues will be returned.
     * 
     * @param credentials
     * @param startAt
     * @param jqlQuery
     * @param additionalFields
     * @param expand
     * @param issueList
     * @return the total Number of Issues for this Search Query
     */
    private Integer getListOfSearchedIssuesFromRest(ClientCredentials credentials, Integer startAt, String jqlQuery,
            String additionalFields, String expand, List<Issue> issueList) {

        String restUrl;
        JSONObject jsonResult;
        JSONArray jsonIssues;

        try {

            restUrl = jiraRestUrlBuilder.constructSearchUrl(credentials, jqlQuery, startAt, MAXRESULTS,
                    additionalFields, expand);

            jsonResult = restConnector.getJsonFromRest(credentials, restUrl);

            int totalIssues = jsonResult.getInt("total");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The Query: '{}' came back with {} Issues", jqlQuery, totalIssues);
            }

            jsonIssues = jsonResult.getJSONArray("issues");
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Processing issues {} to {} of {} total issues.", startAt + 1, startAt + jsonIssues.length() + 1, totalIssues);
            }

            for (int j = 0; j < jsonIssues.length(); j++) {

                issueList.add(jsonToIssueConverter.convertJsonObjectToIssue(jsonIssues.getJSONObject(j),
                        additionalFields, expand));

            }

            return totalIssues;

        } catch (JSONException e) {
            throw new JiraAssistantException("Error while converting Issue JSON", e);
        }

    }

    /**
     * This is a Workaround to go directly to Jira REST API for a search with expanded Changelog. If additionalFields is
     * left empty or null the standard fields from basicFields will be used.
     * summary,issuetype,created,updated,project,status,created are always included. <b>As soon as it is possible to get
     * an expanded Changelog from JRJC through search, this should be replaced.</b>
     * 
     * @param credentials
     * @param jqlQuery
     * @param additionalFields
     *            those are added to the basic Fields <b>No Spaces, separated by ,</b>
     * @param expand
     *            if null or empty the changelog will be expanded
     * @return
     */
    public List<Issue> getSearchResultsWithChangelogWorkaround(ClientCredentials credentials, String jqlQuery,
            String additionalFields, String expand) {

        Integer totalIssues = 0;
        List<Issue> issueList = new ArrayList<>();

        totalIssues = getListOfSearchedIssuesFromRest(credentials, 0, jqlQuery, additionalFields, expand, issueList);

        if (totalIssues <= MAXRESULTS) {
            return issueList;
        }

        for (Integer i = MAXRESULTS; i < totalIssues; i += MAXRESULTS) {

            getListOfSearchedIssuesFromRest(credentials, i, jqlQuery, additionalFields, expand, issueList);

        }

        return issueList;
    }

}
