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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataFull;
import net.netconomy.jiraassistant.base.services.SprintService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.data.SprintResultData;

@Service
public class SprintResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintResultService.class);

    @Autowired
    SprintService sprintService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    BasicIssueService basicIssueService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    HistoryIssueService historyIssueService;

    @Autowired
    IssueStatisticsService issueStatisticsService;

    @Autowired
    AdditionalDefectBugStatisticsService additionalDefectBugStatisticsService;

    @Autowired
    FlaggingStatisticsService flaggingStatisticsService;


    private void processIssuesIntoResultData(List<Issue> issueList, SprintResultData resultData, DateTime startDate,
        DateTime endDate, ClientCredentials credentials) throws JSONException, ConfigurationException {

        String currentIssueTypeName = "";
        List<Issue> stories = new ArrayList<>();
        List<Issue> defectsBugs = new ArrayList<>();
        List<Issue> tasks = new ArrayList<>();
        List<Issue> storySubIssues = new ArrayList<>();
        List<Issue> defectSubIssues = new ArrayList<>();
        List<Issue> taskSubIssues = new ArrayList<>();
        Integer minutesSpentOnSubBugs = 0;
        String parentIssueTypeName = "";
        Integer subIssueCount = 0;

        for (Issue currentIssue : issueList) {

            resultData.addProjektKeyIfNotContained(basicIssueService.projectKeyFromIssue(currentIssue));

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
        }

        resultData.setNumberOfSubIssuesEnd(subIssueCount);

        resultData.setMinutesSpentOnSubBugs(minutesSpentOnSubBugs);

        resultData.setStoryStatistics(issueStatisticsService.getIssueStatistics(stories, storySubIssues,
            startDate,
            endDate, resultData.getSprintDataDelta(), credentials));
        resultData.setDefectBugStatistics(issueStatisticsService.getIssueStatistics(defectsBugs, defectSubIssues,
            startDate, endDate, resultData.getSprintDataDelta(), credentials));
        resultData.setTaskStatistics(issueStatisticsService.getIssueStatistics(tasks, taskSubIssues, startDate,
            endDate, resultData.getSprintDataDelta(), credentials));

        resultData.setAdditionalDefectBugStatistics(additionalDefectBugStatisticsService
            .generateAdditionalDefectBugStatistics(defectsBugs, startDate, endDate,
                resultData.getObservedProjects(), credentials));

        // calculate the Number of Issues (no Sub Issues) at the end of the Sprint
        resultData.setNumberOfAllIssuesEnd(issueList.size() - subIssueCount);

    }

    /**
     * Analysis the Sprint Result based on the given Status and Issue Information for the named Sprint.
     *
     * @param wantedFields
     *     the Field Names that should be delivered additional to the standard light Fields (Key, Issue Type,
     *     Project, Status, Summary, Assignee, Resolution and FixVersions).
     * @param lightAnalysisExt
     *     if true no Light Issue Data will be added, if null it will be false
     * @param relevantProjects
     *     a List of Projects to take into account for the Analysis, if empty only the occurring Projects will be
     *     taken into Account
     */
    public SprintResultData calculateSprintResult(SprintDataFull sprintData, ClientCredentials credentials,
        List<String> wantedFields, Boolean lightAnalysisExt, List<String> relevantProjects) throws JSONException,
        ConfigurationException {

        SprintResultData resultData = new SprintResultData();
        DateTime startDate = sprintData.getStartDateAsDateTime();
        DateTime endDate;
        Integer reopenCount = 0;
        Map<String, Issue> removedIssues;
        IssueLight currentLightIssue;
        Boolean lightAnalysis = false;

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();

        LOGGER.info("Calculating SprintResult for {} Issues.", sprintData.getIssueList().size());

        // Write basic Sprint Data
        resultData.setSprintData(new SprintData(sprintData));

        resultData.addAllProjektKeysIfNotContained(relevantProjects);

        // If lightAnalysis is null it will be false
        if (lightAnalysisExt != null) {
            lightAnalysis = lightAnalysisExt;
        } else {
            lightAnalysis = false;
        }

        // If the sprint is not completed yet the current Date will be used for Calculations
        if (sprintData.getCompleteDate() != null) {
            endDate = sprintData.getCompleteDateAsDateTime();
        } else {
            endDate = DateTime.now();
        }

        // Light Issues will not be included in the light Analysis
        if (!lightAnalysis) {
            resultData.setSprintDataLight(sprintService.getSprintDataLight(sprintData, wantedFields));
        }

        try {
            resultData
                .setSprintDataDelta(sprintService.getSprintDataDelta(credentials, sprintData, configuration.getEstimationFieldName()));
        } catch (HttpClientErrorException e) {
            LOGGER.warn("The RapidView with the ID {} was not found, so no Sprint Delta could be calculated. "
                + "The Message from the Server was: {}", sprintData.getRapidViewId(), e.getLocalizedMessage());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Error while calculating Sprint Delta.", e);
            }
            resultData.setSprintDataDelta(new SprintDataDelta());
        }

        resultData.setStoryIssueTypes(configuration.getStoryIssueTypes());
        resultData.addAllDefectBugIssueType(configuration.getDefectIssueTypes());
        resultData.addAllDefectBugIssueType(configuration.getBugIssueTypes());
        resultData.setInProgressStatus(configuration.getInProgressStatus());
        resultData.setImplementedStatus(configuration.getImplementedStatus());
        resultData.setFinishedStatus(configuration.getFinishedStatus());
        resultData.setClosedStatus(configuration.getClosedStatus());

        // Sub Issues will not be counted in the reopen Count
        for (String currentReopenStatus : configuration.getReopenedStatus()) {
            reopenCount += historyIssueService
                .getStateCountForIssues(basicIssueService.getOnlyIssues(sprintData.getIssueList()),
                    currentReopenStatus, startDate, endDate);
        }

        resultData.setReopenCount(reopenCount);

        processIssuesIntoResultData(sprintData.getIssueList(), resultData, startDate, endDate, credentials);

        // add Issues Removed during Sprint to SprintData Light, only when not doing a light Analysis
        if (!lightAnalysis) {
            removedIssues = basicIssueService.getMultipleIssues(credentials, resultData.getSprintDataDelta()
                .getRemovedIssueKeys(), false);

            for (Issue currentIssue : removedIssues.values()) {

                currentLightIssue = advancedIssueService.getIssueLight(currentIssue, wantedFields);

                resultData.getSprintDataLight().addLightIssue(currentLightIssue);

            }
        }

        // add up Story Points in the Sprint at the End of the Sprint
        resultData.setNumberOfAllStoryPointsEnd(resultData.getStoryStatistics().getNumberOfStoryPoints()
            + resultData.getDefectBugStatistics().getNumberOfStoryPoints()
            + resultData.getTaskStatistics().getNumberOfStoryPoints());

        // add up the planned delivered Issues and StoryPoints from the Issue Statistics
        resultData.setPlannedDeliveredIssues(resultData.getStoryStatistics().getPlannedDeliveredIssues()
            + resultData.getDefectBugStatistics().getPlannedDeliveredIssues()
            + resultData.getTaskStatistics().getPlannedDeliveredIssues());
        resultData.setPlannedDeliveredStoryPoints(resultData.getStoryStatistics().getPlannedDeliveredStoryPoints()
            + resultData.getDefectBugStatistics().getPlannedDeliveredStoryPoints()
            + resultData.getTaskStatistics().getPlannedDeliveredStoryPoints());

        // calculate Number of Issues in the Sprint at the Start of the Sprint
        resultData
            .setNumberOfAllIssuesStart(resultData.getNumberOfAllIssuesEnd()
                - resultData.getSprintDataDelta().getAddedIssues()
                + resultData.getSprintDataDelta().getRemovedIssues());

        // calculate Story Points of Issues in the Sprint at the Start of the Sprint
        resultData.setNumberOfAllStoryPointsStart(resultData.getNumberOfAllStoryPointsEnd()
            - resultData.getSprintDataDelta().getAddedStoryPoints()
            + resultData.getSprintDataDelta().getRemovedStoryPoints());

        // calculate Story Points of all finished Issues in the Sprint
        resultData.setNumberOfAllFinishedStoryPoints(resultData.getStoryStatistics().getFinishedStoryPoints()
            + resultData.getDefectBugStatistics().getFinishedStoryPoints()
            + resultData.getTaskStatistics().getFinishedStoryPoints());

        // calculate Story Points of all closed Issues in the Sprint
        resultData.setNumberOfAllClosedStoryPoints(resultData.getStoryStatistics().getClosedStoryPoints()
            + resultData.getDefectBugStatistics().getClosedStoryPoints()
            + resultData.getTaskStatistics().getClosedStoryPoints());

        resultData.setFlaggingStatistics(flaggingStatisticsService.generateFlaggingStatistics(
            sprintData.getIssueList(), sprintData.getStartDateAsDateTime(), endDate));

        return resultData;

    }

    /**
     * Analysis the Sprint Result based on the Status and Issue Information in the Config File for the named Sprint.
     *
     * @param credentials
     *     the Credentials to reach Jira
     * @param sprintIdentifier
     *     the ID or the Name of the Sprint to analyse
     * @param identifiedByID
     *     true ... when the Id of the Sprint is given as Identifier, false when the Sprint Name is given
     * @param wantedFields
     *     the Field Names that should be delivered additional to the standard light Fields (Key, Issue Type,
     *     Project, Status, Summary, Assignee, Resolution and FixVersions).
     * @param lightAnalysis
     *     if true no Light Issue Data will be added, if null it will be false
     * @param relevantProjects
     *     a List of Projects to take into account for the Analysis, if empty only the occurring Projects will be
     *     taken into Account
     */
    public SprintResultData calculateSprintResult(ClientCredentials credentials, String sprintIdentifier,
        Boolean identifiedByID, List<String> wantedFields, Boolean lightAnalysis, List<String> relevantProjects)
        throws JiraAssistantException {

        SprintDataFull sprintData;

        try {

            LOGGER.info("Trying to retrieve Sprint Data for Sprint '{}'.", sprintIdentifier);

            sprintData = sprintService.getFullSprintData(credentials, sprintIdentifier, identifiedByID, true);

            if(sprintData != null) {
                return calculateSprintResult(sprintData, credentials, wantedFields, lightAnalysis, relevantProjects);
            } else {
                throw new JiraAssistantException("The Sprint " + sprintIdentifier + " does not exist or does not contain any Issues.");
            }

        } catch (ConfigurationException e) {
            throw new JiraAssistantException("Error while reading Project Configuration.", e);
        } catch (JSONException e) {
            throw new JiraAssistantException("Error while reading JSON Structure from Issue.", e);
        }

    }

}
