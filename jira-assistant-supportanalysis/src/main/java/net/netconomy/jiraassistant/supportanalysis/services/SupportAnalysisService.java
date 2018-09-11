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
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.netconomy.jiraassistant.base.services.StatisticsService;

import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.BasicStatisticsData;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.restclient.JiraBasicRestService;
import net.netconomy.jiraassistant.base.restclient.SearchWithExpandsWorkaround;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.ChangedIssuesFilterService;
import net.netconomy.jiraassistant.supportanalysis.data.SupportAnalysisData;

@Service
public class SupportAnalysisService {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    ChangedIssuesFilterService changedIssuesFilterService;

    @Autowired
    SearchWithExpandsWorkaround searchWorkaround;

    @Autowired
    JiraBasicRestService jiraBasicRestService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    SupportKPIService supportKPIService;

    @Autowired
    AdditionalSupportDataService additionalSupportDataService;

    @Autowired
    StatisticsService statisticsService;

    DateTime getFirstOfYear(DateTime dayInYear) {

        return new DateTime(dayInYear.getYear(), 1, 1, 0, 0);

    }

    private List<Issue> getProjectIssues(ClientCredentials credentials, List<String> projectsList, DateTime startDate,
        DateTime endDate, List<String> issueTypesList, String andClause, Boolean withWorkaround) {

        List<Issue> issueList = new ArrayList<>();

        String jqlQuery = changedIssuesFilterService.generateChangedIssuesFilter(projectsList, issueTypesList,
            andClause, startDate, endDate);

        if (withWorkaround) {
            issueList.addAll(searchWorkaround
                .getSearchResultsWithChangelogWorkaround(credentials, jqlQuery, null, null));
        } else {
            issueList.addAll(jiraSearch.searchJiraGetAllIssues(credentials, jqlQuery));
        }

        return issueList;
    }

    private Map<String, List<Issue>> getIssueListsByPriority(List<Issue> allIssues) {

        Map<String, List<Issue>> issueListsByPriority = new HashMap<>();
        String currentPriority;

        for (Issue currentIssue : allIssues) {

            currentPriority = currentIssue.getPriority().getName();

            if (!issueListsByPriority.containsKey(currentPriority)) {
                issueListsByPriority.put(currentPriority, new ArrayList<>());
            }

            issueListsByPriority.get(currentPriority).add(currentIssue);

        }

        return issueListsByPriority;

    }

    private Set<String> getAllPriorities(ClientCredentials credentials) {

        Set<String> prioritySet = new TreeSet<>();
        JSONArray allPrioritiesJSON;
        JSONObject currentPriorityJSON;

        allPrioritiesJSON = jiraBasicRestService.getAllPriorities(credentials);

        for (int i = 0; i < allPrioritiesJSON.length(); i++) {

            currentPriorityJSON = allPrioritiesJSON.optJSONObject(i);

            if (currentPriorityJSON != null) {
                prioritySet.add(currentPriorityJSON.optString("name"));
            }

        }

        return prioritySet;

    }

    private static String getAndClauseWithPriorityPrefix(String andClause) {

        String modifiedAndClausePreFix;

        if (andClause != null && !andClause.isEmpty()) {
            modifiedAndClausePreFix = andClause + " and priority = ";
        } else {
            modifiedAndClausePreFix = "priority = ";
        }

        return modifiedAndClausePreFix;

    }

    private Map<String, Integer> getCreatedIssuesByPriority(List<String> issueTypesList, List<String> projectsList,
        String andClause, DateTime startDate, DateTime endDate, ClientCredentials credentials) {

        Map<String, Integer> createdIssuesByPriority = new TreeMap<>();
        Set<String> allPriorities;
        String modifiedAndClause;

        allPriorities = getAllPriorities(credentials);

        for (String currentPriority : allPriorities) {

            modifiedAndClause = getAndClauseWithPriorityPrefix(andClause) + currentPriority;

            createdIssuesByPriority.put(currentPriority, changedIssuesFilterService.getNumberOfCreatedIssues(
                issueTypesList, projectsList, modifiedAndClause, startDate, endDate, credentials));

        }

        return createdIssuesByPriority;

    }

    private Map<String, Integer> getSolvedIssuesByPriority(List<String> issueTypesList, List<String> projectsList,
        String andClause, DateTime startDate, DateTime endDate, List<String> solvedStatusList,
        ClientCredentials credentials) {

        Map<String, Integer> createdIssuesByPriority = new TreeMap<>();
        Set<String> allPriorities;
        String modifiedAndClause;

        allPriorities = getAllPriorities(credentials);

        for (String currentPriority : allPriorities) {

            modifiedAndClause = getAndClauseWithPriorityPrefix(andClause) + currentPriority;

            createdIssuesByPriority.put(currentPriority, changedIssuesFilterService.getNumberOfIssuesChangedDuringTimeToStatesWorkaround(
                issueTypesList, projectsList, modifiedAndClause, startDate, endDate, solvedStatusList, credentials));

        }

        return createdIssuesByPriority;

    }

    private void fillConfigurationInformation(SupportConfiguration config, SupportAnalysisData supportAnalysisData) {
        supportAnalysisData.setOpenStatus(config.getOpenStatus());
        supportAnalysisData.setSolvedStatus(config.getSolvedStatus());
        supportAnalysisData.setReactionTimeEndStatus(config.getReactionTimeEndStatus());
        supportAnalysisData.setInteractionTimeStartStatus(config.getInteractionTimeStartStatus());
        supportAnalysisData.setInteractionTimeEndStatus(config.getInteractionTimeEndStatus());
        supportAnalysisData.setSolutionTimeStartStatus(config.getSolutionTimeStartStatus());
        supportAnalysisData.setSolutionTimeEndStatus(config.getSolutionTimeEndStatus());
    }

    private void fillSpentTimeMetrics(ClientCredentials credentials, DateTime startDate, DateTime endDate,
        SupportAnalysisData supportAnalysisData, List<Issue> completeIssueList) {

        BasicStatisticsData timeSpentStatistics;
        List<Double> timeSpentListMinutes;

        timeSpentListMinutes = supportKPIService.getTimesSpentDuringTimeFrameStatistics(credentials, completeIssueList,
            startDate, endDate);

        timeSpentStatistics = statisticsService.calculateBasicStatistics(timeSpentListMinutes);

        supportAnalysisData.setMeanTimeSpentDuringTimeFrame(timeSpentStatistics.getMean());
        supportAnalysisData.setMedianTimeSpentDuringTimeFrame(timeSpentStatistics.getMedian());
        supportAnalysisData.setStandardDeviationTimeSpentDuringTimeFrame(timeSpentStatistics.getStandardDeviation());
        supportAnalysisData.setTimeSpentDuringTimeFrame90thPercentile(statisticsService.calculatePercentile(timeSpentListMinutes, 90));
        supportAnalysisData.setTimeSpentDuringTimeFrame99thPercentile(statisticsService.calculatePercentile(timeSpentListMinutes, 99));

        timeSpentListMinutes = supportKPIService.getTimesSpentOnIssuesStatistics(completeIssueList);

        timeSpentStatistics = statisticsService.calculateBasicStatistics(timeSpentListMinutes);

        supportAnalysisData.setMeanTimeSpentOnIssues(timeSpentStatistics.getMean());
        supportAnalysisData.setMedianTimeSpentOnIssues(timeSpentStatistics.getMedian());
        supportAnalysisData.setStandardDeviationTimeSpentOnIssues(timeSpentStatistics.getStandardDeviation());
        supportAnalysisData.setTimeSpentOnIssues90thPercentile(statisticsService.calculatePercentile(timeSpentListMinutes, 90));
        supportAnalysisData.setTimeSpentOnIssues99thPercentile(statisticsService.calculatePercentile(timeSpentListMinutes, 99));
    }

    private void fillIssueAgeMetrics(DateTime startDate, DateTime endDate, SupportAnalysisData supportAnalysisData,
        List<Issue> openIssuesStart, List<Issue> openIssuesEnd) {

        BasicStatisticsData ageStatistics;
        List<Double> ageList;

        ageList = supportKPIService.getAgeData(openIssuesStart, startDate);

        ageStatistics = statisticsService.calculateBasicStatistics(ageList);

        supportAnalysisData.setMeanAgeOfOpenIssuesStartDate(ageStatistics.getMean());
        supportAnalysisData.setMedianAgeOfOpenIssuesStartDate(ageStatistics.getMedian());
        supportAnalysisData.setStandardDeviationAgeOfOpenIssuesStartDate(ageStatistics.getStandardDeviation());
        supportAnalysisData.setAgeOfOpenIssuesStartDate90thPercentile(statisticsService.calculatePercentile(ageList, 90));
        supportAnalysisData.setAgeOfOpenIssuesStartDate99thPercentile(statisticsService.calculatePercentile(ageList, 99));

        ageList = supportKPIService.getAgeData(openIssuesEnd, endDate);

        ageStatistics = statisticsService.calculateBasicStatistics(ageList);

        supportAnalysisData.setMeanAgeOfOpenIssuesEndDate(ageStatistics.getMean());
        supportAnalysisData.setMedianAgeOfOpenIssuesEndDate(ageStatistics.getMedian());
        supportAnalysisData.setStandardDeviationAgeOfOpenIssuesEndDate(ageStatistics.getStandardDeviation());
        supportAnalysisData.setAgeOfOpenIssuesEndDate90thPercentile(statisticsService.calculatePercentile(ageList, 90));
        supportAnalysisData.setAgeOfOpenIssuesEndDate99thPercentile(statisticsService.calculatePercentile(ageList, 99));
    }

    SupportAnalysisData fillSupportAnalysisData(ClientCredentials credentials, SupportConfiguration config,
        List<String> projectsList, DateTime startDate, DateTime endDate, List<String> issueTypesList,
        String andClause) {

        SupportAnalysisData supportAnalysisData = new SupportAnalysisData();
        List<Issue> historyIssueList;
        List<Issue> completeIssueList;
        List<Issue> openIssuesStart;
        List<Issue> openIssuesEnd;
        BasicStatisticsData ageStatistics;
        BasicStatisticsData timeSpentStatistics;
        Map<String, List<Issue>> issueListsByPriority;
        Map<String, List<Issue>> historyIssueListsByPriority;

        supportAnalysisData.setAnalysisDate(DateTime.now().toString());

        fillConfigurationInformation(config, supportAnalysisData);

        completeIssueList = getProjectIssues(credentials, projectsList, startDate, endDate, issueTypesList, andClause,
            false);
        historyIssueList = getProjectIssues(credentials, projectsList, startDate, endDate, issueTypesList, andClause,
            true);

        supportAnalysisData.setProjects(projectsList);
        supportAnalysisData.setStartDate(startDate.toString());
        supportAnalysisData.setEndDate(endDate.toString());
        supportAnalysisData.setIssueTypes(issueTypesList);
        supportAnalysisData.setAndClause(andClause);

        supportAnalysisData.setNumberOfAnalysedIssues(completeIssueList.size());

        issueListsByPriority = getIssueListsByPriority(completeIssueList);
        historyIssueListsByPriority = getIssueListsByPriority(historyIssueList);

        supportAnalysisData.setSlaStatisticsData(supportKPIService.calculateSlaStatistics(completeIssueList,
            historyIssueList, startDate, endDate, config));

        for (Map.Entry<String, List<Issue>> currentPriority : issueListsByPriority.entrySet()) {
            supportAnalysisData.addSlaStatisticsDataByPriority(currentPriority.getKey(),
                supportKPIService.calculateSlaStatistics(currentPriority.getValue(),
                    historyIssueListsByPriority.get(currentPriority.getKey()), startDate, endDate, config));
        }

        supportAnalysisData.setCreatedIssuesInTimeFrame(changedIssuesFilterService.getNumberOfCreatedIssues(
            issueTypesList, projectsList, andClause, startDate, endDate, credentials));

        supportAnalysisData.setCreatedIssuesInTimeFrameByPriority(getCreatedIssuesByPriority(issueTypesList,
            projectsList, andClause, startDate, endDate, credentials));

        supportAnalysisData.setCreatedIssuesThisYear(changedIssuesFilterService.getNumberOfCreatedIssues(
            issueTypesList, projectsList, andClause, getFirstOfYear(endDate), endDate, credentials));

        supportAnalysisData.setCreatedIssuesThisYearByPriority(getCreatedIssuesByPriority(issueTypesList,
            projectsList, andClause, getFirstOfYear(endDate), endDate, credentials));

        supportAnalysisData.setSolvedIssuesDuringTimeFrame(changedIssuesFilterService
            .getNumberOfIssuesChangedDuringTimeToStatesWorkaround(issueTypesList, projectsList, andClause,
                startDate, endDate, config.getSolvedStatus(), credentials));

        supportAnalysisData.setSolvedIssuesDuringTimeFrameByPriority(getSolvedIssuesByPriority(issueTypesList,
            projectsList, andClause, startDate, endDate, config.getSolvedStatus(), credentials));

        supportAnalysisData.setSolvedIssuesThisYear(changedIssuesFilterService
            .getNumberOfIssuesChangedDuringTimeToStatesWorkaround(issueTypesList, projectsList, andClause,
                getFirstOfYear(endDate), endDate, config.getSolvedStatus(), credentials));

        supportAnalysisData.setSolvedIssuesThisYearByPriority(getSolvedIssuesByPriority(issueTypesList,
            projectsList, andClause, getFirstOfYear(endDate), endDate, config.getSolvedStatus(), credentials));

        supportAnalysisData.setNumberOfBillableIssuesInTimeFrame(supportKPIService.getNumberOfBillableIssues(
            completeIssueList, config));
        supportAnalysisData.setNumberOfUnbillableIssuesInTimeFrame(supportKPIService.getNumberOfUnbillableIssues(
            completeIssueList, config));

        fillSpentTimeMetrics(credentials, startDate, endDate, supportAnalysisData, completeIssueList);

        supportAnalysisData.setErrorCategoryData(additionalSupportDataService.gatherErrorCategoryData(
            completeIssueList, config, credentials, startDate, endDate));
        supportAnalysisData.setTechnicalSeverityData(additionalSupportDataService.countTechnicalSeverity(
            completeIssueList, config));

        openIssuesStart = supportKPIService.getOpenIssues(credentials, config, projectsList, issueTypesList, startDate,
            andClause);

        openIssuesEnd = supportKPIService.getOpenIssues(credentials, config, projectsList, issueTypesList, endDate,
            andClause);

        supportAnalysisData.setNumberOfOpenIssuesStartDate(openIssuesStart.size());
        supportAnalysisData.setNumberOfOpenIssuesEndDate(openIssuesEnd.size());

        fillIssueAgeMetrics(startDate, endDate, supportAnalysisData, openIssuesStart, openIssuesEnd);

        return supportAnalysisData;

    }

    public SupportAnalysisData calculateSupportAnalysis(ClientCredentials credentials, List<String> projects,
        DateTime startDate, DateTime endDate, List<String> issueTypes, String andClause) {

        try {

            SupportConfiguration config = configurationService.getSupportConfiguration();

            return fillSupportAnalysisData(credentials, config, projects, startDate, endDate, issueTypes,
                andClause);

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Support Configuration.", e);
        }

    }

    public SupportAnalysisData calculateSupportAnalysis(ClientCredentials credentials, List<String> projects,
        DateTime month, List<String> issueTypes, String andClause) {

        DateTime startDate = new DateTime(month.getYear(), month.getMonthOfYear(), 1, 0, 0);
        DateTime endDate = startDate.plusMonths(1);

        return calculateSupportAnalysis(credentials, projects, startDate, endDate, issueTypes, andClause);

    }

}
