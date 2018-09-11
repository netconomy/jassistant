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
package net.netconomy.jiraassistant.reopenfactor.services;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.reopenfactor.data.ReopenFactorData;

@Service
public class ReopenFactorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReopenFactorService.class);

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    ReopenFactorIssueService reopenFactorIssueService;

    ReopenFactorData calculateReopenFactor(ClientCredentials credentials, List<Issue> issueList, List<String> projects, DateTime startDate,
        DateTime endDate, String andClause, Integer threshold) throws ConfigurationException {

        ProjectConfiguration configuration = configurationService.getProjectConfiguration();
        ReopenFactorData reopenFactorData = new ReopenFactorData();
        List<String> inTestingStatus;
        List<String> reopenedStatus;
        List<String> testedStatus;
        Double reopenCount;
        Double testedCount;
        Double inTestingCount;
        Double processedCount;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Calculating reopenFactor for {} Issues.", issueList.size());
        }

        inTestingStatus = configuration.getInTestingStatus();
        reopenedStatus = configuration.getReopenedStatus();
        testedStatus = configuration.getTestedStatus();

        reopenFactorData.setInTestingStatus(inTestingStatus);
        reopenFactorData.setReopenedStatus(reopenedStatus);
        reopenFactorData.setTestedStatus(testedStatus);

        reopenFactorData.setProjects(projects.toString());
        reopenFactorData.setStartDate(startDate.toString());
        reopenFactorData.setEndDate(endDate.toString());
        reopenFactorData.setAndClause(andClause);
        reopenFactorData.setThreshold(threshold);
        reopenFactorData.setCreationDate(DateTime.now().toString());
        reopenFactorData.setIssuesChangingState(issueList.size());

        reopenFactorIssueService
            .processIssuesIntoReopenFactorData(credentials, reopenFactorData, issueList, startDate, endDate, threshold, configuration);

        reopenCount = reopenFactorData.getReopenCount().doubleValue();
        testedCount = reopenFactorData.getTestedCount().doubleValue();
        inTestingCount = reopenFactorData.getInTestingCount().doubleValue();
        processedCount = reopenCount + testedCount;

        if (reopenCount != 0 && testedCount != 0) {
            reopenFactorData.setReopensByTested(reopenCount / testedCount);
        }

        if (reopenCount != 0 && testedCount != 0 && inTestingCount != 0) {

            reopenFactorData.setProcessedByInTesting(processedCount / inTestingCount);

            reopenFactorData.setReopensByProcessed(reopenCount / processedCount);

            reopenFactorData.setTestedByProcessed(testedCount / processedCount);
        }

        return reopenFactorData;
    }

    /**
     * Calculate Reopen Factor Data for the given Projects during the given timewindow.
     *
     * @param credentials
     * @param projects
     * @param startDate
     * @param endDate
     * @param andClause
     * @param threshold
     * @return
     * @throws ConfigurationException
     */
    public ReopenFactorData calculateReopenFactor(ClientCredentials credentials, List<String> projects,
        DateTime startDate, DateTime endDate, String andClause, Integer threshold) throws ConfigurationException {

        List<Issue> issueList;

        issueList = reopenFactorIssueService.getProjectIssues(credentials, projects, startDate, endDate, andClause);

        return calculateReopenFactor(credentials, issueList, projects, startDate, endDate, andClause, threshold);
    }

}
