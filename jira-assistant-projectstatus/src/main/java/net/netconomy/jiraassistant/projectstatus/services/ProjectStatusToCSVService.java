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
package net.netconomy.jiraassistant.projectstatus.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.projectstatus.data.IssuesInGroupingData;
import net.netconomy.jiraassistant.projectstatus.data.ProjectStatusData;
import net.netconomy.jiraassistant.projectstatus.data.StatusGroupingData;

@Service
public class ProjectStatusToCSVService {

    private static final char DECIMALSEPARATOR = ',';

    private void addFirstCSVLinesForSprintStatus(ProjectStatusData projectStatusData, CSVTable table) {

        CSVLine firstLine = new CSVLine();
        CSVLine secondLine = new CSVLine();

        if (projectStatusData.getCustomGroupingBy() != null) {
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine(projectStatusData.getCustomGroupingBy());
        }

        if (projectStatusData.getOpenIssues() != null) {
            firstLine.addStringToLine("Open");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        if (projectStatusData.getInProgressIssues() != null) {
            firstLine.addStringToLine("In Progress");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        if (projectStatusData.getWaitingIssues() != null) {
            firstLine.addStringToLine("Waiting");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        if (projectStatusData.getImplementedIssues() != null) {
            firstLine.addStringToLine("Implemented");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        if (projectStatusData.getFinishedIssues() != null) {
            firstLine.addStringToLine("Finished");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        if (projectStatusData.getClosedIssues() != null) {
            firstLine.addStringToLine("Closed");
            firstLine.addStringToLine(" ");
            firstLine.addStringToLine(" ");
            secondLine.addStringToLine("Number of Issues");
            secondLine.addStringToLine("Number of Story Points");
            secondLine.addStringToLine("Number of unestimated Issues");
        }

        table.addCSVLineToTable(firstLine);
        table.addCSVLineToTable(secondLine);

    }

    private Set<String> generateGroupingSet(ProjectStatusData projectStatusData) {

        Set<String> groupingKeySet = new HashSet<>();

        if (projectStatusData.getOpenIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getOpenIssues().getCustomGroupingMap().keySet());
        }

        if (projectStatusData.getInProgressIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getInProgressIssues().getCustomGroupingMap().keySet());
        }

        if (projectStatusData.getWaitingIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getWaitingIssues().getCustomGroupingMap().keySet());
        }

        if (projectStatusData.getImplementedIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getImplementedIssues().getCustomGroupingMap().keySet());
        }

        if (projectStatusData.getFinishedIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getFinishedIssues().getCustomGroupingMap().keySet());
        }

        if (projectStatusData.getClosedIssues() != null) {
            groupingKeySet.addAll(projectStatusData.getClosedIssues().getCustomGroupingMap().keySet());
        }

        return groupingKeySet;

    }

    private void addGroupingValuesToLine(CSVLine groupedLine, IssuesInGroupingData groupingData) {

        if (groupingData != null) {
            groupedLine.addStringToLine(groupingData.getNumberOfIssues().toString());
            groupedLine
                    .addStringToLine(groupingData.getNumberOfStoryPoints().toString().replace('.', DECIMALSEPARATOR));
            groupedLine.addStringToLine(groupingData.getNumberOfUnEstimatedIssues().toString());
        } else {
            groupedLine.addStringToLine("0");
            groupedLine.addStringToLine("0" + DECIMALSEPARATOR + "0");
            groupedLine.addStringToLine("0");
        }

    }

    private void addLineForStatusGrouping(ProjectStatusData projectStatusData, CSVTable table) {

        CSVLine groupedLine = new CSVLine();

        StatusGroupingData openIssueData = projectStatusData.getOpenIssues();
        StatusGroupingData inProgressIssueData = projectStatusData.getInProgressIssues();
        StatusGroupingData waitingIssueData = projectStatusData.getWaitingIssues();
        StatusGroupingData implementedIssueData = projectStatusData.getImplementedIssues();
        StatusGroupingData finishedIssueData = projectStatusData.getFinishedIssues();
        StatusGroupingData closedIssueData = projectStatusData.getClosedIssues();

        if (openIssueData != null) {
            addGroupingValuesToLine(groupedLine, openIssueData);
        }

        if (inProgressIssueData != null) {
            addGroupingValuesToLine(groupedLine, inProgressIssueData);
        }

        if (waitingIssueData != null) {
            addGroupingValuesToLine(groupedLine, waitingIssueData);
        }

        if (implementedIssueData != null) {
            addGroupingValuesToLine(groupedLine, implementedIssueData);
        }

        if (finishedIssueData != null) {
            addGroupingValuesToLine(groupedLine, finishedIssueData);
        }

        if (closedIssueData != null) {
            addGroupingValuesToLine(groupedLine, closedIssueData);
        }

        table.addCSVLineToTable(groupedLine);

    }

    private void addLineForCustomGroupingValue(String groupingValue, ProjectStatusData projectStatusData,
            CSVTable table) {

        CSVLine groupedLine = new CSVLine();

        StatusGroupingData openIssueData = projectStatusData.getOpenIssues();
        StatusGroupingData inProgressIssueData = projectStatusData.getInProgressIssues();
        StatusGroupingData waitingIssueData = projectStatusData.getWaitingIssues();
        StatusGroupingData implementedIssueData = projectStatusData.getImplementedIssues();
        StatusGroupingData finishedIssueData = projectStatusData.getFinishedIssues();
        StatusGroupingData closedIssueData = projectStatusData.getClosedIssues();

        groupedLine.addStringToLine(groupingValue);

        if (openIssueData != null) {
            addGroupingValuesToLine(groupedLine, openIssueData.getCustomGroupingMap().get(groupingValue));
        }

        if (inProgressIssueData != null) {
            addGroupingValuesToLine(groupedLine, inProgressIssueData.getCustomGroupingMap().get(groupingValue));
        }

        if (waitingIssueData != null) {
            addGroupingValuesToLine(groupedLine, waitingIssueData.getCustomGroupingMap().get(groupingValue));
        }

        if (implementedIssueData != null) {
            addGroupingValuesToLine(groupedLine, implementedIssueData.getCustomGroupingMap().get(groupingValue));
        }

        if (finishedIssueData != null) {
            addGroupingValuesToLine(groupedLine, finishedIssueData.getCustomGroupingMap().get(groupingValue));
        }

        if (closedIssueData != null) {
            addGroupingValuesToLine(groupedLine, closedIssueData.getCustomGroupingMap().get(groupingValue));
        }

        table.addCSVLineToTable(groupedLine);

    }

    private void processProjectStatusDataIntoCSVCustomGrouped(ProjectStatusData projectStatusData, CSVTable table) {

        Set<String> masterKeySet = generateGroupingSet(projectStatusData);

        for (String currentGroup : masterKeySet) {
            addLineForCustomGroupingValue(currentGroup, projectStatusData, table);
        }

    }

    private void processProjectStatusDataIntoCSVUnGrouped(ProjectStatusData projectStatusData, CSVTable table) {

        addLineForStatusGrouping(projectStatusData, table);

    }

    private CSVLine generateEndInformationLineForSprintStatus(ProjectStatusData projectStatusData) {

        CSVLine lastLine = new CSVLine();

        lastLine.addStringToLine("created: " + projectStatusData.getCreationDate());
        lastLine.addStringToLine("Analysed Issues: " + projectStatusData.getNumberOfAnalysedIssues().toString());
        lastLine.addStringToLine("analysed Projects: " + projectStatusData.getProjects().toString());

        if (projectStatusData.getExcludedTypes() != null && !projectStatusData.getExcludedTypes().isEmpty()) {
            lastLine.addStringToLine("excluded Types: " + projectStatusData.getExcludedTypes().toString());
        }

        if (projectStatusData.getAndClause() != null) {
            lastLine.addStringToLine("and Clause: " + projectStatusData.getAndClause());
        }

        return lastLine;

    }

    public CSVTable generateCSVTableFromProjectStatus(ProjectStatusData projectStatusData) {

        CSVTable table = new CSVTable();

        addFirstCSVLinesForSprintStatus(projectStatusData, table);

        if (projectStatusData.getCustomGroupingBy() != null) {
            processProjectStatusDataIntoCSVCustomGrouped(projectStatusData, table);
        } else {
            processProjectStatusDataIntoCSVUnGrouped(projectStatusData, table);
        }
        
        table.addEmptyLineToTable();
        
        table.addCSVLineToTable(generateEndInformationLineForSprintStatus(projectStatusData));
        
        return table;

    }

}
