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
package net.netconomy.jiraassistant.sprintforecast.cli;

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
import net.netconomy.jiraassistant.sprintforecast.data.Forecast;
import net.netconomy.jiraassistant.sprintforecast.services.ForecastBacklogFilterService;
import net.netconomy.jiraassistant.sprintforecast.services.ForecastToCSVService;
import net.netconomy.jiraassistant.sprintforecast.services.SprintForecastService;

@Service
public class SprintForecastOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintForecastOptionProcessorService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    ForecastBacklogFilterService backlogFilterUtil;

    @Autowired
    SprintForecastService forecastUtil;

    @Autowired
    ForecastToCSVService forecastToCSVUtil;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "forecastFilterJSON":
                    executeForecastFilter(thisOption, true, true);
                    break;
                case "forecastProjectsJSON":
                    executeForecastFilter(thisOption, false, true);
                    break;
                case "forecastFilterCSV":
                    executeForecastFilter(thisOption, true, false);
                    break;
                case "forecastProjectsCSV":
                    executeForecastFilter(thisOption, false, false);
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

    private void executeForecastFilter(Option option, Boolean definedByFilter, Boolean toJson)
            throws ConfigurationException {

        String givenBacklogDefinition;
        String forecastedVelocity;
        String numberOfSprints;
        String andClause;
        String fileName;
        String encoding;
        String fileEnding;
        List<String> projectsList;

        Forecast forecast;
        CSVTable csvTable;

        List<String> wantedFields;

        if (option.getValuesList().size() < 3) {
            throw new JiraAssistantException("the Function forecastFilterJSON needs at lease the 3 Arguments"
                    + " backlogFilter or Projects, forecastVelocity and numberOfSprints.");
        }

        givenBacklogDefinition = option.getValue(0);
        forecastedVelocity = option.getValue(1);
        numberOfSprints = option.getValue(2);

        wantedFields = configuration.getIssueConfiguration().getWantedFields();
        ClientCredentials credentials = configuration.getClientCredentials();

        if (option.getValuesList().size() >= 4) {
            andClause = option.getValue(3);
        } else {
            andClause = "";
        }

        if (definedByFilter) {
            forecast = forecastUtil.generateForecastBasedOnFilter(credentials, givenBacklogDefinition,
                    forecastedVelocity, numberOfSprints, wantedFields);
        } else {
            projectsList = Arrays.asList(givenBacklogDefinition.trim().split(","));

            forecast = forecastUtil.generateForecastBasedOnProjects(credentials, projectsList, andClause,
                    forecastedVelocity, numberOfSprints, wantedFields);
        }

        if (toJson) {
            fileEnding = ".json";
        } else {
            fileEnding = ".csv";
        }

        if (option.getValuesList().size() >= 5) {
            fileName = option.getValue(4);
        } else {
            fileName = "Forecast_" + forecast.getForeCastDate().substring(0, 10) + fileEnding;
        }

        if (option.getValuesList().size() >= 6) {
            encoding = option.getValue(5);
        } else {
            encoding = null;
        }

        if (toJson) {
            fileOutput.writeObjectAsJsonToFile(forecast, fileName, encoding);
        } else {
            csvTable = forecastToCSVUtil.generateCSVTableFromForecast(forecast);

            fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);
        }
    }

}
