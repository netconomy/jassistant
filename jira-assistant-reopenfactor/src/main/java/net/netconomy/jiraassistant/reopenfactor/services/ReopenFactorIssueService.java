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
package net.netconomy.jiraassistant.reopenfactor.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import net.netconomy.jiraassistant.base.data.BasicStatisticsData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.restclient.SearchWithExpandsWorkaround;
import net.netconomy.jiraassistant.base.services.StatisticsService;
import net.netconomy.jiraassistant.base.services.filters.ChangedIssuesFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.reopenfactor.data.ReopenFactorData;
import net.netconomy.jiraassistant.reopenfactor.data.ReopenedIssue;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReopenFactorIssueService {

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    ChangedIssuesFilterService changedIssuesFilterService;

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    SearchWithExpandsWorkaround searchWorkaround;

    List<Issue> getProjectIssues(ClientCredentials credentials, List<String> projects, DateTime startDate,
                                         DateTime endDate, String andClause) {

        List<Issue> issueList = new ArrayList<>();

        String jqlQuery = changedIssuesFilterService.generateChangedIssuesFilter(projects, null, andClause,
                startDate, endDate);

        issueList.addAll(searchWorkaround.getSearchResultsWithChangelogWorkaround(credentials, jqlQuery, null, null));

        return issueList;
    }

    Integer countStatusOccuranceForIssueDuringTime(List<String> statusList, Issue issue, DateTime startDate,
                                                           DateTime endDate) {

        Integer statusCount = 0;

        for (String currentStatusName : statusList) {
            statusCount += historyIssueService.getStateCountForIssue(issue, currentStatusName, startDate, endDate);
        }

        return statusCount;
    }


    /** generate an Reopend Issue Object based on the given Jira Issue
     *
     * @param clientCredentials
     * @param issue
     * @param startDate
     * @param endDate
     * @param configuration
     * @return
     */
    ReopenedIssue generateReopenedIssue(ClientCredentials clientCredentials, Issue issue, DateTime startDate,
                                        DateTime endDate, ProjectConfiguration configuration) {

        ReopenedIssue reopenedIssue = new ReopenedIssue();
        Integer reopenCount;
        Issue completeIssue;
        DateTime firstReopenAfterTestable;
        Integer minutesAfterFirstReopen;
        Integer minutesSpentTotal;
        Double percentageTimeSpentAfterReopen;

        reopenedIssue.setKey(issue.getKey());

        completeIssue = basicIssueService.getIssue(clientCredentials, issue.getKey(), true);

        firstReopenAfterTestable = firstReopenAfterTestable(completeIssue, configuration);

        if(startDate.isAfter(firstReopenAfterTestable)) {
            return reopenedIssue;
        }

        reopenCount = countStatusOccuranceForIssueDuringTime(configuration.getReopenedStatus(),
                issue, firstReopenAfterTestable, endDate);

        reopenedIssue.setNumberOfReopens(reopenCount);

        minutesSpentTotal = advancedIssueService.getMinutesSpent(completeIssue);

        reopenedIssue.setBookedTimeTotal(Double.valueOf(minutesSpentTotal));

        if(minutesSpentTotal != 0 && firstReopenAfterTestable != null) {

            reopenedIssue.setFirstReopenAfterTestable(firstReopenAfterTestable.toString());

            minutesAfterFirstReopen = advancedIssueService.getMinutesSpentWorkaround(clientCredentials, completeIssue,
                    firstReopenAfterTestable, DateTime.now(), true);

            if(minutesAfterFirstReopen != 0) {

                percentageTimeSpentAfterReopen = (Double.valueOf(minutesAfterFirstReopen) / Double.valueOf(minutesSpentTotal))*100.0;

                reopenedIssue.setBookedTimeAfterFirstReopen(Double.valueOf(minutesAfterFirstReopen));
                reopenedIssue.setPercentageOfTimeAfterFirstReopen(percentageTimeSpentAfterReopen);

            }

        }

        return reopenedIssue;

    }

    /**
     * Processes a List of Jira Issues into the given ReopenFactorData based on the given Parameters
     *
     * @param clientCredentials
     * @param reopenFactorData
     * @param issueList
     * @param startDate
     * @param endDate
     * @param threshold
     * @param configuration
     */
    void processIssuesIntoReopenFactorData(ClientCredentials clientCredentials, ReopenFactorData reopenFactorData,
                                           List<Issue> issueList, DateTime startDate, DateTime endDate, Integer threshold,
                                           ProjectConfiguration configuration) {

        String statusAtStart;
        ReopenedIssue currentReopenedIssue;
        Integer inTestingCount = 0;
        Integer reopenCount = 0;
        Integer testedCount = 0;
        Integer numberOfReopenedIssues = 0;
        Integer currentInTestingCount;
        Integer currentTestedCount;
        Integer inTestingAtStartCount = 0;
        Integer reopenedAtStartCount = 0;
        Integer testedAtStartCount = 0;
        List<Double> minutesAfterFirstReopenList = new ArrayList<>();
        List<Double> percentageOfTimeAfterFirstReopenList = new ArrayList<>();
        BasicStatisticsData minutesAfterFirstReopenStats;
        BasicStatisticsData percentageOfTimeAfterFirstReopenStats;

        for (Issue currentIssue : issueList) {

            // subIssues are not counted for this Metric
            if (currentIssue.getIssueType().isSubtask()) {
                continue;
            }

            currentReopenedIssue = generateReopenedIssue(clientCredentials, currentIssue, startDate, endDate, configuration);

            if(currentReopenedIssue.getNumberOfReopens() != 0) {

                if(currentReopenedIssue.getBookedTimeAfterFirstReopen() != null) {

                    minutesAfterFirstReopenList.add(currentReopenedIssue.getBookedTimeAfterFirstReopen().getMinutes());
                    percentageOfTimeAfterFirstReopenList.add(currentReopenedIssue.getPercentageOfTimeAfterFirstReopen());

                }

                numberOfReopenedIssues++;

            }

            currentInTestingCount = countStatusOccuranceForIssueDuringTime(reopenFactorData.getInTestingStatus(),
                    currentIssue, startDate, endDate);

            currentTestedCount = countStatusOccuranceForIssueDuringTime(reopenFactorData.getTestedStatus(),
                    currentIssue, startDate, endDate);

            if (threshold != null && currentReopenedIssue.getNumberOfReopens() >= threshold) {
                reopenFactorData.addIssuePastThreshold(currentReopenedIssue);
            }

            statusAtStart = historyIssueService.getStatusAtTime(currentIssue, startDate);

            if (reopenFactorData.getInTestingStatus().contains(statusAtStart)) {
                inTestingAtStartCount++;
            } else if (reopenFactorData.getReopenedStatus().contains(statusAtStart)) {
                reopenedAtStartCount++;
            } else if (reopenFactorData.getTestedStatus().contains(statusAtStart)) {
                testedAtStartCount++;
            }

            inTestingCount += currentInTestingCount;
            reopenCount += currentReopenedIssue.getNumberOfReopens();
            testedCount += currentTestedCount;

        }

        minutesAfterFirstReopenStats = statisticsService.calculateBasicStatistics(minutesAfterFirstReopenList);
        percentageOfTimeAfterFirstReopenStats = statisticsService.calculateBasicStatistics(percentageOfTimeAfterFirstReopenList);

        reopenFactorData.setMeanTimeSpentAfterFirstReopen(minutesAfterFirstReopenStats.getMean());
        reopenFactorData.setMedianTimeSpentAfterFirstReopen(minutesAfterFirstReopenStats.getMedian());
        reopenFactorData.setStdDeviationTimeSpentAfterFirstReopen(minutesAfterFirstReopenStats.getStandardDeviation());

        reopenFactorData.setMeanPercentageOfTimeSpentAfterFirstReopen(percentageOfTimeAfterFirstReopenStats.getMean());
        reopenFactorData.setMedianPercentageOfTimeSpentAfterFirstReopen(percentageOfTimeAfterFirstReopenStats.getMedian());
        reopenFactorData.setStdDeviationPercentageOfTimeSpentAfterFirstReopen(percentageOfTimeAfterFirstReopenStats.getStandardDeviation());

        reopenFactorData.setInTestingIssuesAtStart(inTestingAtStartCount);
        reopenFactorData.setReopenedIssuesAtStart(reopenedAtStartCount);
        reopenFactorData.setTestedIssuesAtStart(testedAtStartCount);

        reopenFactorData.setInTestingCount(inTestingCount);
        reopenFactorData.setReopenCount(reopenCount);
        reopenFactorData.setTestedCount(testedCount);

        reopenFactorData.setNumberOfReopenedIssues(numberOfReopenedIssues);

    }

    /**
     * Find the first Time the given Issue was reopened after it was in a Testable State
     *
     * @param issue
     * @param configuration
     * @return
     */
    DateTime firstReopenAfterTestable(Issue issue, ProjectConfiguration configuration) {

        DateTime firstReopenAfterTestable;
        DateTime firstTimeInTestableState;

        firstTimeInTestableState = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, configuration.getTestableStatus());

        if(firstTimeInTestableState == null) {
            return null;
        }

        firstReopenAfterTestable = historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(issue, configuration.getReopenedStatus(),
                firstTimeInTestableState);

        return firstReopenAfterTestable;

    }

}
