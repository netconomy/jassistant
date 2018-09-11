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
package net.netconomy.jiraassistant.estimationstatistics.services;

import java.util.ArrayList;
import java.util.List;

import net.netconomy.jiraassistant.base.restclient.SearchWithExpandsWorkaround;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.StatisticsService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatisticsData;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueStatisticsData;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueTypeEstimationStatisticsData;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueTypeSingleEstimationStatisticsData;

@Service
public class IssueTypeEstimationStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueTypeEstimationStatisticsService.class);

    @Autowired
    private JiraSearch jiraSearch;

    @Autowired
    private ConfigurationService configuration;

    @Autowired
    private BasicIssueService basicIssueService;

    @Autowired
    private AdvancedIssueService advancedIssueService;

    @Autowired
    private HistoryIssueService historyIssueService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private EstimationStatisticsFilterService estimationStatisticsFilterService;

    @Autowired
    SearchWithExpandsWorkaround searchWithExpandsWorkaround;

    private List<Issue> getIssuesOfIssueTypeWithChangelog(ClientCredentials credentials, List<String> projects,
            String issueType, DateTime startDate, DateTime endDate, String andClause) throws ConfigurationException {

        List<String> issueKeyList;
        List<Issue> issueList = new ArrayList<>();
        String jqlQuery;

        jqlQuery = estimationStatisticsFilterService.filterForFinishedIssuesByType(projects, issueType, startDate,
                endDate, andClause);

        issueKeyList = jiraSearch.searchJiraGetAllKeys(credentials, jqlQuery);

        issueList.addAll(basicIssueService.getMultipleIssues(credentials, issueKeyList, true).values());

        return issueList;

    }

    private String getEstimationFieldName(Boolean altEstimations) throws ConfigurationException {

        if(altEstimations) {
            return configuration.getProjectConfiguration().getAltEstimationFieldName();
        } else {
            return configuration.getProjectConfiguration().getEstimationFieldName();
        }

    }

    Duration getTimeFromEstimationToInProgress(Issue issue, Boolean altEstimations) throws ConfigurationException {
        
        Duration timeFromEstimationToInProgress;
        DateTime estimationDateTime;
        DateTime inProgressDateTime;
        
        estimationDateTime = historyIssueService.getDateTimeOfLastFieldChange(issue, getEstimationFieldName(altEstimations));

        inProgressDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configuration
                .getProjectConfiguration().getInProgressStatus());
        
        if (estimationDateTime == null || inProgressDateTime == null) {
            return null;
        }

        timeFromEstimationToInProgress = new Duration(estimationDateTime, inProgressDateTime);

        return timeFromEstimationToInProgress;

    }

    Duration getTimeFromInProgressToFinished(Issue issue) throws ConfigurationException {

        Duration timeFromInProgressToFinished;
        DateTime inProgressDateTime;
        DateTime finishedDateTime;

        inProgressDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configuration
                .getProjectConfiguration().getInProgressStatus());

        finishedDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configuration
                .getProjectConfiguration().getFinishedStatus());

        if (inProgressDateTime == null || finishedDateTime == null) {
            return null;
        }

        timeFromInProgressToFinished = new Duration(inProgressDateTime, finishedDateTime);

        return timeFromInProgressToFinished;
    }
    
    private void calculateStatisticsData(EstimationStatisticsData genericStatisticsData) {

        List<Integer> minutesSpentAllList = genericStatisticsData.getMinutesSpentAllList();
        List<Integer> minutesEstimatedAllList = genericStatisticsData.getMinutesEstimatedAllList();
        List<Integer> minutesDeltaEstimatedToSpentAllList = genericStatisticsData
                .getMinutesDeltaEstimatedToSpentAllList();
        List<Integer> estimationAgeAllList = genericStatisticsData.getEstimationAgeAllList();
        List<Integer> cycleTimeAllList = genericStatisticsData.getCycleTimeAllList();

        Double meanTimeSpentMinutes = statisticsService.calculateMeanInt(minutesSpentAllList);
        Double medianTimeSpentMinutes = statisticsService.calculateMedianInt(minutesSpentAllList);
        Double stdDeviationTimeSpentMinutes = statisticsService.calculateStandardDeviationInt(minutesSpentAllList);

        Double lowerBorderSpentMinutes = meanTimeSpentMinutes - stdDeviationTimeSpentMinutes;
        Double upperBorderSpentMinutes = meanTimeSpentMinutes + stdDeviationTimeSpentMinutes;

        Integer missingEstimates = minutesSpentAllList.size() - minutesEstimatedAllList.size();

        Double meanTimeEstimatedMinutes = statisticsService.calculateMeanInt(minutesEstimatedAllList);
        Double medianTimeEstimatedMinutes = statisticsService.calculateMedianInt(minutesEstimatedAllList);
        Double stdDeviationTimeEstimatedMinutes = statisticsService
                .calculateStandardDeviationInt(minutesEstimatedAllList);

        Double meanTimeDeltaEstimatedToSpentMinutes = statisticsService
                .calculateMeanInt(minutesDeltaEstimatedToSpentAllList);
        Double medianTimeDeltaEstimatedToSpentMinutes = statisticsService
                .calculateMedianInt(minutesDeltaEstimatedToSpentAllList);
        Double stdDeviationTimeDeltaEstimatedToSpentMinutes = statisticsService
                .calculateStandardDeviationInt(minutesDeltaEstimatedToSpentAllList);

        Double meanEstimationAgeDays = statisticsService.calculateMeanInt(estimationAgeAllList);
        Double medianEstimationAgeDays = statisticsService.calculateMedianInt(estimationAgeAllList);
        Double stdDeviationEstimationAgeDays = statisticsService.calculateStandardDeviationInt(estimationAgeAllList);

        Double meanCycleTimeDays = statisticsService.calculateMeanInt(cycleTimeAllList);
        Double medianCycleTimeDays = statisticsService.calculateMedianInt(cycleTimeAllList);
        Double stdDeviationCycleTimeDays = statisticsService.calculateStandardDeviationInt(cycleTimeAllList);

        genericStatisticsData.setMeanTimeSpent(meanTimeSpentMinutes);
        genericStatisticsData.setMedianTimeSpent(medianTimeSpentMinutes);
        genericStatisticsData.setStdDeviationTimeSpent(stdDeviationTimeSpentMinutes);
        genericStatisticsData.setLowerBorderSpent(lowerBorderSpentMinutes);
        genericStatisticsData.setUpperBorderSpent(upperBorderSpentMinutes);

        genericStatisticsData.setMissingOriginalEstimates(missingEstimates);

        genericStatisticsData.setMeanTimeEstimated(meanTimeEstimatedMinutes);
        genericStatisticsData.setMedianTimeEstimated(medianTimeEstimatedMinutes);
        genericStatisticsData.setStdDeviationTimeEstimated(stdDeviationTimeEstimatedMinutes);

        genericStatisticsData.setMeanTimeDeltaEstimatedToSpent(meanTimeDeltaEstimatedToSpentMinutes);
        genericStatisticsData.setMedianTimeDeltaEstimatedToSpent(medianTimeDeltaEstimatedToSpentMinutes);
        genericStatisticsData
                .setStdDeviationTimeDeltaEstimatedToSpent(stdDeviationTimeDeltaEstimatedToSpentMinutes);

        genericStatisticsData.setMeanEstimationAgeDays(meanEstimationAgeDays);
        genericStatisticsData.setMedianEstimationAgeDays(medianEstimationAgeDays);
        genericStatisticsData.setStdDeviationEstimationAgeDays(stdDeviationEstimationAgeDays);

        genericStatisticsData.setMeanCycleTimeDays(meanCycleTimeDays);
        genericStatisticsData.setMedianCycleTimeDays(medianCycleTimeDays);
        genericStatisticsData.setStdDeviationCycleTimeDays(stdDeviationCycleTimeDays);

    }

    void calculateSingleStatistics(IssueTypeSingleEstimationStatisticsData issueTypeSingleEstimationStatistics) {

        Double minutesSpentOnCurrentIssue;

        calculateStatisticsData(issueTypeSingleEstimationStatistics);

        for (IssueStatisticsData currentIssueData : issueTypeSingleEstimationStatistics.getAllIssues()) {

            minutesSpentOnCurrentIssue = currentIssueData.getMinutesSpent().doubleValue();

            if ((minutesSpentOnCurrentIssue < issueTypeSingleEstimationStatistics.getLowerBorderSpent().getMinutes())
                    || (minutesSpentOnCurrentIssue > issueTypeSingleEstimationStatistics.getUpperBorderSpent().getMinutes())) {

                issueTypeSingleEstimationStatistics.addDeviantIssue(currentIssueData);

            }

        }

    }

    void calculateIssueTypeStatistics(IssueTypeEstimationStatisticsData issueTypeEstimationStatistics) {

        calculateStatisticsData(issueTypeEstimationStatistics);

    }

    Boolean singleEstimationStatisticsExists(IssueTypeEstimationStatisticsData issueTypeEstimationStatistics,
            String estimation) {

        for (IssueTypeSingleEstimationStatisticsData current : issueTypeEstimationStatistics
                .getSingleEstimationStatisticsMap().values()) {
            if (current.getEstimation().equals(estimation)) {
                return true;
            }
        }

        return false;
    }

    private Issue getIssueWithWorkaround(String issueKey, ClientCredentials credentials) {

        String jqlQuery = "key = " + issueKey;
        String additionalFields = "timespent,aggregatetimespent";

        List<Issue> issueList = searchWithExpandsWorkaround.getSearchResultsWithChangelogWorkaround(credentials, jqlQuery, additionalFields, null);

        if(issueList != null && !issueList.isEmpty()) {
            return issueList.get(0);
        } else {
            return null;
        }

    }

    private String getEstimation(Issue issue, Boolean altEstimations) throws ConfigurationException {

        if(altEstimations) {
            return advancedIssueService.getAltEstimation(issue, getEstimationFieldName(altEstimations));
        } else {
            return advancedIssueService.getEstimation(issue, getEstimationFieldName(altEstimations)).toString();
        }

    }

    void processIssue(IssueTypeEstimationStatisticsData issueTypeEstimationStatistics, Issue issue, ClientCredentials credentials, Boolean altEstimations)
            throws ConfigurationException {
        
        String estimation;
        String issueKey;
        Integer minutesSpent = 0;
        Integer minutesOriginallyEstimated = 0;
        Duration timeFromEstimationToInProgressInDays;
        Duration timeFromInProgressToFinishedInDays;
        String singleEstimationStatisticsKey;
        IssueTypeSingleEstimationStatisticsData newIssueTypeSingleEstimationStatistics;
        IssueStatisticsData newStatisticsIssueData;
        Issue issueFromWorkaround;

        estimation = getEstimation(issue, altEstimations);
        
        issueKey = issue.getKey();
        
        if (issue.getTimeTracking().getOriginalEstimateMinutes() != null) {
            minutesOriginallyEstimated = issue.getTimeTracking().getOriginalEstimateMinutes();
        }

        issueFromWorkaround = getIssueWithWorkaround(issueKey, credentials);

        minutesSpent = advancedIssueService.getMinutesSpentForWorkaround(issueFromWorkaround, true);
        
        timeFromEstimationToInProgressInDays = getTimeFromEstimationToInProgress(issue, altEstimations);
        timeFromInProgressToFinishedInDays = getTimeFromInProgressToFinished(issue);
        
        if (timeFromEstimationToInProgressInDays != null && timeFromInProgressToFinishedInDays != null) {
            newStatisticsIssueData = new IssueStatisticsData(issueKey, minutesSpent, minutesOriginallyEstimated, Long
                    .valueOf(timeFromEstimationToInProgressInDays.getStandardDays()).intValue(), Long.valueOf(
                    timeFromInProgressToFinishedInDays.getStandardDays()).intValue());
        } else {
            LOGGER.debug("The Issue '{}' was ignored for Estimation Statistics because "
                    + "timeFromEstimationToInProgressInDays or timeFromInProgressToFinishedInDays was null.", issue.getKey());
            return;
        }

        if (singleEstimationStatisticsExists(issueTypeEstimationStatistics, estimation)) {

            singleEstimationStatisticsKey = IssueTypeEstimationStatisticsData.estimationKeyFromEstimation(estimation, altEstimations);

            issueTypeEstimationStatistics.getSingleEstimationStatisticsMap().get(singleEstimationStatisticsKey)
                    .addIssue(newStatisticsIssueData);

        } else {

            newIssueTypeSingleEstimationStatistics = new IssueTypeSingleEstimationStatisticsData(
                    issueTypeEstimationStatistics.getIssueType(), estimation);

            newIssueTypeSingleEstimationStatistics.addIssue(newStatisticsIssueData);

            issueTypeEstimationStatistics.addSingleEstimationStatistics(estimation,
                    newIssueTypeSingleEstimationStatistics, altEstimations);
        }

    }

    /**
     * Calculate the Estimation Statistics for the given IssueType, in the given Project for Issues changed during the
     * given Time Window.
     * 
     * @param credentials
     * @param projects
     * @param issueType
     * @param startDate
     * @param endDate
     * @param andClause
     * @return
     * @throws ConfigurationException
     */
    public IssueTypeEstimationStatisticsData calculateIssueTypeEstimationStatistics(ClientCredentials credentials,
            List<String> projects, String issueType, DateTime startDate, DateTime endDate, Boolean altEstimations, String andClause)
            throws ConfigurationException {

        IssueTypeEstimationStatisticsData issueTypeEstimationStatistics = new IssueTypeEstimationStatisticsData(issueType);
        List<Issue> issueList;

        issueList = getIssuesOfIssueTypeWithChangelog(credentials, projects, issueType, startDate, endDate, andClause);

        if (issueList.isEmpty()) {
            return issueTypeEstimationStatistics;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Processing {} Issues of Type {} for Estimation Statistics.", issueList.size(), issueType);
        }

        for (Issue currentIssue : issueList) {
            if (historyIssueService.issueWasUpdatedDuring(currentIssue, startDate, endDate)) {
                processIssue(issueTypeEstimationStatistics, currentIssue, credentials, altEstimations);
            } else {
                LOGGER.warn("The Issue {} has a Resolution Date between {} and {} but was not changed during this Time.",
                    currentIssue.getKey(), startDate.toString(), endDate.toString());
            }
        }

        for (IssueTypeSingleEstimationStatisticsData currentIssueTypeSingleEstimationStatistics : issueTypeEstimationStatistics
                .getSingleEstimationStatisticsMap().values()) {
            calculateSingleStatistics(currentIssueTypeSingleEstimationStatistics);
        }

        calculateIssueTypeStatistics(issueTypeEstimationStatistics);

        return issueTypeEstimationStatistics;
    }

}
