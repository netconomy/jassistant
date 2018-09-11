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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.kanbananalysis.data.AltIssueStatistics;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanMetrics;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanResultData;
import net.netconomy.jiraassistant.kanbananalysis.data.MultipleKanbanAnalysisData;
import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisToCSVService;

@Service
public class MultipleKanbanAnalysisToCSVService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleKanbanAnalysisToCSVService.class);

    private static final char DECIMALSEPARATOR = ',';

    @Autowired
    private MultipleSprintAnalysisToCSVService multipleSprintAnalysisToCSVService;

    public void addAltIssueStatisticsToFirstLine(CSVLine firstLine, String issueType) {

        firstLine.addStringToLine("Number of " + issueType);
        firstLine.addStringToLine("Issue Estimations " + issueType);
        firstLine.addStringToLine("Reopen-Count " + issueType);
        firstLine.addStringToLine("Minutes spent on " + issueType);
        firstLine.addStringToLine("Number of all implemented " + issueType);
        firstLine.addStringToLine("Issue Estimations of all implemented " + issueType);
        firstLine.addStringToLine("Number of all finished " + issueType);
        firstLine.addStringToLine("Issue Estimations of all finished " + issueType);
        firstLine.addStringToLine("Number of all closed " + issueType);
        firstLine.addStringToLine("Issue Estimations of all closed " + issueType);

    }

    public void addAltIssueStatisticsToLine(CSVLine line, AltIssueStatistics altIssueStatistics) {

        line.addStringToLine(altIssueStatistics.getNumberOfIssues().toString());
        line.addStringToLine(altIssueStatistics.getIssuesByEstimation().toString());
        line.addStringToLine(altIssueStatistics.getReopenCount().toString());
        line.addStringToLine(altIssueStatistics.getMinutesSpent().toString());
        line.addStringToLine(altIssueStatistics.getImplementedIssues().toString());
        line.addStringToLine(altIssueStatistics.getImplemented().toString());
        line.addStringToLine(altIssueStatistics.getFinishedIssues().toString());
        line.addStringToLine(altIssueStatistics.getFinished().toString());
        line.addStringToLine(altIssueStatistics.getClosedIssues().toString());
        line.addStringToLine(altIssueStatistics.getClosed().toString());

    }

    public void addKanbanDataToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("Projects");
        firstLine.addStringToLine("Start Date");
        firstLine.addStringToLine("End Date");

    }

    public void addKanbanDataToLine(CSVLine line, KanbanResultData kanbanResultData) {

        line.addStringToLine(kanbanResultData.getObservedProjects().toString());
        line.addStringToLine(kanbanResultData.getStartDate());
        line.addStringToLine(kanbanResultData.getEndDate());

    }

    public void addBasicResultDataToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("Number of all Issues");
        firstLine.addStringToLine("Number of all Sub Issues");

        firstLine.addStringToLine("Number of all Story Points");

        firstLine.addStringToLine("Number of all finished Issues");
        firstLine.addStringToLine("Number of all closed Issues");

        firstLine.addStringToLine("Number of all finished Story Points");
        firstLine.addStringToLine("Number of all closed Story Points");

        firstLine.addStringToLine("Minutes spent on Issues");
        firstLine.addStringToLine("Minutes spent on Sub Issues");
        firstLine.addStringToLine("Minutes spent on Sub Bugs");

        firstLine.addStringToLine("Reopen-Count");

    }

    public void addBasicResultDataToLine(CSVLine line, KanbanResultData kanbanResultData) {

        line.addStringToLine(kanbanResultData.getNumberOfIssues().toString());
        line.addStringToLine(kanbanResultData.getNumberOfSubIssues().toString());

        line.addStringToLine(kanbanResultData.getNumberOfStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(kanbanResultData.getNumberOfAllFinishedIssues().toString());
        line.addStringToLine(kanbanResultData.getNumberOfAllClosedIssues().toString());

        line.addStringToLine(kanbanResultData.getNumberOfAllFinishedStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(kanbanResultData.getNumberOfAllClosedStoryPoints().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(kanbanResultData.getMinutesSpentOnIssues().toString());
        line.addStringToLine(kanbanResultData.getMinutesSpentOnSubIssues().toString());
        line.addStringToLine(kanbanResultData.getMinutesSpentOnSubBugs().toString());

        line.addStringToLine(kanbanResultData.getReopenCount().toString());

    }

    public void addKanbanMetricsToFirstLine(CSVLine firstLine) {

        firstLine.addStringToLine("Mean of Lead Time in Minutes");
        firstLine.addStringToLine("Median of Lead Time in Minutes");
        firstLine.addStringToLine("Standard Deviation of Lead Time in Minutes");

        firstLine.addStringToLine("Mean of Cycle Time in Minutes");
        firstLine.addStringToLine("Median of Cycle Time in Minutes");
        firstLine.addStringToLine("Standard Deviation of Cycle Time in Minutes");

        firstLine.addStringToLine("Mean of Waiting Time in Minutes");
        firstLine.addStringToLine("Median of Waiting Time in Minutes");
        firstLine.addStringToLine("Standard Deviation of Waiting Time in Minutes");

    }

    public void addKanbanMetricsToLine(CSVLine line, KanbanMetrics kanbanMetrics) {

        if(kanbanMetrics.getMeanLeadTime().getMinutes() != null && kanbanMetrics.getMedianLeadTime().getMinutes() != null
            && kanbanMetrics.getStandardDeviationLeadTime().getMinutes() != null) {
            line.addStringToLine(kanbanMetrics.getMeanLeadTime().getMinutes().toString().replace('.', DECIMALSEPARATOR));
            line.addStringToLine(kanbanMetrics.getMedianLeadTime().getMinutes().toString().replace('.', DECIMALSEPARATOR));
            line.addStringToLine(kanbanMetrics.getStandardDeviationLeadTime().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            line.addStringToLine("");
            line.addStringToLine("");
            line.addStringToLine("");
        }

        line.addStringToLine(kanbanMetrics.getMeanCycleTime().getMinutes().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(kanbanMetrics.getMedianCycleTime().getMinutes().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(kanbanMetrics.getStandardDeviationCycleTime().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));

        line.addStringToLine(kanbanMetrics.getMeanWaitingTime().getMinutes().toString().replace('.', DECIMALSEPARATOR));
        line.addStringToLine(kanbanMetrics.getMedianWaitingTime().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        line.addStringToLine(kanbanMetrics.getStandardDeviationWaitingTime().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));

    }

    public CSVLine generateFirstCSVLineForMultipleKanbanResults(List<String> defectReasonList) {

        CSVLine firstLine = new CSVLine();

        addKanbanDataToFirstLine(firstLine);

        addBasicResultDataToFirstLine(firstLine);

        addAltIssueStatisticsToFirstLine(firstLine, "Stories");
        addAltIssueStatisticsToFirstLine(firstLine, "Defects/Bugs");
        addAltIssueStatisticsToFirstLine(firstLine, "Tasks");

        addKanbanMetricsToFirstLine(firstLine);

        multipleSprintAnalysisToCSVService.addFlaggingStatisticsPartToFirstLine(firstLine, "Issues");
        multipleSprintAnalysisToCSVService.addFlaggingStatisticsPartToFirstLine(firstLine, "Sub-Issues");

        multipleSprintAnalysisToCSVService.addAdditionalDefectBugStatisticsToFirstLine(firstLine, defectReasonList);

        return firstLine;

    }

    public CSVLine generateCSVLineFromKanbanResultData(KanbanResultData kanbanResultData, List<String> defectReasonList) {

        CSVLine generatedLine = new CSVLine();

        addKanbanDataToLine(generatedLine, kanbanResultData);

        addBasicResultDataToLine(generatedLine, kanbanResultData);

        if(kanbanResultData.getWithAltEstimations()) {
            addAltIssueStatisticsToLine(generatedLine, kanbanResultData.getAltStoryStatistics());
            addAltIssueStatisticsToLine(generatedLine, kanbanResultData.getAltDefectBugStatistics());
            addAltIssueStatisticsToLine(generatedLine, kanbanResultData.getAltTaskStatistics());
        } else {
            multipleSprintAnalysisToCSVService.addIssueStatisticsToLine(generatedLine,
                kanbanResultData.getStoryStatistics());
            multipleSprintAnalysisToCSVService.addIssueStatisticsToLine(generatedLine,
                kanbanResultData.getDefectBugStatistics());
            multipleSprintAnalysisToCSVService
                .addIssueStatisticsToLine(generatedLine, kanbanResultData.getTaskStatistics());
        }

        addKanbanMetricsToLine(generatedLine, kanbanResultData.getKanbanMetrics());

        multipleSprintAnalysisToCSVService.addFlaggingStatisticsPartToLine(generatedLine, kanbanResultData
                .getFlaggingStatistics().getIssueFlaggingStatistics());
        multipleSprintAnalysisToCSVService.addFlaggingStatisticsPartToLine(generatedLine, kanbanResultData
                .getFlaggingStatistics().getSubIssueFlaggingStatistics());

        multipleSprintAnalysisToCSVService.addAdditionalDefectBugStatisticsToLine(generatedLine,
                kanbanResultData.getAdditionalDefectBugStatistics(), defectReasonList);

        return generatedLine;

    }

    public CSVTable generateCSVTableFromMultipleKanbanAnalysis(MultipleKanbanAnalysisData multipleKanbanAnalysisData) {

        CSVTable table = new CSVTable();
        List<String> defectReasonList = new ArrayList<>();
        Set<String> currentDefectReasons;
        List<KanbanResultData> kanbanAnalysisDataList = multipleKanbanAnalysisData.getKanbanResultList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generating CSV out of {} Kanban Analysis Data Sets.", multipleKanbanAnalysisData.getKanbanResultList().size());
        }

        // sort List by Period Start Date
        Collections.sort(kanbanAnalysisDataList);

        // generate Defect Reason List
        for (KanbanResultData currentKanbanData : kanbanAnalysisDataList) {

            currentDefectReasons = currentKanbanData.getAdditionalDefectBugStatistics().getDefectReasonDataMap()
                    .keySet();

            for (String currentDefectReason : currentDefectReasons) {
                if (!defectReasonList.contains(currentDefectReason)) {
                    defectReasonList.add(currentDefectReason);
                }
            }
        }

        // add First Line
        table.addCSVLineToTable(generateFirstCSVLineForMultipleKanbanResults(defectReasonList));

        for (KanbanResultData currentKanbanData : multipleKanbanAnalysisData.getKanbanResultList()) {
            table.addCSVLineToTable(generateCSVLineFromKanbanResultData(currentKanbanData, defectReasonList));
        }

        return table;

    }

}
