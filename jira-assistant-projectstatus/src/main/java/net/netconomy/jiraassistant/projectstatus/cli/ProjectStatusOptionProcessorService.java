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
package net.netconomy.jiraassistant.projectstatus.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.configuration.ConfigurationException;
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
import net.netconomy.jiraassistant.projectstatus.data.ProjectStatusData;
import net.netconomy.jiraassistant.projectstatus.services.ProjectStatusService;
import net.netconomy.jiraassistant.projectstatus.services.ProjectStatusToCSVService;

@Service
public class ProjectStatusOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectStatusOptionProcessorService.class);

    @Autowired
    ProjectStatusService projectStatusService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    ProjectStatusToCSVService projectStatusToCSVService;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "analyseProjectsJSON":
                    executeAnalyseProject(thisOption, true);
                    break;
                case "analyseProjectsCSV":
                    executeAnalyseProject(thisOption, false);
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

    private void executeAnalyseProject(Option thisOption, Boolean toJson) throws ConfigurationException {
        
        String projectsString;
        List<String> projectList;
        String groupBy;
        String excludedTypesString;
        List<String> excludedTypes;
        String andClause;
        String fileName;
        String encoding;
        String fileEnding;

        CSVTable csvTable;

        ProjectStatusData projectStatusData;

        ClientCredentials credentials = configuration.getClientCredentials();

        if (thisOption.getValuesList().isEmpty()) {
            throw new JiraAssistantException("this Function needs at lease the Argument projects, separated by Colon.");
        }

        projectsString = thisOption.getValue(0);

        projectList = Arrays.asList(projectsString.trim().split(","));

        if (thisOption.getValuesList().size() >= 2) {
            groupBy = thisOption.getValue(1);

            if ("null".equals(groupBy)) {
                groupBy = null;
            }

        } else {
            groupBy = null;
        }

        if (thisOption.getValuesList().size() >= 3) {
            excludedTypesString = thisOption.getValue(2);
            excludedTypes = Arrays.asList(excludedTypesString.trim().split(","));
        } else {
            excludedTypes = new ArrayList<>();
        }

        if (thisOption.getValuesList().size() >= 4) {
            andClause = thisOption.getValue(3);
        } else {
            andClause = "";
        }

        if (toJson) {
            fileEnding = ".json";
        } else {
            fileEnding = ".csv";
        }

        if (thisOption.getValuesList().size() >= 5) {
            fileName = thisOption.getValue(4);
        } else {
            fileName = "projectStatusAnalysis_" + projectsString.replace(',', '_') + fileEnding;
        }

        if (thisOption.getValuesList().size() >= 6) {
            encoding = thisOption.getValue(5);
        } else {
            encoding = null;
        }

        projectStatusData = projectStatusService.analyseProjects(credentials, projectList, groupBy, excludedTypes,
                andClause);

        if (toJson) {
            fileOutput.writeObjectAsJsonToFile(projectStatusData, fileName, encoding);
        } else {
            csvTable = projectStatusToCSVService.generateCSVTableFromProjectStatus(projectStatusData);

            fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);
        }
        
    }
    
}
