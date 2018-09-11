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
package net.netconomy.jiraassistant.base.restclient;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class JiraRestUrlBuilderTest {

    @Autowired
    private JiraRestUrlBuilder jiraRestUrlBuilder;
    
    private ClientCredentials clientCredentials = new ClientCredentials();

    private static final String BASICFIELDS = "summary,issuetype,created,updated,project,status,priority,created";

    @Before
    public void setUp() throws Exception {

        clientCredentials.setJiraUri("https://jira.company.com/");

    }

    @Test
    public void constructSearchUrlTestNullFields() {

        Integer startAt = 0;
        Integer maxResults = 50;
        String givenFilter = "project in (PROJ1, PROJ2, PROJ3) AND type not in (Test, Tempo) order by Rank asc";

        String expectedUrl = "https://jira.company.com/rest/api/latest/search?jql=" + givenFilter + "&startAt="
                + startAt + "&maxResults=" + maxResults + "&fields=" + BASICFIELDS + "&expand=changelog";
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructSearchUrl(clientCredentials, givenFilter, startAt, maxResults, null,
                null);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructSearchUrlTestWithFields() {

        Integer startAt = 0;
        Integer maxResults = 50;
        String fields = "customfield_10002";
        String givenFilter = "project in (PROJ1, PROJ2, PROJ3) AND type not in (Test, Tempo) order by Rank asc";

        String expectedUrl = "https://jira.company.com/rest/api/latest/search?jql=" + givenFilter + "&startAt="
                + startAt + "&maxResults=" + maxResults + "&fields=" + BASICFIELDS + "," + fields + "&expand=changelog";
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructSearchUrl(clientCredentials, givenFilter, startAt, maxResults, fields,
                null);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructSearchUrlTestWithExpand() {

        Integer startAt = 0;
        Integer maxResults = 50;
        String expand = "hugo";
        String givenFilter = "project in (PROJ1, PROJ2, PROJ3) AND type not in (Test, Tempo) order by Rank asc";

        String expectedUrl = "https://jira.company.com/rest/api/latest/search?jql=" + givenFilter + "&startAt="
                + startAt + "&maxResults=" + maxResults + "&fields=" + BASICFIELDS + "&expand=" + expand;
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructSearchUrl(clientCredentials, givenFilter, startAt, maxResults, null,
                expand);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructSearchUrlTestOnlyFilter() {

        Integer startAt = 0;
        Integer maxResults = 50;
        String givenFilter = "project in (PROJ1, PROJ2, PROJ3) AND type not in (Test, Tempo) order by Rank asc";

        String expectedUrl = "https://jira.company.com/rest/api/latest/search?jql=" + givenFilter + "&startAt="
                + startAt + "&maxResults=" + maxResults + "&expand=changelog";
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructSearchUrl(clientCredentials, givenFilter, startAt, maxResults);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructWorklogsUrlTest() {

        String issueKey = "TST-1";

        String expectedUrl = "https://jira.company.com/rest/api/latest/issue/" + issueKey + "/worklog";
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructWorklogsUrl(clientCredentials, issueKey);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructIssueUrlTest() {

        String issueKey = "TST-1";

        String expectedUrl = "https://jira.company.com/rest/api/latest/issue/" + issueKey;
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructIssueUrl(clientCredentials, issueKey);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @Test
    public void constructPriorityUrlTest() {

        String expectedUrl = "https://jira.company.com/rest/api/latest/priority";
        String actualUrl;

        actualUrl = jiraRestUrlBuilder.constructPriorityUrl(clientCredentials);

        assertEquals("generated URL did not match Expectations", expectedUrl, actualUrl);

    }

    @After
    public void tearDown() throws Exception {
    }

}
