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
package net.netconomy.jiraassistant.accountprogress.cli;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.accountprogress.data.AccountProgressResultData;
import net.netconomy.jiraassistant.accountprogress.services.AccountProgressService;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.cli.AbstractOptionProcessorService;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;

@Service
public class AccountProgressOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountProgressOptionProcessorService.class);

    @Autowired
    AccountProgressService accountProgressService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    FileOutput fileOutput;

    @Override
    public void processOptions(Option[] options) {

        try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "accountProgressByKeyJSON":
                    executeAccountProgressJSON(thisOption, false);
                    break;
                case "accountProgressByIDJSON":
                    executeAccountProgressJSON(thisOption, true);
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

    private void executeAccountProgressJSON(Option option, Boolean identifiedByIDs) throws ConfigurationException {

        String accountKey;
        List<String> accountKeyList;
        String andClause;
        String fileName;
        String encoding;

        AccountProgressResultData accountProgressResult;

        ClientCredentials credentials = configuration.getClientCredentials();

        if (option.getValuesList().size() < 1) {
            throw new JiraAssistantException("this Function needs at least the Argument accountKey.");
        }

        accountKey = option.getValue(0);

        accountKeyList = Arrays.asList(accountKey.trim().split(","));

        if (option.getValuesList().size() >= 2) {
            andClause = option.getValue(1);
        } else {
            andClause = null;
        }

        if (option.getValuesList().size() >= 3) {
            fileName = option.getValue(2);
        } else {
            fileName = "Account_" + accountKey + ".json";
        }

        if (option.getValuesList().size() >= 4) {
            encoding = option.getValue(3);
        } else {
            encoding = null;
        }

        accountProgressResult = accountProgressService.calculateAccountProgress(credentials, accountKeyList,
                identifiedByIDs, andClause);

        fileOutput.writeObjectAsJsonToFile(accountProgressResult, fileName, encoding);
    }

}
