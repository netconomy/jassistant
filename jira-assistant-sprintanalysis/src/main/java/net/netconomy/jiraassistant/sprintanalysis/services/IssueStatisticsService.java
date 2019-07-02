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
package net.netconomy.jiraassistant.sprintanalysis.services;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.data.IssueStatistics;

@Service
public class IssueStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueStatisticsService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;
    
    @Autowired
    FlaggingStatisticsService flaggingStatisticsService;

    @Autowired
    BasicIssueService basicIssueService;

    private Integer calculateReopenCount(List<Issue> issues, DateTime sprintStart, DateTime sprintEnd)
            throws ConfigurationException {

        Integer reopenCount = 0;
        List<String> reopenStatus = configurationService.getProjectConfiguration().getReopenedStatus();

        for (String currentReopenStatus : reopenStatus) {
            reopenCount += historyIssueService.getStateCountForIssues(issues, currentReopenStatus, sprintStart,
                    sprintEnd);
        }

        return reopenCount;
    }

    private Integer getMinutesSpentOnSubIssues(List<Issue> subIssues, DateTime sprintStart, DateTime sprintEnd,
            ClientCredentials credentials) {

        Integer minutesSpent = 0;

        for (Issue currentSubIssue : subIssues) {
            minutesSpent += advancedIssueService.getMinutesSpentWorkaround(credentials, currentSubIssue, sprintStart,
                    sprintEnd, false);
        }

        return minutesSpent;
    }

    /**
     * Get Issue Statistics based on the given Status Lists, estimationField for the given Issues.
     * 
     * @param issues
     * @param subIssues
     * @param startDate
     *            The Time the Sprint started.
     * @param endDate
     *            The Time the Sprint ended, or will end. This Time will be used to get the Status at the End of the
     *            Sprint.
     * @param sprintDataDelta
     * @param credentials
     * @return
     * @throws ConfigurationException
     */
    IssueStatistics getIssueStatistics(List<Issue> issues, List<Issue> subIssues, DateTime startDate, DateTime endDate,
            SprintDataDelta sprintDataDelta, ClientCredentials credentials, IssueFilter filter) throws ConfigurationException {
        IssueStatistics issueStatistics = new IssueStatistics();
        Integer issueCount = 0;
        Double storyPoints = 0.0;
        Integer minutesSpent = 0;
        Integer currentMinutesSpent = 0;
        Double currentEstimation = 0.0;
        String currentStatus = "";
        String statusAtStart = "";
        Integer inProgressIssueCount = 0;
        Integer waitingIssueCount = 0;
        Integer implementedIssueCount = 0;
        Integer finishedIssueCount = 0;
        Integer closedIssueCount = 0;
        Double inProgressStoryPoints = 0.0;
        Double waitingStoryPoints = 0.0;
        Double implementedStoryPoints = 0.0;
        Double finishedStoryPoints = 0.0;
        Double closedStoryPoints = 0.0;
        Integer plannedDeliveredIssues = 0;
        Double plannedDeliveredStoryPoints = 0.0;

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generating Issue Statistics for {} Issues.", issues.size());
        }

        issueStatistics.setReopenCount(calculateReopenCount(issues, startDate, endDate));

        for (Issue currentIssue : issues) {
            if (!this.basicIssueService.isIncluded(currentIssue, filter)) {
                continue;
            }
            currentEstimation = advancedIssueService
                    .getEstimation(currentIssue, configuration.getEstimationFieldName());
            currentStatus = historyIssueService.getStatusAtTime(currentIssue, endDate);

            statusAtStart = historyIssueService.getStatusAtTime(currentIssue, startDate);
            
            currentMinutesSpent = advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue, startDate,
                    endDate, false);

            if (configuration.getFinishedStatus().contains(currentStatus)
                    && !configuration.getFinishedStatus().contains(statusAtStart)) {
                // the Issue will only be counted as finished if it wasn't finished at the start of the period
                issueStatistics.addFinishedKey(currentIssue.getKey());
                finishedStoryPoints += currentEstimation;

                if (sprintDataDelta == null) {
                    plannedDeliveredIssues = null;
                    plannedDeliveredStoryPoints = null;
                } else if (!sprintDataDelta.getAddedIssueKeys().contains(currentIssue.getKey())) {
                    // was the issue added during Sprint, if not it was planned
                    plannedDeliveredIssues++;
                    plannedDeliveredStoryPoints += currentEstimation;
                }

                finishedIssueCount++;
            } else if (configuration.getClosedStatus().contains(currentStatus)
                    && !configuration.getClosedStatus().contains(statusAtStart)) {
                // the Issue will only be counted as closed if it wasn't closed at the start of the period
                issueStatistics.addClosedKey(currentIssue.getKey());
                closedStoryPoints += currentEstimation;
                closedIssueCount++;
            } else if (configuration.getImplementedStatus().contains(currentStatus)) {
                issueStatistics.addImplementedKey(currentIssue.getKey());
                implementedStoryPoints += currentEstimation;
                implementedIssueCount++;
            } else if (configuration.getInProgressStatus().contains(currentStatus)) {
                issueStatistics.addInProgressKey(currentIssue.getKey());
                inProgressStoryPoints += currentEstimation;
                inProgressIssueCount++;
            } else if (configuration.getWaitingStatus().contains(currentStatus)) {
                issueStatistics.addWaitingKey(currentIssue.getKey());
                waitingStoryPoints += currentEstimation;
                waitingIssueCount++;
            }

            minutesSpent += currentMinutesSpent;
            storyPoints += currentEstimation;
            issueCount++;

        }

        issueStatistics.setFlaggingStatistics(flaggingStatisticsService.generateFlaggingStatistics(issues, subIssues,
                startDate, endDate));

        minutesSpent += getMinutesSpentOnSubIssues(subIssues, startDate, endDate, credentials);

        issueStatistics.setNumberOfIssues(issueCount);
        issueStatistics.setNumberOfSubIssues(subIssues.size());
        issueStatistics.setNumberOfStoryPoints(storyPoints);

        issueStatistics.setPlannedDeliveredIssues(plannedDeliveredIssues);
        issueStatistics.setPlannedDeliveredStoryPoints(plannedDeliveredStoryPoints);

        issueStatistics.setMinutesSpent(minutesSpent);

        issueStatistics.setFinishedIssues(finishedIssueCount);
        issueStatistics.setClosedIssues(closedIssueCount);
        issueStatistics.setImplementedIssues(implementedIssueCount);
        issueStatistics.setInProgressIssues(inProgressIssueCount);
        issueStatistics.setWaitingIssues(waitingIssueCount);

        issueStatistics.setFinishedStoryPoints(finishedStoryPoints);
        issueStatistics.setClosedStoryPoints(closedStoryPoints);
        issueStatistics.setImplementedStoryPoints(implementedStoryPoints);
        issueStatistics.setInProgressStoryPoints(inProgressStoryPoints);
        issueStatistics.setWaitingStoryPoints(waitingStoryPoints);

        return issueStatistics;
    }

    /**
     * Get Issue Statistics based on the given Status Lists, estimationField for the given Issues.
     * 
     * @param issues
     * @param subIssues
     * @param configuration
     * @param startDate
     * @param endDate
     * @return
     * @throws ConfigurationException
     */
    public IssueStatistics getIssueStatistics(List<Issue> issues, List<Issue> subIssues, DateTime startDate,
            DateTime endDate, ClientCredentials credentials, IssueFilter issueFilter) throws ConfigurationException {

        return getIssueStatistics(issues, subIssues, startDate, endDate, null, credentials, issueFilter);

    }

}
