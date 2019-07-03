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
package net.netconomy.jiraassistant.sprintanalysis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
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
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataFull;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.SprintAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.sprintanalysis.data.IssueStatistics;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { SprintAnalysisTestDIConfiguration.class })
public class IssueStatisticsServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private IssueStatisticsService issueStatisticsService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private SprintDataFull sprintDataDummy;
    private SprintDataDelta sprintDataDeltaDummy;

    private String testSprint = "testSprint";
    private Integer testId = 9;
    private Integer testRapidViewId = 11;
    private String testState = "Closed";
    private DateTime testStartDate = DateTime.now();
    private DateTime testEndDate = DateTime.now().plusDays(14);

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
    private String estimationFieldName = "Story Points";

    private ProjectConfiguration mockedConfiguration = mock(ProjectConfiguration.class);

    private ClientCredentials credentials = new ClientCredentials();

    @Before
    public void setUp() throws Exception {
        sprintDataDummy = new SprintDataFull();

        sprintDataDummy.setId(testId);
        sprintDataDummy.setRapidViewId(testRapidViewId);
        sprintDataDummy.setName(testSprint);
        sprintDataDummy.setState(testState);
        sprintDataDummy.setStartDate(testStartDate.toString());
        sprintDataDummy.setEndDate(testEndDate.toString());
        sprintDataDummy.setCompleteDate(null);

        sprintDataDeltaDummy = new SprintDataDelta(sprintDataDummy);

        when(mockedIssue1.getKey()).thenReturn("TST-1");
        when(mockedIssue2.getKey()).thenReturn("TST-2");
        when(mockedIssue3.getKey()).thenReturn("TST-3");
        when(mockedIssue4.getKey()).thenReturn("TST-4");
        when(mockedIssue5.getKey()).thenReturn("TST-5");
        when(mockedIssue6.getKey()).thenReturn("TST-6");
        when(mockedIssue7.getKey()).thenReturn("TST-7");

        BasicProject proj = new BasicProject(null, "TST", 0l, "");
        when(mockedIssue1.getProject()).thenReturn(proj);
        when(mockedIssue2.getProject()).thenReturn(proj);
        when(mockedIssue3.getProject()).thenReturn(proj);
        when(mockedIssue4.getProject()).thenReturn(proj);
        when(mockedIssue5.getProject()).thenReturn(proj);
        when(mockedIssue6.getProject()).thenReturn(proj);
        when(mockedIssue7.getProject()).thenReturn(proj);

        when(mockedSubIssue1.getProject()).thenReturn(proj);
        when(mockedSubIssue2.getProject()).thenReturn(proj);

        when(
                mockedHistoryIssueService.getValueDurationForIssueField(any(Issue.class), any(String.class),
                        any(String.class), any(DateTime.class), any(DateTime.class))).thenReturn(new Duration(0));

        sprintDataDummy.addIssue(mockedIssue1);
        sprintDataDummy.addIssue(mockedIssue2);
        sprintDataDummy.addIssue(mockedIssue3);
        sprintDataDummy.addIssue(mockedIssue4);
        sprintDataDummy.addIssue(mockedIssue5);
        sprintDataDummy.addIssue(mockedIssue6);
        sprintDataDummy.addIssue(mockedIssue7);

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

        when(mockedConfiguration.getEstimationFieldName()).thenReturn(estimationFieldName);
        when(mockedConfiguration.getStoryIssueTypes()).thenReturn(storyIssueTypes);
        when(mockedConfiguration.getDefectIssueTypes()).thenReturn(defectIssueTypes);
        when(mockedConfiguration.getInProgressStatus()).thenReturn(inProgressStatus);
        when(mockedConfiguration.getImplementedStatus()).thenReturn(implementedStatus);
        when(mockedConfiguration.getFinishedStatus()).thenReturn(finishedStatus);
        when(mockedConfiguration.getFlaggedFieldName()).thenReturn("Markiert");
        when(mockedConfiguration.getFlaggedFieldValue()).thenReturn("Hindernis");

    }

    @Test
    public void getIssueStatisticsTest() throws ConfigurationException {

        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue1, testEndDate)).thenReturn("Open");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue2, testEndDate)).thenReturn("Open");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue3, testEndDate)).thenReturn("In Implementation");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue4, testEndDate)).thenReturn("Resolved");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue5, testEndDate)).thenReturn("Testing");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue6, testEndDate)).thenReturn("Tested");
        when(mockedHistoryIssueService.getStatusAtTime(mockedIssue7, testEndDate)).thenReturn("Live deployed (P)");

        when(mockedAdvancedIssueService.getEstimation(mockedIssue1, estimationFieldName)).thenReturn(
                Double.valueOf("1.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue2, estimationFieldName)).thenReturn(
                Double.valueOf("2.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue3, estimationFieldName)).thenReturn(
                Double.valueOf("3.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue4, estimationFieldName)).thenReturn(
                Double.valueOf("4.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue5, estimationFieldName)).thenReturn(
                Double.valueOf("5.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue6, estimationFieldName)).thenReturn(
                Double.valueOf("6.0"));
        when(mockedAdvancedIssueService.getEstimation(mockedIssue7, estimationFieldName)).thenReturn(
                Double.valueOf("7.0"));

        IssueFilter issueFilter = new IssueFilter();
        issueFilter.getProjectKeys().add("TST");
        IssueStatistics actualStatistics = issueStatisticsService.getIssueStatistics(
                sprintDataDummy.getIssueList(), subIssueList, testStartDate, testEndDate, sprintDataDeltaDummy,
                credentials, issueFilter);

        assertEquals("Wrong number of Issues", Integer.valueOf(7), actualStatistics.getNumberOfIssues());
        assertEquals("Wrong number of SubIssues", Integer.valueOf(2), actualStatistics.getNumberOfSubIssues());
        assertEquals("Wrong number of Issues in Progress", Integer.valueOf(1),
                actualStatistics.getInProgressIssues());
        assertEquals("Wrong number of implemented Issues", Integer.valueOf(2),
                actualStatistics.getImplementedIssues());
        assertEquals("Wrong number of finished Issues", Integer.valueOf(2), actualStatistics.getFinishedIssues());

        assertEquals("Wrong number of Story Points", Double.valueOf(28), actualStatistics.getNumberOfStoryPoints());
        assertEquals("Wrong number of Story Points in Progress", Double.valueOf(3),
                actualStatistics.getInProgressStoryPoints());
        assertEquals("Wrong number of implemented Story Points", Double.valueOf(9),
                actualStatistics.getImplementedStoryPoints());
        assertEquals("Wrong number of finished Story Points", Double.valueOf(13),
                actualStatistics.getFinishedStoryPoints());

    }

    @After
    public void tearDown() throws Exception {

    }

}
