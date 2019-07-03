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
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.DataConversionService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanResultData;
import net.netconomy.jiraassistant.sprintanalysis.services.AdditionalDefectBugStatisticsService;
import net.netconomy.jiraassistant.sprintanalysis.services.FlaggingStatisticsService;
import net.netconomy.jiraassistant.sprintanalysis.services.IssueStatisticsService;

@Service
public class KanbanResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanbanResultService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    BacklogFilterService backlogFilterService;

    @Autowired
    DataConversionService dataConversionService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    IssueStatisticsService issueStatisticsService;

    @Autowired
    AltIssueStatisticsService altIssueStatisticsService;

    @Autowired
    KanbanMetricsService kanbanMetricsService;

    @Autowired
    AdditionalDefectBugStatisticsService additionalDefectBugStatisticsService;

    @Autowired
    FlaggingStatisticsService flaggingStatisticsService;

    private String createChangedJQLString(DateTime startDate, DateTime endDate) {

        String changedJQLString;
        String startDateString = dataConversionService.convertDateTimeToJQLDate(startDate);
        String endDateString = dataConversionService.convertDateTimeToJQLDate(endDate);

        changedJQLString = "(updatedDate >= '" + startDateString + "' and updatedDate <= '" + endDateString
                + "') or status changed during ('" + startDateString + "', '" + endDateString + "')";

        return changedJQLString;

    }

    private void processIssuesIntoResultData(List<Issue> issueList, KanbanResultData resultData, DateTime startDate, DateTime endDate,
            ClientCredentials credentials, Boolean withAltEstimations, IssueFilter issueFilter) throws JSONException, ConfigurationException {

        String currentIssueTypeName = "";
        List<Issue> stories = new ArrayList<>();
        List<Issue> defectsBugs = new ArrayList<>();
        List<Issue> tasks = new ArrayList<>();
        List<Issue> storySubIssues = new ArrayList<>();
        List<Issue> defectSubIssues = new ArrayList<>();
        List<Issue> taskSubIssues = new ArrayList<>();
        List<Issue> allIssues = new ArrayList<>();
        Integer minutesSpentOnSubBugs = 0;
        String parentIssueTypeName = "";
        Integer subIssueCount = 0;
        Double storyPointCount = 0.0;
        Integer minutesSpentOnIssues = 0;
        Integer minutesSpentOnSubIssues = 0;

        for (Issue currentIssue : issueList) {

            resultData.addProjektKeyIfNotContained(basicIssueService.projectKeyFromIssue(currentIssue));

            if(withAltEstimations) {
                resultData.addIssueWithEstimation(advancedIssueService.getAltEstimation(currentIssue, configurationService
                    .getProjectConfiguration().getAltEstimationFieldName()));
            } else {
                storyPointCount += advancedIssueService.getEstimation(currentIssue, configurationService
                    .getProjectConfiguration().getEstimationFieldName());
            }

            // Subtasks are processed differently
            if (currentIssue.getIssueType().isSubtask()) {

                if ("Sub-bug".equals(currentIssue.getIssueType().getName())) {
                    minutesSpentOnSubBugs += advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue,
                            startDate, endDate, false);
                }

                parentIssueTypeName = advancedIssueService.getParentIssueTypeName(currentIssue);

                if (resultData.getStoryIssueTypes().contains(parentIssueTypeName)) {
                    // The Parent is is a Story
                    storySubIssues.add(currentIssue);
                } else if (resultData.getDefectBugIssueTypes().contains(parentIssueTypeName)) {
                    // The Parent is a Defect
                    defectSubIssues.add(currentIssue);
                } else {
                    // The Parent is neither a Story nor a Defect and will be counted as Task
                    taskSubIssues.add(currentIssue);
                }

                minutesSpentOnSubIssues += advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue,
                        startDate, endDate, false);

                subIssueCount++;
                continue;
            }

            currentIssueTypeName = currentIssue.getIssueType().getName();

            if (resultData.getStoryIssueTypes().contains(currentIssueTypeName)) {
                // The Issue is a Story
                stories.add(currentIssue);
            } else if (resultData.getDefectBugIssueTypes().contains(currentIssueTypeName)) {
                // The Issue is a Defect
                defectsBugs.add(currentIssue);
            } else {
                // The Issue is neither a Story nor a Defect and will be counted as Task
                tasks.add(currentIssue);

                // Document which Issue Types where counted as Tasks
                if (!resultData.getTaskIssueTypes().contains(currentIssueTypeName)) {
                    resultData.addTaskIssueType(currentIssueTypeName);
                }
            }

            minutesSpentOnIssues += advancedIssueService.getMinutesSpentWorkaround(credentials, currentIssue,
                    startDate, endDate, false);

            allIssues.add(currentIssue);

        }

        resultData.setNumberOfIssues(issueList.size() - subIssueCount);
        resultData.setNumberOfSubIssues(subIssueCount);

        if(!withAltEstimations) {
            resultData.setNumberOfStoryPoints(storyPointCount);
        }

        resultData.setMinutesSpentOnIssues(minutesSpentOnIssues);
        resultData.setMinutesSpentOnSubIssues(minutesSpentOnSubIssues);

        resultData.setMinutesSpentOnSubBugs(minutesSpentOnSubBugs);

        if(withAltEstimations) {
            resultData.setAltStoryStatistics(altIssueStatisticsService.getAltIssueStatistics(stories, storySubIssues, startDate,
                endDate, credentials));
            resultData.setAltDefectBugStatistics(altIssueStatisticsService.getAltIssueStatistics(defectsBugs, defectSubIssues,
                startDate, endDate, credentials));
            resultData.setAltTaskStatistics(altIssueStatisticsService.getAltIssueStatistics(tasks, defectSubIssues,
                startDate, endDate, credentials));
        } else {
            resultData.setStoryStatistics(issueStatisticsService.getIssueStatistics(stories, storySubIssues, startDate,
                endDate, credentials, issueFilter));
            resultData.setDefectBugStatistics(issueStatisticsService.getIssueStatistics(defectsBugs, defectSubIssues,
                startDate, endDate, credentials, issueFilter));
            resultData.setTaskStatistics(issueStatisticsService.getIssueStatistics(tasks, taskSubIssues, startDate,
                endDate, credentials, issueFilter));
        }

        resultData.setKanbanMetrics(kanbanMetricsService.calculateKanbanMetrics(allIssues, endDate));

        resultData.addKanbanMetricsByEstimation(kanbanMetricsService.getKanbanMetricsByEstimation(allIssues, endDate, withAltEstimations));

        resultData.setAdditionalDefectBugStatistics(additionalDefectBugStatisticsService
                .generateAdditionalDefectBugStatistics(defectsBugs, startDate, endDate,
                        resultData.getObservedProjects(), credentials));

    }

    public KanbanResultData calculateKanbanResult(ClientCredentials credentials, ProjectConfiguration configuration,
            String backlogFilter, DateTime startDate, DateTime endDate, List<String> wantedFields,
            Boolean lightAnalysisExt, List<String> relevantProjects, Boolean withAltEstimations)
            throws JSONException, ConfigurationException {

        IssueFilter issueFilter = new IssueFilter();
        issueFilter.setProjectKeys(relevantProjects);
        KanbanResultData kanbanResultData = new KanbanResultData(withAltEstimations);
        Integer numberOfResults = 0;
        List<String> issueKeys;
        List<Issue> allIssues = new ArrayList<>();
        Boolean lightAnalysis = false;
        Integer reopenCount = 0;
        Integer maxResults;

        LOGGER.info("Trying to retrieve Issue Data through Filter '{}'.", backlogFilter);

        maxResults = configurationService.getPerformanceConfiguration().getMaxKanbanResults();

        numberOfResults = jiraSearch.searchJiraGetNumberOfResults(credentials, backlogFilter);
        
        if (numberOfResults > maxResults) {
            throw new JiraAssistantException("The Number of Issues for the Filter: '" + backlogFilter + "' was "
                    + numberOfResults + " and exceeded the defined Maximum of " + maxResults
                    + ". Please try to narrow down the timeframe or search criteria for this Analysis.");
        }
        
        issueKeys = this.basicIssueService.filterIssueKeys(jiraSearch.searchJiraGetAllKeys(credentials, backlogFilter), issueFilter);
        
        LOGGER.info("Calculating KanbanResults for {} Issues between {} and {}.", issueKeys.size(),
            dataConversionService.convertDateTimeToJQLDate(startDate), dataConversionService.convertDateTimeToJQLDate(endDate));

        allIssues.addAll(basicIssueService.getMultipleIssues(credentials, issueKeys, true).values());

        kanbanResultData.setUsedFilter(backlogFilter);
        kanbanResultData.setStartDate(startDate.toString());
        kanbanResultData.setEndDate(endDate.toString());

        kanbanResultData.addAllProjektKeysIfNotContained(relevantProjects);

        // If lightAnalysis is null it will be false
        if (lightAnalysisExt != null) {
            lightAnalysis = lightAnalysisExt;
        } else {
            lightAnalysis = false;
        }

        if (!lightAnalysis) {
            for (Issue currentIssue : allIssues) {
                kanbanResultData.addLightIssue(advancedIssueService.getIssueLight(currentIssue, wantedFields));
            }
        }

        kanbanResultData.setStoryIssueTypes(configuration.getStoryIssueTypes());
        kanbanResultData.addAllDefectBugIssueType(configuration.getDefectIssueTypes());
        kanbanResultData.addAllDefectBugIssueType(configuration.getBugIssueTypes());
        kanbanResultData.setInProgressStatus(configuration.getInProgressStatus());
        kanbanResultData.setWaitingStatus(configuration.getWaitingStatus());
        kanbanResultData.setImplementedStatus(configuration.getImplementedStatus());
        kanbanResultData.setFinishedStatus(configuration.getFinishedStatus());
        kanbanResultData.setClosedStatus(configuration.getClosedStatus());

        // Sub Issues will not be counted in the reopen Count
        for (String currentReopenStatus : configuration.getReopenedStatus()) {
            reopenCount += historyIssueService.getStateCountForIssues(basicIssueService.getOnlyIssues(allIssues),
                    currentReopenStatus,
                    startDate, endDate);
        }

        kanbanResultData.setReopenCount(reopenCount);

        processIssuesIntoResultData(allIssues, kanbanResultData, startDate, endDate, credentials, withAltEstimations, issueFilter);

        if(withAltEstimations) {
            // calculate all finished Issues
            kanbanResultData.setNumberOfAllFinishedIssues(kanbanResultData.getAltStoryStatistics().getFinishedIssues()
                + kanbanResultData.getAltDefectBugStatistics().getFinishedIssues()
                + kanbanResultData.getAltTaskStatistics().getFinishedIssues());

            // calculate all closed Issues
            kanbanResultData.setNumberOfAllClosedIssues(kanbanResultData.getAltStoryStatistics().getClosedIssues()
                + kanbanResultData.getAltDefectBugStatistics().getClosedIssues()
                + kanbanResultData.getAltTaskStatistics().getClosedIssues());

            kanbanResultData.addFinishedIssuesByEstimation(kanbanResultData.getAltStoryStatistics().getFinished());
            kanbanResultData.addFinishedIssuesByEstimation(kanbanResultData.getAltDefectBugStatistics().getFinished());
            kanbanResultData.addFinishedIssuesByEstimation(kanbanResultData.getAltTaskStatistics().getFinished());

            kanbanResultData.addClosedIssuesByEstimation(kanbanResultData.getAltStoryStatistics().getClosed());
            kanbanResultData.addClosedIssuesByEstimation(kanbanResultData.getAltDefectBugStatistics().getClosed());
            kanbanResultData.addClosedIssuesByEstimation(kanbanResultData.getAltTaskStatistics().getClosed());
        } else {
            // calculate all finished Issues
            kanbanResultData.setNumberOfAllFinishedIssues(kanbanResultData.getStoryStatistics().getFinishedIssues()
                + kanbanResultData.getDefectBugStatistics().getFinishedIssues()
                + kanbanResultData.getTaskStatistics().getFinishedIssues());

            // calculate all closed Issues
            kanbanResultData.setNumberOfAllClosedIssues(kanbanResultData.getStoryStatistics().getClosedIssues()
                + kanbanResultData.getDefectBugStatistics().getClosedIssues()
                + kanbanResultData.getTaskStatistics().getClosedIssues());

            // calculate Story Points of all finished Issues
            kanbanResultData.setNumberOfAllFinishedStoryPoints(kanbanResultData.getStoryStatistics().getFinishedStoryPoints()
                + kanbanResultData.getDefectBugStatistics().getFinishedStoryPoints()
                + kanbanResultData.getTaskStatistics().getFinishedStoryPoints());

            // calculate Story Points of all closed Issues
            kanbanResultData.setNumberOfAllClosedStoryPoints(kanbanResultData.getStoryStatistics().getClosedStoryPoints()
                + kanbanResultData.getDefectBugStatistics().getClosedStoryPoints()
                + kanbanResultData.getTaskStatistics().getClosedStoryPoints());
        }

        kanbanResultData.setFlaggingStatistics(flaggingStatisticsService.generateFlaggingStatistics(allIssues,
            startDate, endDate));

        return kanbanResultData;

    }

    public KanbanResultData calculateKanbanResultBasedOnFilter(ClientCredentials credentials, String givenFilter,
            DateTime startDate, DateTime endDate, List<String> wantedFields, Boolean lightAnalysis,
            List<String> relevantProjects, Boolean withAltEstimations) throws JiraAssistantException {

        try {
            ProjectConfiguration projConf = configurationService.getProjectConfiguration();

            createChangedJQLString(startDate, endDate);

            String changedJQLString = createChangedJQLString(startDate, endDate);

            String backlogFilter = backlogFilterService.generateBacklogFilterBasedOnFilter(givenFilter,
                    changedJQLString);

            return calculateKanbanResult(credentials, projConf, backlogFilter, startDate, endDate, wantedFields,
                    lightAnalysis, relevantProjects, withAltEstimations);

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        } catch (JSONException e) {
            throw new JiraAssistantException("Error while reading JSON Structure from Issue.", e);
        }

    }

    public KanbanResultData calculateKanbanResultBasedOnProjects(ClientCredentials credentials, List<String> projects,
            DateTime startDate, DateTime endDate, List<String> excludedStatus, List<String> excludedTypes,
            String andClause, List<String> wantedFields, Boolean lightAnalysis, Boolean withAltEstimations) throws JiraAssistantException {

        try {
            ProjectConfiguration projConf = configurationService.getProjectConfiguration();

            String changedJQLString = createChangedJQLString(startDate, endDate);

            String backlogFilter = backlogFilterService.generateBacklogFilterBasedOnProjects(projects, andClause,
                    changedJQLString, excludedStatus, excludedTypes, true, false);

            return calculateKanbanResult(credentials, projConf, backlogFilter, startDate, endDate, wantedFields,
                    lightAnalysis, projects, withAltEstimations);

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        } catch (JSONException e) {
            throw new JiraAssistantException("Error while reading JSON Structure from Issue.", e);
        }

    }

}
