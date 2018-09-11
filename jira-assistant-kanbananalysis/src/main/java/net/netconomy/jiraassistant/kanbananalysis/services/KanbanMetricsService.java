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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.StatisticsService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanMetrics;

@Service
public class KanbanMetricsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanbanMetricsService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    HistoryIssueService historyIssueService;
    
    @Autowired
    StatisticsService statisticsService;

    /**
     * Get the Duration from the first Start of the Issue to the Resolution of the Issue, null if the Issue is not
     * resolved yet.
     * 
     * @param issue
     * @return
     * @throws ConfigurationException
     */
    public Duration getCycleTime(Issue issue) throws ConfigurationException {

        Duration timeFromInProgressToFinished;
        DateTime inProgressDateTime;
        DateTime resolvedDateTime;

        inProgressDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configurationService
                .getProjectConfiguration().getInProgressStatus());

        resolvedDateTime = historyIssueService.getResolutionDate(issue, configurationService.getProjectConfiguration()
                .getResolutionDateFieldName());

        if (inProgressDateTime == null || resolvedDateTime == null) {
            return null;
        }

        timeFromInProgressToFinished = new Duration(inProgressDateTime, resolvedDateTime);

        return timeFromInProgressToFinished;
    }

    /**
     * Get the Duration from the creation of the Issue to the Time it went Live, null if the Issue is not live yet.
     * 
     * @param issue
     * @return
     * @throws ConfigurationException
     */
    public Duration getLeadTime(Issue issue) throws ConfigurationException {

        Duration timeFromInProgressToLive;
        DateTime createdDateTime;
        DateTime liveDateTime;

        createdDateTime = issue.getCreationDate();

        liveDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configurationService
            .getProjectConfiguration().getLiveStatus());

        if (createdDateTime == null || liveDateTime == null) {
            return null;
        }

        timeFromInProgressToLive = new Duration(createdDateTime, liveDateTime);

        return timeFromInProgressToLive;
    }
    
    /**
     * Get the Duration this Issue was in a Waiting Status.
     * 
     * @param issue
     * @param endDate
     * @return
     * @throws ConfigurationException
     */
    public Duration getWaitingTime(Issue issue, DateTime endDate) throws ConfigurationException {

        Duration waitingTimeDuration = new Duration(0);
        Duration currentDuration;
        DateTime createdDateTime;

        createdDateTime = issue.getCreationDate();

        if (createdDateTime == null) {
            return null;
        }

        for (String currentWaitingStatus : configurationService.getProjectConfiguration().getWaitingStatus()) {

            currentDuration = historyIssueService.getValueDurationForIssueField(issue, "status", currentWaitingStatus,
                    createdDateTime, endDate);

            waitingTimeDuration = waitingTimeDuration.plus(currentDuration);
        }

        return waitingTimeDuration;
    }

    public KanbanMetrics calculateKanbanMetrics(List<Issue> issueList, DateTime endDate)
            throws ConfigurationException {

        KanbanMetrics kanbanMetrics = new KanbanMetrics();
        Duration leadTime;
        Duration cycleTime;
        Duration waitingTime;
        Double leadTimeMinutes;
        Double cycleTimeMinutes;
        Double waitingTimeMinutes;
        List<Double> allLeadTimesMinutes = new ArrayList<>();
        List<Double> allCycleTimesMinutes = new ArrayList<>();
        List<Double> allWaitingTimesMinutes = new ArrayList<>();

        LOGGER.info("Calculating Kanban Metrics for {} Issues.", issueList.size());

        for (Issue currentIssue : issueList) {

            leadTime = getLeadTime(currentIssue);
            cycleTime = getCycleTime(currentIssue);
            waitingTime = getWaitingTime(currentIssue, endDate);

            if (leadTime != null) {

                leadTimeMinutes = Double.valueOf(leadTime.getStandardMinutes());

                allLeadTimesMinutes.add(leadTimeMinutes);

            }

            if (cycleTime != null) {

                cycleTimeMinutes = Double.valueOf(cycleTime.getStandardMinutes());

                allCycleTimesMinutes.add(cycleTimeMinutes);
            }

            if (waitingTime != null) {

                waitingTimeMinutes = Double.valueOf(waitingTime.getStandardMinutes());

                allWaitingTimesMinutes.add(waitingTimeMinutes);

            }

        }

        kanbanMetrics.setMeanLeadTime(statisticsService.calculateMean(allLeadTimesMinutes));
        kanbanMetrics.setMedianLeadTime(statisticsService.calculateMedian(allLeadTimesMinutes));
        kanbanMetrics.setStandardDeviationLeadTime(statisticsService.calculateStandardDeviation(allLeadTimesMinutes));

        kanbanMetrics.setMeanCycleTime(statisticsService.calculateMean(allCycleTimesMinutes));
        kanbanMetrics.setMedianCycleTime(statisticsService.calculateMedian(allCycleTimesMinutes));
        kanbanMetrics.setStandardDeviationCycleTime(statisticsService.calculateStandardDeviation(allCycleTimesMinutes));

        kanbanMetrics.setMeanWaitingTime(statisticsService.calculateMean(allWaitingTimesMinutes));
        kanbanMetrics.setMedianWaitingTime(statisticsService.calculateMedian(allWaitingTimesMinutes));
        kanbanMetrics.setStandardDeviationWaitingTime(statisticsService
                .calculateStandardDeviation(allWaitingTimesMinutes));

        return kanbanMetrics;

    }

    Map<String, List<Issue>> getIssuesByAltEstimation(List<Issue> issueList, Boolean withAltEstimations) throws ConfigurationException {

        Map<String, List<Issue>> issueMap = new HashMap<>();
        String currentEstimation;

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();

        for(Issue currentIssue : issueList) {

            if(withAltEstimations) {
                currentEstimation = advancedIssueService.getAltEstimation(currentIssue, configuration.getAltEstimationFieldName());
            } else {
                currentEstimation = advancedIssueService.getEstimation(currentIssue, configuration.getEstimationFieldName()).toString();
            }

            if(!issueMap.containsKey(currentEstimation)) {
                issueMap.put(currentEstimation, new ArrayList<>());
            }

            issueMap.get(currentEstimation).add(currentIssue);

        }

        return issueMap;

    }

    public Map<String, KanbanMetrics> getKanbanMetricsByEstimation(List<Issue> issueList, DateTime endDate, Boolean withAltEstimations) throws ConfigurationException {

        Map<String, KanbanMetrics> metricsMap = new HashMap<>();

        Map<String, List<Issue>> issueByEstimationMap = getIssuesByAltEstimation(issueList, withAltEstimations);

        for(Map.Entry<String, List<Issue>> currentEntry : issueByEstimationMap.entrySet()) {

            List<Issue> currentEstimationIssues = currentEntry.getValue();

            KanbanMetrics currentEstimationMetrics = calculateKanbanMetrics(currentEstimationIssues, endDate);

            metricsMap.put(currentEntry.getKey(), currentEstimationMetrics);

        }

        return metricsMap;

    }

}
