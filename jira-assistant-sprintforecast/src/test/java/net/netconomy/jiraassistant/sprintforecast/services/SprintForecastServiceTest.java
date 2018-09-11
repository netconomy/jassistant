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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.jirafunctions.JiraSearch;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.sprintforecast.SprintForecastTestDIConfiguration;
import net.netconomy.jiraassistant.sprintforecast.data.Forecast;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { SprintForecastTestDIConfiguration.class })
public class SprintForecastServiceTest {

    @Autowired
    private SprintForecastService sprintForecastService;

    @Autowired
    @ReplaceWithMock
    private JiraSearch mockedJiraSearch;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    private String backlogFilter;
    private String estimationFieldName;

    private List<Issue> issueList = new ArrayList<>();
    private List<String> wantedFields = new ArrayList<>();

    private ClientCredentials testCredentials = new ClientCredentials();

    private Issue mockedIssue1 = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);
    private Issue mockedIssue4 = mock(Issue.class);
    private Issue mockedIssue5 = mock(Issue.class);
    private Issue mockedIssue6 = mock(Issue.class);
    private Issue mockedIssue7 = mock(Issue.class);

    private IssueLight mockedIssueLight1 = mock(IssueLight.class);
    private IssueLight mockedIssueLight2 = mock(IssueLight.class);
    private IssueLight mockedIssueLight3 = mock(IssueLight.class);
    private IssueLight mockedIssueLight4 = mock(IssueLight.class);
    private IssueLight mockedIssueLight5 = mock(IssueLight.class);
    private IssueLight mockedIssueLight6 = mock(IssueLight.class);
    private IssueLight mockedIssueLight7 = mock(IssueLight.class);

    @Before
    public void setUp() throws Exception {

        backlogFilter = "Testfilter";
        estimationFieldName = "Story Points";

        testCredentials.setJiraUri("testserver");
        testCredentials.setUserName("testuser");
        testCredentials.setPassword("testuser");

        issueList.add(mockedIssue1);
        issueList.add(mockedIssue2);
        issueList.add(mockedIssue3);
        issueList.add(mockedIssue4);
        issueList.add(mockedIssue5);
        issueList.add(mockedIssue6);
        issueList.add(mockedIssue7);

        when(mockedJiraSearch.searchJira(testCredentials, backlogFilter, 50, 0, null)).thenReturn(issueList);

        when(mockedAdvancedIssueService.getIssueLight(mockedIssue1, wantedFields)).thenReturn(mockedIssueLight1);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue2, wantedFields)).thenReturn(mockedIssueLight2);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue3, wantedFields)).thenReturn(mockedIssueLight3);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue4, wantedFields)).thenReturn(mockedIssueLight4);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue5, wantedFields)).thenReturn(mockedIssueLight5);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue6, wantedFields)).thenReturn(mockedIssueLight6);
        when(mockedAdvancedIssueService.getIssueLight(mockedIssue7, wantedFields)).thenReturn(mockedIssueLight7);

    }

    @Test
    public void generateForecastCorrectSpreadTest() {

        Forecast actualForecast;
        Double sprintEstimation = 0.0;

        when(mockedAdvancedIssueService.getEstimation(mockedIssue1, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue2, estimationFieldName)).thenReturn(4.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue3, estimationFieldName)).thenReturn(5.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue4, estimationFieldName)).thenReturn(4.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue5, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue6, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue7, estimationFieldName)).thenReturn(3.0);

        when(mockedIssueLight1.getFieldByName(estimationFieldName)).thenReturn("3.0");
        when(mockedIssueLight2.getFieldByName(estimationFieldName)).thenReturn("4.0");
        when(mockedIssueLight3.getFieldByName(estimationFieldName)).thenReturn("5.0");
        when(mockedIssueLight4.getFieldByName(estimationFieldName)).thenReturn("4.0");
        when(mockedIssueLight5.getFieldByName(estimationFieldName)).thenReturn("3.0");
        when(mockedIssueLight6.getFieldByName(estimationFieldName)).thenReturn("3.0");
        when(mockedIssueLight7.getFieldByName(estimationFieldName)).thenReturn("3.0");

        try {
            actualForecast = sprintForecastService.generateForecast(testCredentials, backlogFilter, "12", "2",
                    wantedFields, estimationFieldName);

            assertEquals("Number of forcasted Sprints is not correct", 2, actualForecast.getForecastedSprints().size());

            for (IssueLight currentIssue : actualForecast.getForecastedSprints().get(0).getForecastedIssues()) {
                sprintEstimation += Double.parseDouble(currentIssue.getFieldByName(estimationFieldName));
            }

            assertEquals("The forcasted Sprint was not in the given Boundries.", Double.valueOf(12.0), sprintEstimation);
            sprintEstimation = 0.0;

            for (IssueLight currentIssue : actualForecast.getForecastedSprints().get(1).getForecastedIssues()) {
                sprintEstimation += Double.parseDouble(currentIssue.getFieldByName(estimationFieldName));
            }

            assertEquals("The forcasted Sprint was not in the given Boundries.", Double.valueOf(10.0), sprintEstimation);

        } catch (ConfigurationException e) {
            fail("Exception was thrown during Test.");
        }

    }

    @Test
    public void generateForecastCorrectIgnoredTest() {

        Forecast actualForecast;

        when(mockedAdvancedIssueService.getEstimation(mockedIssue1, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue2, estimationFieldName)).thenReturn(4.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue3, estimationFieldName)).thenReturn(5.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue4, estimationFieldName)).thenReturn(0.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue5, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue6, estimationFieldName)).thenReturn(3.0);
        when(mockedAdvancedIssueService.getEstimation(mockedIssue7, estimationFieldName)).thenReturn(0.0);

        try {

            actualForecast = sprintForecastService.generateForecast(testCredentials, backlogFilter, "12", "2",
                    wantedFields, estimationFieldName);

            assertEquals("Number of ignored Issues was not correct.", 2, actualForecast.getIgnoredIssues().size());

            assertTrue("Expected Issue was not in ignored Issues.",
                    actualForecast.getIgnoredIssues().contains(mockedIssueLight4));

            assertTrue("Expected Issue was not in ignored Issues.",
                    actualForecast.getIgnoredIssues().contains(mockedIssueLight7));

        } catch (ConfigurationException e) {
            fail("Exception was thrown during Test.");
        }
    }

    @After
    public void tearDown() throws Exception {
    }

}
