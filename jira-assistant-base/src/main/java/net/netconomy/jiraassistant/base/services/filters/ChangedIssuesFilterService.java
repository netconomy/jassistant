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
package net.netconomy.jiraassistant.base.services.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.restclient.SearchWithExpandsWorkaround;
import net.netconomy.jiraassistant.base.services.DataConversionService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;

@Service
public class ChangedIssuesFilterService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");

    @Autowired
    DataConversionService dataConversionService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    SearchWithExpandsWorkaround searchWithExpandsWorkaround;

    @Autowired
    HistoryIssueService historyIssueService;

    /**
     * generate Filter based on the given Projects, the given start and end Date, the given Issue Types and the given
     * And-Clause, for Issues that were changed during the given Time Window. Based on Status Changes!
     * 
     * @param projectsList
     * @param issueTypesList
     *            can be null or empty
     * @param andClause
     *            can be null or empty
     * @param startDate
     * @param endDate
     * @return
     */
    public String generateChangedIssuesFilter(List<String> projectsList, List<String> issueTypesList, String andClause,
            DateTime startDate, DateTime endDate) {

        StringBuilder generatedFilter = new StringBuilder();
        Joiner joiner = Joiner.on("','");
        String startDateString;
        String endDateString;

        generatedFilter.append("project in ('");

        joiner.appendTo(generatedFilter, projectsList);

        generatedFilter.append("')");

        startDateString = dataConversionService.convertDateTimeToJQLDate(startDate);
        endDateString = dataConversionService.convertDateTimeToJQLDate(endDate);

        generatedFilter.append(" and status changed during ('" + startDateString + "', '" + endDateString + "')");

        if (issueTypesList != null && !issueTypesList.isEmpty()) {

            generatedFilter.append(" and type in ('");

            joiner.appendTo(generatedFilter, issueTypesList);

            generatedFilter.append("')");

        }

        if (andClause != null && !andClause.trim().isEmpty()) {
            generatedFilter.append(" and (" + andClause + ")");
        }

        return generatedFilter.toString();
    }


    /**
     * generate Filter based on the given Projects, the given start and end Date, the given Issue Types and the given
     * And-Clause, for Issues that were changed during the given Time Window. Based on Status Changes!
     * 
     * @param projectsString
     *            Project Short Names separated by ',' no spaces
     * @param issueTypesString
     *            Issue Type Names separated by ',' no spaces, can be null or empty
     * @param andClause
     *            can be null or empty
     * @param startDate
     * @param endDate
     * @return
     */
    public String generateChangedIssuesFilter(String projectsString, String issueTypesString, String andClause,
            DateTime startDate, DateTime endDate) {

        List<String> projectsList = Arrays.asList(projectsString.trim().split(","));
        List<String> issueTypesList = null;

        if (issueTypesString != null && !issueTypesString.trim().isEmpty()) {
            issueTypesList = Arrays.asList(issueTypesString.trim().split(","));
        }

        return generateChangedIssuesFilter(projectsList, issueTypesList, andClause, startDate, endDate);
    }

    /**
     * Creates a Filter for Issues created in the given Projects during the given Time
     * 
     * @param issueTypes
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @return
     */
    public String createFilterForCreatedIssues(List<String> issueTypes, List<String> projects, String andClause,
            DateTime startDate, DateTime endDate) {

        StringBuilder jqlQueryBuilder = new StringBuilder();
        Joiner joiner = Joiner.on("','");

        jqlQueryBuilder.append("project in ('");

        joiner.appendTo(jqlQueryBuilder, projects);

        if (issueTypes != null && !issueTypes.isEmpty()) {

            jqlQueryBuilder.append("') and issuetype in ('");

            joiner.appendTo(jqlQueryBuilder, issueTypes);

        }

        jqlQueryBuilder.append("') and created >= '");

        jqlQueryBuilder.append(startDate.toString(dateTimeFormatter));

        jqlQueryBuilder.append("' and created <= '");

        jqlQueryBuilder.append(endDate.toString(dateTimeFormatter));

        jqlQueryBuilder.append("'");

        if (andClause != null && !andClause.trim().isEmpty()) {
            jqlQueryBuilder.append(" and (" + andClause + ")");
        }

        return jqlQueryBuilder.toString();

    }

    /**
     * Returns the Keys of created Issues in the given Projects during the given Time
     * 
     * @param issueTypes
     * @param projects
     * @param startDate
     * @param endDate
     * @param credentials
     * @return
     */
    public List<String> getKeysOfCreatedIssues(List<String> issueTypes, List<String> projects, DateTime startDate,
            DateTime endDate, ClientCredentials credentials) {

        String jqlQuery = createFilterForCreatedIssues(issueTypes, projects, null, startDate, endDate);

        return jiraSearch.searchJiraGetAllKeys(credentials, jqlQuery);
    }

    /**
     * Returns the Keys of created Issues in the given Projects during the given Time
     * 
     * @param issueTypes
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param credentials
     * @return
     */
    public List<String> getKeysOfCreatedIssues(List<String> issueTypes, List<String> projects, String andClause,
            DateTime startDate, DateTime endDate, ClientCredentials credentials) {

        String jqlQuery = createFilterForCreatedIssues(issueTypes, projects, andClause, startDate, endDate);

        return jiraSearch.searchJiraGetAllKeys(credentials, jqlQuery);
    }

    /**
     * Returns the Number of created Issues in the given Projects during the given Time
     * 
     * @param issueTypes
     * @param projects
     * @param startDate
     * @param endDate
     * @param credentials
     * @return
     */
    public Integer getNumberOfCreatedIssues(List<String> issueTypes, List<String> projects, DateTime startDate,
            DateTime endDate, ClientCredentials credentials) {

        String jqlQuery = createFilterForCreatedIssues(issueTypes, projects, null, startDate, endDate);

        return jiraSearch.searchJiraGetNumberOfResults(credentials, jqlQuery);
    }

    /**
     * Returns the Number of created Issues in the given Projects during the given Time
     * 
     * @param issueTypes
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param credentials
     * @return
     */
    public Integer getNumberOfCreatedIssues(List<String> issueTypes, List<String> projects, String andClause,
            DateTime startDate, DateTime endDate, ClientCredentials credentials) {

        String jqlQuery = createFilterForCreatedIssues(issueTypes, projects, andClause, startDate, endDate);

        return jiraSearch.searchJiraGetNumberOfResults(credentials, jqlQuery);
    }

    /**
     * This Function creates a Filter for finding Issues changed to certain Status during a given Time. <b>If you set
     * the simple query to false it will create a query that will check if transitioned Issues were still in those
     * status at the end of the given time window, but the query will be very complicated and can overwhelm JIRA.</b>
     * 
     * @param issueTypes
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param stateNames
     * @param simpleQuery
     * @return
     */
    String createFilterForChangedDuringTimeToStates(List<String> issueTypes, List<String> projects,
            String andClause,
            DateTime startDate, DateTime endDate, List<String> stateNames, Boolean simpleQuery) {

        StringBuilder jqlQueryBuilder = new StringBuilder();
        Joiner joiner = Joiner.on("','");

        jqlQueryBuilder.append("project in ('");

        joiner.appendTo(jqlQueryBuilder, projects);

        if (issueTypes != null && !issueTypes.isEmpty()) {

            jqlQueryBuilder.append("') and issuetype in ('");

            joiner.appendTo(jqlQueryBuilder, issueTypes);

        }

        jqlQueryBuilder.append("') and status changed to ('");

        joiner.appendTo(jqlQueryBuilder, stateNames);

        jqlQueryBuilder.append("') after '");

        jqlQueryBuilder.append(startDate.toString(dateTimeFormatter));

        jqlQueryBuilder.append("' before '");

        jqlQueryBuilder.append(endDate.toString(dateTimeFormatter));

        if (simpleQuery) {

            jqlQueryBuilder.append("'");

            if (andClause != null && !andClause.trim().isEmpty()) {
                jqlQueryBuilder.append(" and (" + andClause + ")");
            }

            return jqlQueryBuilder.toString();

        }

        jqlQueryBuilder.append("' and status was not in ('");

        joiner.appendTo(jqlQueryBuilder, stateNames);

        jqlQueryBuilder.append("') on '");

        jqlQueryBuilder.append(startDate.toString(dateTimeFormatter));

        jqlQueryBuilder.append("' and status was in ('");

        joiner.appendTo(jqlQueryBuilder, stateNames);

        jqlQueryBuilder.append("') on '");

        jqlQueryBuilder.append(endDate.toString(dateTimeFormatter));

        jqlQueryBuilder.append("'");

        if (andClause != null && !andClause.trim().isEmpty()) {
            jqlQueryBuilder.append(" and (" + andClause + ")");
        }

        return jqlQueryBuilder.toString();
    }

    /**
     * @deprecated This Function uses a very complicated JQL Query to get Data from Jira, this often leads to Timeouts.
     * 
     * @param issueType
     * @param projects
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @return
     */
    @Deprecated
    public List<String> getKeysOfIssuesChangedDuringTimeToStates(List<String> issueType, List<String> projects,
            DateTime startDate, DateTime endDate, List<String> statusNames, ClientCredentials credentials) {

        String jqlQuery = createFilterForChangedDuringTimeToStates(issueType, projects, null, startDate, endDate,
                statusNames, false);

        return jiraSearch.searchJiraGetAllKeys(credentials, jqlQuery);
    }
    
    /**
     * Gets the Issues who changed States to the given states during the given Time. This Functions uses the REST
     * Workaround to be faster.
     * 
     * @param issueType
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @param additionalFields
     *            those are added to the basic Fields <b>No Spaces, separated by ,</b> , can be null if not needed
     * @return
     */
    public List<Issue> getIssuesChangedDuringTimeToStatesWorkaround(List<String> issueType, List<String> projects,
            String andClause, DateTime startDate, DateTime endDate, List<String> statusNames,
            ClientCredentials credentials, String additionalFields) {

        List<Issue> issuesChangedToStateDuringTime;
        String jqlQuery = createFilterForChangedDuringTimeToStates(issueType, projects, andClause, startDate, endDate,
                statusNames, true);
        String statusAtStart;
        String statusAtEnd;
        List<Issue> resultIssues = new ArrayList<>();

        issuesChangedToStateDuringTime = searchWithExpandsWorkaround.getSearchResultsWithChangelogWorkaround(
                credentials, jqlQuery, additionalFields, null);

        for (Issue currentIssue : issuesChangedToStateDuringTime) {

            statusAtStart = historyIssueService.getStatusAtTime(currentIssue, startDate);
            statusAtEnd = historyIssueService.getStatusAtTime(currentIssue, endDate);

            // We are looking for Issues that were not already in the given states at the Start and are still in the
            // given states at the end
            if (statusNames.contains(statusAtStart) || !statusNames.contains(statusAtEnd)) {
                continue;
            }

            resultIssues.add(currentIssue);

        }

        return resultIssues;
    }

    /**
     * Gets the Keys of the Issues who changed States to the given states during the given Time. This Functions uses the
     * REST Workaround to be faster.
     * 
     * @param issueType
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @return
     */
    public List<String> getKeysOfIssuesChangedDuringTimeToStatesWorkaround(List<String> issueType,
            List<String> projects, String andClause, DateTime startDate, DateTime endDate, List<String> statusNames,
            ClientCredentials credentials) {

        List<Issue> issuesChangedToStateDuringTime;
        List<String> resultKeys = new ArrayList<>();

        issuesChangedToStateDuringTime = getIssuesChangedDuringTimeToStatesWorkaround(issueType, projects, andClause,
                startDate, endDate, statusNames, credentials, null);

        for (Issue currentIssue : issuesChangedToStateDuringTime) {

            resultKeys.add(currentIssue.getKey());

        }

        return resultKeys;
    }

    /**
     * Gets the Keys of the Issues who changed States to the given states during the given Time. This Functions uses the
     * REST Workaround to be faster.
     * 
     * @param issueType
     * @param projects
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @return
     */
    public List<String> getKeysOfIssuesChangedDuringTimeToStatesWorkaround(List<String> issueType,
            List<String> projects, DateTime startDate, DateTime endDate, List<String> statusNames,
            ClientCredentials credentials) {

        return getKeysOfIssuesChangedDuringTimeToStatesWorkaround(issueType, projects, null, startDate, endDate,
                statusNames, credentials);

    }

    /**
     * Gets the Number of the Issues who changed States to the given states during the given Time. This Functions uses
     * the REST Workaround to be faster.
     * 
     * @param issueType
     * @param projects
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @return
     */
    public Integer getNumberOfIssuesChangedDuringTimeToStatesWorkaround(List<String> issueType, List<String> projects,
            DateTime startDate, DateTime endDate, List<String> statusNames, ClientCredentials credentials) {

        return getKeysOfIssuesChangedDuringTimeToStatesWorkaround(issueType, projects, startDate, endDate, statusNames,
                credentials).size();

    }

    /**
     * Gets the Number of the Issues who changed States to the given states during the given Time. This Functions uses
     * the REST Workaround to be faster.
     * 
     * @param issueType
     * @param projects
     * @param andClause
     * @param startDate
     * @param endDate
     * @param statusNames
     * @param credentials
     * @return
     */
    public Integer getNumberOfIssuesChangedDuringTimeToStatesWorkaround(List<String> issueType, List<String> projects,
            String andClause, DateTime startDate, DateTime endDate, List<String> statusNames,
            ClientCredentials credentials) {

        return getKeysOfIssuesChangedDuringTimeToStatesWorkaround(issueType, projects, andClause, startDate, endDate,
                statusNames, credentials).size();

    }

}
