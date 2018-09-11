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
package net.netconomy.jiraassistant.estimationstatistics.services;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.estimationstatistics.data.EstimationStatistics;
import net.netconomy.jiraassistant.estimationstatistics.data.IssueTypeEstimationStatisticsData;

@Service
public class EstimationStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstimationStatisticsService.class);

    @Autowired
    private IssueTypeEstimationStatisticsService issueTypeEstimationStatisticsService;

    @Autowired
    private ConfigurationService configuration;

    /**
     * Calculate the Estimation Statistics for the given IssueTypes, in the given Project for Issues changed during the
     * given Time Window.
     * 
     * @param credentials
     * @param projects
     * @param issueTypes
     * @param startDate
     * @param endDate
     * @return
     * @throws ConfigurationException
     */
    public EstimationStatistics calculateEstimationStatistics(ClientCredentials credentials, List<String> projects,
            List<String> issueTypes, DateTime startDate, DateTime endDate, Boolean altEstimations, String andClause)
            throws ConfigurationException {

        EstimationStatistics estimationStatistics = new EstimationStatistics(projects.toString(), DateTime.now()
                .toString(),
                startDate.toString(), endDate.toString(), andClause);
        IssueTypeEstimationStatisticsData currentIssueTypeEstimationStatistics;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Calculating Estimation Statistics for IssueTypes: {}", issueTypes.toString());
        }

        for (String currentType : issueTypes) {
            currentIssueTypeEstimationStatistics = issueTypeEstimationStatisticsService
                    .calculateIssueTypeEstimationStatistics(credentials, projects, currentType, startDate, endDate,
                            altEstimations, andClause);

            estimationStatistics.addEstimationStatisticsForIssueType(currentType, currentIssueTypeEstimationStatistics);
        }

        estimationStatistics.setInProgressStatus(configuration.getProjectConfiguration().getInProgressStatus());
        estimationStatistics.setFinishedStatus(configuration.getProjectConfiguration().getFinishedStatus());

        return estimationStatistics;

    }

}
