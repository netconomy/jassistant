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
package net.netconomy.jiraassistant.billing.cli;

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
import net.netconomy.jiraassistant.billing.data.BillingData;
import net.netconomy.jiraassistant.billing.services.BillingDataToCSVService;
import net.netconomy.jiraassistant.billing.services.BillingService;

@Service
public class BillingOptionProcessorService extends AbstractOptionProcessorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingOptionProcessorService.class);

	@Autowired
	ConfigurationService configuration;

	@Autowired
	FileOutput fileOutput;

	@Autowired
    BillingService billingService;

	@Autowired
    BillingDataToCSVService billingDataToCSVService;

	@Override
	public void processOptions(Option[] options) {

		try {

			for (Option thisOption : options) {

				switch (thisOption.getOpt()) {
                case "billingByProjects":
                    executeBillingCSV(thisOption, true, false);
					break;
                case "billingByAccounts":
                    executeBillingCSV(thisOption, false, false);
                    break;
                case "billingByProjectsMonth":
                    executeBillingCSV(thisOption, true, true);
                    break;
                case "billingByAccountsMonth":
                    executeBillingCSV(thisOption, false, true);
                    break;
				case "help":
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("JiraAssistant", allOptions);
					break;
				default:
					break;
				}

			}

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);

		}

	}

    private void executeBillingCSV(Option option, Boolean identifiedByProjects, Boolean byMonth) throws ConfigurationException {

        String identifiersString;
        List<String> identifiers;
        String dateString;
        DateTime startDate = null;
        DateTime endDate = null;
        String monthString = "";
        DateTime month = null;
        BillingData billingData;
        String issueTypesString;
        List<String> issueTypes;
        String linksToListString;
        List<String> linksToList;
        String additionalAccountsString;
        List<String> additionalAccounts;
        String hoursInAPersonDayString;
        Double hoursInAPersonDay;
        String additionalFieldsString;
        List<String> additionalFields;
		CSVTable csvTable;
        String andClause;
		String fileName;
        String encoding;
        Integer currentIndex;

        ClientCredentials credentials = configuration.getClientCredentials();

        if (byMonth && option.getValuesList().size() < 3) {
            throw new JiraAssistantException(
                "this Function needs at least the Arguments projectKeys/accountKeys, month and hours in a Personday.");
        }else if (!byMonth && option.getValuesList().size() < 4) {
            throw new JiraAssistantException(
                    "this Function needs at least the Arguments projectKeys/accountKeys, startDate, endDate and hours in a Personday.");
        }

        identifiersString = option.getValue(0);
        identifiers = Arrays.asList(identifiersString.trim().split(","));

        if(byMonth) {

            monthString = option.getValue(1);
            month = DateTime.parse(monthString);
            currentIndex = 2;

        } else {

            dateString = option.getValue(1);
            startDate = DateTime.parse(dateString);

            dateString = option.getValue(2);
            endDate = DateTime.parse(dateString);

            currentIndex = 3;

        }

        hoursInAPersonDayString = option.getValue(currentIndex);
        hoursInAPersonDay = Double.parseDouble(hoursInAPersonDayString);

        currentIndex++;

        if (option.getValuesList().size() > currentIndex) {
            additionalFieldsString = option.getValue(currentIndex);
            additionalFields = Arrays.asList(additionalFieldsString.trim().split(","));
            currentIndex++;
        } else {
            additionalFields = new ArrayList<>();
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
            linksToListString = option.getValue(currentIndex);
            linksToList = Arrays.asList(linksToListString.trim().split(","));
            currentIndex++;
        } else {
            linksToList = new ArrayList<>();
        }

        if (identifiedByProjects) {

            if (option.getValuesList().size() > currentIndex) {
                additionalAccountsString = option.getValue(currentIndex);
                additionalAccounts = Arrays.asList(additionalAccountsString.trim().split(","));
                currentIndex++;
            } else {
                additionalAccounts = new ArrayList<>();
            }

            if (option.getValuesList().size() > currentIndex) {
                fileName = option.getValue(currentIndex);
                currentIndex++;
            } else {

                if(byMonth) {
                    fileName = "BillingData_" + identifiersString.replace(',', '_') + "_" + monthString + ".csv";
                } else {
                    fileName = "BillingData_" + identifiersString.replace(',', '_') + "_"
                        + startDate.toString("yyyy-mm-dd") + "_" + endDate.toString("yyyy-mm-dd") + ".csv";
                }

            }

            if (option.getValuesList().size() > currentIndex) {
                encoding = option.getValue(currentIndex);
            } else {
                encoding = null;
            }

        } else {

            additionalAccounts = new ArrayList<>();

            if (option.getValuesList().size() > currentIndex) {
                fileName = option.getValue(currentIndex);
                currentIndex++;
            } else {
                fileName = "BillingData_" + identifiersString.replace(',', '_') + "_"
                        + startDate.toString("yyyy-mm-dd") + "_" + endDate.toString("yyyy-mm-dd") + ".csv";
            }

            if (option.getValuesList().size() > currentIndex) {
                encoding = option.getValue(currentIndex);
                currentIndex++;
            } else {
                encoding = null;
            }

        }

        if(byMonth) {
            billingData = billingService.generateBillingData(credentials, identifiers, month, hoursInAPersonDay,
                issueTypes, andClause, identifiedByProjects, linksToList, additionalAccounts, additionalFields);
        } else {
            billingData = billingService.generateBillingData(credentials, identifiers, startDate, endDate,
                hoursInAPersonDay, issueTypes, andClause, identifiedByProjects, linksToList, additionalAccounts,
                additionalFields);
        }

        csvTable = billingDataToCSVService.generateCSVTableFromBillingData(billingData, additionalFields);
		
        fileOutput.writeCSVTableToFile(csvTable, fileName, ';', encoding);

	}

}
