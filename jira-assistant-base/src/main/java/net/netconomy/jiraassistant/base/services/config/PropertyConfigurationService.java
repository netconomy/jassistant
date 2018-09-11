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
package net.netconomy.jiraassistant.base.services.config;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

import net.netconomy.jiraassistant.base.data.config.ProjectSetupConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netconomy.jiraassistant.base.data.config.BillingConfiguration;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ForecastConfiguration;
import net.netconomy.jiraassistant.base.data.config.IssueConfiguration;
import net.netconomy.jiraassistant.base.data.config.PerformanceConfiguration;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;

public class PropertyConfigurationService implements ConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyConfigurationService.class);

    private static final String FILEPATH_PROPERTY = "jassistant.config.file";

    private static final String FILENAME = "jassistant.properties";

    PropertiesConfiguration config = null;

    private synchronized void loadConfig(Path configPath) throws ConfigurationException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Trying to load Configuration from {}", configPath.toString());
        }
        final PropertiesConfiguration cfg = new PropertiesConfiguration();
        cfg.load(configPath.toFile());
        this.config = cfg;
    }

    /**
     * loadProperties tries to load the configuration file first from the FILEPATH_PRORPERTY path, then from the
     * current working directory and finally from within the catalina.base.
     *
     * @throws ConfigurationException
     */
    private void loadProperties() throws ConfigurationException {
        if (this.config != null) {
            return;
        }

        String filePath = System.getProperty(FILEPATH_PROPERTY);
        if (!StringUtils.isEmpty(filePath)) {
            this.loadConfig(FileSystems.getDefault().getPath(filePath));
            return;
        }

        final String catalinaBase = System.getProperty("catalina.base");
        Path catalinaPath = FileSystems.getDefault().getPath(catalinaBase, "conf", FILENAME);
        Path localPath = FileSystems.getDefault().getPath(FILENAME);

        if (localPath.toFile().exists()) {
            this.loadConfig(localPath);
            return;
        }

        if (catalinaBase != null) {
            this.loadConfig(catalinaPath);
            return;
        }

        throw new ConfigurationException("No configuration file found");
    }

    String getProperty(String propertyName) throws ConfigurationException {

        String returnProperty = "";

        loadProperties();

        returnProperty = config.getString(propertyName);

        return returnProperty;

    }

    String[] getPropertyArray(String propertyName) throws ConfigurationException {

        String[] returnPropertyArray;

        loadProperties();

        returnPropertyArray = config.getStringArray(propertyName);

        return returnPropertyArray;

    }

    @Override
    public ClientCredentials getClientCredentials() throws ConfigurationException {

        ClientCredentials credentials = new ClientCredentials();

        credentials.setJiraUri(getProperty("jiraUri"));
        credentials.setUserName(getProperty("user"));
        credentials.setPassword(getProperty("password"));
        credentials.setTimeZone(getProperty("timeZone"));

        return credentials;
    }

    @Override
    public ProjectConfiguration getProjectConfiguration() throws ConfigurationException {

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        projectConfiguration.setEstimationFieldName(getProperty("estimationFieldName"));
        projectConfiguration.setAltEstimationFieldName(getProperty("altEstimationFieldName"));
        projectConfiguration.setResolutionDateFieldName(getProperty("resolutionDateFieldName"));
        projectConfiguration.setSprintFieldName(getProperty("sprintFieldName"));
        projectConfiguration.setDefectReasonFieldName(getProperty("defectReasonFieldName"));
        projectConfiguration.setDefectReasonInfoFieldName(getProperty("defectReasonInfoFieldName"));
        projectConfiguration.setFlaggedFieldName(getProperty("flaggedFieldName"));
        projectConfiguration.setFlaggedFieldValue(getProperty("flaggedFieldValue"));

        projectConfiguration.setReopenedStatus(Arrays.asList(getPropertyArray("reopenedStatus")));
        projectConfiguration.setTestedStatus(Arrays.asList(getPropertyArray("testedStatus")));
        projectConfiguration.setRejectedStatus(Arrays.asList(getPropertyArray("rejectedStatus")));
        projectConfiguration.setTestableStatus(Arrays.asList(getPropertyArray("testableStatus")));
        projectConfiguration.setInTestingStatus(Arrays.asList(getPropertyArray("inTestingStatus")));
        projectConfiguration.setDefectTriageStatus(Arrays.asList(getPropertyArray("defectTriageStatus")));
        projectConfiguration.setStoryIssueTypes(Arrays.asList(getPropertyArray("storyIssueTypes")));
        projectConfiguration.setDefectIssueTypes(Arrays.asList(getPropertyArray("defectIssueTypes")));
        projectConfiguration.setBugIssueTypes(Arrays.asList(getPropertyArray("bugIssueTypes")));
        projectConfiguration.setInProgressStatus(Arrays.asList(getPropertyArray("inProgressStatus")));
        projectConfiguration.setWaitingStatus(Arrays.asList(getPropertyArray("waitingStatus")));
        projectConfiguration.setImplementedStatus(Arrays.asList(getPropertyArray("implementedStatus")));
        projectConfiguration.setFinishedStatus(Arrays.asList(getPropertyArray("finishedStatus")));
        projectConfiguration.setLiveStatus(Arrays.asList(getPropertyArray("liveStatus")));
        projectConfiguration.setClosedStatus(Arrays.asList(getPropertyArray("closedStatus")));

        return projectConfiguration;
    }

    @Override
    public ForecastConfiguration getForecastConfiguration() throws ConfigurationException {

        ForecastConfiguration forecastConfiguration = new ForecastConfiguration();

        forecastConfiguration.setIgnoreIssueTypes(Arrays.asList(getPropertyArray("ignoreIssueTypesForecast")));

        return forecastConfiguration;
    }

    @Override
    public IssueConfiguration getIssueConfiguration() throws ConfigurationException {

        IssueConfiguration issueConfiguration = new IssueConfiguration();

        issueConfiguration.setWantedFields(Arrays.asList(getPropertyArray("wantedFields")));

        return issueConfiguration;
    }

    @Override
    public PerformanceConfiguration getPerformanceConfiguration() throws ConfigurationException {
        
        PerformanceConfiguration performanceConfiguration = new PerformanceConfiguration();
        
        Integer maxKanbanResults;
        Integer maxSprintsMultiSprintAnalysis;

        try {
            maxKanbanResults = Integer.valueOf(getProperty("maxKanbanResults"));
            maxSprintsMultiSprintAnalysis = Integer.valueOf(getProperty("maxSprintsMultiSprintAnalysis"));
        } catch (NumberFormatException e) {
            throw new ConfigurationException(
                    "Illegal Value in Configuration for maxKanbanResults or maxSprintsMultiSprintAnalysis. "
                            + "Values have to be Integers.", e);
        }
        
        performanceConfiguration.setMaxKanbanResults(maxKanbanResults);
        performanceConfiguration.setMaxSprintsMultiSprintAnalysis(maxSprintsMultiSprintAnalysis);

        return performanceConfiguration;
    }

    @Override
    public SupportConfiguration getSupportConfiguration() throws ConfigurationException {

        SupportConfiguration supportConfiguration = new SupportConfiguration();

        supportConfiguration.setBillableSupportFieldName(getProperty("billableSupportFieldName"));
        supportConfiguration.setReactionSlaFieldName(getProperty("reactionSlaFieldName"));
        supportConfiguration.setInteractionSlaFieldName(getProperty("interactionSlaFieldName"));
        supportConfiguration.setSolutionSlaFieldName(getProperty("solutionSlaFieldName"));
        supportConfiguration.setErrorCategoryFieldName(getProperty("errorCategoryFieldName"));
        supportConfiguration.setTechnicalSeverityFieldName(getProperty("technicalSeverityFieldName"));

        supportConfiguration.setReactionTimeEndStatus(Arrays.asList(getPropertyArray("reactionTimeEndStatus")));
        supportConfiguration.setInteractionTimeStartStatus(Arrays
                .asList(getPropertyArray("interactionTimeStartStatus")));
        supportConfiguration.setInteractionTimeEndStatus(Arrays.asList(getPropertyArray("interactionTimeEndStatus")));
        supportConfiguration.setSolutionTimeStartStatus(Arrays.asList(getPropertyArray("solutionTimeStartStatus")));
        supportConfiguration.setSolutionTimeEndStatus(Arrays.asList(getPropertyArray("solutionTimeEndStatus")));
        supportConfiguration.setOpenStatus(Arrays.asList(getPropertyArray("openStatus")));
        supportConfiguration.setSolvedStatus(Arrays.asList(getPropertyArray("solvedStatus")));

        return supportConfiguration;
    }

    @Override
    public BillingConfiguration getBillingConfiguration() throws ConfigurationException {

        BillingConfiguration billingConfiguration = new BillingConfiguration();

        // TODO fill billingConfiguration here

        return billingConfiguration;
    }

    @Override
    public ProjectSetupConfiguration getProjectSetupConfiguration() throws ConfigurationException {
        return new ProjectSetupConfiguration();
    }

}
