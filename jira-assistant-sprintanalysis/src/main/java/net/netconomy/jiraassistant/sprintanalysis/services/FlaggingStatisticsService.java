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
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingSingleData;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatisticsPartData;

@Service
public class FlaggingStatisticsService {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    HistoryIssueService historyIssueService;

    double[] intListToDoubleArray(List<Integer> intList) {

        double[] doubleArray = new double[intList.size()];

        for (int i = 0; i < intList.size(); i++) {
            doubleArray[i] = intList.get(i).doubleValue();
        }

        return doubleArray;

    }

    void processIssue(Issue issue, FlaggingStatisticsPartData flaggingStatisticsPartData, DateTime sprintStart,
            DateTime sprintEnd) throws ConfigurationException {

        String flaggedFieldName = configurationService.getProjectConfiguration().getFlaggedFieldName();
        String flaggedFieldValue = configurationService.getProjectConfiguration().getFlaggedFieldValue();

        Boolean flagged = false;
        String flaggedStart = historyIssueService.getFieldValueAtTime(issue, flaggedFieldName, sprintStart);
        String flaggedEnd = historyIssueService.getFieldValueAtTime(issue, flaggedFieldName, sprintEnd);
        Integer flaggedOverTime = historyIssueService.getValueCountForIssueField(issue, flaggedFieldName,
                flaggedFieldValue, sprintStart, sprintEnd);
        Duration flaggedDuration = historyIssueService.getValueDurationForIssueField(issue, flaggedFieldName,
                flaggedFieldValue, sprintStart, sprintEnd);
        FlaggingSingleData currentFlaggingSingleData;

        if (flaggedFieldValue.equals(flaggedStart)) {
            flaggingStatisticsPartData.setNumberOfFlaggedIssuesStart(flaggingStatisticsPartData
                    .getNumberOfFlaggedIssuesStart() + 1);
            flagged = true;
        }

        if (flaggedFieldValue.equals(flaggedEnd)) {
            flaggingStatisticsPartData.setNumberOfFlaggedIssuesEnd(flaggingStatisticsPartData
                    .getNumberOfFlaggedIssuesEnd() + 1);
            flagged = true;
        }

        if (flaggedOverTime > 0) {
            flaggingStatisticsPartData.setNumberOfFlaggings(flaggingStatisticsPartData.getNumberOfFlaggings()
                    + flaggedOverTime);
            flagged = true;
        }

        if (flagged) {
            currentFlaggingSingleData = new FlaggingSingleData();

            currentFlaggingSingleData.setIssueKey(issue.getKey());
            currentFlaggingSingleData.setNumberOfFlaggings(flaggedOverTime);
            currentFlaggingSingleData.setFlaggingDurationsMinutes(Long.valueOf(flaggedDuration.getStandardMinutes())
                    .intValue());

            flaggingStatisticsPartData.addSingleFlaggingData(currentFlaggingSingleData);
        }

    }

    FlaggingStatisticsPartData generateFlaggingStatisticsPartData(List<Issue> issueList, DateTime sprintStart,
            DateTime sprintEnd) throws ConfigurationException {

        FlaggingStatisticsPartData flaggingStatisticsPartData = new FlaggingStatisticsPartData();
        List<Integer> flaggingDurations = new ArrayList<>();
        Mean mean = new Mean();
        Median median = new Median();
        Integer sumOfDurations = 0;

        for (Issue currentIssue : issueList) {
            processIssue(currentIssue, flaggingStatisticsPartData, sprintStart, sprintEnd);
        }

        for (FlaggingSingleData currentSingleData : flaggingStatisticsPartData.getFlaggingSingleData()) {
            flaggingDurations.add(currentSingleData.getFlaggingDurationsMinutes());

            sumOfDurations += currentSingleData.getFlaggingDurationsMinutes();
        }
        
        flaggingStatisticsPartData.setSumOfFlaggingDurationsMinutes(sumOfDurations);
        
        if (!flaggingDurations.isEmpty()) {
            mean.setData(intListToDoubleArray(flaggingDurations));
            flaggingStatisticsPartData.setMeanOfFlaggingDurationsMinutes(mean.evaluate());

            median.setData(intListToDoubleArray(flaggingDurations));
            flaggingStatisticsPartData.setMedianOfFlaggingDurationsMinutes(median.evaluate());
        }

        return flaggingStatisticsPartData;

    }

    /**
     * Generates a Flagging Statistic based on the given Issues and Sub Issues during the Sprint
     * 
     * @param issueList
     * @param subIssueList
     * @param configuration
     * @param sprintStart
     * @param sprintEnd
     * @return
     * @throws ConfigurationException
     */
    public FlaggingStatistics generateFlaggingStatistics(List<Issue> issueList, List<Issue> subIssueList,
            DateTime sprintStart, DateTime sprintEnd) throws ConfigurationException {

        FlaggingStatistics flaggingStatistics = new FlaggingStatistics();

        flaggingStatistics.setIssueFlaggingStatistics(generateFlaggingStatisticsPartData(issueList,
                sprintStart, sprintEnd));
        flaggingStatistics.setSubIssueFlaggingStatistics(generateFlaggingStatisticsPartData(subIssueList,
 sprintStart,
                sprintEnd));

        return flaggingStatistics;
    }

    /**
     * Generates a Flagging Statistic based on the given Issues and Sub Issues during the Sprint
     * 
     * @param allIssueList
     * @param configuration
     * @param sprintStart
     * @param sprintEnd
     * @return
     * @throws ConfigurationException
     */
    public FlaggingStatistics generateFlaggingStatistics(List<Issue> allIssueList, DateTime sprintStart,
            DateTime sprintEnd) throws ConfigurationException {

        List<Issue> issueList = new ArrayList<>();
        List<Issue> subIssueList = new ArrayList<>();

        for (Issue current : allIssueList) {
            if (current.getIssueType().isSubtask()) {
                subIssueList.add(current);
            } else {
                issueList.add(current);
            }
        }

        return generateFlaggingStatistics(issueList, subIssueList, sprintStart, sprintEnd);
    }

}
