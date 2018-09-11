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

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.ChangedIssuesFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.sprintanalysis.data.AdditionalDefectBugStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.defectreasons.DefectReasonData;
import net.netconomy.jiraassistant.sprintanalysis.data.defectreasons.DefectReasonSingleData;

@Service
public class AdditionalDefectBugStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalDefectBugStatisticsService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    AdvancedIssueService advancedIssueService;

    @Autowired
    ChangedIssuesFilterService changedIssuesFilterService;

    DefectReasonSingleData generateSingleDefectReasonData(Issue defect, DateTime startDate, DateTime endDate,
            String defectReasonFieldName, String defectReasonInfoFieldName, ClientCredentials credentials) {

        DefectReasonSingleData singleDefectReasonData = new DefectReasonSingleData();

        singleDefectReasonData.setIssueKey(defect.getKey());
        singleDefectReasonData.setMinutesSpent(advancedIssueService.getMinutesSpentWorkaround(credentials, defect,
                startDate, endDate, false));
        singleDefectReasonData.setDefectReasons(advancedIssueService.getMultipleChoiceFieldValues(defect,
                defectReasonFieldName));
        singleDefectReasonData.setDefectReasonInfos(advancedIssueService.getMultiValueFreeTagFieldValues(defect,
                defectReasonInfoFieldName));

        for (String currentReason : singleDefectReasonData.getDefectReasons()) {
            singleDefectReasonData.addMinutesPerReasonAliquot(currentReason, singleDefectReasonData.getMinutesSpent()
                    .doubleValue() / singleDefectReasonData.getDefectReasons().size());
        }

        return singleDefectReasonData;

    }

    private void processSingleDefectReasonData(AdditionalDefectBugStatistics additionalDefectBugStatistics,
            DefectReasonSingleData singleDefectReasonData) {

        DefectReasonData newDefectReasonData;
        DefectReasonData currentDefectReasonData;

        for (String currentReason : singleDefectReasonData.getDefectReasons()) {

            // When the Reason is already existing in the Statistics
            if (additionalDefectBugStatistics.getDefectReasonDataMap().containsKey(currentReason)) {

                currentDefectReasonData = additionalDefectBugStatistics.getDefectReasonDataMap().get(currentReason);

                currentDefectReasonData.setReasonCount(currentDefectReasonData.getReasonCount() + 1);
                currentDefectReasonData.setReasonMinutesSpentFull(currentDefectReasonData
                        .getReasonMinutesSpentFull() + singleDefectReasonData.getMinutesSpent());
                currentDefectReasonData.setReasonMinutesSpentAliquot(currentDefectReasonData
                        .getReasonMinutesSpentAliquot()
                        + singleDefectReasonData.getMinutesPerReasonAliquot().get(currentReason));
                currentDefectReasonData.addDefectReasonInfoTags(singleDefectReasonData.getDefectReasonInfos());

            } else { // When the Reason is not yet existing in the Statistics

                newDefectReasonData = new DefectReasonData();

                newDefectReasonData.setReasonName(currentReason);
                newDefectReasonData.setReasonCount(1);
                newDefectReasonData.setReasonMinutesSpentFull(singleDefectReasonData.getMinutesSpent());
                newDefectReasonData.setReasonMinutesSpentAliquot(singleDefectReasonData
                        .getMinutesPerReasonAliquot().get(currentReason));
                newDefectReasonData.addDefectReasonInfoTags(singleDefectReasonData.getDefectReasonInfos());

                additionalDefectBugStatistics.getDefectReasonDataMap().put(currentReason, newDefectReasonData);

            }

        }
    }

    private List<String> getKeysFromIssues(List<Issue> issueList) {

        List<String> keyList = new ArrayList<>();

        for (Issue issue : issueList) {

            keyList.add(issue.getKey());

        }

        return keyList;

    }

    private Integer getSpentMinutesFromIssues(List<Issue> issueList) {

        Integer sumOfTimeSpent = 0;

        for (Issue issue : issueList) {

            sumOfTimeSpent += advancedIssueService.getMinutesSpentForWorkaround(issue, false);

        }

        return sumOfTimeSpent;

    }

    private void getFinishedIssuesData(AdditionalDefectBugStatistics additionalDefectBugStatistics, DateTime startDate,
            DateTime endDate, List<String> projects, ClientCredentials credentials) throws ConfigurationException {

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();
        Integer minutesSpentOnDefects;
        Integer minutesSpentOnBugs;
        String additionalFields = "timespent,aggregatetimespent";

        List<Issue> finishedDefects = changedIssuesFilterService.getIssuesChangedDuringTimeToStatesWorkaround(
                configuration.getDefectIssueTypes(), projects, null, startDate, endDate,
                configuration.getFinishedStatus(), credentials, additionalFields);
        List<Issue> finishedBugs = changedIssuesFilterService.getIssuesChangedDuringTimeToStatesWorkaround(
                configuration.getBugIssueTypes(), projects, null, startDate, endDate,
                configuration.getFinishedStatus(), credentials, additionalFields);

        additionalDefectBugStatistics.setFinishedDefectsList(getKeysFromIssues(finishedDefects));
        additionalDefectBugStatistics.setFinishedDefects(finishedDefects.size());

        additionalDefectBugStatistics.setFinishedBugsList(getKeysFromIssues(finishedBugs));
        additionalDefectBugStatistics.setFinishedBugs(finishedBugs.size());

        minutesSpentOnDefects = getSpentMinutesFromIssues(finishedDefects);
        minutesSpentOnBugs = getSpentMinutesFromIssues(finishedBugs);

        additionalDefectBugStatistics.setMinutesSpentOnFinishedDefects(minutesSpentOnDefects);
        additionalDefectBugStatistics.setMinutesSpentOnFinishedBugs(minutesSpentOnBugs);
        additionalDefectBugStatistics.setMinutesSpentOnFinishedDefectsAndBugs(minutesSpentOnDefects
                + minutesSpentOnBugs);
        
        if (!finishedDefects.isEmpty()) {

            additionalDefectBugStatistics.setMinutesSpentOnFinishedDefectsPerDefect(Double
                    .valueOf(minutesSpentOnDefects)
                / Double.valueOf(finishedDefects.size()));

        }

        if (!finishedBugs.isEmpty()) {

            additionalDefectBugStatistics.setMinutesSpentOnFinishedBugsPerBug(Double.valueOf(minutesSpentOnBugs)
                / Double.valueOf(finishedBugs.size()));
        }

        if (!finishedDefects.isEmpty() || !finishedBugs.isEmpty()) {

            additionalDefectBugStatistics.setMinutesSpentOnFinishedDefectsAndBugsPerDefectAndBug(Double
                .valueOf(minutesSpentOnDefects + minutesSpentOnBugs)
                / Double.valueOf(finishedDefects.size() + finishedBugs.size()));

        }

    }

    public AdditionalDefectBugStatistics generateAdditionalDefectBugStatistics(List<Issue> defectBugList,
            DateTime startDate, DateTime endDate, List<String> projects, ClientCredentials credentials)
            throws ConfigurationException {

        AdditionalDefectBugStatistics additionalDefectBugStatistics = new AdditionalDefectBugStatistics();
        ProjectConfiguration configuration = configurationService.getProjectConfiguration();

        DefectReasonSingleData singleDefectReasonData;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getting Defect Reasons for {} Defects/Bugs.", defectBugList.size());
        }

        for (Issue currentIssue : defectBugList) {

            singleDefectReasonData = generateSingleDefectReasonData(currentIssue, startDate, endDate,
                    configuration.getDefectReasonFieldName(), configuration.getDefectReasonInfoFieldName(), credentials);

            processSingleDefectReasonData(additionalDefectBugStatistics, singleDefectReasonData);

            additionalDefectBugStatistics.addDefectReasonInfoTags(singleDefectReasonData.getDefectReasonInfos());

        }

        additionalDefectBugStatistics.setCreatedDefectsList(changedIssuesFilterService.getKeysOfCreatedIssues(
                configuration.getDefectIssueTypes(), projects, startDate, endDate, credentials));
        additionalDefectBugStatistics.setCreatedDefects(additionalDefectBugStatistics.getCreatedDefectsList().size());

        additionalDefectBugStatistics.setCreatedBugsList(changedIssuesFilterService.getKeysOfCreatedIssues(
                configuration.getBugIssueTypes(), projects, startDate, endDate, credentials));
        additionalDefectBugStatistics.setCreatedBugs(additionalDefectBugStatistics.getCreatedBugsList().size());

        additionalDefectBugStatistics.setCreatedDefectsBugs(additionalDefectBugStatistics.getCreatedDefects()
                + additionalDefectBugStatistics.getCreatedBugs());

        getFinishedIssuesData(additionalDefectBugStatistics, startDate, endDate, projects, credentials);

        additionalDefectBugStatistics.setClosedDefectsList(changedIssuesFilterService
                .getKeysOfIssuesChangedDuringTimeToStatesWorkaround(configuration.getDefectIssueTypes(), projects,
                        startDate, endDate, configuration.getClosedStatus(), credentials));
        additionalDefectBugStatistics.setClosedDefects(additionalDefectBugStatistics.getClosedDefectsList().size());

        additionalDefectBugStatistics.setClosedBugsList(changedIssuesFilterService
                .getKeysOfIssuesChangedDuringTimeToStatesWorkaround(configuration.getBugIssueTypes(), projects,
                        startDate, endDate, configuration.getClosedStatus(), credentials));
        additionalDefectBugStatistics.setClosedBugs(additionalDefectBugStatistics.getClosedBugsList().size());

        return additionalDefectBugStatistics;

    }

}
