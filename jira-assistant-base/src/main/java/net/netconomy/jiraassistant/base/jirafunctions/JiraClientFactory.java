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
package net.netconomy.jiraassistant.base.jirafunctions;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@Service
public class JiraClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraClientFactory.class);

    /**
     * Creates a JiraRestClient to the given URI with the given user credentials. The Client has to be closed when its
     * is no longer needed.
     * 
     * @param credentials
     * @return
     * @throws URISyntaxException
     */
    public JiraRestClient create(ClientCredentials credentials) throws URISyntaxException {

        final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        final URI jiraServerUri = new URI(credentials.getJiraUri());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating REST Client for Server '{}' with User '{}'",
                    credentials.getJiraUri(), credentials.getUserName());
        }

        final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri,
                credentials.getUserName(), credentials.getPassword());

        return restClient;
    }

}
