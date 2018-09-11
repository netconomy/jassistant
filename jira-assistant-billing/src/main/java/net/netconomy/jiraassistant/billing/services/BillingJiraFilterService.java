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
package net.netconomy.jiraassistant.billing.services;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.services.DataConversionService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;

@Service
public class BillingJiraFilterService {

	@Autowired
	ConfigurationService configuration;

	@Autowired
	BacklogFilterService backlogFilterService;

    @Autowired
    DataConversionService dataConversionService;

    /**
     * This Function provides a Filter for all Issues from one or more Projects or Accounts that have booked Time during
     * the given period. This Function is very accurate but only works for Jira 6.4 or later.
     * 
     * @param identifiers either project or account Keys
     * @param startDate
     * @param endDate
     * @param issueTypes
     * @param andClause
     * @param additionalAccounts this is only relevant if the Billing is based on Projects
     * @param subIssues if this is true the Filter will only provide SubIssues, of it is false the Filter will only Provide Issues
     * @param basedOnProjects if the given identifiers are Projects this has to be true, if they are accounts this has to be false
     * @return
     */
    String generateBillingFilter(List<String> identifiers, DateTime startDate, DateTime endDate,
            List<String> issueTypes, String andClause, List<String> additionalAccounts, Boolean subIssues, Boolean basedOnProjects) {

        StringBuilder generatedFilter = new StringBuilder();
        Joiner joiner = Joiner.on("','");
        String jqlStartDate = dataConversionService.convertDateTimeToJQLDate(startDate);
        String jqlEndDate = dataConversionService.convertDateTimeToJQLDate(endDate);

        if(basedOnProjects) {
            generatedFilter.append("(project in ('");

            joiner.appendTo(generatedFilter, identifiers);

            if (additionalAccounts != null && !additionalAccounts.isEmpty()) {
                generatedFilter.append("') or account in ('");

                joiner.appendTo(generatedFilter, additionalAccounts);
            }

            generatedFilter.append("'))");

        } else {
            generatedFilter.append("account in ('");

            joiner.appendTo(generatedFilter, identifiers);

            generatedFilter.append("')");
        }

        generatedFilter.append(" and worklogDate >= '" + jqlStartDate + "' and worklogDate < '" + jqlEndDate + "'");

        if (issueTypes != null && !issueTypes.isEmpty()) {
            generatedFilter.append(" and issuetype in ('");

            joiner.appendTo(generatedFilter, issueTypes);

            generatedFilter.append("')");
        }

        if (andClause != null && !andClause.isEmpty()) {
            generatedFilter.append(" and (" + andClause + ")");
        }

        if(subIssues) {
            generatedFilter.append(" and issuetype in subTaskIssueTypes() order by Rank asc");
        } else {
            generatedFilter.append(" and issuetype not in subTaskIssueTypes() order by Rank asc");
        }

        return generatedFilter.toString();

    }

}
