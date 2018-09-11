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
package net.netconomy.jiraassistant.projectstatus.services;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.projectstatus.data.CustomGroupingData;
import net.netconomy.jiraassistant.projectstatus.data.IssuesInGroupingData;
import net.netconomy.jiraassistant.projectstatus.data.ProjectStatusData;
import net.netconomy.jiraassistant.projectstatus.data.StatusGroupingData;

@Service
public class ProjectStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectStatusService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    BacklogFilterService backlogFilterService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    JiraSearch jiraSearch;

    private String createJQLFilter(List<String> projectList, List<String> excludedTypes, String andClause) {

        return backlogFilterService.generateBacklogFilterBasedOnProjects(projectList, andClause, null, null,
                excludedTypes, true, null);

    }

    void processIssue(Issue thisIssue, IssuesInGroupingData issuesInGroupingData, ProjectConfiguration projConf) {

        Double currentEstimation = 0.0;

        issuesInGroupingData.countIssue();

        issuesInGroupingData.countIssueType(thisIssue.getIssueType().getName());

        currentEstimation = advancedIssueService.getEstimation(thisIssue, projConf.getEstimationFieldName());

        if (currentEstimation.compareTo(0.0) != 0) {
            issuesInGroupingData.increaseStoryPoints(currentEstimation);
        } else {
            issuesInGroupingData.countUnEstimatedIssue();
        }

    }

    void processCustomGrouping(Issue thisIssue, StatusGroupingData issuesInStatusData, String groupBy,
            ProjectConfiguration projConf) {

        IssueField groupingField = thisIssue.getFieldByName(groupBy);
        String groupingValue;
        CustomGroupingData customGroupingData;

        if (groupingField != null && groupingField.getValue() != null) {
            groupingValue = groupingField.getValue().toString();
        } else {
            groupingValue = "unGrouped";
        }

        if (issuesInStatusData.getCustomGroupingMap() != null
                && issuesInStatusData.getCustomGroupingMap().containsKey(groupingValue)) {
            customGroupingData = issuesInStatusData.getCustomGroupingMap().get(groupingValue);
        } else {
            customGroupingData = new CustomGroupingData(groupBy, groupingValue);
        }

        processIssue(thisIssue, customGroupingData, projConf);

        issuesInStatusData.putCustomGrouping(groupingValue, customGroupingData);

    }

    void processStatusGrouping(Issue thisIssue, StatusGroupingData issuesInStatusData, String groupBy,
            ProjectConfiguration projConf) {

        processIssue(thisIssue, issuesInStatusData, projConf);

        issuesInStatusData.addStatus(thisIssue.getStatus().getName());
        
        if (groupBy != null) {
            processCustomGrouping(thisIssue, issuesInStatusData, groupBy, projConf);
        }

    }

    ProjectStatusData calculateProjectStatus(ClientCredentials credentials, ProjectConfiguration projConf,
            List<String> projectList, String groupBy, List<String> excludedTypes, String andClause) {

        ProjectStatusData projectStatusData = new ProjectStatusData();

        String jqlQuery = createJQLFilter(projectList, excludedTypes, andClause);

        List<Issue> issueList;
        String currentIssueStatus;

        StatusGroupingData openIssues = null;
        StatusGroupingData inProgressIssues = null;
        StatusGroupingData waitingIssues = null;
        StatusGroupingData implementedIssues = null;
        StatusGroupingData finishedIssues = null;
        StatusGroupingData closedIssues = null;

        projectStatusData.setCreationDate(DateTime.now().toString());
        projectStatusData.setProjects(projectList);
        projectStatusData.setExcludedTypes(excludedTypes);
        projectStatusData.setAndClause(andClause);
        projectStatusData.setUsedFilter(jqlQuery);
        projectStatusData.setCustomGroupingBy(groupBy);
        
        projectStatusData.setInProgressStatus(projConf.getInProgressStatus());
        projectStatusData.setWaitingStatus(projConf.getWaitingStatus());
        projectStatusData.setImplementedStatus(projConf.getImplementedStatus());
        projectStatusData.setFinishedStatus(projConf.getFinishedStatus());
        projectStatusData.setClosedStatus(projConf.getClosedStatus());

        issueList = jiraSearch.searchJiraGetAllIssues(credentials, jqlQuery);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Analysing {} Issues for Project Status.", issueList.size());
        }

        for (Issue currentIssue : issueList) {

            projectStatusData.countIssue();

            currentIssueStatus = currentIssue.getStatus().getName();

            if (projConf.getInProgressStatus().contains(currentIssueStatus)) {

                if (inProgressIssues == null) {
                    inProgressIssues = new StatusGroupingData("In Progress");
                }
                processStatusGrouping(currentIssue, inProgressIssues, groupBy, projConf);

            } else if (projConf.getWaitingStatus().contains(currentIssueStatus)) {

                if (waitingIssues == null) {
                    waitingIssues = new StatusGroupingData("Waiting");
                }
                processStatusGrouping(currentIssue, waitingIssues, groupBy, projConf);

            } else if (projConf.getImplementedStatus().contains(currentIssueStatus)) {

                if (implementedIssues == null) {
                    implementedIssues = new StatusGroupingData("Implemented");
                }
                processStatusGrouping(currentIssue, implementedIssues, groupBy, projConf);

            } else if (projConf.getFinishedStatus().contains(currentIssueStatus)) {

                if (finishedIssues == null) {
                    finishedIssues = new StatusGroupingData("Finished");
                }
                processStatusGrouping(currentIssue, finishedIssues, groupBy, projConf);

            } else if (projConf.getClosedStatus().contains(currentIssueStatus)) {

                if (closedIssues == null) {
                    closedIssues = new StatusGroupingData("Closed");
                }
                processStatusGrouping(currentIssue, closedIssues, groupBy, projConf);

            } else {

                projectStatusData.addOpenStatus(currentIssueStatus);

                if (openIssues == null) {
                    openIssues = new StatusGroupingData("Open");
                }
                processStatusGrouping(currentIssue, openIssues, groupBy, projConf);

            }
        }

        projectStatusData.setOpenIssues(openIssues);
        projectStatusData.setInProgressIssues(inProgressIssues);
        projectStatusData.setWaitingIssues(waitingIssues);
        projectStatusData.setImplementedIssues(implementedIssues);
        projectStatusData.setFinishedIssues(finishedIssues);
        projectStatusData.setClosedIssues(closedIssues);

        return projectStatusData;
    }

    public ProjectStatusData analyseProjects(ClientCredentials credentials, List<String> projectList, String groupBy,
            List<String> excludedTypes, String andClause) {

        try {
            ProjectConfiguration projConf = configurationService.getProjectConfiguration();

            return calculateProjectStatus(credentials, projConf, projectList, groupBy, excludedTypes, andClause);

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        }

    }

}
