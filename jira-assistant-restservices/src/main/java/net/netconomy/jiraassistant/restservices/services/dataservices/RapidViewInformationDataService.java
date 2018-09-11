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
package net.netconomy.jiraassistant.restservices.services.dataservices;

import com.atlassian.jira.rest.client.api.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.services.RapidViewInformationService;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.restservices.services.JsonAnswerWrapper;

@Service
public class RapidViewInformationDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidViewInformationDataService.class);

    @Autowired
    ConfigurationService configuration;

    @Autowired
    JsonAnswerWrapper jsonAnswerWrapper;

    @Autowired
    RapidViewInformationService rapidViewInformationService;

    /**
     * Returns available Rapid Views, when withSprintSupportEnabledString is null all are returned. If
     * withSprintSupportEnabledString is 'true' or 'True' only SprintViews are returned, otherwise only non-SprintViews
     * are returned.
     * 
     * @param withSprintSupportEnabledString
     * @return
     */
    public String getAllRapidViewsJSON(String withSprintSupportEnabledString) {

        ClientCredentials credentials;
        Boolean withSprintSupportEnabled;

        try {

            credentials = configuration.getClientCredentials();

            if (withSprintSupportEnabledString != null) {
                withSprintSupportEnabled = Boolean.parseBoolean(withSprintSupportEnabledString);

                return jsonAnswerWrapper.wrapObjectForJSONOutput(rapidViewInformationService.getAllRapidViews(
                        credentials, withSprintSupportEnabled));
            } else {
                return jsonAnswerWrapper.wrapObjectForJSONOutput(rapidViewInformationService
                        .getAllRapidViews(credentials));
            }

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    public String getAllSprintsForRapidViewJSON(String rapidViewId) {

        ClientCredentials credentials;

        try {

            credentials = configuration.getClientCredentials();

            return jsonAnswerWrapper.wrapObjectForJSONOutput(rapidViewInformationService.getSprintsForRapidView(
                    credentials, rapidViewId));

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

    public String getAllSprintsForRapidViewAfterDateJSON(String rapidViewId, String startDateString) {

        ClientCredentials credentials;

        try {

            credentials = configuration.getClientCredentials();

            return jsonAnswerWrapper.wrapObjectForJSONOutput(rapidViewInformationService
                    .getSprintsForRapidViewCompletedAfterDate(credentials,
                    rapidViewId, startDateString));

        } catch (JiraAssistantException|RestClientException e) {

            LOGGER.warn(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return jsonAnswerWrapper.wrapJsonForRest("{}", e);

        }

    }

}
