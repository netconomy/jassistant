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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.sprintanalysis.data.AdditionalDefectBugStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.IssueStatistics;
import net.netconomy.jiraassistant.sprintanalysis.data.MultipleSprintAnalysisData;
import net.netconomy.jiraassistant.sprintanalysis.data.SprintResultData;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatisticsPartData;

@Service
public class MultipleSprintAnalysisToCSVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleSprintAnalysisToCSVService.class);

    private static final char DECIMALSEPARATOR = ',';

    public void addSprintDataToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("Sprint Name");
        firstLine.addStringToLine("Start Date");
        firstLine.addStringToLine("Complete Date");

    }

    public void addSprintDataToLine(CSVLine line, SprintData sprintData) {

        line.addStringToLine(sprintData.getName());
        line.addStringToLine(sprintData.getStartDate());
        line.addStringToLine(sprintData.getCompleteDate());

    }

    public void addBasicResultDataToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("Number of all Issues Start");
        firstLine.addStringToLine("Number of all Issues End");

        firstLine.addStringToLine("Number of all Story Points Start");
        firstLine.addStringToLine("Number of all Story Points End");

        firstLine.addStringToLine("Number of all finished Story Points");
        firstLine.addStringToLine("Number of all closed Story Points");

        firstLine.addStringToLine("planned and delivered Issues");
        firstLine.addStringToLine("planned and delivered Story Points");

        firstLine.addStringToLine("Reopen-Count");
        firstLine.addStringToLine("Minutes spent on Sub Bugs");

    }

    public void addBasicResultDataToLine(CSVLine line, SprintResultData sprintResultData) {

        line.addStringToLine(sprintResultData.getNumberOfAllIssuesStart().toString());
        line.addStringToLine(sprintResultData.getNumberOfAllIssuesEnd().toString());

        line.addStringToLine(sprintResultData.getNumberOfAllStoryPointsStart().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(sprintResultData.getNumberOfAllStoryPointsEnd().toString().replace('.', DECIMALSEPARATOR));

        line.addStringToLine(sprintResultData.getNumberOfAllFinishedStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(sprintResultData.getNumberOfAllClosedStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(sprintResultData.getPlannedDeliveredIssues().toString());
        line.addStringToLine(sprintResultData.getPlannedDeliveredStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(sprintResultData.getReopenCount().toString());
        line.addStringToLine(sprintResultData.getMinutesSpentOnSubBugs().toString());

    }

    public void addIssueStatisticsToFirstLine(CSVLine firstLine, String issueType) {

        firstLine.addStringToLine("Number of " + issueType);
        firstLine.addStringToLine("Number of Story Points " + issueType);
        firstLine.addStringToLine("Reopen-Count " + issueType);
        firstLine.addStringToLine("Minutes spent on " + issueType);
        firstLine.addStringToLine("Number of all implemented " + issueType);
        firstLine.addStringToLine("Number of all implemented Story Points " + issueType);
        firstLine.addStringToLine("Number of all finished " + issueType);
        firstLine.addStringToLine("Number of all finished Story Points " + issueType);
        firstLine.addStringToLine("Number of all closed " + issueType);
        firstLine.addStringToLine("Number of all closed Story Points " + issueType);

    }

    public void addIssueStatisticsToLine(CSVLine line, IssueStatistics issueStatistics) {

        line.addStringToLine(issueStatistics.getNumberOfIssues().toString());
        line.addStringToLine(issueStatistics.getNumberOfStoryPoints().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(issueStatistics.getReopenCount().toString());
        line.addStringToLine(issueStatistics.getMinutesSpent().toString());
        line.addStringToLine(issueStatistics.getImplementedIssues().toString());
        line.addStringToLine(issueStatistics.getImplementedStoryPoints().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(issueStatistics.getFinishedIssues().toString());
        line.addStringToLine(issueStatistics.getFinishedStoryPoints().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(issueStatistics.getClosedIssues().toString());
        line.addStringToLine(issueStatistics.getClosedStoryPoints().toString().replace('.', DECIMALSEPARATOR));

    }

    public void addFlaggingStatisticsPartToFirstLine(CSVLine firstLine, String issueType) {

        firstLine.addStringToLine("Number of " + issueType + " flagged at the Start of the Sprint");
        firstLine.addStringToLine("Number of " + issueType + " flagged at the End of the Sprint");
        firstLine.addStringToLine("Number of " + issueType + " flaggings during the Sprint");

        firstLine.addStringToLine("Sum of Minutes " + issueType + " were flagged during the Sprint");
        firstLine.addStringToLine("Mean of Minutes " + issueType + " were flagged during the Sprint");
        firstLine.addStringToLine("Median of Minutes " + issueType + " were flagged during the Sprint");

    }

    public void addFlaggingStatisticsPartToLine(CSVLine line, FlaggingStatisticsPartData flaggingStatisticsPartData) {

        line.addStringToLine(flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart().toString());
        line.addStringToLine(flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd().toString());
        line.addStringToLine(flaggingStatisticsPartData.getNumberOfFlaggings().toString());

        line.addStringToLine(flaggingStatisticsPartData.getSumOfFlaggingDurationsMinutes().toString());
        line.addStringToLine(flaggingStatisticsPartData.getMeanOfFlaggingDurationsMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(flaggingStatisticsPartData.getMedianOfFlaggingDurationsMinutes().toString()
                .replace('.', DECIMALSEPARATOR));

    }

    public void addAdditionalDefectBugStatisticsToFirstLine(CSVLine firstLine, List<String> defectReasonList) {

        firstLine.addStringToLine("Created Defects during Sprint");
        firstLine.addStringToLine("Created Bugs during Sprint");

        firstLine.addStringToLine("Finished Defects during Sprint");
        firstLine.addStringToLine("Finished Bugs during Sprint");

        firstLine.addStringToLine("Total Minutes Spent on Finished Defects and Bugs");
        firstLine.addStringToLine("Total Minutes Spent on Finished Defects");
        firstLine.addStringToLine("Total Minutes Spent on Finished Bugs");

        firstLine.addStringToLine("Total Minutes Spent on Finished Defects and Bugs per Issues");
        firstLine.addStringToLine("Total Minutes Spent on Finished Defects per Issues");
        firstLine.addStringToLine("Total Minutes Spent on Finished Bugs per Issues");

        firstLine.addStringToLine("Closed Defects during Sprint");
        firstLine.addStringToLine("Closed Bugs during Sprint");

        for (String currentReason : defectReasonList) {
            firstLine.addStringToLine("\"" + currentReason + "\" Count");
            firstLine.addStringToLine("\"" + currentReason + "\" Minutes Aliquot");
            firstLine.addStringToLine("\"" + currentReason + "\" Minutes Full");
        }

    }

    public void addAdditionalDefectBugStatisticsToLine(CSVLine line,
            AdditionalDefectBugStatistics additionalDefectBugStatistics, List<String> defectReasonList) {

        line.addStringToLine(additionalDefectBugStatistics.getCreatedDefects().toString());
        line.addStringToLine(additionalDefectBugStatistics.getCreatedBugs().toString());

        line.addStringToLine(additionalDefectBugStatistics.getFinishedDefects().toString());
        line.addStringToLine(additionalDefectBugStatistics.getFinishedBugs().toString());

        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedDefectsAndBugs().toString());
        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedDefects().toString());
        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedBugs().toString());

        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedDefectsAndBugsPerDefectAndBug()
                .toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedDefectsPerDefect().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(additionalDefectBugStatistics.getMinutesSpentOnFinishedBugsPerBug().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(additionalDefectBugStatistics.getClosedDefects().toString());
        line.addStringToLine(additionalDefectBugStatistics.getClosedBugs().toString());

        for (String currentReason : defectReasonList) {

            if (additionalDefectBugStatistics.getDefectReasonDataMap().containsKey(currentReason)) {
                line.addStringToLine(additionalDefectBugStatistics.getDefectReasonDataMap().get(currentReason)
                        .getReasonCount().toString());
                line.addStringToLine(additionalDefectBugStatistics.getDefectReasonDataMap().get(currentReason)
                        .getReasonMinutesSpentAliquot().toString().replace('.', DECIMALSEPARATOR));
                line.addStringToLine(additionalDefectBugStatistics.getDefectReasonDataMap().get(currentReason)
                        .getReasonMinutesSpentFull().toString());
            } else {
                line.addStringToLine("0");
                line.addStringToLine("0");
                line.addStringToLine("0");
            }

        }

    }

    public void addSprintDeltaToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("added Issues");
        firstLine.addStringToLine("removed Issues");
        firstLine.addStringToLine("added Story Points");
        firstLine.addStringToLine("removed Story Points");

    }

    public void addSprintDeltaToLine(CSVLine line, SprintDataDelta sprintDataDelta) {

        line.addStringToLine(sprintDataDelta.getAddedIssues().toString());
        line.addStringToLine(sprintDataDelta.getRemovedIssues().toString());
        line.addStringToLine(sprintDataDelta.getAddedStoryPoints().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(sprintDataDelta.getRemovedStoryPoints().toString().replace('.', DECIMALSEPARATOR));

    }

    public CSVLine generateFirstCSVLineForMultipleSprints(List<String> defectReasonList) {

        CSVLine firstLine = new CSVLine();

        addSprintDataToFirstLine(firstLine);

        addBasicResultDataToFirstLine(firstLine);

        addIssueStatisticsToFirstLine(firstLine, "Stories");
        addIssueStatisticsToFirstLine(firstLine, "Defects/Bugs");
        addIssueStatisticsToFirstLine(firstLine, "Tasks");

        addFlaggingStatisticsPartToFirstLine(firstLine, "Issues");
        addFlaggingStatisticsPartToFirstLine(firstLine, "Sub-Issues");

        addSprintDeltaToFirstLine(firstLine);

        addAdditionalDefectBugStatisticsToFirstLine(firstLine, defectReasonList);

        return firstLine;

    }

    public CSVLine generateCSVLineFromSprintResultData(SprintResultData sprintResultData, List<String> defectReasonList) {

        CSVLine generatedLine = new CSVLine();

        addSprintDataToLine(generatedLine, sprintResultData.getSprintData());

        addBasicResultDataToLine(generatedLine, sprintResultData);

        addIssueStatisticsToLine(generatedLine, sprintResultData.getStoryStatistics());
        addIssueStatisticsToLine(generatedLine, sprintResultData.getDefectBugStatistics());
        addIssueStatisticsToLine(generatedLine, sprintResultData.getTaskStatistics());

        addFlaggingStatisticsPartToLine(generatedLine, sprintResultData.getFlaggingStatistics()
                .getIssueFlaggingStatistics());
        addFlaggingStatisticsPartToLine(generatedLine, sprintResultData.getFlaggingStatistics()
                .getSubIssueFlaggingStatistics());

        addSprintDeltaToLine(generatedLine, sprintResultData.getSprintDataDelta());

        addAdditionalDefectBugStatisticsToLine(generatedLine, sprintResultData.getAdditionalDefectBugStatistics(),
                defectReasonList);

        return generatedLine;

    }

    public CSVTable generateCSVTableFromMultipleSprintAnalysis(MultipleSprintAnalysisData multipleSprintAnalysisData) {

        CSVTable table = new CSVTable();
        List<String> defectReasonList = new ArrayList<>();
        Set<String> currentDefectReasons;
        List<SprintResultData> sprintAnalysisDataList = multipleSprintAnalysisData.getSprintResultList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generating CSV out of {} Sprint Analysis Data Sets.", multipleSprintAnalysisData.getSprintResultList().size());
        }

        // sort List by Sprint Start Date
        Collections.sort(sprintAnalysisDataList);

        // generate Defect Reason List
        for (SprintResultData currentSprintData : sprintAnalysisDataList) {

            currentDefectReasons = currentSprintData.getAdditionalDefectBugStatistics().getDefectReasonDataMap().keySet();

            for (String currentDefectReason : currentDefectReasons) {
                if (!defectReasonList.contains(currentDefectReason)) {
                    defectReasonList.add(currentDefectReason);
                }
            }
        }

        // add First Line
        table.addCSVLineToTable(generateFirstCSVLineForMultipleSprints(defectReasonList));

        for (SprintResultData currentSprintData : multipleSprintAnalysisData.getSprintResultList()) {
            table.addCSVLineToTable(generateCSVLineFromSprintResultData(currentSprintData, defectReasonList));
        }

        return table;

    }

}
