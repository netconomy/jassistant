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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.services.DataConversionService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;

@Service
public class EstimationStatisticsFilterService {

    @Autowired
    private ConfigurationService configuration;
    
    @Autowired
    private DataConversionService dataConversionService;

    String createFilterForProjectIssueTypeAndStatusList(List<String> projectsList, String issueType,
            List<String> statusList,
            DateTime startDate, DateTime endDate, String andClause) {
     
        StringBuilder generatedFilter = new StringBuilder();
        StringBuilder finishedStatusString = new StringBuilder();
        Joiner joiner = Joiner.on("','");
        
        for (String currentStatus : statusList) {

            if (finishedStatusString.length() != 0) {
                finishedStatusString.append(",");
            }

            finishedStatusString.append("'" + currentStatus + "'");
        }
        
        generatedFilter.append("project in ('");

        joiner.appendTo(generatedFilter, projectsList);

        generatedFilter.append("')");
        generatedFilter.append(" and issuetype = '" + issueType + "'");
        generatedFilter.append(" and status in (" + finishedStatusString.toString() + ")");
        generatedFilter.append(" and resolutiondate >= '" + dataConversionService.convertDateTimeToJQLDate(startDate)
                + "'");
        generatedFilter.append(" and resolutiondate <= '" + dataConversionService.convertDateTimeToJQLDate(endDate)
                + "'");
        
        if (andClause != null && !andClause.trim().isEmpty()) {
            generatedFilter.append(" and (" + andClause + ")");
        }

        return generatedFilter.toString();
        
    }
    
    String createFilterForProjectIssueTypeStatusListAndEstimation(List<String> projects, String issueType,
            List<String> statusList, Double estimation, String estimationFieldName, DateTime startDate,
            DateTime endDate, String andClause) {

        StringBuilder generatedFilter = new StringBuilder(createFilterForProjectIssueTypeAndStatusList(projects,
                issueType, statusList, startDate, endDate, andClause));

        generatedFilter.append(" and '" + estimationFieldName + "' = " + estimation);

        return generatedFilter.toString();

    }

    /**
     * Creates a Filter for finished Issues in the given Project with the given IssueType and resolved during the given
     * Time Window. What is finished is defined by the Configuration.
     * 
     * @param project
     * @param issueType
     * @param startDate
     * @param endDate
     * @param andClause
     * @return
     * @throws ConfigurationException
     */
    public String filterForFinishedIssuesByType(List<String> projects, String issueType, DateTime startDate,
            DateTime endDate,
            String andClause) throws ConfigurationException {

        List<String> finishedStatus = configuration.getProjectConfiguration().getFinishedStatus();

        return createFilterForProjectIssueTypeAndStatusList(projects, issueType, finishedStatus, startDate, endDate,
                andClause);
    }

    /**
     * Creates a Filter for finished Issues in the given Project with the given IssueType and Estimation and resolved
     * during the given Time Window. What is finished is defined by the Configuration.
     * 
     * @param project
     * @param issueType
     * @param estimation
     * @param startDate
     * @param endDate
     * @param andClause
     * @return
     * @throws ConfigurationException
     */
    public String filterForFinishedIssuesByTypeAndEstimation(List<String> projects, String issueType,
            Double estimation,
            DateTime startDate, DateTime endDate, String andClause)
            throws ConfigurationException {

        List<String> finishedStatus = configuration.getProjectConfiguration().getFinishedStatus();

        String estimationFieldName = configuration.getProjectConfiguration().getEstimationFieldName();

        return createFilterForProjectIssueTypeStatusListAndEstimation(projects, issueType, finishedStatus, estimation,
                estimationFieldName, startDate, endDate, andClause);
    }

}
