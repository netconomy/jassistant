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
package net.netconomy.jiraassistant.estimationstatistics.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import net.netconomy.jiraassistant.base.output.ZipFileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatistics;
import net.netconomy.jiraassistant.estimationstatistics.services.EstimationStatisticsService;
import net.netconomy.jiraassistant.estimationstatistics.services.EstimationStatisticsToCSVService;

@Service
public class EstimationStatisticsOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstimationStatisticsOptionProcessorService.class);

    @Autowired
    EstimationStatisticsService estimationStatisticsService;

    @Autowired
    EstimationStatisticsToCSVService estimationStatisticsToCSVService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    ZipFileOutput zipFileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "estimationStatisticsJSON":
                    calculateEstimationStatistics(thisOption, true);
                    break;
                case "estimationStatisticsCSV":
                    calculateEstimationStatistics(thisOption, false);
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
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    private void calculateEstimationStatistics(Option option, Boolean toJson) throws JiraAssistantException,
            ConfigurationException, IOException {

        String projectsString;
        List<String> projects;
        String issueTypesString;
        String startDateString;
        String endDateString;
        List<String> issueTypes;
        DateTime startDate;
        DateTime endDate;
        Boolean altEstimations;
        String andClause;
        String fileName;
        String encoding;
        String fileEnding;


        EstimationStatistics estimationStatistics;
        Map<String, CSVTable> csvTables;
        
        ClientCredentials credentials = configuration.getClientCredentials();

        if (option.getValuesList().size() < 4) {
            throw new JiraAssistantException(
                    "the Function estimationStatistics needs at lease the 4 Arguments project, issueTypes, startDate and endDate.");
        }
        
        projectsString = option.getValue(0);
        issueTypesString = option.getValue(1);
        startDateString = option.getValue(2);
        endDateString = option.getValue(3);

        projects = Arrays.asList(projectsString.trim().split(","));
        issueTypes = Arrays.asList(issueTypesString.trim().split(","));

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        if (toJson) {
            fileEnding = ".json";
        } else {
            fileEnding = ".csv";
        }

        if (option.getValuesList().size() >= 5) {
            altEstimations = Boolean.parseBoolean(option.getValue(4));
        } else {
            altEstimations = false;
        }

        if (option.getValuesList().size() >= 6) {
            andClause = option.getValue(5);
        } else {
            andClause = null;
        }

        if (option.getValuesList().size() >= 7) {
            fileName = option.getValue(6);
        } else {
            fileName = "estimationStatistics_" + projects.toString().replace(',', '_') + fileEnding;
        }

        if (option.getValuesList().size() >= 8) {
            encoding = option.getValue(7);
        } else {
            encoding = null;
        }
        
        estimationStatistics = estimationStatisticsService.calculateEstimationStatistics(credentials, projects,
                issueTypes, startDate, endDate, altEstimations, andClause);
        
        if (toJson) {
            fileOutput.writeObjectAsJsonToFile(estimationStatistics, fileName, encoding);
        } else {
            csvTables = estimationStatisticsToCSVService
                    .generateCSVTablesFromEstimationStatistics(estimationStatistics);

            createZippedCsvFiles(projects.toString(), encoding, fileEnding, csvTables);

        }

    }

    private void createZippedCsvFiles(String projects, String encoding, String fileEnding,
            Map<String, CSVTable> csvTables) throws IOException {

        String fileName;
        Path tempFile;
        Map<String, Path> csvFiles = new HashMap<>();

        for (Entry<String, CSVTable> current : csvTables.entrySet()) {
            fileName = projects.replace(',', '_') + "_" + current.getKey() + fileEnding;

            tempFile = Files.createTempFile("estimationStatistics_" + projects.replace(',', '_'), ".csv");

            fileOutput.writeCSVTableToFile(current.getValue(), tempFile, ';', encoding);

            csvFiles.put(fileName, tempFile);
        }

        zipFileOutput.createZipFile("estimationStatistics_" + projects.replace(',', '_') + ".zip", csvFiles);
    }
    
}
