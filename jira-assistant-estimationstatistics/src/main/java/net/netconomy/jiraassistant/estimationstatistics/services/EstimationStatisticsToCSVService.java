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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatistics;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatisticsData;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueTypeEstimationStatisticsData;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueTypeSingleEstimationStatisticsData;

@Service
public class EstimationStatisticsToCSVService {

    private static final char DECIMALSEPARATOR = ',';

    private CSVTable generateCSVBasedOnEstimationStatistics(EstimationStatisticsData estimationStatisticsData) {

        CSVTable csvTable = new CSVTable();

        CSVLine currentLine;

        // Time Spent
        currentLine = new CSVLine();

        currentLine.addStringToLine("Mean Time Spent in Minutes");
        currentLine.addStringToLine("Median Time Spent in Minutes");
        currentLine.addStringToLine("Standard Deviation Time Spent in Minutes");
        currentLine.addStringToLine("Lower Border Time Spent in Minutes");
        currentLine.addStringToLine("Upper Border Time Spent in Minutes");

        csvTable.addCSVLineToTable(currentLine);

        currentLine = new CSVLine();

        currentLine.addStringToLine(estimationStatisticsData.getMeanTimeSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getMedianTimeSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getStdDeviationTimeSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getLowerBorderSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getUpperBorderSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));

        csvTable.addCSVLineToTable(currentLine);
        csvTable.addEmptyLineToTable();

        // Original Estimate
        currentLine = new CSVLine();

        currentLine.addStringToLine("Mean Time Estimated in Minutes");
        currentLine.addStringToLine("Median Time Estimated in Minutes");
        currentLine.addStringToLine("Standard Deviation Time Estimated in Minutes");
        currentLine.addStringToLine("Missing Original Estimates");

        csvTable.addCSVLineToTable(currentLine);

        currentLine = new CSVLine();

        if (estimationStatisticsData.getMeanTimeEstimated().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getMeanTimeEstimated().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }
        if (estimationStatisticsData.getMedianTimeEstimated().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getMedianTimeEstimated().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }
        if (estimationStatisticsData.getStdDeviationTimeEstimated().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getStdDeviationTimeEstimated().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }
        if (estimationStatisticsData.getMissingOriginalEstimates() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getMissingOriginalEstimates().toString()
                    .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }

        csvTable.addCSVLineToTable(currentLine);
        csvTable.addEmptyLineToTable();

        // Delta Estimated To Spent
        currentLine = new CSVLine();

        currentLine.addStringToLine("Mean Time Delta Estimated To Spent in Minutes");
        currentLine.addStringToLine("Median Time Delta Estimated To Spent in Minutes");
        currentLine.addStringToLine("Standard Deviation Time Delta Estimated To Spent in Minutes");

        csvTable.addCSVLineToTable(currentLine);

        currentLine = new CSVLine();

        if (estimationStatisticsData.getMeanTimeDeltaEstimatedToSpent().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getMeanTimeDeltaEstimatedToSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }
        if (estimationStatisticsData.getMedianTimeDeltaEstimatedToSpent().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getMedianTimeDeltaEstimatedToSpent().getMinutes().toString()
                .replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }
        if (estimationStatisticsData.getStdDeviationTimeDeltaEstimatedToSpent().getMinutes() != null) {
            currentLine.addStringToLine(estimationStatisticsData.getStdDeviationTimeDeltaEstimatedToSpent().getMinutes()
                .toString().replace('.', DECIMALSEPARATOR));
        } else {
            currentLine.addStringToLine(" ");
        }

        csvTable.addCSVLineToTable(currentLine);
        csvTable.addEmptyLineToTable();

        // Estimation Age
        currentLine = new CSVLine();

        currentLine.addStringToLine("Mean Estimation Age in Days");
        currentLine.addStringToLine("Median Estimation Age in Days");
        currentLine.addStringToLine("Standard Deviation Estimation Age in Days");

        csvTable.addCSVLineToTable(currentLine);

        currentLine = new CSVLine();

        currentLine.addStringToLine(estimationStatisticsData.getMeanEstimationAgeDays().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getMedianEstimationAgeDays().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getStdDeviationEstimationAgeDays().toString()
                .replace('.', DECIMALSEPARATOR));

        csvTable.addCSVLineToTable(currentLine);
        csvTable.addEmptyLineToTable();

        // Cycle Time
        currentLine = new CSVLine();

        currentLine.addStringToLine("Mean Cycle Time in Days");
        currentLine.addStringToLine("Median Cycle Time in Days");
        currentLine.addStringToLine("Standard Deviation Cycle Time in Days");

        csvTable.addCSVLineToTable(currentLine);

        currentLine = new CSVLine();

        currentLine.addStringToLine(estimationStatisticsData.getMeanCycleTimeDays().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getMedianCycleTimeDays().toString()
                .replace('.', DECIMALSEPARATOR));
        currentLine.addStringToLine(estimationStatisticsData.getStdDeviationCycleTimeDays().toString()
                .replace('.', DECIMALSEPARATOR));

        csvTable.addCSVLineToTable(currentLine);
        csvTable.addEmptyLineToTable();

        // Raw Data
        currentLine = new CSVLine();

        currentLine.addStringToLine("Raw Data Time Spent in Minutes");
        currentLine.addStringToLine("Raw Data Time Estimated in Minutes");
        currentLine.addStringToLine("Raw Data Time Delta Estimated To Spent in Minutes");
        currentLine.addStringToLine("Raw Data Estimation Age in Days");
        currentLine.addStringToLine("Raw Data Cycle Time in Days");
        
        csvTable.addCSVLineToTable(currentLine);
        
        for (int i = 0; i < estimationStatisticsData.getMinutesSpentAllList().size(); i++) {

            currentLine = new CSVLine();

            currentLine.addStringToLine(estimationStatisticsData.getMinutesSpentAllList().get(i).toString());

            if (i < estimationStatisticsData.getMinutesEstimatedAllList().size()) {
                currentLine.addStringToLine(estimationStatisticsData.getMinutesEstimatedAllList().get(i).toString());
            } else {
                currentLine.addStringToLine(" ");
            }
            
            if (i < estimationStatisticsData.getMinutesDeltaEstimatedToSpentAllList().size()) {
                currentLine.addStringToLine(estimationStatisticsData.getMinutesDeltaEstimatedToSpentAllList().get(i)
                        .toString());
            } else {
                currentLine.addStringToLine(" ");
            }
            
            currentLine.addStringToLine(estimationStatisticsData.getEstimationAgeAllList().get(i).toString());
            currentLine.addStringToLine(estimationStatisticsData.getCycleTimeAllList().get(i).toString());

            csvTable.addCSVLineToTable(currentLine);
            
        }
        
        return csvTable;

    }

    private Map<String, CSVTable> generateCSVTablesFromIssueTypeEstimationStatistics(
            IssueTypeEstimationStatisticsData issueTypeEstimationStatisticsData) {

        Map<String, CSVTable> csvTables = new HashMap<>();
        String fileNamePart;

        for (Entry<String, IssueTypeSingleEstimationStatisticsData> current : issueTypeEstimationStatisticsData
                .getSingleEstimationStatisticsMap().entrySet()) {

            fileNamePart = issueTypeEstimationStatisticsData.getIssueType() + "_"
                    + current.getKey().replace(',', '_').replace(' ', '_');

            csvTables.put(fileNamePart, generateCSVBasedOnEstimationStatistics(current.getValue()));

        }

        fileNamePart = issueTypeEstimationStatisticsData.getIssueType();

        csvTables.put(fileNamePart, generateCSVBasedOnEstimationStatistics(issueTypeEstimationStatisticsData));

        return csvTables;

    }

    public Map<String, CSVTable> generateCSVTablesFromEstimationStatistics(EstimationStatistics estimationStatistics) {

        Map<String, CSVTable> csvTables = new HashMap<>();

        for (Entry<String, IssueTypeEstimationStatisticsData> current : estimationStatistics
                .getEstimationStatisticsMap().entrySet()) {
            
            csvTables.putAll(generateCSVTablesFromIssueTypeEstimationStatistics(current.getValue()));
            
        }

        return csvTables;

    }

}
