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
package net.netconomy.jiraassistant.base.services.issues;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.IssueRestClient.Expandos;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import org.springframework.util.CollectionUtils;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraClientFactory;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;

@Service
public class BasicIssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicIssueService.class);

    @Autowired
    JiraClientFactory clientFactory;

    private Issue getIssue(JiraRestClient client, String issueKey, Boolean withChangelog) {

        List<Expandos> expandosList = new ArrayList<>();

        if (withChangelog) {
            expandosList.add(Expandos.CHANGELOG);
        }

        Issue issue = client.getIssueClient().getIssue(issueKey, expandosList).claim();

        return issue;

    }

    /**
     * Get an Issue from Jira, with or without Changelog
     * 
     * @param credentials
     * @param issueKey
     * @param withChangelog
     * @return
     */
    public Issue getIssue(ClientCredentials credentials, String issueKey, Boolean withChangelog) {

        try (JiraRestClient client = clientFactory.create(credentials);) {
            return getIssue(client, issueKey, withChangelog);
        } catch (IOException | URISyntaxException e) {
            throw new JiraAssistantException(e.getMessage(), e);
        }
    }

    /**
     * Get multiple Issues from Jira
     * 
     * @param credentials
     * @param issueKeyList
     * @param withChangelog
     * @return
     */
    public Map<String, Issue> getMultipleIssues(ClientCredentials credentials, List<String> issueKeyList,
            Boolean withChangelog) {

        Map<String, Issue> issueMap = new HashMap<>();
        if (CollectionUtils.isEmpty(issueKeyList)) {
            return issueMap;
        }

        try (JiraRestClient client = clientFactory.create(credentials);) {

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Retrieving {} issues from Jira {}.", issueKeyList.size(), withChangelog ? "with changelog" : "without changelog");
            }

            for (String currentKey : issueKeyList) {
                issueMap.put(currentKey, getIssue(client, currentKey, withChangelog));
            }

            return issueMap;
        } catch (IOException | URISyntaxException e) {
            throw new JiraAssistantException(e.getMessage(), e);
        }
    }

    /**
     * Returns the Project Key from the given Issue Key
     * 
     * @param issueKey
     * @return
     */
    public String projectKeyFromIssueKey(String issueKey) {

        String projectKey;

        projectKey = issueKey.substring(0, issueKey.indexOf("-"));

        return projectKey;

    }

    /**
     * Returns the Project Key from the given Issue
     * 
     * @param issue
     * @return
     */
    public String projectKeyFromIssue(Issue issue) {

        return projectKeyFromIssueKey(issue.getKey());

    }

    /**
     * Returns a List of Issues, the Sub Issues from the source List will not be part of the returned List
     * 
     * @param issueList
     * @return
     */
    public List<Issue> getOnlyIssues(List<Issue> issueList) {

        List<Issue> onlyIssuesList = new ArrayList<>();

        for (Issue currentIssue : issueList) {
            if (!currentIssue.getIssueType().isSubtask()) {
                onlyIssuesList.add(currentIssue);
            }
        }

        return onlyIssuesList;
    }

    public List<Issue> filterIssues(List<Issue> issues, IssueFilter filter) {
        return issues.stream().filter(issue -> this.isIncluded(issue, filter)).collect(Collectors.toList());
    }


    public boolean isIncluded(String issueKey, IssueFilter filter) {
        return this.isProjectIncluded(this.projectKeyFromIssueKey(issueKey), filter);
    }

    private boolean isProjectIncluded(String key, IssueFilter filter) {
        if (filter == null || filter.getProjectKeys().isEmpty()) {
            return true;
        }
        return filter.getProjectKeys().contains(key);
    }

    public boolean isIncluded(Issue issue, IssueFilter filter) {
        if (issue.getProject() == null) {
            return this.isIncluded(issue.getKey(), filter);
        }
        return this.isProjectIncluded(issue.getProject().getKey(), filter);
    }

    public List<String> filterIssueKeys(final List<String> issueKeys, final IssueFilter filter) {
        return issueKeys.stream().filter(key -> this.isIncluded(key, filter)).collect(Collectors.toList());
    }

}
