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
package net.netconomy.jiraassistant.base.services.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.ChangelogItem;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.Status;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.JiraAssistantException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class HistoryIssueServiceTest {

    @Autowired
    private HistoryIssueService historyIssueService;

    private Status mockedStatus = mock(Status.class);

    private IssueField mockedIssueFields = mock(IssueField.class);

    private Issue mockedIssueWithHistory = mock(Issue.class);

    private List<ChangelogGroup> testChangeLogEntriesNoStatusChangeInfo = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesNoStatusChangeInTime = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesSimple = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesComplex = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesReopenFactor = new ArrayList<>();

    private String testFieldName = "testField";

    private List<ChangelogGroup> testChangeLogEntriesNoValueChangeInfo = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesNoValueChangeInTime = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesValueSimple = new ArrayList<>();
    private List<ChangelogGroup> testChangeLogEntriesValueComplex = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        DateTime issueCreationDate;

        issueCreationDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);

        when(mockedIssueWithHistory.getCreationDate()).thenReturn(issueCreationDate);

        // construct the mocked underlying Data structure
        ChangelogGroup mockedGroupNoChange1 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupNoChange2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupInProgress = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupResolved = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupTesting = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupReopened = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupInProgress2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupResolved2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupReopened2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupInProgress3 = mock(ChangelogGroup.class);

        // ValueAtTime
        ChangelogGroup mockedGroupNoValueChange1 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupNoValueChange2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupValueChange1 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupValueChange2 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupValueChange3 = mock(ChangelogGroup.class);
        ChangelogGroup mockedGroupValueChange4 = mock(ChangelogGroup.class);

        ChangelogItem mockedItemNoChange1 = mock(ChangelogItem.class);
        ChangelogItem mockedItemNoChange2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemInProgress = mock(ChangelogItem.class);
        ChangelogItem mockedItemResolved = mock(ChangelogItem.class);
        ChangelogItem mockedItemTesting = mock(ChangelogItem.class);
        ChangelogItem mockedItemReopened = mock(ChangelogItem.class);
        ChangelogItem mockedItemInProgress2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemResolved2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemReopened2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemInProgress3 = mock(ChangelogItem.class);

        // ValueAtTime
        ChangelogItem mockedItemNoValueChange1 = mock(ChangelogItem.class);
        ChangelogItem mockedItemNoValueChange2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemValueChange1 = mock(ChangelogItem.class);
        ChangelogItem mockedItemValueChange2 = mock(ChangelogItem.class);
        ChangelogItem mockedItemValueChange3 = mock(ChangelogItem.class);
        ChangelogItem mockedItemValueChange4 = mock(ChangelogItem.class);

        List<ChangelogItem> mockedItemNoChange1List = new ArrayList<>();
        List<ChangelogItem> mockedItemNoChange2List = new ArrayList<>();
        List<ChangelogItem> mockedItemInProgressList = new ArrayList<>();
        List<ChangelogItem> mockedItemResolvedList = new ArrayList<>();
        List<ChangelogItem> mockedItemTestingList = new ArrayList<>();
        List<ChangelogItem> mockedItemReopenedList = new ArrayList<>();
        List<ChangelogItem> mockedItemInProgressList2 = new ArrayList<>();
        List<ChangelogItem> mockedItemResolvedList2 = new ArrayList<>();
        List<ChangelogItem> mockedItemReopenedList2 = new ArrayList<>();
        List<ChangelogItem> mockedItemInProgressList3 = new ArrayList<>();

        // ValueAtTime
        List<ChangelogItem> mockedItemNoValueChange1List = new ArrayList<>();
        List<ChangelogItem> mockedItemNoValueChange2List = new ArrayList<>();
        List<ChangelogItem> mockedItemValueChange1List = new ArrayList<>();
        List<ChangelogItem> mockedItemValueChange2List = new ArrayList<>();
        List<ChangelogItem> mockedItemValueChange3List = new ArrayList<>();
        List<ChangelogItem> mockedItemValueChange4List = new ArrayList<>();

        mockedItemNoChange1List.add(mockedItemNoChange1);
        mockedItemNoChange2List.add(mockedItemNoChange2);
        mockedItemInProgressList.add(mockedItemInProgress);
        mockedItemResolvedList.add(mockedItemResolved);
        mockedItemTestingList.add(mockedItemTesting);
        mockedItemReopenedList.add(mockedItemReopened);
        mockedItemInProgressList2.add(mockedItemInProgress2);
        mockedItemResolvedList2.add(mockedItemResolved2);
        mockedItemReopenedList2.add(mockedItemReopened2);
        mockedItemInProgressList3.add(mockedItemInProgress3);

        // ValueAtTime
        mockedItemNoValueChange1List.add(mockedItemNoValueChange1);
        mockedItemNoValueChange2List.add(mockedItemNoValueChange2);
        mockedItemValueChange1List.add(mockedItemValueChange1);
        mockedItemValueChange2List.add(mockedItemValueChange2);
        mockedItemValueChange3List.add(mockedItemValueChange3);
        mockedItemValueChange4List.add(mockedItemValueChange4);

        when(mockedGroupNoChange1.getItems()).thenReturn(mockedItemNoChange1List);
        when(mockedGroupNoChange1.getCreated()).thenReturn(new DateTime(2014, 1, 1, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupInProgress.getItems()).thenReturn(mockedItemInProgressList);
        when(mockedGroupInProgress.getCreated()).thenReturn(new DateTime(2014, 2, 3, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupResolved.getItems()).thenReturn(mockedItemResolvedList);
        when(mockedGroupResolved.getCreated()).thenReturn(new DateTime(2014, 2, 4, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupTesting.getItems()).thenReturn(mockedItemTestingList);
        when(mockedGroupTesting.getCreated()).thenReturn(new DateTime(2014, 3, 5, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupReopened.getItems()).thenReturn(mockedItemReopenedList);
        when(mockedGroupReopened.getCreated()).thenReturn(new DateTime(2014, 4, 6, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupInProgress2.getItems()).thenReturn(mockedItemInProgressList2);
        when(mockedGroupInProgress2.getCreated()).thenReturn(new DateTime(2014, 4, 8, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupResolved2.getItems()).thenReturn(mockedItemResolvedList2);
        when(mockedGroupResolved2.getCreated()).thenReturn(new DateTime(2014, 4, 10, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupReopened2.getItems()).thenReturn(mockedItemReopenedList2);
        when(mockedGroupReopened2.getCreated()).thenReturn(new DateTime(2014, 4, 20, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupInProgress3.getItems()).thenReturn(mockedItemInProgressList3);
        when(mockedGroupInProgress3.getCreated()).thenReturn(new DateTime(2014, 4, 30, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupNoChange2.getItems()).thenReturn(mockedItemNoChange2List);
        when(mockedGroupNoChange2.getCreated()).thenReturn(new DateTime(2014, 5, 6, 20, 0, 0, DateTimeZone.UTC));

        // ValueAtTime
        when(mockedGroupNoValueChange1.getItems()).thenReturn(mockedItemNoValueChange1List);
        when(mockedGroupNoValueChange1.getCreated()).thenReturn(new DateTime(2014, 1, 1, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupValueChange1.getItems()).thenReturn(mockedItemValueChange1List);
        when(mockedGroupValueChange1.getCreated()).thenReturn(new DateTime(2014, 2, 3, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupValueChange2.getItems()).thenReturn(mockedItemValueChange2List);
        when(mockedGroupValueChange2.getCreated()).thenReturn(new DateTime(2014, 2, 4, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupValueChange3.getItems()).thenReturn(mockedItemValueChange3List);
        when(mockedGroupValueChange3.getCreated()).thenReturn(new DateTime(2014, 3, 5, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupValueChange4.getItems()).thenReturn(mockedItemValueChange4List);
        when(mockedGroupValueChange4.getCreated()).thenReturn(new DateTime(2014, 4, 6, 20, 0, 0, DateTimeZone.UTC));

        when(mockedGroupNoValueChange2.getItems()).thenReturn(mockedItemNoValueChange2List);
        when(mockedGroupNoValueChange2.getCreated()).thenReturn(new DateTime(2014, 5, 6, 20, 0, 0, DateTimeZone.UTC));

        when(mockedItemNoChange1.getField()).thenReturn("testField");
        when(mockedItemNoChange1.getToString()).thenReturn("testValue");

        when(mockedItemNoChange2.getField()).thenReturn("testField2");
        when(mockedItemNoChange2.getToString()).thenReturn("testValue2");

        when(mockedItemInProgress.getField()).thenReturn("status");
        when(mockedItemInProgress.getFromString()).thenReturn("Open");
        when(mockedItemInProgress.getToString()).thenReturn("In Progress");

        when(mockedItemResolved.getField()).thenReturn("status");
        when(mockedItemResolved.getFromString()).thenReturn("In Progress");
        when(mockedItemResolved.getToString()).thenReturn("Resolved");

        when(mockedItemTesting.getField()).thenReturn("status");
        when(mockedItemTesting.getFromString()).thenReturn("Resolved");
        when(mockedItemTesting.getToString()).thenReturn("Testing");

        when(mockedItemReopened.getField()).thenReturn("status");
        when(mockedItemReopened.getFromString()).thenReturn("Testing");
        when(mockedItemReopened.getToString()).thenReturn("Reopened");

        when(mockedItemInProgress2.getField()).thenReturn("status");
        when(mockedItemInProgress2.getFromString()).thenReturn("Reopened");
        when(mockedItemInProgress2.getToString()).thenReturn("In Progress");

        when(mockedItemResolved2.getField()).thenReturn("status");
        when(mockedItemResolved2.getFromString()).thenReturn("In Progress");
        when(mockedItemResolved2.getToString()).thenReturn("Resolved");

        when(mockedItemReopened2.getField()).thenReturn("status");
        when(mockedItemReopened2.getFromString()).thenReturn("Resolved");
        when(mockedItemReopened2.getToString()).thenReturn("Reopened");

        when(mockedItemInProgress3.getField()).thenReturn("status");
        when(mockedItemInProgress3.getFromString()).thenReturn("Reopened");
        when(mockedItemInProgress3.getToString()).thenReturn("In Progress");

        // ValueAtTime
        when(mockedItemNoValueChange1.getField()).thenReturn("otherTestField");
        when(mockedItemNoValueChange1.getToString()).thenReturn("testValue");

        when(mockedItemValueChange1.getField()).thenReturn(testFieldName);
        when(mockedItemValueChange1.getFromString()).thenReturn("1");
        when(mockedItemValueChange1.getToString()).thenReturn("2");

        when(mockedItemValueChange2.getField()).thenReturn(testFieldName);
        when(mockedItemValueChange2.getFromString()).thenReturn("2");
        when(mockedItemValueChange2.getToString()).thenReturn("3");

        when(mockedItemValueChange3.getField()).thenReturn(testFieldName);
        when(mockedItemValueChange3.getFromString()).thenReturn("3");
        when(mockedItemValueChange3.getToString()).thenReturn("4");

        when(mockedItemValueChange4.getField()).thenReturn(testFieldName);
        when(mockedItemValueChange4.getFromString()).thenReturn("4");
        when(mockedItemValueChange4.getToString()).thenReturn("5");

        when(mockedItemNoValueChange2.getField()).thenReturn("otherTestField");
        when(mockedItemNoValueChange2.getToString()).thenReturn("testValue");

        // Set up Lists for Testscenarios
        testChangeLogEntriesNoStatusChangeInfo.add(mockedGroupNoChange1);
        testChangeLogEntriesNoStatusChangeInfo.add(mockedGroupNoChange2);

        testChangeLogEntriesNoStatusChangeInTime.add(mockedGroupInProgress);
        testChangeLogEntriesNoStatusChangeInTime.add(mockedGroupResolved);

        testChangeLogEntriesSimple.add(mockedGroupInProgress);
        testChangeLogEntriesSimple.add(mockedGroupResolved);

        testChangeLogEntriesComplex.add(mockedGroupNoChange1);
        testChangeLogEntriesComplex.add(mockedGroupInProgress);
        testChangeLogEntriesComplex.add(mockedGroupResolved);
        testChangeLogEntriesComplex.add(mockedGroupTesting);
        testChangeLogEntriesComplex.add(mockedGroupReopened);
        testChangeLogEntriesComplex.add(mockedGroupNoChange2);

        testChangeLogEntriesReopenFactor.add(mockedGroupNoChange1);
        testChangeLogEntriesReopenFactor.add(mockedGroupInProgress);
        testChangeLogEntriesReopenFactor.add(mockedGroupResolved);
        testChangeLogEntriesReopenFactor.add(mockedGroupTesting);
        testChangeLogEntriesReopenFactor.add(mockedGroupReopened);
        testChangeLogEntriesReopenFactor.add(mockedGroupInProgress2);
        testChangeLogEntriesReopenFactor.add(mockedGroupResolved2);
        testChangeLogEntriesReopenFactor.add(mockedGroupReopened2);
        testChangeLogEntriesReopenFactor.add(mockedGroupInProgress3);
        testChangeLogEntriesReopenFactor.add(mockedGroupNoChange2);

        // ValueAtTime
        testChangeLogEntriesNoValueChangeInfo.add(mockedGroupNoValueChange1);
        testChangeLogEntriesNoValueChangeInfo.add(mockedGroupNoValueChange2);

        testChangeLogEntriesNoValueChangeInTime.add(mockedGroupValueChange1);
        testChangeLogEntriesNoValueChangeInTime.add(mockedGroupValueChange2);

        testChangeLogEntriesValueSimple.add(mockedGroupValueChange1);
        testChangeLogEntriesValueSimple.add(mockedGroupValueChange2);

        testChangeLogEntriesValueComplex.add(mockedGroupNoValueChange1);
        testChangeLogEntriesValueComplex.add(mockedGroupValueChange1);
        testChangeLogEntriesValueComplex.add(mockedGroupValueChange2);
        testChangeLogEntriesValueComplex.add(mockedGroupValueChange3);
        testChangeLogEntriesValueComplex.add(mockedGroupValueChange4);
        testChangeLogEntriesValueComplex.add(mockedGroupNoValueChange2);

    }

    @Test
    public void getStatusAtTimeTestSimple() {

        String testCurrentStatus = "Resolved";
        String expectedStatusAtTime = "In Progress";
        String actualStatusAtTime;
        DateTime time = new DateTime(2014, 2, 3, 22, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualStatusAtTime = historyIssueService.getStatusAtTime(mockedIssueWithHistory, time);

        assertEquals("Simple Status at Time Test failed", expectedStatusAtTime, actualStatusAtTime);

    }

    @Test
    public void getStatusAtTimeTestNoChangeInfo() {

        String testCurrentStatus = "Open";
        String expectedStatusAtTime = "Open";
        String actualStatusAtTime;
        DateTime time = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInfo);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualStatusAtTime = historyIssueService.getStatusAtTime(mockedIssueWithHistory, time);

        assertEquals("Status at Time Test With no change Info failed", expectedStatusAtTime, actualStatusAtTime);

    }

    @Test
    public void getStatusAtTimeTestNoChangeInTime() {

        String testCurrentStatus = "Resolved";
        String expectedStatusAtTime = "Open";
        String actualStatusAtTime;
        DateTime time = new DateTime(2014, 1, 1, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInTime);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualStatusAtTime = historyIssueService.getStatusAtTime(mockedIssueWithHistory, time);

        assertEquals("Status at Time Test With no changes failed", expectedStatusAtTime, actualStatusAtTime);

    }

    @Test
    public void getStatusAtTimeTestComplex() {

        String testCurrentStatus = "Reopened";
        String expectedStatusAtTime = "Reopened";
        String actualStatusAtTime;
        DateTime time = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesComplex);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualStatusAtTime = historyIssueService.getStatusAtTime(mockedIssueWithHistory, time);

        assertEquals("Complex Status at Time Test failed", expectedStatusAtTime, actualStatusAtTime);

    }

    @Test
    public void getStatusAtTimeTestNoHistory() {

        String testCurrentStatus = "testStatus";
        DateTime time = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(null);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        try {

            historyIssueService.getStatusAtTime(mockedIssueWithHistory, time);

            fail("No Exception was thrown");

        } catch (JiraAssistantException e) {
            assertTrue(true);
        }

    }

    @Test
    public void getStateCountForIssueTest() {

        String testCurrentStatus = "testStatus";
        String reopenStateName = "Reopened";
        Integer actualResult;
        Integer expectedResult = 1;

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 4, 8, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getStateCountForIssue(mockedIssueWithHistory, reopenStateName,
                testStartDate,
                testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getStateCountForIssueTestComplex() {

        String testCurrentStatus = "testStatus";
        String reopenStateName = "Reopened";
        Integer actualResult;
        Integer expectedResult = 2;

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getStateCountForIssue(mockedIssueWithHistory, reopenStateName,
                testStartDate,
                testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getStateCountForIssueTestComplex2() {

        String testCurrentStatus = "testStatus";
        String reopenStateName = "Reopened";
        Integer actualResult;
        Integer expectedResult = 1;

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesComplex);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getStateCountForIssue(mockedIssueWithHistory, reopenStateName,
                testStartDate,
                testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestSimple() {

        String testCurrentStatus = "Resolved";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration(new DateTime(2014, 2, 3, 20, 0, 0), new DateTime(2014, 2, 4, 20, 0, 0));

        DateTime testStartDate = new DateTime(2014, 2, 2, 20, 0, 0);
        DateTime testEndDate = new DateTime(2014, 2, 5, 20, 0, 0);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName,
                "In Progress", testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestSimple2() {

        String testCurrentStatus = "Resolved";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration(new DateTime(2014, 2, 4, 8, 0, 0), new DateTime(2014, 2, 4, 20, 0, 0));

        DateTime testStartDate = new DateTime(2014, 2, 4, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 2, 5, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName,
                "In Progress", testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestSimple3() {

        String testCurrentStatus = "Resolved";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration(new DateTime(2014, 2, 3, 20, 0, 0, DateTimeZone.UTC), new DateTime(2014, 2, 4, 8, 0, 0, DateTimeZone.UTC));

        DateTime testStartDate = new DateTime(2014, 2, 2, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 2, 4, 8, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName,
                "In Progress", testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestComplex() {

        String testCurrentStatus = "In Progress";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration("PT266400S"); // 3 days and 2 hours

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 4, 30, 22, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName,
                "In Progress", testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestComplex2() {

        String testCurrentStatus = "testStatus";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration("PT216000S"); // 2,5 days

        DateTime testStartDate = new DateTime(2014, 2, 4, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 4, 22, 20, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName,
                "In Progress", testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestNoChangeInTime() {

        String testCurrentStatus = "In Progress";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration(0);

        DateTime testStartDate = new DateTime(2014, 2, 3, 23, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 2, 4, 11, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInTime);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName, "Resolved",
                testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueDurationForIssueTestNoChange() {

        String testCurrentStatus = "In Progress";
        String fieldName = "status";
        Duration actualResult;
        Duration expectedResult = new Duration(0);

        DateTime testStartDate = new DateTime(2014, 1, 1, 10, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 2, 4, 11, 0, 0, DateTimeZone.UTC);

        when(mockedStatus.getName()).thenReturn(testCurrentStatus);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInTime);
        when(mockedIssueWithHistory.getStatus()).thenReturn(mockedStatus);

        actualResult = historyIssueService.getValueDurationForIssueField(mockedIssueWithHistory, fieldName, "Resolved",
                testStartDate, testEndDate);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void getValueAtTimeTestSimple() {

        String testCurrentValue = "3";
        String expectedValueAtTime = "2";
        String actualValueAtTime;
        DateTime time = new DateTime(2014, 2, 3, 22, 0, 0, DateTimeZone.UTC);

        when(mockedIssueFields.getValue()).thenReturn(testCurrentValue);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesValueSimple);
        when(mockedIssueWithHistory.getFieldByName(testFieldName)).thenReturn(mockedIssueFields);

        actualValueAtTime = historyIssueService.getFieldValueAtTime(mockedIssueWithHistory, testFieldName, time);

        assertEquals("Simple Value at Time Test failed", expectedValueAtTime, actualValueAtTime);

    }

    @Test
    public void getValueAtTimeTestNoChangeInfo() {

        String testCurrentValue = "1";
        String expectedValueAtTime = "1";
        String actualValueAtTime;
        DateTime time = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueFields.getValue()).thenReturn(testCurrentValue);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoValueChangeInfo);
        when(mockedIssueWithHistory.getFieldByName(testFieldName)).thenReturn(mockedIssueFields);

        actualValueAtTime = historyIssueService.getFieldValueAtTime(mockedIssueWithHistory, testFieldName, time);

        assertEquals("Value at Time Test With no change Info failed", expectedValueAtTime, actualValueAtTime);

    }

    @Test
    public void getValueAtTimeTestNoChangeInTime() {

        String testCurrentValue = "3";
        String expectedValueAtTime = "1";
        String actualValueAtTime;
        DateTime time = new DateTime(2014, 1, 1, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueFields.getValue()).thenReturn(testCurrentValue);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoValueChangeInTime);
        when(mockedIssueWithHistory.getFieldByName(testFieldName)).thenReturn(mockedIssueFields);

        actualValueAtTime = historyIssueService.getFieldValueAtTime(mockedIssueWithHistory, testFieldName, time);

        assertEquals("Value at Time Test With no changes failed", expectedValueAtTime, actualValueAtTime);

    }

    @Test
    public void getValueAtTimeTestComplex() {

        String testCurrentValue = "5";
        String expectedValueAtTime = "5";
        String actualValueAtTime;
        DateTime time = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueFields.getValue()).thenReturn(testCurrentValue);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesValueComplex);
        when(mockedIssueWithHistory.getFieldByName(testFieldName)).thenReturn(mockedIssueFields);

        actualValueAtTime = historyIssueService.getFieldValueAtTime(mockedIssueWithHistory, testFieldName, time);

        assertEquals("Complex Value at Time Test failed", expectedValueAtTime, actualValueAtTime);

    }

    @Test
    public void issueWasUpdatedDuringTestSimple() {

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        assertTrue(historyIssueService.issueWasUpdatedDuring(mockedIssueWithHistory, testStartDate, testEndDate));

    }

    @Test
    public void issueWasUpdatedDuringTestNoChange() {

        DateTime testStartDate = new DateTime(2014, 2, 4, 21, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        assertFalse(historyIssueService.issueWasUpdatedDuring(mockedIssueWithHistory, testStartDate, testEndDate));

    }

    @Test
    public void issueWasUpdatedDuringTestComplex() {

        DateTime testStartDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime testEndDate = new DateTime(2014, 6, 6, 20, 0, 0, DateTimeZone.UTC);

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesComplex);

        assertTrue(historyIssueService.issueWasUpdatedDuring(mockedIssueWithHistory, testStartDate, testEndDate));

    }

    @Test
    public void getDateTimeOfLastFieldChangeTestSimple() {

        String fieldName = "status";
        DateTime expectedDate = new DateTime(2014, 2, 4, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        actualDate = historyIssueService.getDateTimeOfLastFieldChange(mockedIssueWithHistory, fieldName);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfLastFieldChangeTestNoChange() {

        String fieldName = "status";
        DateTime expectedDate = new DateTime(2014, 1, 1, 8, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInfo);

        actualDate = historyIssueService.getDateTimeOfLastFieldChange(mockedIssueWithHistory, fieldName);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfLastFieldChangeTestComplex() {

        String fieldName = "status";
        DateTime expectedDate = new DateTime(2014, 4, 6, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesComplex);

        actualDate = historyIssueService.getDateTimeOfLastFieldChange(mockedIssueWithHistory, fieldName);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListTestSimple() {

        List<String> statusList = new ArrayList<>();
        DateTime expectedDate = new DateTime(2014, 2, 3, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("In Progress");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssueWithHistory, statusList);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListTestNoChange() {

        List<String> statusList = new ArrayList<>();
        DateTime actualDate;

        statusList.add("In Progress");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesNoStatusChangeInfo);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssueWithHistory, statusList);

        assertNull(actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListTestComplex() {

        List<String> statusList = new ArrayList<>();
        DateTime expectedDate = new DateTime(2014, 4, 6, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("Reopened");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesComplex);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssueWithHistory, statusList);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListAfterTestSimple() {

        List<String> statusList = new ArrayList<>();
        DateTime date = new DateTime(2014, 2, 2, 20, 0, 0, DateTimeZone.UTC);
        DateTime expectedDate = new DateTime(2014, 2, 3, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("In Progress");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(mockedIssueWithHistory, statusList, date);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListAfterTestNull() {

        List<String> statusList = new ArrayList<>();
        DateTime date = new DateTime(2014, 2, 4, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("In Progress");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesSimple);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(mockedIssueWithHistory, statusList, date);

        assertNull(actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListAfterTestComplexFirst() {

        List<String> statusList = new ArrayList<>();
        DateTime date = new DateTime(2014, 4, 4, 20, 0, 0, DateTimeZone.UTC);
        DateTime expectedDate = new DateTime(2014, 4, 6, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("Reopened");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(mockedIssueWithHistory, statusList, date);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getDateTimeOfFirstStatusInSpecifiedListAfterTestComplexSecond() {

        List<String> statusList = new ArrayList<>();
        DateTime date = new DateTime(2014, 4, 8, 20, 0, 0, DateTimeZone.UTC);
        DateTime expectedDate = new DateTime(2014, 4, 20, 20, 0, 0, DateTimeZone.UTC);
        DateTime actualDate;

        statusList.add("Reopened");

        when(mockedIssueWithHistory.getChangelog()).thenReturn(testChangeLogEntriesReopenFactor);

        actualDate = historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(mockedIssueWithHistory, statusList, date);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getResolutionDateTest() {

        Issue mockIssue = mock(Issue.class);
        IssueField mockIssueField = mock(IssueField.class);
        String fieldName = "resolutiondate";
        DateTime expectedDate = new DateTime(2016, 1, 13, 16, 15, 9, 728, DateTimeZone.UTC);
        DateTime actualDate;

        when(mockIssue.getFieldByName(fieldName)).thenReturn(mockIssueField);
        when(mockIssueField.getValue()).thenReturn("2016-01-13T16:15:09.728Z");

        actualDate = historyIssueService.getResolutionDate(mockIssue, fieldName);

        assertEquals(expectedDate, actualDate);

    }

    @Test
    public void getResolutionDateTestNull() {

        Issue mockIssue = mock(Issue.class);
        String fieldName = "resolutiondate";
        DateTime actualDate;

        when(mockIssue.getFieldByName(fieldName)).thenReturn(null);

        actualDate = historyIssueService.getResolutionDate(mockIssue, fieldName);

        assertEquals(null, actualDate);

    }

    @After
    public void tearDown() throws Exception {
    }


}
