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
package net.netconomy.jiraassistant.sprintanalysis.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.configuration.ConfigurationException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.cli.AbstractOptionProcessorService;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.data.rapidviewinformation.SprintDataCollection;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.RapidViewInformationService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.sprintanalysis.data.MultipleSprintAnalysisData;
import net.netconomy.jiraassistant.sprintanalysis.data.SprintResultData;
import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisService;
import net.netconomy.jiraassistant.sprintanalysis.services.MultipleSprintAnalysisToCSVService;
import net.netconomy.jiraassistant.sprintanalysis.services.SprintResultService;

@Service
public class SprintAnalysisOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintAnalysisOptionProcessorService.class);

    @Autowired
    SprintResultService sprintResultService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    MultipleSprintAnalysisToCSVService multipleAnalysisToCSVService;

    @Autowired
    MultipleSprintAnalysisService multipleSprintAnalysisService;

    @Autowired
    RapidViewInformationService rapidViewInformationService;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "sprintAnalysisByNameJSON":
                    executeSprintAnalysisJSON(thisOption, false);
                    break;
                case "sprintAnalysisByIdJSON":
                    executeSprintAnalysisJSON(thisOption, true);
                    break;
                case "multipleSprintAnalysisFromFolder":
                    executeMultipleSprintAnalysisFromFolder(thisOption);
                    break;
                case "multipleSprintAnalysisFromFolderAndRestByName":
                    executeMultipleSprintAnalysisFromFolderAndRest(thisOption, false);
                    break;
                case "multipleSprintAnalysisFromFolderAndRestById":
                    executeMultipleSprintAnalysisFromFolderAndRest(thisOption, true);
                    break;
                case "multipleSprintAnalysisFromFolderAndRestByBoard":
                    executeMultipleSprintAnalysisFromFolderAndRestByBoard(thisOption);
                    break;
                case "multipleSprintAnalysisFromRestByID":
                    executeMultipleSprintAnalysisFromRestByID(thisOption);
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
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void executeSprintAnalysisJSON(Option option, Boolean identifiedByID) throws JiraAssistantException,
            ConfigurationException {

        String sprintIdentifier;
        String fileName;
        String lightAnalysisString;
        Boolean lightAnalysis;
        String observedProjectsString;
        List<String> relevantProjects;
        String encoding;

        SprintResultData sprintResult;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().size() < 2) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments sprintIdentifier and lightAnalysis.");
        }

        sprintIdentifier = option.getValue(0);

        lightAnalysisString = option.getValue(1);

        lightAnalysis = Boolean.valueOf(lightAnalysisString);

        if (option.getValuesList().size() >= 3) {
            observedProjectsString = option.getValue(2);
            relevantProjects = Arrays.asList(observedProjectsString.trim().split(","));
        } else {
            relevantProjects = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 4) {
            fileName = option.getValue(3);
        } else if (identifiedByID) {
            fileName = "Sprint_" + sprintIdentifier + ".json";
        } else {
            fileName = sprintIdentifier + ".json";
        }

        if (option.getValuesList().size() >= 5) {
            encoding = option.getValue(4);
        } else {
            encoding = null;
        }

        sprintResult = sprintResultService.calculateSprintResult(credentials, sprintIdentifier, identifiedByID,
                wantedFields, lightAnalysis, relevantProjects);

        fileOutput.writeObjectAsJsonToFile(sprintResult, fileName, encoding);
    }

    private void executeMultipleSprintAnalysisFromFolder(Option option) {

        String folderName;
        String fileName;
        String encoding;
        MultipleSprintAnalysisData multipleSprintAnalysisData;
        CSVTable csvTable;

        if (option.getValuesList().isEmpty()) {
            throw new JiraAssistantException(
"this Function needs at lease the Argument folderName.");
        }

        folderName = option.getValue(0);

        if (option.getValuesList().size() >= 2) {
            fileName = option.getValue(1);
        } else {
            fileName = "multipleSprintAnalysis_" + folderName + ".csv";
        }

        if (option.getValuesList().size() >= 3) {
            encoding = option.getValue(2);
        } else {
            encoding = null;
        }

        multipleSprintAnalysisData = multipleSprintAnalysisService.generateMultipleSprintAnalysisDataFromFolder(
                folderName, encoding);

        csvTable = multipleAnalysisToCSVService.generateCSVTableFromMultipleSprintAnalysis(multipleSprintAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);
    }

    private void executeMultipleSprintAnalysisFromFolderAndRest(Option option, Boolean identifiedByID)
            throws JiraAssistantException, ConfigurationException {

        String sprintIdentifiers;
        String folderName;
        String fileName;
        String sprintFileName;
        String encoding;
        String currentSprintIdentifier;
        String observedProjectsString;
        List<String> observedProjects;

        SprintResultData sprintResult;
        MultipleSprintAnalysisData multipleSprintAnalysisData;
        CSVTable csvTable;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().size() < 2) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments sprintIdentifiers and folderName.");
        }

        sprintIdentifiers = option.getValue(0);

        folderName = option.getValue(1);

        if (option.getValuesList().size() >= 3) {
            observedProjectsString = option.getValue(2);
            observedProjects = Arrays.asList(observedProjectsString.trim().split(","));
        } else {
            observedProjects = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 4) {
            fileName = option.getValue(3);
        } else {
            fileName = "multipleSprintAnalysis_" + folderName + ".csv";
        }

        if (option.getValuesList().size() >= 5) {
            encoding = option.getValue(4);
        } else {
            encoding = null;
        }

        for (String current : sprintIdentifiers.split(",")) {

            currentSprintIdentifier = current.trim();

            if (identifiedByID) {
                sprintFileName = folderName + "\\Sprint_" + currentSprintIdentifier + ".json";
            } else {
                sprintFileName = folderName + "\\" + currentSprintIdentifier + ".json";
            }

            sprintResult = sprintResultService.calculateSprintResult(credentials, currentSprintIdentifier,
                    identifiedByID,
                    wantedFields, true, observedProjects);

            fileOutput.writeObjectAsJsonToFile(sprintResult, sprintFileName, encoding);

        }

        multipleSprintAnalysisData = multipleSprintAnalysisService.generateMultipleSprintAnalysisDataFromFolder(
                folderName, encoding);

        csvTable = multipleAnalysisToCSVService.generateCSVTableFromMultipleSprintAnalysis(multipleSprintAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);

    }

    private void executeMultipleSprintAnalysisFromFolderAndRestByBoard(Option option) throws JiraAssistantException,
            ConfigurationException, JSONException {

        String boardID;
        String startDateString;
        String folderName;
        String fileName;
        String sprintFileName;
        String encoding;
        String currentSprintIdentifier;
        String observedProjectsString;
        List<String> observedProjects;
        SprintDataCollection sprintDataCollection;

        SprintResultData sprintResult;
        MultipleSprintAnalysisData multipleSprintAnalysisData;
        CSVTable csvTable;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().size() < 3) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments boardID, startDate and folderName.");
        }

        boardID = option.getValue(0);

        startDateString = option.getValue(1);

        folderName = option.getValue(2);

        if (option.getValuesList().size() >= 4) {
            observedProjectsString = option.getValue(3);
            observedProjects = Arrays.asList(observedProjectsString.trim().split(","));
        } else {
            observedProjects = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 5) {
            fileName = option.getValue(4);
        } else {
            fileName = "multipleSprintAnalysis_" + folderName + ".csv";
        }

        if (option.getValuesList().size() >= 6) {
            encoding = option.getValue(5);
        } else {
            encoding = null;
        }

        sprintDataCollection = rapidViewInformationService.getSprintsForRapidViewCompletedAfterDate(credentials,
                boardID, startDateString);

        for (SprintData current : sprintDataCollection.getSprintDataList()) {

            currentSprintIdentifier = current.getId().toString();

            sprintFileName = folderName + "\\Sprint_" + currentSprintIdentifier + ".json";

            sprintResult = sprintResultService.calculateSprintResult(credentials, currentSprintIdentifier,
 true,
                    wantedFields, true, observedProjects);

            fileOutput.writeObjectAsJsonToFile(sprintResult, sprintFileName, encoding);

        }

        multipleSprintAnalysisData = multipleSprintAnalysisService.generateMultipleSprintAnalysisDataFromFolder(
                folderName, encoding);

        csvTable = multipleAnalysisToCSVService.generateCSVTableFromMultipleSprintAnalysis(multipleSprintAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);

    }

    private void executeMultipleSprintAnalysisFromRestByID(Option option)
            throws JiraAssistantException, ConfigurationException {

        String sprintIdentifiers;
        List<String> sprintIDList;
        String fileName;
        String encoding;
        String observedProjectsString;
        List<String> observedProjects;

        MultipleSprintAnalysisData multipleSprintAnalysisData;
        CSVTable csvTable;

        ClientCredentials credentials = configuration.getClientCredentials();
        List<String> wantedFields = configuration.getIssueConfiguration().getWantedFields();

        if (option.getValuesList().isEmpty()) {
            throw new JiraAssistantException(
                    "this Function needs at lease the Arguments sprintIdentifiers.");
        }

        sprintIdentifiers = option.getValue(0);

        if (option.getValuesList().size() >= 2) {
            observedProjectsString = option.getValue(1);
            observedProjects = Arrays.asList(observedProjectsString.trim().split(","));
        } else {
            observedProjects = new ArrayList<>();
        }

        if (option.getValuesList().size() >= 3) {
            fileName = option.getValue(2);
        } else {
            fileName = "multipleSprintAnalysis_" + sprintIdentifiers.replace(',', '_') + ".csv";
        }

        if (option.getValuesList().size() >= 4) {
            encoding = option.getValue(3);
        } else {
            encoding = null;
        }

        sprintIDList = Arrays.asList(sprintIdentifiers.trim().split(","));

        multipleSprintAnalysisData = multipleSprintAnalysisService.generateMultipleSprintAnalysisDataFromIDList(
                sprintIDList, credentials, wantedFields, observedProjects);

        csvTable = multipleAnalysisToCSVService.generateCSVTableFromMultipleSprintAnalysis(multipleSprintAnalysisData);

        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);

    }

}
