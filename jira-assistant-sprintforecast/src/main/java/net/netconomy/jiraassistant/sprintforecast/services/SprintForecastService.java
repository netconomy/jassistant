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
package net.netconomy.jiraassistant.sprintforecast.services;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.sprintforecast.data.Forecast;
import net.netconomy.jiraassistant.sprintforecast.data.ForecastedSprint;

@Service
public class SprintForecastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SprintForecastService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    BacklogFilterService backlogFilterService;

    @Autowired
    ForecastBacklogFilterService forecastBacklogFilterService;

    @Autowired
    JiraSearch jiraSearch;

    @Autowired
    AdvancedIssueService advancedIssueService;

    /**
     * Generate a Forecast based on the given Filter with the given Velocity for the given number of Sprints. The light
     * Issues will contain the wanted Fields, if they are not empty.
     * 
     * @param credentials
     * @param backlogFilter
     * @param forecastedVelocityString
     * @param numberOfSprintsString
     * @param wantedFields
     * @return
     * @throws ConfigurationException
     */
    Forecast generateForecast(ClientCredentials credentials, String backlogFilter, String forecastedVelocityString,
            String numberOfSprintsString, List<String> wantedFields, String estimationFieldName)
            throws ConfigurationException {

        Forecast forecast = new Forecast();
        List<Issue> backlogIssues;
        ForecastedSprint currentSprintForecast = new ForecastedSprint();
        Double forecastedVelocity = Double.parseDouble(forecastedVelocityString);
        Integer numberOfSprints = Integer.parseInt(numberOfSprintsString);
        Double storyPointCount = 0.0;
        Integer maxResults = 50;
        Integer searchStartAt = 0;
        Integer sprintCount = 0;
        Double currentEstimation;

        forecast.setUsedFilter(backlogFilter);
        forecast.setNumberOfForecastedSprints(numberOfSprints);
        forecast.setForecastVelocity(forecastedVelocity);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("generating Forecast based on the following Filter: {}", backlogFilter);
        }

        backlogIssues = jiraSearch.searchJira(credentials, backlogFilter, maxResults, searchStartAt, null);

        forecast.setForeCastDate(DateTime.now().toString());

        while (!backlogIssues.isEmpty()) {

            for (Issue currentIssue : backlogIssues) {

                currentEstimation = advancedIssueService.getEstimation(currentIssue, estimationFieldName);

                // Issues without Estimation or bigger then the forecasted Velocity are ignored
                if (currentEstimation.compareTo(0.0) == 0 || (currentEstimation > forecastedVelocity)) {
                    forecast.addIgnoredIssue(advancedIssueService.getIssueLight(currentIssue, wantedFields));
                    continue;
                }

                // Is the forecasted Sprint full?
                if (forecastedVelocity < (storyPointCount + currentEstimation)) {

                    forecast.addForecastedSprint(currentSprintForecast);

                    // Do we have enough Forecasts? Sprint Count starts at 0
                    if (numberOfSprints <= sprintCount + 1) {
                        return forecast;
                    } else {
                        currentSprintForecast = new ForecastedSprint();
                        storyPointCount = 0.0;
                        sprintCount++;
                    }
                }



                currentSprintForecast.addIssueToForecastedSprint(advancedIssueService.getIssueLight(currentIssue,
                        wantedFields));

                storyPointCount += currentEstimation;

            }

            searchStartAt += maxResults;

            backlogIssues = jiraSearch.searchJira(credentials, backlogFilter, maxResults, searchStartAt, null);

        }

        return forecast;
    }

    /**
     * Generate a Forecast based on the given Filter with the given Velocity for the given number of Sprints. The given
     * Filter will be modified to be ordered by Rank. The light Issues will contain the wanted Fields, if they are not
     * empty. The field name for the Estimation is taken from the Properties File.
     * 
     * @param credentials
     * @param givenFilter
     * @param forecastedVelocity
     * @param numberOfSprints
     * @param wantedFields
     * @return
     * @throws ConfigurationException
     */
    public Forecast generateForecastBasedOnFilter(ClientCredentials credentials, String givenFilter,
            String forecastedVelocity, String numberOfSprints, List<String> wantedFields) throws ConfigurationException {

        String estimationFieldName = configuration.getProjectConfiguration().getEstimationFieldName();

        String backlogFilter = backlogFilterService.generateBacklogFilterBasedOnFilter(givenFilter);

        return generateForecast(credentials, backlogFilter, forecastedVelocity, numberOfSprints, wantedFields,
                estimationFieldName);

    }

    /**
     * Generate a Forecast based on the given Projects with the given Velocity for the given number of Sprints. The
     * light Issues will contain the wanted Fields, if they are not empty. The field name for the Estimation is taken
     * from the Properties File.
     * 
     * @param credentials
     * @param projects
     * @param forecastedVelocity
     * @param numberOfSprints
     * @param wantedFields
     * @return
     * @throws ConfigurationException
     */
    public Forecast generateForecastBasedOnProjects(ClientCredentials credentials, List<String> projects,
            String andClause,
            String forecastedVelocity, String numberOfSprints, List<String> wantedFields) throws ConfigurationException {

        String estimationFieldName = configuration.getProjectConfiguration().getEstimationFieldName();

        String backlogFilter = forecastBacklogFilterService.generateBacklogFilterBasedOnProjects(projects, andClause);

        return generateForecast(credentials, backlogFilter, forecastedVelocity, numberOfSprints, wantedFields,
                estimationFieldName);

    }

}
