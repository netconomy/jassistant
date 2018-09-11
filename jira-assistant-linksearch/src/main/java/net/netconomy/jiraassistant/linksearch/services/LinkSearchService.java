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
package net.netconomy.jiraassistant.linksearch.services;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchResult;
import net.netconomy.jiraassistant.linksearch.data.LinkedIssues;
import net.netconomy.jiraassistant.linksearch.data.ProjectSearchData;

@Service
public class LinkSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkSearchService.class);

    @Autowired
    FilterService filterService;

    @Autowired
    JiraSearch search;

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    Boolean linkedToProject(Issue issue, String otherProjectKey, String linkType) {

        Iterable<IssueLink> issueLinks = issue.getIssueLinks();
        String currentLinkedProjectKey;
        Boolean linkTypeFit = false;

        for (IssueLink currentLink : issueLinks) {

            currentLinkedProjectKey = basicIssueService.projectKeyFromIssueKey(currentLink.getTargetIssueKey());

            if (linkTypeEmpty(linkType) || linkType.equalsIgnoreCase(currentLink.getIssueLinkType().getName())) {
                linkTypeFit = true;
            }

            if (currentLinkedProjectKey.equals(otherProjectKey) && linkTypeFit) {
                return true;
            }

            linkTypeFit = false;

        }

        return false;

    }

    private boolean linkTypeEmpty(String linkType) {
        return linkType == null || linkType.trim().isEmpty();
    }

    private boolean linkTypeFit(String linkType, IssueLink currentLink) {
        return !linkTypeEmpty(linkType) && linkType.equalsIgnoreCase(currentLink.getIssueLinkType().getName());
    }

    private boolean projectFit(ProjectSearchData otherProjectData, String currentLinkedProjectKey) {
        return currentLinkedProjectKey.equals(otherProjectData.getProject());
    }

    private boolean projectAndLinkTypeFit(ProjectSearchData otherProjectData, String linkType,
        String currentLinkedProjectKey, IssueLink currentLink) {
        return projectFit(otherProjectData, currentLinkedProjectKey)
            && (linkTypeFit(linkType, currentLink) || linkTypeEmpty(linkType));
    }

    private boolean issueTypeEmpty(ProjectSearchData otherProjectData) {
        return otherProjectData.getIssueTypes().isEmpty();
    }

    private boolean statusEmpty(ProjectSearchData otherProjectData) {
        return otherProjectData.getStatus().isEmpty();
    }

    private boolean issueTypeNotNegatedAndContained(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return !issueTypeEmpty(otherProjectData) && !otherProjectData.getNegateIssueTypes()
            && otherProjectData.getIssueTypes().contains(currentLinkedIssue.getIssueType().getName());
    }

    private boolean issueTypeNegatedAndNotContained(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return !issueTypeEmpty(otherProjectData) && otherProjectData.getNegateIssueTypes()
            && !otherProjectData.getIssueTypes().contains(currentLinkedIssue.getIssueType().getName());
    }

    private boolean issueTypeFit(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return issueTypeNotNegatedAndContained(otherProjectData, currentLinkedIssue)
            || issueTypeNegatedAndNotContained(otherProjectData, currentLinkedIssue);
    }

    private boolean statusNotNegatedAndContained(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return !statusEmpty(otherProjectData) && !otherProjectData.getNegateStatus()
            && otherProjectData.getStatus().contains(currentLinkedIssue.getStatus().getName());
    }

    private boolean statusNegatedAndNotContained(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return !statusEmpty(otherProjectData) && otherProjectData.getNegateStatus()
            && !otherProjectData.getStatus().contains(currentLinkedIssue.getStatus().getName());
    }

    private boolean statusFit(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return statusNotNegatedAndContained(otherProjectData, currentLinkedIssue)
            || statusNegatedAndNotContained(otherProjectData, currentLinkedIssue);
    }

    private boolean issueTypeAndStatusEmpty(ProjectSearchData otherProjectData) {
        return issueTypeEmpty(otherProjectData) && statusEmpty(otherProjectData);
    }

    private boolean issueTypeAndStatusFit(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return issueTypeFit(otherProjectData, currentLinkedIssue) && statusFit(otherProjectData, currentLinkedIssue);
    }

    private boolean issueTypeEmptyAndStatusFit(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return issueTypeEmpty(otherProjectData) && statusFit(otherProjectData, currentLinkedIssue);
    }

    private boolean statusEmptyAndIssueTypFit(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return statusEmpty(otherProjectData) && issueTypeFit(otherProjectData, currentLinkedIssue);
    }

    private boolean oneIsEmptyAndTheOtherFits(ProjectSearchData otherProjectData, Issue currentLinkedIssue) {
        return issueTypeEmptyAndStatusFit(otherProjectData, currentLinkedIssue)
            || statusEmptyAndIssueTypFit(otherProjectData, currentLinkedIssue);
    }

    private List<Issue> linkToSpecifiedIssues(ClientCredentials credentials, Issue issue,
        ProjectSearchData otherProjectData,
        String linkType) {

        List<Issue> resultList = new ArrayList<>();
        Iterable<IssueLink> issueLinks = issue.getIssueLinks();
        String currentLinkedProjectKey;
        Issue currentLinkedIssue;

        if (!linkedToProject(issue, otherProjectData.getProject(), linkType)) {
            return resultList;
        }

        for (IssueLink currentLink : issueLinks) {

            currentLinkedProjectKey = basicIssueService.projectKeyFromIssueKey(currentLink.getTargetIssueKey());

            // The linked Project and the Link Type fit or the Link Type is empty
            if (projectAndLinkTypeFit(otherProjectData, linkType, currentLinkedProjectKey, currentLink)) {

                currentLinkedIssue = basicIssueService.getIssue(credentials, currentLink.getTargetIssueKey(), false);

                // No restriction on Issue Type or Status
                if (issueTypeAndStatusEmpty(otherProjectData) ||
                    issueTypeAndStatusFit(otherProjectData, currentLinkedIssue) ||
                    oneIsEmptyAndTheOtherFits(otherProjectData, currentLinkedIssue)) {
                    resultList.add(currentLinkedIssue);
                }

            }

        }

        return resultList;

    }

    /**
     * Search in Links according to the given linkSearchData
     */
    public LinkSearchResult searchLinks(ClientCredentials credentials, LinkSearchData linkSearchData,
        List<String> wantedFields) {

        LinkSearchResult linkSearchResult = new LinkSearchResult();
        String queryProject1;
        List<Issue> foundIssues;
        LinkedIssues currentLinkedIssues;
        List<Issue> foundFittingLinkedIssues;
        List<IssueLight> foundFittingLinkedLightIssues;
        Integer numberOfTotalResults = 0;
        Boolean linkedToSecondProject = false;

        linkSearchResult.setLinkSearchData(linkSearchData);

        queryProject1 = filterService.createFilter(linkSearchData.getProject1Data());

        linkSearchResult.setUsedFilter(queryProject1);

        linkSearchResult.setLinkSearchDate(DateTime.now().toString());

        numberOfTotalResults = search.searchJiraGetNumberOfResults(credentials, queryProject1);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{} Issues found in Total.", numberOfTotalResults);
        }

        foundIssues = search.searchJiraGetAllIssues(credentials, queryProject1);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Processing Links of {} Issues.", numberOfTotalResults);
        }

        for (Issue currentIssue : foundIssues) {

            currentLinkedIssues = new LinkedIssues();

            linkedToSecondProject = linkedToProject(currentIssue, linkSearchData.getProject2Data().getProject(),
                linkSearchData.getLinkType());

            if (linkSearchData.getNoLink()) {

                if (!linkedToSecondProject) {
                    currentLinkedIssues.setRootIssue(advancedIssueService.getIssueLight(currentIssue, wantedFields));
                    linkSearchResult.addSearchResultIssueLink(currentLinkedIssues);
                }

                continue;
            }

            foundFittingLinkedIssues = linkToSpecifiedIssues(credentials, currentIssue,
                linkSearchData.getProject2Data(), linkSearchData.getLinkType());

            if (!foundFittingLinkedIssues.isEmpty()) {

                currentLinkedIssues.setRootIssue(advancedIssueService.getIssueLight(currentIssue, wantedFields));

                foundFittingLinkedLightIssues = new ArrayList<>();

                for (Issue currentLinkedIssue : foundFittingLinkedIssues) {
                    foundFittingLinkedLightIssues.add(advancedIssueService.getIssueLight(currentLinkedIssue,
                        wantedFields));
                }

                currentLinkedIssues.setLinkedIssueList(foundFittingLinkedLightIssues);
                linkSearchResult.addSearchResultIssueLink(currentLinkedIssues);

            }

        }

        linkSearchResult.setNumberOfFoundLinkItems(linkSearchResult.getSearchResultIssueLinks().size());

        return linkSearchResult;
    }

}
