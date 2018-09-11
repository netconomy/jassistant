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
package net.netconomy.jiraassistant.reopenfactor.cli;

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
import net.netconomy.jiraassistant.reopenfactor.data.ReopenFactorData;
import net.netconomy.jiraassistant.reopenfactor.services.ReopenFactorService;

@Service
public class ReopenFactorOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReopenFactorOptionProcessorService.class);

    @Autowired
    ReopenFactorService reopenFactorService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "reopenFactor":
                    executeReopenFactorJSON(thisOption);
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

    private void executeReopenFactorJSON(Option option) throws ConfigurationException {

        String projectName;
        String startDateString;
        String endDateString;
        DateTime startDate;
        DateTime endDate;
        String fileName;
        String andClause;
        String thresholdString;
        Integer threshold;
        String encoding;
        List<String> projects;

        ReopenFactorData reopenFactorData;

        ClientCredentials credentials = configuration.getClientCredentials();

        if (option.getValuesList().size() < 4) {
            throw new JiraAssistantException(
                    "the Function reopenFactor needs at lease the 4 Arguments project, startDate, endDate and fileName.");
        }

        projectName = option.getValue(0);
        startDateString = option.getValue(1);
        endDateString = option.getValue(2);
        fileName = option.getValue(3);

        startDate = DateTime.parse(startDateString);
        endDate = DateTime.parse(endDateString);

        if (option.getValuesList().size() >= 5) {
            thresholdString = option.getValue(4);
            threshold = Integer.parseInt(thresholdString);
        } else {
            threshold = null;
        }

        if (option.getValuesList().size() >= 6) {
            andClause = option.getValue(5);
        } else {
            andClause = null;
        }

        if (option.getValuesList().size() >= 7) {
            encoding = option.getValue(6);
        } else {
            encoding = null;
        }

        projects = Arrays.asList(projectName.trim().split(","));

        reopenFactorData = reopenFactorService.calculateReopenFactor(credentials, projects, startDate, endDate,
                andClause, threshold);

        fileOutput.writeObjectAsJsonToFile(reopenFactorData, fileName, encoding);
    }

}
