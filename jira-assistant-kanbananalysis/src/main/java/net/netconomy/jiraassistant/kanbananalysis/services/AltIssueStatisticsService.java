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
package net.netconomy.jiraassistant.kanbananalysis.services;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.kanbananalysis.data.AltIssueStatistics;
import net.netconomy.jiraassistant.sprintanalysis.services.FlaggingStatisticsService;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AltIssueStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AltIssueStatisticsService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    FlaggingStatisticsService flaggingStatisticsService;

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

    public AltIssueStatistics getAltIssueStatistics(List<Issue> issues, List<Issue> subIssues, DateTime startDate, DateTime endDate,
            ClientCredentials credentials) throws ConfigurationException {

        AltIssueStatistics altIssueStatistics = new AltIssueStatistics();

        Integer issueCount = 0;
        Integer minutesSpent = 0;
        Integer currentMinutesSpent = 0;
        String currentEstimation;
        String currentStatus = "";
        String statusAtStart = "";
        Integer inProgressIssueCount = 0;
        Integer waitingIssueCount = 0;
        Integer implementedIssueCount = 0;
        Integer finishedIssueCount = 0;
        Integer closedIssueCount = 0;

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generating Issue Statistics for {} Issues.", issues.size());
        }

        altIssueStatistics.setReopenCount(calculateReopenCount(issues, startDate, endDate));

        for (Issue currentIssue : issues) {
            currentEstimation = advancedIssueService.getAltEstimation(currentIssue, configuration.getAltEstimationFieldName());
            currentStatus = historyIssueService.getStatusAtTime(currentIssue, endDate);

            statusAtStart = historyIssueService.getStatusAtTime(currentIssue, startDate);

            currentMinutesSpent = advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue, startDate,
                endDate, false);

            if (configuration.getFinishedStatus().contains(currentStatus)
                && !configuration.getFinishedStatus().contains(statusAtStart)) {
                // the Issue will only be counted as finished if it wasn't finished at the start of the period
                altIssueStatistics.addFinishedKey(currentIssue.getKey());
                altIssueStatistics.raiseFinished(currentEstimation);

                finishedIssueCount++;
            } else if (configuration.getClosedStatus().contains(currentStatus)
                && !configuration.getClosedStatus().contains(statusAtStart)) {
                // the Issue will only be counted as closed if it wasn't closed at the start of the period
                altIssueStatistics.addClosedKey(currentIssue.getKey());
                altIssueStatistics.raiseClosed(currentEstimation);
                closedIssueCount++;
            } else if (configuration.getImplementedStatus().contains(currentStatus)) {
                altIssueStatistics.addImplementedKey(currentIssue.getKey());
                altIssueStatistics.raiseImplemented(currentEstimation);
                implementedIssueCount++;
            } else if (configuration.getInProgressStatus().contains(currentStatus)) {
                altIssueStatistics.addInProgressKey(currentIssue.getKey());
                altIssueStatistics.raiseInProgress(currentEstimation);
                inProgressIssueCount++;
            } else if (configuration.getWaitingStatus().contains(currentStatus)) {
                altIssueStatistics.addWaitingKey(currentIssue.getKey());
                altIssueStatistics.raiseWaiting(currentEstimation);
                waitingIssueCount++;
            }

            minutesSpent += currentMinutesSpent;
            altIssueStatistics.addIssueWithEstimation(currentEstimation);
            issueCount++;

        }

        altIssueStatistics.setFlaggingStatistics(flaggingStatisticsService.generateFlaggingStatistics(issues, subIssues,
            startDate, endDate));

        minutesSpent += getMinutesSpentOnSubIssues(subIssues, startDate, endDate, credentials);

        altIssueStatistics.setNumberOfIssues(issueCount);
        altIssueStatistics.setNumberOfSubIssues(subIssues.size());

        altIssueStatistics.setMinutesSpent(minutesSpent);

        altIssueStatistics.setFinishedIssues(finishedIssueCount);
        altIssueStatistics.setClosedIssues(closedIssueCount);
        altIssueStatistics.setImplementedIssues(implementedIssueCount);
        altIssueStatistics.setInProgressIssues(inProgressIssueCount);
        altIssueStatistics.setWaitingIssues(waitingIssueCount);

        return altIssueStatistics;

    }

}
