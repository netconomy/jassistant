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
package net.netconomy.jiraassistant.projectstatus.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Status;

import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.projectstatus.ProjectStatusTestDIConfiguration;
import net.netconomy.jiraassistant.projectstatus.data.IssuesInGroupingData;
import net.netconomy.jiraassistant.projectstatus.data.StatusGroupingData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { ProjectStatusTestDIConfiguration.class })
public class ProjectStatusServiceTest {

    @Autowired
    ProjectStatusService projectStatusService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    private Issue mockedIssue1 = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);
    private Issue mockedIssue4 = mock(Issue.class);

    private Status mockedStatus1 = mock(Status.class);
    private Status mockedStatus2 = mock(Status.class);
    private Status mockedStatus3 = mock(Status.class);
    private Status mockedStatus4 = mock(Status.class);

    private IssueType mockedIssueType1 = mock(IssueType.class);
    private IssueType mockedIssueType2 = mock(IssueType.class);
    private IssueType mockedIssueType3 = mock(IssueType.class);
    private IssueType mockedIssueType4 = mock(IssueType.class);

    private IssueField mockedIssueField1 = mock(IssueField.class);
    private IssueField mockedIssueField2 = mock(IssueField.class);
    private IssueField mockedIssueField3 = mock(IssueField.class);
    private IssueField mockedIssueField4 = mock(IssueField.class);

    private String estimationFieldName = "StoryPoints";

    private String groupingFieldName = "Grouping";

    private ProjectConfiguration projectConfiguration = mock(ProjectConfiguration.class);

    @Before
    public void setUp() throws Exception {

        when(projectConfiguration.getEstimationFieldName()).thenReturn(estimationFieldName);

        when(mockedIssue1.getStatus()).thenReturn(mockedStatus1);
        when(mockedIssue2.getStatus()).thenReturn(mockedStatus2);
        when(mockedIssue3.getStatus()).thenReturn(mockedStatus3);
        when(mockedIssue4.getStatus()).thenReturn(mockedStatus4);

        when(mockedIssue1.getIssueType()).thenReturn(mockedIssueType1);
        when(mockedIssue2.getIssueType()).thenReturn(mockedIssueType2);
        when(mockedIssue3.getIssueType()).thenReturn(mockedIssueType3);
        when(mockedIssue4.getIssueType()).thenReturn(mockedIssueType4);

        when(mockedIssue1.getFieldByName(groupingFieldName)).thenReturn(mockedIssueField1);
        when(mockedIssue2.getFieldByName(groupingFieldName)).thenReturn(mockedIssueField2);
        when(mockedIssue3.getFieldByName(groupingFieldName)).thenReturn(mockedIssueField3);
        when(mockedIssue4.getFieldByName(groupingFieldName)).thenReturn(mockedIssueField4);

        when(mockedAdvancedIssueService.getEstimation(mockedIssue1, estimationFieldName)).thenReturn(8.0);
        when(mockedStatus1.getName()).thenReturn("Ready for Development");
        when(mockedIssueType1.getName()).thenReturn("Arbeitspaket");
        when(mockedIssueField1.getValue()).thenReturn("Group 1");

        when(mockedAdvancedIssueService.getEstimation(mockedIssue2, estimationFieldName)).thenReturn(0.0);
        when(mockedStatus2.getName()).thenReturn("Open");
        when(mockedIssueType2.getName()).thenReturn("Bug");
        when(mockedIssueField2.getValue()).thenReturn("Group 2");

        when(mockedAdvancedIssueService.getEstimation(mockedIssue3, estimationFieldName)).thenReturn(5.0);
        when(mockedStatus3.getName()).thenReturn("In Implementation");
        when(mockedIssueType3.getName()).thenReturn("Change Request");
        when(mockedIssueField3.getValue()).thenReturn("Group 1");

        when(mockedAdvancedIssueService.getEstimation(mockedIssue4, estimationFieldName)).thenReturn(3.0);
        when(mockedStatus4.getName()).thenReturn("Ready for Development");
        when(mockedIssueType4.getName()).thenReturn("Arbeitspaket");
        when(mockedIssueField4.getValue()).thenReturn("Group 3");

    }

    @Test
    public void processIssueTestEstimated() {

        IssuesInGroupingData issuesInGroupingData = new IssuesInGroupingData();

        projectStatusService.processIssue(mockedIssue1, issuesInGroupingData, projectConfiguration);

        assertEquals("Issue Count was not correct.", Integer.valueOf(1), issuesInGroupingData.getNumberOfIssues());
        assertEquals("Story Points were not correct.", Double.valueOf(8), issuesInGroupingData.getNumberOfStoryPoints());
        assertEquals("Unestimated Issue Count was not correct.", Integer.valueOf(0),
                issuesInGroupingData.getNumberOfUnEstimatedIssues());

        assertEquals("Issue Types (Names) were not counted correctly.", Integer.valueOf(1), issuesInGroupingData
                .getIssueTypes().get("Arbeitspaket"));
        assertEquals("Issue Types (Count) were not counted correctly.", 1, issuesInGroupingData.getIssueTypes().size());

    }

    @Test
    public void processIssueTestUnEstimated() {

        IssuesInGroupingData issuesInGroupingData = new IssuesInGroupingData();

        projectStatusService.processIssue(mockedIssue2, issuesInGroupingData, projectConfiguration);

        assertEquals("Issue Count was not correct.", Integer.valueOf(1), issuesInGroupingData.getNumberOfIssues());
        assertEquals("Story Points were not correct.", Double.valueOf(0), issuesInGroupingData.getNumberOfStoryPoints());
        assertEquals("Unestimated Issue Count was not correct.", Integer.valueOf(1),
                issuesInGroupingData.getNumberOfUnEstimatedIssues());

        assertEquals("Issue Types (Names) were not counted correctly.", Integer.valueOf(1), issuesInGroupingData
                .getIssueTypes().get("Bug"));
        assertEquals("Issue Types (Count) were not counted correctly.", 1, issuesInGroupingData.getIssueTypes().size());

    }

    @Test
    public void processIssueTestMoreIssues() {

        IssuesInGroupingData issuesInGroupingData = new IssuesInGroupingData();

        projectStatusService.processIssue(mockedIssue1, issuesInGroupingData, projectConfiguration);
        projectStatusService.processIssue(mockedIssue2, issuesInGroupingData, projectConfiguration);
        projectStatusService.processIssue(mockedIssue3, issuesInGroupingData, projectConfiguration);
        projectStatusService.processIssue(mockedIssue4, issuesInGroupingData, projectConfiguration);

        assertEquals("Issue Count was not correct.", Integer.valueOf(4), issuesInGroupingData.getNumberOfIssues());
        assertEquals("Story Points were not correct.", Double.valueOf(16),
                issuesInGroupingData.getNumberOfStoryPoints());
        assertEquals("Unestimated Issue Count was not correct.", Integer.valueOf(1),
                issuesInGroupingData.getNumberOfUnEstimatedIssues());

        assertEquals("Issue Types (Names) were not counted correctly.", Integer.valueOf(2), issuesInGroupingData
                .getIssueTypes().get("Arbeitspaket"));
        assertEquals("Issue Types (Names) were not counted correctly.", Integer.valueOf(1), issuesInGroupingData
                .getIssueTypes().get("Bug"));
        assertEquals("Issue Types (Names) were not counted correctly.", Integer.valueOf(1), issuesInGroupingData
                .getIssueTypes().get("Change Request"));
        assertEquals("Issue Types (Count) were not counted correctly.", 3, issuesInGroupingData.getIssueTypes().size());

    }

    @Test
    public void processStatusGroupingSingleTest() {

        StatusGroupingData statusGroupingData = new StatusGroupingData("Test Status Group");

        projectStatusService.processStatusGrouping(mockedIssue1, statusGroupingData, null, projectConfiguration);

        assertEquals("Status (Names) were not registered correctly.", true, statusGroupingData.getStatusList()
                .contains("Ready for Development"));
        assertEquals("Status (Count) were not registered correctly.", 1, statusGroupingData.getStatusList().size());

    }

    @Test
    public void processStatusGroupingMultipleTest() {

        StatusGroupingData statusGroupingData = new StatusGroupingData("Test Status Group");

        projectStatusService.processStatusGrouping(mockedIssue1, statusGroupingData, null, projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue2, statusGroupingData, null, projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue3, statusGroupingData, null, projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue4, statusGroupingData, null, projectConfiguration);

        assertEquals("Status (Names) were not registered correctly.", true, statusGroupingData.getStatusList()
                .contains("Ready for Development"));
        assertEquals("Status (Names) were not registered correctly.", true, statusGroupingData.getStatusList()
                .contains("Open"));
        assertEquals("Status (Names) were not registered correctly.", true, statusGroupingData.getStatusList()
                .contains("In Implementation"));
        assertEquals("Status (Count) were not registered correctly.", 3, statusGroupingData.getStatusList().size());

    }

    @Test
    public void processCustomGroupingSingleTest() {

        StatusGroupingData statusGroupingData = new StatusGroupingData("Test Status Group");

        projectStatusService.processStatusGrouping(mockedIssue1, statusGroupingData, groupingFieldName,
                projectConfiguration);

        assertEquals("Custom Grouping was not created.", true,
                statusGroupingData.getCustomGroupingMap().containsKey("Group 1"));
        assertEquals("Custom Grouping was counted correctly.", Integer.valueOf(1), statusGroupingData
                .getCustomGroupingMap().get("Group 1").getNumberOfIssues());

    }

    @Test
    public void processCustomGroupingMultipleTest() {

        StatusGroupingData statusGroupingData = new StatusGroupingData("Test Status Group");

        projectStatusService.processStatusGrouping(mockedIssue1, statusGroupingData, groupingFieldName,
                projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue2, statusGroupingData, groupingFieldName,
                projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue3, statusGroupingData, groupingFieldName,
                projectConfiguration);
        projectStatusService.processStatusGrouping(mockedIssue4, statusGroupingData, groupingFieldName,
                projectConfiguration);

        assertEquals("Custom Grouping was not created.", true,
                statusGroupingData.getCustomGroupingMap().containsKey("Group 1"));
        assertEquals("Custom Grouping was not created.", true,
                statusGroupingData.getCustomGroupingMap().containsKey("Group 2"));
        assertEquals("Custom Grouping was not created.", true,
                statusGroupingData.getCustomGroupingMap().containsKey("Group 3"));

        assertEquals("Custom Grouping was counted correctly.", Integer.valueOf(2), statusGroupingData
                .getCustomGroupingMap().get("Group 1").getNumberOfIssues());
        assertEquals("Custom Grouping was counted correctly.", Integer.valueOf(1), statusGroupingData
                .getCustomGroupingMap().get("Group 2").getNumberOfIssues());
        assertEquals("Custom Grouping was counted correctly.", Integer.valueOf(1), statusGroupingData
                .getCustomGroupingMap().get("Group 3").getNumberOfIssues());

    }

    @After
    public void tearDown() throws Exception {
    }

}
