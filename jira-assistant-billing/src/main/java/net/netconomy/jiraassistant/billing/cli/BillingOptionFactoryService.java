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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class BillingOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option billingByProjects = Option
                .builder("billingByProjects")
                .hasArgs()
                .argName(
                        "projectKeys(separated_by_colon)> <startDate(yyyy-mm-dd)> <endDate(yyyy-mm-dd)> <hoursInAPersonDay> <additionalFields(separated_by_colon)> <IssueTypes(separated_by_colon)> <andClause> <LinksToList(separated_by_colon)> <accountKeys(separated_by_colon)> <fileName> <encoding")
                .desc("creates a billing export for the given projects in the specified timeframe").build();

        Option billingByAccounts = Option
                .builder("billingByAccounts")
                .hasArgs()
                .argName(
                        "accountKeys(separated_by_colon)> <startDate(yyyy-mm-dd)> <endDate(yyyy-mm-dd)> <hoursInAPersonDay> <additionalFields(separated_by_colon)> <IssueTypes(separated_by_colon)> <andClause> <LinksToList(separated_by_colon)> <fileName> <encoding")
                .desc("creates a billing export for the given projects in the specified timeframe").build();
        Option billingByProjectsMonth = Option
                .builder("billingByProjectsMonth")
                .hasArgs()
                .argName(
                        "projectKeys(separated_by_colon)> <month(yyyy-mm)> <hoursInAPersonDay> <additionalFields(separated_by_colon)> <IssueTypes(separated_by_colon)> <andClause> <LinksToList(separated_by_colon)> <accountKeys(separated_by_colon)> <fileName> <encoding")
                .desc("creates a billing export for the given projects in the specified timeframe").build();

        Option billingByAccountsMonth = Option
                .builder("billingByAccountsMonth")
                .hasArgs()
                .argName(
                        "accountKeys(separated_by_colon)> <month(yyyy-mm)> <hoursInAPersonDay> <additionalFields(separated_by_colon)> <IssueTypes(separated_by_colon)> <andClause> <LinksToList(separated_by_colon)> <fileName> <encoding")
                .desc("creates a billing export for the given projects in the specified timeframe").build();
        
        Option help = new Option("help", "print this message");

        options.addOption(billingByProjects);
        options.addOption(billingByAccounts);
        options.addOption(billingByProjectsMonth);
        options.addOption(billingByAccountsMonth);
        options.addOption(help);

        return options;
    }

}
