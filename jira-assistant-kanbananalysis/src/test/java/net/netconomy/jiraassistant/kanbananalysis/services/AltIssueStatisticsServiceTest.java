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

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataFull;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.kanbananalysis.KanbanAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.kanbananalysis.data.AltIssueStatistics;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.apache.commons.configuration.ConfigurationException;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { KanbanAnalysisTestDIConfiguration.class })
public class AltIssueStatisticsServiceTest {

    @Autowired
    private AltIssueStatisticsService altIssueStatisticsService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private DateTime testStartDate = DateTime.now();
    private DateTime testEndDate = DateTime.now().plusDays(14);

    private List<Issue> issueList = new ArrayList<>();

    private Issue mockedIssue1 = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);
    private Issue mockedIssue4 = mock(Issue.class);
    private Issue mockedIssue5 = mock(Issue.class);
    private Issue mockedIssue6 = mock(Issue.class);
    private Issue mockedIssue7 = mock(Issue.class);

    private List<Issue> subIssueList = new ArrayList<>();

    private Issue mockedSubIssue1 = mock(Issue.class);
    private Issue mockedSubIssue2 = mock(Issue.class);

    private ArrayList<String> storyIssueTypes = new ArrayList<>();
    private ArrayList<String> defectIssueTypes = new ArrayList<>();
    private ArrayList<String> inProgressStatus = new ArrayList<>();
    private ArrayList<String> implementedStatus = new ArrayList<>();
    private ArrayList<String> finishedStatus = new ArrayList<>();
    private ArrayList<String> wantedFields = new ArrayList<>();
    private String altEstimationFieldName = "T-Shirt Size";

    private ProjectConfiguration mockedConfiguration = mock(ProjectConfiguration.class);

    private ClientCredentials credentials = new ClientCredentials();

    @Before
    public void setUp() throws Exception {

        when(mockedIssue1.getKey()).thenReturn("TST-1");
        when(mockedIssue2.getKey()).thenReturn("TST-2");
        when(mockedIssue3.getKey()).thenReturn("TST-3");
        when(mockedIssue4.getKey()).thenReturn("TST-4");
        when(mockedIssue5.getKey()).thenReturn("TST-5");
        when(mockedIssue6.getKey()).thenReturn("TST-6");
        when(mockedIssue7.getKey()).thenReturn("TST-7");

        when(
            mockedHistoryIssueService.getValueDurationForIssueField(any(Issue.class), any(String.class),
                any(String.class), any(DateTime.class), any(DateTime.class))).thenReturn(new Duration(0));

        issueList.add(mockedIssue1);
        issueList.add(mockedIssue2);
        issueList.add(mockedIssue3);
        issueList.add(mockedIssue4);
        issueList.add(mockedIssue5);
        issueList.add(mockedIssue6);
        issueList.add(mockedIssue7);

        subIssueList.add(mockedSubIssue1);
        subIssueList.add(mockedSubIssue2);

        storyIssueTypes.add("Arbeitspaket");
        storyIssueTypes.add("Change Request");

        defectIssueTypes.add("Defect");
        defectIssueTypes.add("Bug");

        inProgressStatus.add("In Progress");
        inProgressStatus.add("In Implementation");

        implementedStatus.add("Resolved");
        implementedStatus.add("Testing");

        finishedStatus.add("Tested");
        finishedStatus.add("Ready to deploy (Q)");
        finishedStatus.add("Testing (Q)");
        finishedStatus.add("Ready to deploy (P)");
        finishedStatus.add("Live deployed (P)");
        finishedStatus.add("Done");
        finishedStatus.add("Closed");

        wantedFields.add("Story Points");
        wantedFields.add("Akzeptanzkriterien");

        when(mockedConfigurationService.getProjectConfiguration()).thenReturn(mockedConfiguration);

        when(mockedConfiguration.getAltEstimationFieldName()).thenReturn(altEstimationFieldName);
        when(mockedConfiguration.getStoryIssueTypes()).thenReturn(storyIssueTypes);
        when(mockedConfiguration.getDefectIssueTypes()).thenReturn(defectIssueTypes);
        when(mockedConfiguration.getInProgressStatus()).thenReturn(inProgressStatus);
        when(mockedConfiguration.getImplementedStatus()).thenReturn(implementedStatus);
        when(mockedConfiguration.getFinishedStatus()).thenReturn(finishedStatus);
        when(mockedConfiguration.getFlaggedFieldName()).thenReturn("Markiert");
        when(mockedConfiguration.getFlaggedFieldValue()).thenReturn("Hindernis");

    }

    @Test
    public void getAltIssueStatisticsTest() throws ConfigurationException {

        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue1, testEndDate)).thenReturn("Open");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue2, testEndDate)).thenReturn("Open");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue3, testEndDate)).thenReturn("In Implementation");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue4, testEndDate)).thenReturn("Resolved");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue5, testEndDate)).thenReturn("Testing");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue6, testEndDate)).thenReturn("Tested");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue7, testEndDate)).thenReturn("Live deployed (P)");

        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue1, altEstimationFieldName)).thenReturn("XS");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue2, altEstimationFieldName)).thenReturn("S");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue3, altEstimationFieldName)).thenReturn("S");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue4, altEstimationFieldName)).thenReturn("M");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue5, altEstimationFieldName)).thenReturn("L");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue6, altEstimationFieldName)).thenReturn("L");
        when(mockedAdvancedIssueService.getAltEstimation(mockedIssue7, altEstimationFieldName)).thenReturn("L");

        AltIssueStatistics actualStatistics = altIssueStatisticsService.getAltIssueStatistics(
            issueList, subIssueList, testStartDate, testEndDate,
            credentials);

        assertEquals("Wrong number of Issues", Integer.valueOf(7), actualStatistics.getNumberOfIssues());
        assertEquals("Wrong number of SubIssues", Integer.valueOf(2), actualStatistics.getNumberOfSubIssues());
        assertEquals("Wrong number of Issues in Progress", Integer.valueOf(1),
            actualStatistics.getInProgressIssues());
        assertEquals("Wrong number of implemented Issues", Integer.valueOf(2),
            actualStatistics.getImplementedIssues());
        assertEquals("Wrong number of finished Issues", Integer.valueOf(2), actualStatistics.getFinishedIssues());

        assertEquals("Wrong number of XS Issues", Integer.valueOf(1), actualStatistics.getIssuesByEstimation().get("XS"));
        assertEquals("Wrong number of S Issues", Integer.valueOf(2), actualStatistics.getIssuesByEstimation().get("S"));
        assertEquals("Wrong number of M Issues", Integer.valueOf(1), actualStatistics.getIssuesByEstimation().get("M"));
        assertEquals("Wrong number of L Issues", Integer.valueOf(3), actualStatistics.getIssuesByEstimation().get("L"));

        assertEquals("Wrong number of XS Issues in Progress", null, actualStatistics.getInProgress().get("XS"));
        assertEquals("Wrong number of S Issues in Progress", Integer.valueOf(1), actualStatistics.getInProgress().get("S"));
        assertEquals("Wrong number of M Issues in Progress", null, actualStatistics.getInProgress().get("M"));
        assertEquals("Wrong number of L Issues in Progress", null, actualStatistics.getInProgress().get("L"));

        assertEquals("Wrong number of XS Issues implemented", null, actualStatistics.getImplemented().get("XS"));
        assertEquals("Wrong number of S Issues implemented", null, actualStatistics.getImplemented().get("S"));
        assertEquals("Wrong number of M Issues implemented", Integer.valueOf(1), actualStatistics.getImplemented().get("M"));
        assertEquals("Wrong number of L Issues implemented", Integer.valueOf(1), actualStatistics.getImplemented().get("L"));

        assertEquals("Wrong number of XS Issues finished", null, actualStatistics.getFinished().get("XS"));
        assertEquals("Wrong number of S Issues finished", null, actualStatistics.getFinished().get("S"));
        assertEquals("Wrong number of M Issues finished", null, actualStatistics.getFinished().get("M"));
        assertEquals("Wrong number of L Issues finished", Integer.valueOf(2), actualStatistics.getFinished().get("L"));

    }

    @After
    public void tearDown() throws Exception {
    }

}
