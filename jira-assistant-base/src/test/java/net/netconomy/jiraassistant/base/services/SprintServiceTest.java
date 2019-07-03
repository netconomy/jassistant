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
package net.netconomy.jiraassistant.base.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.sprint.SprintData;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataDelta;
import net.netconomy.jiraassistant.base.data.sprint.SprintDataFull;
import net.netconomy.jiraassistant.base.restclient.JiraAgileRestService;
import net.netconomy.jiraassistant.base.services.filters.IssueFilter;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.BasicIssueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class SprintServiceTest {

    private SprintData testSprintData;

    private DateTime testStartDate = DateTime.now();

    private ClientCredentials testCredentials = new ClientCredentials();

    @Autowired
    private SprintService sprintUtils;

    @Autowired
    @ReplaceWithMock
    private BasicIssueService mockedBasicIssueUtil;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueUtil;

    @Autowired
    @ReplaceWithMock
    private JiraAgileRestService agileRestService;

    private Issue mockedIssue = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);

    private IssueField mockedField = mock(IssueField.class);

    @Before
    public void setUp() {

        testSprintData = new SprintData();
    }

    @Test
    public void parseSprintTest() {

        DateTime expectedStart = new DateTime(2014, 5, 22, 7, 0, 0, 0, DateTimeZone.forOffsetHours(1));
        DateTime expectedEnd = new DateTime(2014, 6, 4, 16, 0, 0, 0, DateTimeZone.forOffsetHours(1));
        DateTime expectedComplete = new DateTime(2014, 6, 5, 9, 52, 45, 94, DateTimeZone.forOffsetHours(1));

        String testSprintString = "com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-06-04,startDate=2014-05-22T07:00:00.000+01:00,endDate=2014-06-04T16:00:00.000+01:00,completeDate=2014-06-05T09:52:45.094+01:00,id=322]";

        testSprintData = sprintUtils.parseSprintString(testSprintString);

        assertEquals("Sprint Id was not parsed correctly.", Integer.valueOf(322), testSprintData.getId());
        assertEquals("Sprint rapidViewId was not parsed correctly.", Integer.valueOf(18),
                testSprintData.getRapidViewId());
        assertEquals("Sprint Name was not parsed correctly.", "PROJ Sprint-2014-06-04", testSprintData.getName());
        assertEquals("Sprint State was not parsed correctly.", "CLOSED", testSprintData.getState());
        assertEquals("Sprint startDate was not parsed correctly.", expectedStart.toString(),
                testSprintData.getStartDate());
        assertEquals("Sprint endDate was not parsed correctly.", expectedEnd.toString(), testSprintData.getEndDate());
        assertEquals("Sprint completeDate was not parsed correctly.", expectedComplete.toString(),
                testSprintData.getCompleteDate());

    }

    @Test
    public void parseSprintWithCommaNotFailTest() {

        Boolean noException = true;
        String testSprintString = "com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=18,state=CLOSED,name=TestString, PROJ Sprint-2014-06-04,startDate=2014-05-22T07:00:00.000+01:00,endDate=2014-06-04T16:00:00.000+01:00,completeDate=2014-06-05T09:52:45.094+01:00,id=322]";

        try {
            testSprintData = sprintUtils.parseSprintString(testSprintString);
        }
        catch (Exception e) {
            noException = false;

            fail("Exception was thrown during Test with comma in Sprint Name.");
        }

        assertTrue(noException);

    }

    @Test
    public void parseSprintWithNullTest() {

        String testSprintString = "com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=<null>,state=<null>,name=<null>,startDate=<null>,endDate=<null>,completeDate=<null>,id=<null>]";

        testSprintData = sprintUtils.parseSprintString(testSprintString);

        assertEquals("Sprint Id was not parsed correctly.", null, testSprintData.getId());
        assertEquals("Sprint rapidViewId was not parsed correctly.", null, testSprintData.getRapidViewId());
        assertEquals("Sprint Name was not parsed correctly.", null, testSprintData.getName());
        assertEquals("Sprint State was not parsed correctly.", null, testSprintData.getState());
        assertEquals("Sprint startDate was not parsed correctly.", null, testSprintData.getStartDate());
        assertEquals("Sprint endDate was not parsed correctly.", null, testSprintData.getEndDate());
        assertEquals("Sprint completeDate was not parsed correctly.", null, testSprintData.getCompleteDate());

    }

    @Test
    public void parseSprintWithUnknownTest() {

        String testSprintString = "com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=<null>,state=<null>,new=<null>,name=<null>,startDate=<null>,endDate=<null>,completeDate=<null>,id=<null>]";

        try {

            sprintUtils.parseSprintString(testSprintString);

            assertTrue(true);

        } catch (JiraAssistantException e) {
            fail("Exception was thrown.");
        }
    }

    @Test
    public void parseSprintWithIllegalArgumentTest() {

        String testSprintString = "com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=<null>,state=<null>,name=<null>,startDate=<null>,endDate=Testdate,completeDate=<null>,id=<null>]";

        try {

            sprintUtils.parseSprintString(testSprintString);

            fail("Expected Exception was not thrown.");

        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void getSprintMetaDataFromIssueTest() {

        String sprintName = "PROJ Sprint-2014-05-07";

        JSONArray testSprints = new JSONArray();

        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-06-04,startDate=2014-05-22T07:00:00.000+01:00,endDate=2014-06-04T16:00:00.000+01:00,completeDate=2014-06-05T09:52:45.094+01:00,id=322]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@179cb815[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-05-07,startDate=2014-04-24T11:14:31.165+01:00,endDate=2014-05-07T11:14:00.000+01:00,completeDate=2014-05-08T13:07:56.284+01:00,id=320]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@31f976cf[rapidViewId=18,state=ACTIVE,name=PROJ Sprint-2014-05-21,startDate=2014-05-08T15:12:54.367+01:00,endDate=2014-05-21T15:12:00.000+01:00,completeDate=<null>,id=321]");

        when(mockedIssue.getFieldByName("Sprint")).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testSprints);

        testSprintData = sprintUtils.getSprintMetaDataFromIssue(mockedIssue, sprintName, false);

        assertEquals("Correct Sprint was not found in Sprint String.", sprintName, testSprintData.getName());

    }

    @Test(expected = JiraAssistantException.class)
    public void getSprintMetaDataFromIssueNotFoundTest() {

        String sprintName = "PROJ Sprint-2014-07-02";

        JSONArray testSprints = new JSONArray();

        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-06-04,startDate=2014-05-22T07:00:00.000+01:00,endDate=2014-06-04T16:00:00.000+01:00,completeDate=2014-06-05T09:52:45.094+01:00,id=322]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@179cb815[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-05-07,startDate=2014-04-24T11:14:31.165+01:00,endDate=2014-05-07T11:14:00.000+01:00,completeDate=2014-05-08T13:07:56.284+01:00,id=320]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@31f976cf[rapidViewId=18,state=ACTIVE,name=PROJ Sprint-2014-05-21,startDate=2014-05-08T15:12:54.367+01:00,endDate=2014-05-21T15:12:00.000+01:00,completeDate=<null>,id=321]");

        when(mockedIssue.getFieldByName("Sprint")).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testSprints);

        testSprintData = sprintUtils.getSprintMetaDataFromIssue(mockedIssue, sprintName, false);

    }

    @Test
    public void getSprintMetaDataFromIssueTestByID() {

        String sprintID = "322";

        JSONArray testSprints = new JSONArray();

        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@3fef14e0[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-06-04,startDate=2014-05-22T07:00:00.000+01:00,endDate=2014-06-04T16:00:00.000+01:00,completeDate=2014-06-05T09:52:45.094+01:00,id=322]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@179cb815[rapidViewId=18,state=CLOSED,name=PROJ Sprint-2014-05-07,startDate=2014-04-24T11:14:31.165+01:00,endDate=2014-05-07T11:14:00.000+01:00,completeDate=2014-05-08T13:07:56.284+01:00,id=320]");
        testSprints
                .put("com.atlassian.greenhopper.service.sprint.Sprint@31f976cf[rapidViewId=18,state=ACTIVE,name=PROJ Sprint-2014-05-21,startDate=2014-05-08T15:12:54.367+01:00,endDate=2014-05-21T15:12:00.000+01:00,completeDate=<null>,id=321]");

        when(mockedIssue.getFieldByName("Sprint")).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testSprints);

        testSprintData = sprintUtils.getSprintMetaDataFromIssue(mockedIssue, sprintID, true);

        assertEquals("Correct Sprint was not found in Sprint String.", sprintID, testSprintData.getId().toString());

    }

    @Test
    public void getSprintDataDeltaTest() {

        SprintDataFull sprintDataFullDummy = new SprintDataFull();
        SprintDataDelta sprintDataDelta;

        JSONObject testJsonObject;

        Map<String, Issue> addedIssues = new HashMap<>();
        Map<String, Issue> removedIssues = new HashMap<>();

        List<String> addedIssueKeys = new ArrayList<>();
        List<String> removedIssueKeys = new ArrayList<>();

        String estimationFieldName = "testfield";

        addedIssueKeys.add("TST-1");
        addedIssues.put("TST-1", mockedIssue);
        addedIssueKeys.add("TST-2");
        addedIssues.put("TST-2", mockedIssue2);

        removedIssueKeys.add("TST-3");
        removedIssues.put("TST-3", mockedIssue3);

        sprintDataFullDummy.setName("Testsprint");
        sprintDataFullDummy.setStartDate(testStartDate.toString());
        sprintDataFullDummy.setId(5);
        sprintDataFullDummy.setRapidViewId(6);

        IssueFilter issueFilter = new IssueFilter();

        try {

            testJsonObject = new JSONObject(
                    "{\"contents\":{\"puntedIssues\":[{\"key\":\"TST-3\"}],\"issueKeysAddedDuringSprint\":{\"TST-1\":true,\"TST-2\":true}}}");

            when(
                    agileRestService.getSprintReport(testCredentials, sprintDataFullDummy.getRapidViewId(),
                            sprintDataFullDummy.getId())).thenReturn(testJsonObject);

            when(mockedBasicIssueUtil.getMultipleIssues(testCredentials, addedIssueKeys, false))
                    .thenReturn(addedIssues);
            when(mockedBasicIssueUtil.getMultipleIssues(testCredentials, removedIssueKeys, false)).thenReturn(
                    removedIssues);
            when(mockedBasicIssueUtil.isIncluded(anyString(), eq(issueFilter))).thenReturn(true);
            when(mockedBasicIssueUtil.isIncluded(any(Issue.class), eq(issueFilter))).thenReturn(true);

            when(mockedAdvancedIssueUtil.getEstimation(mockedIssue, estimationFieldName)).thenReturn(3.0);
            when(mockedAdvancedIssueUtil.getEstimation(mockedIssue2, estimationFieldName)).thenReturn(3.0);
            when(mockedAdvancedIssueUtil.getEstimation(mockedIssue3, estimationFieldName)).thenReturn(6.0);

            sprintDataDelta = sprintUtils.getSprintDataDelta(testCredentials, sprintDataFullDummy, estimationFieldName, issueFilter);

            assertTrue("The added Issue Key was not contained in Delta.",
                    sprintDataDelta.getAddedIssueKeys().contains("TST-1"));
            assertTrue("The second added Issue Key was not contained in Delta.", sprintDataDelta.getAddedIssueKeys()
                    .contains("TST-2"));
            assertTrue("The removed Issue Key was not contained in Delta.", sprintDataDelta.getRemovedIssueKeys()
                    .contains("TST-3"));

            assertTrue("There were too many or to little added Issue Keys contained in Delta.", sprintDataDelta
                    .getAddedIssueKeys().size() == 2);
            assertTrue("There were too many or to little removed Issue Keys contained in Delta.", sprintDataDelta
                    .getRemovedIssueKeys().size() == 1);

            assertEquals("The number of added Issues was not right.", Integer.valueOf(2),
                    sprintDataDelta.getAddedIssues());
            assertEquals("The number of removed Issues was not right.", Integer.valueOf(1),
                    sprintDataDelta.getRemovedIssues());

            assertEquals("The Number of added StoryPoints was not right", Double.valueOf(6.0),
                    sprintDataDelta.getAddedStoryPoints());
            assertEquals("The Number of removed StoryPoints was not right", Double.valueOf(6.0),
                    sprintDataDelta.getRemovedStoryPoints());

        } catch (JSONException e) {
            e.printStackTrace();
            fail("Exception was thrown: " + e.getMessage());
        }

    }

    @Test
    public void getSprintDataDeltaTestNull() {

        SprintDataFull sprintDataFullDummy = new SprintDataFull();

        sprintDataFullDummy.setName("Testsprint");
        sprintDataFullDummy.setStartDate(testStartDate.toString());
        sprintDataFullDummy.setId(5);
        sprintDataFullDummy.setRapidViewId(null);

        String estimationFieldName = "testfield";

        try {

            sprintUtils.getSprintDataDelta(testCredentials, sprintDataFullDummy, estimationFieldName, new IssueFilter());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown: " + e.getMessage());
        }

    }

}
