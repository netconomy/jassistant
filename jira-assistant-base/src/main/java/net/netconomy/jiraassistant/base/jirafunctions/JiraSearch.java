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
package net.netconomy.jiraassistant.base.jirafunctions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraSearch {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraSearch.class);

    /**
     * Those are the Fields needed to get a Response from Jira without an Exception
     */
    public static final Set<String> MINIMUMFIELDS = new TreeSet<>();

    private static final Integer MAXRESULTS = 50;

    static {
        MINIMUMFIELDS.add("summary");
        MINIMUMFIELDS.add("issuetype");
        MINIMUMFIELDS.add("created");
        MINIMUMFIELDS.add("updated");
        MINIMUMFIELDS.add("project");
        MINIMUMFIELDS.add("status");
    }

    @Autowired
    JiraClientFactory clientFactory;

    private SearchResult searchJira(JiraRestClient client, String jqlQuery, Integer maxResults, Integer startAt,
            Set<String> fields) {

        SearchRestClient searchClient = client.getSearchClient();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The Query: '{}' will be sent to Jira.", jqlQuery);
        }

        Promise<SearchResult> resultPromise = searchClient.searchJql(jqlQuery, maxResults, startAt, fields);

        return resultPromise.claim();
    }

    private List<Issue> getIssueListFromIterable(Iterable<Issue> iterable) {

        List<Issue> issueList = new ArrayList<>();

        for (Issue currentIssue : iterable) {
            issueList.add(currentIssue);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Found Issue: {}", currentIssue.getKey());
            }
        }

        return issueList;

    }

    /**
     * Performs a JQL search over the given JiraRestClient and returns issues matching the query. The first startAt
     * issues will be skipped and SearchResult will contain at most maxResults issues. List of issue fields which should
     * be included in the result may be specified.
     *
     * @param credentials
     *            The Credentials to access Jira
     * @param jqlQuery
     *            a valid JQL query (will be properly encoded by JIRA client). Restricted JQL characters (like '/') must
     *            be properly escaped. All issues matches to the null or empty JQL.
     * @param maxResults
     *            maximum results for this search. When null is given, the default maxResults configured in JIRA is used
     *            (usually 50).
     * @param startAt
     *            starting index (0-based) defining how many issues should be skipped in the results. For example for
     *            startAt=5 and maxResults=3 the results will include matching issues with index 5, 6 and 7. For startAt
     *            = 0 and maxResults=3 the issues returned are from position 0, 1 and 2. When null is given, the default
     *            startAt is used (0).
     * @param fields
     *            set of fields which should be retrieved. You can specify *all for all fields or *navigable (which is
     *            the default value, used when null is given) which will cause to include only navigable fields in the
     *            result. To ignore the specific field you can use "-" before the field's name. Note that the following
     *            fields: summary, issuetype, created, updated, project and status are required. These fields are
     *            included in *all and *navigable.
     * @return issues matching given JQL query
     */
    public List<Issue> searchJira(ClientCredentials credentials, String jqlQuery, Integer maxResults, Integer startAt,
            Set<String> fields) {

        List<Issue> resultList;

        SearchResult result;

        try (JiraRestClient client = clientFactory.create(credentials);) {

            result = searchJira(client, jqlQuery, maxResults, startAt, fields);

            resultList = getIssueListFromIterable(result.getIssues());

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Retrieved {} issues (starting at {}) from {} total issues.", resultList.size(), startAt, result.getTotal());
            }

            return resultList;

        } catch (IOException | URISyntaxException e) {
            throw new JiraAssistantException(e.getMessage(), e);
        }
    }

    /**
     * Returns all Issues found with the given Query over the given Client.
     * 
     * @param credentials
     *            The Credentials to access Jira
     * @param jqlQuery
     *            a valid JQL query (will be properly encoded by JIRA client). Restricted JQL characters (like '/') must
     *            be properly escaped. All issues matches to the null or empty JQL.
     * @param fields
     *            set of fields which should be retrieved. You can specify *all for all fields or *navigable (which is
     *            the default value, used when null is given) which will cause to include only navigable fields in the
     *            result. To ignore the specific field you can use "-" before the field's name. Note that the following
     *            fields: summary, issuetype, created, updated, project and status are required. These fields are
     *            included in *all and *navigable.
     * @return
     */
    public List<Issue> searchJiraGetAllIssues(ClientCredentials credentials, String jqlQuery, Set<String> fields) {
        int totalIssues;
        List<Issue> allIssues = new ArrayList<>();
        SearchResult result;

        try (JiraRestClient client = clientFactory.create(credentials);) {
            int startAt = 0;
            while(true) {
                result = searchJira(client, jqlQuery, MAXRESULTS, startAt, fields);
                totalIssues = result.getTotal();
                allIssues.addAll(getIssueListFromIterable(result.getIssues()));

                if (totalIssues >= allIssues.size()) {
                    break;
                }
                startAt += MAXRESULTS;
            }

            return allIssues;

        } catch (IOException | URISyntaxException e) {
            throw new JiraAssistantException(e.getMessage(), e);
        }
    }

    /**
     * Returns all Keys of the Issues found with the given Query over the given Client.
     * 
     * @param credentials
     * @param jqlQuery
     * @return
     */
    public List<String> searchJiraGetAllKeys(ClientCredentials credentials, String jqlQuery) {

        List<String> allIssueKeys = new ArrayList<>();

        List<Issue> allIssues = searchJiraGetAllIssues(credentials, jqlQuery, MINIMUMFIELDS);

        for (Issue currenIssue : allIssues) {
            allIssueKeys.add(currenIssue.getKey());
        }

        return allIssueKeys;
    }

    /**
     * Returns all Issues found with the given Query over the given Client.
     * 
     * @param credentials
     * @param jqlQuery
     * @return
     */
    public List<Issue> searchJiraGetAllIssues(ClientCredentials credentials, String jqlQuery) {

        Set<String> fields = new HashSet<>();

        fields.add("*all");

        return searchJiraGetAllIssues(credentials, jqlQuery, fields);
    }

    /**
     * Returns the Number of Results for the given Query.
     * 
     * @param credentials
     * @param jqlQuery
     * @return
     */
    public Integer searchJiraGetNumberOfResults(ClientCredentials credentials, String jqlQuery) {

        SearchResult result;
        Integer totalIssues = 0;

        try (JiraRestClient client = clientFactory.create(credentials);) {

            result = searchJira(client, jqlQuery, 1, 0, null);

            totalIssues = result.getTotal();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The Query: '{}' came back with {} Issues", jqlQuery, totalIssues);
            }

        } catch (IOException | URISyntaxException e) {
            throw new JiraAssistantException(e.getMessage(), e);
        }

        return totalIssues;

    }

}
