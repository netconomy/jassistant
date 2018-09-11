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

import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraRestUrlBuilder {

    private static final String BASEURL = "{serverUrl}rest/api/latest/";

    private static final String SEARCHURL = "search?jql={jql}&startAt={startAt}&maxResults={maxResults}";

    private static final String FIELDSURL = "&fields={fields}";
    
    private static final String EXPANDURL = "&expand={expand}";
    
    private static final String BASICFIELDS = "summary,issuetype,created,updated,project,status,priority,created";

    private static final String CHANGELOGEXPAND = "changelog";

    private static final String WORKLOGURL = "issue/{issueKey}/worklog";

    private static final String ISSUEURL = "issue/{issueKey}";

    private static final String PRIORITYURL = "priority";

    private String constructSearchUrl(ClientCredentials credentials, String jqlQuery, Integer startAt,
            Integer maxResults, Boolean allFields, String additionalFields, String expand) {
        
        StringBuilder restUrlBuilder = new StringBuilder();
        String restUrl;
        String fieldString = "";
        String expandString = "";

        restUrlBuilder.append(BASEURL);
        restUrlBuilder.append(SEARCHURL);

        if (allFields == false) {
            
            restUrlBuilder.append(FIELDSURL);
            
            if (additionalFields == null || additionalFields.isEmpty()) {
                fieldString = BASICFIELDS;
            } else {
                fieldString = BASICFIELDS + "," + additionalFields;
            }
        }

        restUrlBuilder.append(EXPANDURL);

        if(expand == null || expand.isEmpty()) {
            expandString = CHANGELOGEXPAND;
        } else {
            expandString = expand;
        }

        restUrl = restUrlBuilder.toString();

        restUrl = restUrl.replace("{serverUrl}", credentials.getJiraUri());

        restUrl = restUrl.replace("{startAt}", startAt.toString());

        restUrl = restUrl.replace("{maxResults}", maxResults.toString());

        restUrl = restUrl.replace("{jql}", jqlQuery);
        
        restUrl = restUrl.replace("{fields}", fieldString);
        
        restUrl = restUrl.replace("{expand}", expandString);

        return restUrl;
        
    }
    
    /**
     * This Function will return a Search Rest URL based on the given query and credentials. If additionalFields is left
     * empty or null the standard fields from basicFields will be used:
     * summary,issuetype,created,updated,project,status,created are always included.
     * 
     * @param credentials
     * @param jqlQuery
     * @param startAt
     * @param maxResults
     * @param additionalFields
     *            those are added to the basic Fields
     * @param expand
     *            if empty or null the Changelog will be expanded
     * @return
     */
    String constructSearchUrl(ClientCredentials credentials, String jqlQuery, Integer startAt,
            Integer maxResults, String additionalFields, String expand) {
        return constructSearchUrl(credentials, jqlQuery, startAt, maxResults, false, additionalFields, expand);
    }

    /**
     * This Function will return a Search Rest URL based on the given query and credentials.
     * 
     * @param credentials
     * @param jqlQuery
     * @param startAt
     * @param maxResults
     * @return
     */
    String constructSearchUrl(ClientCredentials credentials, String jqlQuery, Integer startAt, Integer maxResults) {
        return constructSearchUrl(credentials, jqlQuery, startAt, maxResults, true, null, null);
    }
    
    /**
     * This Function will return a Issue Worklog Rest URL for the given issueKey.
     * 
     * @param credentials
     * @param issueKey
     * @return
     */
    String constructWorklogsUrl(ClientCredentials credentials, String issueKey) {

        String restUrl = BASEURL + WORKLOGURL;

        restUrl = restUrl.replace("{serverUrl}", credentials.getJiraUri());

        restUrl = restUrl.replace("{issueKey}", issueKey);

        return restUrl;

    }

    /**
     * This Function will return an Issue Rest URL for the given issueKey.
     *
     * @param credentials
     * @param issueKey
     * @return
     */
    String constructIssueUrl(ClientCredentials credentials, String issueKey) {

        String restUrl = BASEURL + ISSUEURL;

        restUrl = restUrl.replace("{serverUrl}", credentials.getJiraUri());

        restUrl = restUrl.replace("{issueKey}", issueKey);

        return restUrl;

    }

    /**
     * This Function will return an Priority Rest URL, where you will find Information about all Priorities
     *
     * @param credentials
     * @return
     */
    String constructPriorityUrl(ClientCredentials credentials) {

        String restUrl = BASEURL + PRIORITYURL;

        restUrl = restUrl.replace("{serverUrl}", credentials.getJiraUri());

        return restUrl;

    }

}
