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
package net.netconomy.jiraassistant.kanbananalysis.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.cli.AbstractOptionProcessorService;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanResultData;
import net.netconomy.jiraassistant.kanbananalysis.data.MultipleKanbanAnalysisData;
import net.netconomy.jiraassistant.kanbananalysis.services.KanbanResultService;
import net.netconomy.jiraassistant.kanbananalysis.services.MultipleKanbanAnalysisService;
import net.netconomy.jiraassistant.kanbananalysis.services.MultipleKanbanAnalysisToCSVService;

@Service
public class KanbanAnalysisOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KanbanAnalysisOptionProcessorService.class);

    @Autowired
    KanbanResultService kanbanResultService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    MultipleKanbanAnalysisToCSVService multipleKanbanAnalysisToCSVService;

    @Autowired
    MultipleKanbanAnalysisService multipleKanbanAnalysisService;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "kanbanAnalysisByFilterJSON":
                    executeKanbanAnalysisByFilter(thisOption);
                    break;
                case "kanbanAnalysisByProjectsJSON":
                    executeKanbanAnalysisByProjects(thisOption);
                    break;
                case "multipleKanbanAnalysisFromFolder":
                    executeMultipleKanbanAnalysisFromFolder(thisOption);
                    break;
                case "help":
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("JiraAssistant", allOptions);
                    break;
                default:
                    break;
                }

            }

        } catch (ConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void executeKanbanAnalysisByFilter(Option option) throws JiraAssistantException, ConfigurationException {

        String backlogFilter;
        String startDateString;
        String endDateString;
        DateTime startDate;
        DateTime endDate;
        String withAltEstimationsString;
        Boolean withAltEstimations;
        String lightAnalysisString;
        Boolean lightAnalysis;
        String observedProjectsString;
        List<String> relevantProjects;
        String fileName;
        String encoding;

        KanbanResultData kanbanResult;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().size() < 4) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments backlogFilter, startDate, endDate, withAltEstimations and lightAnalysis.");
        }

        backlogFilter = option.getValue(0);

        startDateString = option.getValue(1);

        endDateString = option.getValue(2);

        withAltEstimationsString = option.getValue(3);

        lightAnalysisString = option.getValue(4);

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        withAltEstimations = Boolean.valueOf(withAltEstimationsString);
        lightAnalysis = Boolean.valueOf(lightAnalysisString);

        if (option.getValuesList().size() >= 6) {
            observedProjectsString = option.getValue(5);
            relevantProjects = Arrays.asList(observedProjectsString.trim().split(","));
        } else {
            relevantProjects = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 7) {
            fileName = option.getValue(6);
        } else {
            fileName = "kanbanAnalysis_" + startDateString + "_" + endDateString + ".json";
        }

        if (option.getValuesList().size() >= 8) {
            encoding = option.getValue(7);
        } else {
            encoding = null;
        }

        kanbanResult = kanbanResultService.calculateKanbanResultBasedOnFilter(credentials, backlogFilter, startDate,
                endDate, wantedFields, lightAnalysis, relevantProjects, withAltEstimations);

        fileOutput.writeObjectAsJsonToFile(kanbanResult, fileName, encoding);

    }

    private void executeKanbanAnalysisByProjects(Option option) throws JiraAssistantException, ConfigurationException {

        String projectsString;
        List<String> projectList;
        String startDateString;
        String endDateString;
        DateTime startDate;
        DateTime endDate;
        String withAltEstimationsString;
        Boolean withAltEstimations;
        String lightAnalysisString;
        Boolean lightAnalysis;
        String excludedStatusString;
        List<String> excludedStatus;
        String excludedTypesString;
        List<String> excludedTypes;
        String andClause;
        String fileName;
        String encoding;

        KanbanResultData kanbanResult;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().size() < 4) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments backlogFilter, startDate, endDate, withAltEstimations and lightAnalysis.");
        }

        projectsString = option.getValue(0);

        startDateString = option.getValue(1);

        endDateString = option.getValue(2);

        withAltEstimationsString = option.getValue(3);

        withAltEstimations = Boolean.valueOf(withAltEstimationsString);
        lightAnalysisString = option.getValue(4);

        projectList = Arrays.asList(projectsString.trim().split(","));

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        lightAnalysis = Boolean.valueOf(lightAnalysisString);

        if (option.getValuesList().size() >= 6) {
            excludedStatusString = option.getValue(5);
            excludedStatus = Arrays.asList(excludedStatusString.trim().split(","));
        } else {
            excludedStatus = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 7) {
            excludedTypesString = option.getValue(6);
            excludedTypes = Arrays.asList(excludedTypesString.trim().split(","));
        } else {
            excludedTypes = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 8) {
            andClause = option.getValue(7);
        } else {
            andClause = "";
        }

        if (option.getValuesList().size() >= 9) {
            fileName = option.getValue(8);
        } else {
            fileName = "kanbanAnalysis_" + startDateString + "_" + endDateString + ".json";
        }

        if (option.getValuesList().size() >= 10) {
            encoding = option.getValue(9);
        } else {
            encoding = null;
        }

        kanbanResult = kanbanResultService.calculateKanbanResultBasedOnProjects(credentials, projectList, startDate,
                endDate, excludedStatus, excludedTypes, andClause, wantedFields, lightAnalysis, withAltEstimations);

        fileOutput.writeObjectAsJsonToFile(kanbanResult, fileName, encoding);

    }

    private void executeMultipleKanbanAnalysisFromFolder(Option option) {

        String folderName;
        String fileName;
        String encoding;
        MultipleKanbanAnalysisData multipleKanbanAnalysisData;
        CSVTable csvTable;

        if (option.getValuesList().isEmpty()) {
            throw new JiraAssistantException("this Function needs at lease the Argument folderName.");
        }

        folderName = option.getValue(0);

        if (option.getValuesList().size() >= 2) {
            fileName = option.getValue(1);
        } else {
            fileName = "multipleKanbanAnalysis_" + folderName + ".csv";
        }

        if (option.getValuesList().size() >= 3) {
            encoding = option.getValue(2);
        } else {
            encoding = null;
        }

        multipleKanbanAnalysisData = multipleKanbanAnalysisService.generateMultipleKanbanAnalysisDataFromFolder(
                folderName, encoding);

        csvTable = multipleKanbanAnalysisToCSVService
                .generateCSVTableFromMultipleKanbanAnalysis(multipleKanbanAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);
    }

}
