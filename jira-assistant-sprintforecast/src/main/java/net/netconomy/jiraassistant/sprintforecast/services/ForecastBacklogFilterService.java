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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.BacklogFilterService;

@Service
public class ForecastBacklogFilterService {

    @Autowired
    ConfigurationService configuration;

    @Autowired
    BacklogFilterService backlogFilterService;

    /**
     * generate Filter based on the given Projects, the status and issue types to exclude are taken from the properties
     * 
     * @param projectsString
     * @param andClause
     * @return
     * @throws ConfigurationException
     * @throws Exception
     */
    public String generateBacklogFilterBasedOnProjects(List<String> projects, String andClause)
            throws ConfigurationException {

        List<String> excludedStatus = new ArrayList<>();

        List<String> excludedIssueTypes = configuration.getForecastConfiguration().getIgnoreIssueTypes();
        List<String> implementedStatus = configuration.getProjectConfiguration().getImplementedStatus();
        List<String> finishedStatus = configuration.getProjectConfiguration().getFinishedStatus();
        List<String> closedStatus = configuration.getProjectConfiguration().getClosedStatus();

        excludedStatus.addAll(implementedStatus);
        excludedStatus.addAll(finishedStatus);
        excludedStatus.addAll(closedStatus);

        return backlogFilterService.generateBacklogFilterBasedOnProjects(projects, andClause, null,
                excludedStatus, excludedIssueTypes, true, true);
    }

}
