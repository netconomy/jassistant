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

import net.netconomy.jiraassistant.base.data.config.ProjectSetupConfiguration;
import org.apache.commons.configuration.ConfigurationException;

import net.netconomy.jiraassistant.base.data.config.BillingConfiguration;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ForecastConfiguration;
import net.netconomy.jiraassistant.base.data.config.IssueConfiguration;
import net.netconomy.jiraassistant.base.data.config.PerformanceConfiguration;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;

public interface ConfigurationService {

    public ClientCredentials getClientCredentials() throws ConfigurationException;

    public ProjectConfiguration getProjectConfiguration() throws ConfigurationException;

    public ForecastConfiguration getForecastConfiguration() throws ConfigurationException;

    public IssueConfiguration getIssueConfiguration() throws ConfigurationException;

    public PerformanceConfiguration getPerformanceConfiguration() throws ConfigurationException;

    public SupportConfiguration getSupportConfiguration() throws ConfigurationException;

    public BillingConfiguration getBillingConfiguration() throws ConfigurationException;

    public ProjectSetupConfiguration getProjectSetupConfiguration() throws ConfigurationException;

}
