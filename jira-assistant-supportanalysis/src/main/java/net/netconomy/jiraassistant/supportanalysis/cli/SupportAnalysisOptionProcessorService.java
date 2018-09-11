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
package net.netconomy.jiraassistant.supportanalysis.cli;

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
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.supportanalysis.data.SupportAnalysisData;
import net.netconomy.jiraassistant.supportanalysis.services.SupportAnalysisService;

@Service
public class SupportAnalysisOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportAnalysisOptionProcessorService.class);

    @Autowired
    SupportAnalysisService supportAnalysisService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "supportAnalysis":
                    executeSupportAnalysisJSON(thisOption, false);
                    break;
                case "supportAnalysisMonth":
                    executeSupportAnalysisJSON(thisOption, true);
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

    private void executeSupportAnalysisJSON(Option option, Boolean byMonth) throws ConfigurationException {

        String projectNames;
        String startDateString = "";
        String endDateString = "";
        DateTime startDate = null;
        DateTime endDate = null;
        String monthString = "";
        DateTime month = null;
        String fileName;
        String issueTypesString;
        String andClause;
        String encoding;
        List<String> projects;
        List<String> issueTypes;
        Integer currentIndex;

        SupportAnalysisData supportAnalysisData;

        ClientCredentials credentials = configuration.getClientCredentials();

        if (byMonth && option.getValuesList().size() < 2) {
            throw new JiraAssistantException(
                "the Function supportAnalysisMonth needs at lease the 2 Arguments project and month.");
        } else if (!byMonth && option.getValuesList().size() < 3) {
            throw new JiraAssistantException(
                "the Function supportAnalysis needs at lease the 3 Arguments project, startDate and endDate.");
        }

        projectNames = option.getValue(0);

        projects = Arrays.asList(projectNames.trim().split(","));

        if(byMonth) {
            monthString = option.getValue(1);

            month = DateTime.parse(monthString);

            currentIndex = 2;
        } else {
            startDateString = option.getValue(1);
            endDateString = option.getValue(2);

            startDate = DateTime.parse(startDateString);
            endDate = DateTime.parse(endDateString);

            currentIndex = 3;
        }

        if (option.getValuesList().size() > currentIndex) {
            issueTypesString = option.getValue(currentIndex);

            issueTypes = Arrays.asList(issueTypesString.trim().split(","));
            currentIndex++;
        } else {
            issueTypes = new ArrayList<>();
        }

        if (option.getValuesList().size() > currentIndex) {
            andClause = option.getValue(currentIndex);
            currentIndex++;
        } else {
            andClause = null;
        }

        if (option.getValuesList().size() > currentIndex) {
            fileName = option.getValue(currentIndex);
            currentIndex++;
        } else if(byMonth) {
            fileName = "supportAnalysis_" + projectNames.replace(',', '_') + "_" + monthString + ".json";
        } else {
            fileName = "supportAnalysis_" + projectNames.replace(',', '_') + "_" + startDateString + "_"
                    + endDateString
                    + ".json";
        }

        if (option.getValuesList().size() > currentIndex) {
            encoding = option.getValue(currentIndex);
        } else {
            encoding = null;
        }

        if(byMonth) {
            supportAnalysisData = supportAnalysisService.calculateSupportAnalysis(credentials, projects, month,
                issueTypes, andClause);
        } else {
            supportAnalysisData = supportAnalysisService.calculateSupportAnalysis(credentials, projects, startDate,
                endDate, issueTypes, andClause);
        }



        fileOutput.writeObjectAsJsonToFile(supportAnalysisData, fileName, encoding);
    }

}
