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
package net.netconomy.jiraassistant.kanbananalysis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
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

import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.kanbananalysis.KanbanAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.kanbananalysis.data.KanbanMetrics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { KanbanAnalysisTestDIConfiguration.class })
public class KanbanMetricsServiceTest {

    @Autowired
    KanbanMetricsService kanbanMetricsService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private Issue mockedIssue = mock(Issue.class);
    private ProjectConfiguration mockedConfiguration = mock(ProjectConfiguration.class);
    private String resolutionDateFieldName = "fieldName";
    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> liveStatus = new ArrayList<>();
    private String firstWaitingStatus = "firstWaiting";
    private String secondWaitingStatus = "secondWaiting";
    private List<String> waitingStatus = new ArrayList<>();
    private String altEstimationFieldName = "altEstimations";

    @Before
    public void setUp() throws Exception {

        inProgressStatus.add("firstInProgress");
        inProgressStatus.add("secondInProgress");

        waitingStatus.add(firstWaitingStatus);
        waitingStatus.add(secondWaitingStatus);

        liveStatus.add("Live Deployed");

        when(mockedConfiguration.getResolutionDateFieldName()).thenReturn(resolutionDateFieldName);
        when(mockedConfiguration.getInProgressStatus()).thenReturn(inProgressStatus);
        when(mockedConfiguration.getWaitingStatus()).thenReturn(waitingStatus);
        when(mockedConfiguration.getLiveStatus()).thenReturn(liveStatus);
        when(mockedConfiguration.getAltEstimationFieldName()).thenReturn(altEstimationFieldName);

        when(mockedConfigurationService.getProjectConfiguration()).thenReturn(mockedConfiguration);

    }

    @Test
    public void getCycleTimeTest() {

        DateTime firstInProgressDate = new DateTime(2015, 1, 1, 20, 0, 0);
        DateTime resolutionDate = new DateTime(2015, 1, 14, 23, 0, 0);
        Duration actualDuration;
        Duration expectedDuration = new Duration(firstInProgressDate, resolutionDate);

        try {

            when(mockedHistoryIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, inProgressStatus))
                    .thenReturn(firstInProgressDate);
            when(mockedHistoryIssueService.getResolutionDate(mockedIssue, resolutionDateFieldName)).thenReturn(
                    resolutionDate);

            actualDuration = kanbanMetricsService.getCycleTime(mockedIssue);

            assertEquals(expectedDuration, actualDuration);
        
        } catch (Exception e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }

    }

    @Test
    public void getLeadTimeTest() {

        DateTime creationDate = new DateTime(2015, 1, 1, 20, 0, 0);
        DateTime liveDate = new DateTime(2015, 1, 14, 23, 0, 0);
        Duration actualDuration;
        Duration expectedDuration = new Duration(creationDate, liveDate);

        try {

            when(mockedIssue.getCreationDate()).thenReturn(creationDate);
            when(mockedHistoryIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, liveStatus))
                .thenReturn(liveDate);

            actualDuration = kanbanMetricsService.getLeadTime(mockedIssue);

            assertEquals(expectedDuration, actualDuration);

        } catch (Exception e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }

    }

    @Test
    public void getWaitingTimeTest() {

        DateTime creationDate = new DateTime(2015, 1, 1, 20, 0, 0);
        DateTime endDate = new DateTime(2015, 1, 14, 23, 0, 0);
        DateTime firstDurationEndDate = new DateTime(2015, 1, 3, 20, 0, 0);
        DateTime secondDurationStartDate = new DateTime(2015, 1, 12, 20, 0, 0);
        Duration firstDuration = new Duration(creationDate, firstDurationEndDate);
        Duration secondDuration = new Duration(secondDurationStartDate, endDate);
        Duration actualDuration;
        Duration expectedDuration = new Duration(0);

        try {

            expectedDuration = expectedDuration.plus(firstDuration);
            expectedDuration = expectedDuration.plus(secondDuration);

            when(mockedIssue.getCreationDate()).thenReturn(creationDate);
            when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue, "status", firstWaitingStatus, creationDate,
                endDate)).thenReturn(firstDuration);
            when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue, "status", secondWaitingStatus, creationDate,
                endDate)).thenReturn(secondDuration);

            actualDuration = kanbanMetricsService.getWaitingTime(mockedIssue, endDate);

            assertEquals(expectedDuration, actualDuration);

        } catch (Exception e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }

    }

    @Test
    public void getIssuesByAltEstimationTestEmpty() {

        List<Issue> emptyList = new ArrayList<>();
        Map<String, List<Issue>> issueMap;

        try {

            issueMap = kanbanMetricsService.getIssuesByAltEstimation(emptyList, true);

            assertTrue(issueMap.size() == 0);

        } catch (Exception e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }

    }

    @Test
    public void getIssuesByAltEstimationTest() {

        List<Issue> issueList = new ArrayList<>();
        Map<String, List<Issue>> issueMap;

        Issue mockedIssue1 = mock(Issue.class);
        Issue mockedIssue2 = mock(Issue.class);
        Issue mockedIssue3 = mock(Issue.class);
        Issue mockedIssue4 = mock(Issue.class);
        Issue mockedIssue5 = mock(Issue.class);
        Issue mockedIssue6 = mock(Issue.class);

        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue1, altEstimationFieldName)).thenReturn("L");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue2, altEstimationFieldName)).thenReturn("L");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue3, altEstimationFieldName)).thenReturn("S");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue4, altEstimationFieldName)).thenReturn("XL");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue5, altEstimationFieldName)).thenReturn("S");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue6, altEstimationFieldName)).thenReturn("S");

        issueList.add(mockedIssue1);
        issueList.add(mockedIssue2);
        issueList.add(mockedIssue3);
        issueList.add(mockedIssue4);
        issueList.add(mockedIssue5);
        issueList.add(mockedIssue6);

        try {

            issueMap = kanbanMetricsService.getIssuesByAltEstimation(issueList, true);

            assertTrue(issueMap.size() ==  3);

            assertTrue(issueMap.get("S").size() == 3);
            assertTrue(issueMap.get("L").size() == 2);
            assertTrue(issueMap.get("XL").size() == 1);

        } catch (Exception e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }

    }

    @After
    public void tearDown() throws Exception {
    }

}
