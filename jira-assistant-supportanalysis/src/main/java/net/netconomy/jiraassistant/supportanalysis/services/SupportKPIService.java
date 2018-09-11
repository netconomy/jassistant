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
package net.netconomy.jiraassistant.supportanalysis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.data.BasicStatisticsData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.DataConversionService;
import net.netconomy.jiraassistant.base.services.StatisticsService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaCycle;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaData;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaStatisticsData;

@Service
public class SupportKPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportKPIService.class);

    @Autowired
    StatisticsService statisticsService;
    
    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    BacklogFilterService backlogFilterService;

    @Autowired
    DataConversionService dataConversionService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    SLADataService slaDataService;

    Duration calculateKPITime(DateTime start, DateTime end) {

        Duration timeFromStartToEnd;

        if (start == null || end == null) {
            return null;
        }

        timeFromStartToEnd = new Duration(start, end);

        return timeFromStartToEnd;
        
    }
    
    Duration calculateKPITime(Issue issue, List<String> startStatusList, List<String> endStatusList) {

        DateTime start;
        DateTime end;

        start = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, startStatusList);
        end = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, endStatusList);

        return calculateKPITime(start, end);

    }

    private Boolean isInTimeFrame(DateTime date, DateTime start, DateTime end) {

        return date.isAfter(start) && date.isBefore(end);

    }

    /**
     * Enriches the given SlaStatisticsData with SLA Data from the given Issue, returns the elapsed time in Minutes per
     * SLA occurrence as a List. If the Field was empty no Data is added and the returned List will be empty.
     * 
     * @param slaStatisticsData
     * @param issue
     * @param slaFieldName
     * @return
     */
    List<Double> addSlaData(SlaStatisticsData slaStatisticsData, Issue issue, String slaFieldName, DateTime startDate,
            DateTime endDate) {

        List<Double> elapsedTimeList = new ArrayList<>();
        IssueField currentSlaField;
        SlaData slaData;        
        SlaCycle ongoingCycle;

        currentSlaField = issue.getFieldByName(slaFieldName);

        slaData = slaDataService.parseSlaField(currentSlaField);

        if (slaData == null) {
            return elapsedTimeList;
        }

        for (SlaCycle currentCycle : slaData.getCompleteCycles()) {

            if (!isInTimeFrame(currentCycle.getStartTime(), startDate, endDate)) {
                continue;
            }

            elapsedTimeList.add(currentCycle.getElapsedTimeMinutes());
            
            if (currentCycle.getBreached()) {
                slaStatisticsData.raiseNumberOfBrokenSLAs();
            } else {
                slaStatisticsData.raiseNumberOfKeptSLAs();
            }

        }
        
        ongoingCycle = slaData.getOngoingCycle();

        if (ongoingCycle != null && ongoingCycle.getBreached()
                && isInTimeFrame(ongoingCycle.getStartTime(), startDate, endDate)) {
            slaStatisticsData.raiseNumberOfBrokenSLAs();
        }

        return elapsedTimeList;

    }

    Double getFallbackReactionTime(Issue issue, SupportConfiguration config) {

        DateTime createdDateTime;
        DateTime reactionTimeEndDateTime;
        Duration reactionTimeDuration;
        List<String> endStatusList = config.getReactionTimeEndStatus();

        createdDateTime = issue.getCreationDate();

        reactionTimeEndDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue, endStatusList);

        reactionTimeDuration = calculateKPITime(createdDateTime, reactionTimeEndDateTime);

        if (reactionTimeDuration != null) {
            return Double.valueOf(reactionTimeDuration.getStandardMinutes());
        } else {
            return null;
        }

    }

    Double getFallbackInteractionTime(Issue issue, SupportConfiguration config) {

        Duration interactionTimeDuration;
        List<String> interactionTimeStartStatus = config.getInteractionTimeStartStatus();
        List<String> interactionTimeEndStatus = config.getInteractionTimeEndStatus();

        interactionTimeDuration = calculateKPITime(issue, interactionTimeStartStatus, interactionTimeEndStatus);

        if (interactionTimeDuration != null) {
            return Double.valueOf(interactionTimeDuration.getStandardMinutes());
        } else {
            return null;
        }

    }

    Double getFallbackSolutionTime(Issue issue, SupportConfiguration config) {

        DateTime createdDateTime;
        DateTime solutionTimeEndDateTime;
        Duration solutionTimeDuration;
        List<String> solutionTimeEndStatus = config.getSolutionTimeEndStatus();

        createdDateTime = issue.getCreationDate();
        
        solutionTimeEndDateTime = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(issue,
                solutionTimeEndStatus);
        
        solutionTimeDuration = calculateKPITime(createdDateTime, solutionTimeEndDateTime);

        if (solutionTimeDuration != null) {
            return Double.valueOf(solutionTimeDuration.getStandardMinutes());
        } else {
            return null;
        }

    }

    Map<String, Issue> convertIssueListToMap(List<Issue> issueList) {

        Map<String, Issue> issueMap = new HashMap<>();

        for (Issue current : issueList) {
            issueMap.put(current.getKey(), current);
        }

        return issueMap;

    }

    Boolean useFallbackForReactionTime(Issue issue, SupportConfiguration config) {

        IssueField slaField = issue.getFieldByName(config.getReactionSlaFieldName());

        if(slaField != null && slaField.getValue() != null) {
            return false;
        } else {
            return true;
        }

    }

    Boolean useFallbackForInteractionTime(Issue issue, SupportConfiguration config) {

        IssueField slaField = issue.getFieldByName(config.getInteractionSlaFieldName());

        if(slaField != null && slaField.getValue() != null) {
            return false;
        } else {
            return true;
        }

    }

    Boolean useFallbackForSolutionTime(Issue issue, SupportConfiguration config) {

        IssueField slaField = issue.getFieldByName(config.getSolutionSlaFieldName());

        if(slaField != null && slaField.getValue() != null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * This Function retrieves and calculates SLA Data From Jira.
     * 
     * @param issueCompleteList
     *            List of all Issues to analyze, with all Fields
     * @param issueHistoryList
     *            List of all Issues including their History, but not necessarily with all Fields (REST Workaround)
     * @param config
     * @return
     */
    public SlaStatisticsData calculateSlaStatistics(List<Issue> issueCompleteList, List<Issue> issueHistoryList,
            DateTime startDate, DateTime endDate, SupportConfiguration config) {

        SlaStatisticsData slaStatisticsData = new SlaStatisticsData();
        Map<String, Issue> fallbackIssueMap;
        Issue fallbackIssue;
        List<Double> reactionTimesMinutesSingleIssue;
        List<Double> interactionTimesMinutesSingleIssue;
        List<Double> solutionTimesMinutesSingleIssue;
        List<Double> allReactionTimesMinutes = new ArrayList<>();
        List<Double> allInteractionTimesMinutes = new ArrayList<>();
        List<Double> allSolutionTimesMinutes = new ArrayList<>();
        BasicStatisticsData reactionTimeBundle;
        BasicStatisticsData interactionTimeBundle;
        BasicStatisticsData solutionTimeBundle;
        Boolean useReactionTimeFallback = true;
        Boolean useInteractionTimeFallback = true;
        Boolean useSolutionTimeFallback = true;

        if(!issueCompleteList.isEmpty()) {
            useReactionTimeFallback = useFallbackForReactionTime(issueCompleteList.get(0), config);
            useInteractionTimeFallback = useFallbackForInteractionTime(issueCompleteList.get(0), config);
            useSolutionTimeFallback = useFallbackForSolutionTime(issueCompleteList.get(0), config);
        }

        fallbackIssueMap = convertIssueListToMap(issueHistoryList);

        slaStatisticsData.setNumberOfIssues(issueCompleteList.size());

        slaStatisticsData.setFallbackUsedForReactionTime(useReactionTimeFallback);
        slaStatisticsData.setFallbackUsedForInteractionTime(useInteractionTimeFallback);
        slaStatisticsData.setFallbackUsedForSolutionTime(useSolutionTimeFallback);

        for (Issue currentIssue : issueCompleteList) {

            fallbackIssue = fallbackIssueMap.get(currentIssue.getKey());

            reactionTimesMinutesSingleIssue = addSlaData(slaStatisticsData, currentIssue,
                    config.getReactionSlaFieldName(), startDate, endDate);

            if (useReactionTimeFallback && reactionTimesMinutesSingleIssue.isEmpty() && getFallbackReactionTime(fallbackIssue, config) != null) {
                reactionTimesMinutesSingleIssue.add(getFallbackReactionTime(fallbackIssue, config));
            }

            interactionTimesMinutesSingleIssue = addSlaData(slaStatisticsData, currentIssue,
                    config.getInteractionSlaFieldName(), startDate, endDate);

            if (useInteractionTimeFallback && interactionTimesMinutesSingleIssue.isEmpty() && getFallbackInteractionTime(fallbackIssue, config) != null) {
                interactionTimesMinutesSingleIssue.add(getFallbackInteractionTime(fallbackIssue, config));
            }

            solutionTimesMinutesSingleIssue = addSlaData(slaStatisticsData, currentIssue,
                    config.getSolutionSlaFieldName(), startDate, endDate);

            if (useSolutionTimeFallback && solutionTimesMinutesSingleIssue.isEmpty() && getFallbackSolutionTime(fallbackIssue, config) != null) {
                solutionTimesMinutesSingleIssue.add(getFallbackSolutionTime(fallbackIssue, config));
            }

            allReactionTimesMinutes.addAll(reactionTimesMinutesSingleIssue);
            allInteractionTimesMinutes.addAll(interactionTimesMinutesSingleIssue);
            allSolutionTimesMinutes.addAll(solutionTimesMinutesSingleIssue);

        }

        reactionTimeBundle = statisticsService.calculateBasicStatistics(allReactionTimesMinutes);
        interactionTimeBundle = statisticsService.calculateBasicStatistics(allInteractionTimesMinutes);
        solutionTimeBundle = statisticsService.calculateBasicStatistics(allSolutionTimesMinutes);

        slaStatisticsData.setMeanReactionTimeInTimeFrame(reactionTimeBundle.getMean());
        slaStatisticsData.setMedianReactionTimeInTimeFrame(reactionTimeBundle.getMedian());
        slaStatisticsData.setStandardDeviationReactionTimeInTimeFrame(reactionTimeBundle.getStandardDeviation());
        slaStatisticsData.setReactionTimeInTimeFrame90thPercentile(statisticsService.calculatePercentile(allReactionTimesMinutes, 90));
        slaStatisticsData.setReactionTimeInTimeFrame99thPercentile(statisticsService.calculatePercentile(allReactionTimesMinutes, 99));

        slaStatisticsData.setMeanInteractionTimeInTimeFrame(interactionTimeBundle.getMean());
        slaStatisticsData.setMedianInteractionTimeInTimeFrame(interactionTimeBundle.getMedian());
        slaStatisticsData.setStandardDeviationInteractionTimeInTimeFrame(interactionTimeBundle.getStandardDeviation());
        slaStatisticsData.setInteractionTimeInTimeFrame90thPercentile(statisticsService.calculatePercentile(allInteractionTimesMinutes, 90));
        slaStatisticsData.setInteractionTimeInTimeFrame99thPercentile(statisticsService.calculatePercentile(allInteractionTimesMinutes, 99));

        slaStatisticsData.setMeanSolutionTime(solutionTimeBundle.getMean());
        slaStatisticsData.setMedianSolutionTime(solutionTimeBundle.getMedian());
        slaStatisticsData.setStandardDeviationSolutionTime(solutionTimeBundle.getStandardDeviation());
        slaStatisticsData.setSolutionTime90thPercentile(statisticsService.calculatePercentile(allSolutionTimesMinutes, 90));
        slaStatisticsData.setSolutionTime99thPercentile(statisticsService.calculatePercentile(allSolutionTimesMinutes, 99));

        return slaStatisticsData;

    }

    /**
     * This Function returns the Issues that were open at given Date and Time.
     * 
     * @param credentials
     * @param config
     * @param projectsList
     * @param issueTypesList
     * @param date
     * @param andClause
     * @return
     */
    public List<Issue> getOpenIssues(ClientCredentials credentials, SupportConfiguration config,
            List<String> projectsList, List<String> issueTypesList, DateTime date, String andClause) {

        List<String> openStatus;
        String openIssuesFilter;
        StringBuilder andClause2Builder = new StringBuilder();
        Joiner joiner = Joiner.on("','");
        String andClause2;

        openStatus = config.getOpenStatus();

        andClause2Builder.append("status was in ('");

        joiner.appendTo(andClause2Builder, openStatus);

        andClause2Builder.append("') on '");

        andClause2Builder.append(dataConversionService.convertDateTimeToJQLDateTime(date));

        andClause2Builder.append("'");

        andClause2 = andClause2Builder.toString();

        openIssuesFilter = backlogFilterService.generateBacklogFilterBasedOnProjects(projectsList, andClause,
                andClause2, null, issueTypesList, false, false);

        return jiraSearch.searchJiraGetAllIssues(credentials, openIssuesFilter);

    }

    /**
     * This Function returns a List of Data about the Age of the given Issues at the given Date and Time.
     *
     * @param issues
     * @param date
     * @return
     */
    public List<Double> getAgeData(List<Issue> issues, DateTime date) {

        List<Double> ageList = new ArrayList<>();
        Duration currentAge;

        for(Issue current : issues) {

            currentAge = new Duration(current.getCreationDate(), date);
            ageList.add(Double.valueOf(currentAge.getStandardMinutes()));

        }

        return ageList;

    }

    /**
     * @depricated
     *
     * This Function returns Statistical Data about the Age of the given Issues at the given Date and Time.
     * 
     * @param issues
     * @param date
     * @return
     */
    public BasicStatisticsData getAgeStatistics(List<Issue> issues, DateTime date) {
        
        List<Double> ageList = new ArrayList<>();
        Duration currentAge;
        
        for(Issue current : issues) {
            
            currentAge = new Duration(current.getCreationDate(), date);
            ageList.add(Double.valueOf(currentAge.getStandardMinutes()));
            
        }
        
        return statisticsService.calculateBasicStatistics(ageList);
        
    }

    Integer getNumberOfBillableOrUnbillableIssues(List<Issue> issueList, Boolean getBillable,
            SupportConfiguration config) {

        Integer billableCount = 0;
        IssueField billableField;
        Double fieldValueDouble;

        for (Issue current : issueList) {

            billableField = current.getFieldByName(config.getBillableSupportFieldName());

            if (billableField != null && billableField.getValue() != null
                    && !billableField.getValue().toString().isEmpty()) {

                try {

                    fieldValueDouble = Double.parseDouble(billableField.getValue().toString());

                    if ((fieldValueDouble.equals(0.0) && !getBillable) || (!fieldValueDouble.equals(0.0) && getBillable)) {
                        billableCount++;
                    }

                } catch (NumberFormatException e) {
                    LOGGER.warn("The Field {} of the Issue {} was not a valid Double Value. Field Value was: '{}'",
                        config.getBillableSupportFieldName(), current.getKey(), billableField.getValue());
                }

            }

        }

        return billableCount;

    }

    /**
     * Gets the number of Issues whose BillableSupportField (defined by Configuration) is not null, empty or 0.
     * 
     * @param issueList
     * @param config
     * @return
     */
    public Integer getNumberOfBillableIssues(List<Issue> issueList, SupportConfiguration config) {

        return getNumberOfBillableOrUnbillableIssues(issueList, true, config);

    }

    /**
     * Gets the number of Issues whose BillableSupportField (defined by Configuration) is 0.
     * 
     * @param issueList
     * @param config
     * @return
     */
    public Integer getNumberOfUnbillableIssues(List<Issue> issueList, SupportConfiguration config) {

        return getNumberOfBillableOrUnbillableIssues(issueList, false, config);

    }

    public List<Double> getTimesSpentOnIssuesStatistics(List<Issue> issueList) {

        List<Double> timeSpentListMinutes = new ArrayList<>();

        for (Issue current : issueList) {

            timeSpentListMinutes.add(Double.valueOf(advancedIssueService.getMinutesSpent(current)));

        }

        return timeSpentListMinutes;

    }

    /**
     * Takes only Time spent during the given Time Frame into Account
     *
     * @param credentials
     * @param issueList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Double> getTimesSpentDuringTimeFrameStatistics(ClientCredentials credentials, List<Issue> issueList, DateTime startDate, DateTime endDate) {

        List<Double> timeSpentListMinutes = new ArrayList<>();

        for (Issue current : issueList) {

            timeSpentListMinutes.add(Double.valueOf(advancedIssueService.getMinutesSpentWorkaround(credentials,
                    current, startDate, endDate, true)));

        }

        return timeSpentListMinutes;

    }

}
