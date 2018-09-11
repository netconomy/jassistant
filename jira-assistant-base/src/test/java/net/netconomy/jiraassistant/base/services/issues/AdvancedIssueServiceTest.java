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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.Worklog;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class AdvancedIssueServiceTest {

    @Autowired
    private AdvancedIssueService advancedIssueService;

    private Issue mockedIssue;
    private IssueField mockedField;
    private String estimationFieldName = "Story Points";
    private String altEstimationFieldName = "T-Shirt Size";

    private Worklog worklog1 = mock(Worklog.class);
    private Worklog worklog2 = mock(Worklog.class);
    private Worklog worklog3 = mock(Worklog.class);
    private Worklog worklog4 = mock(Worklog.class);
    private Worklog worklog5 = mock(Worklog.class);

    private List<Worklog> worklogList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        mockedIssue = mock(Issue.class);
        mockedField = mock(IssueField.class);

    }

    @Test
    public void getEstimationTest() {

        Double expected = 3.0;
        Double inputDouble = 3.0;
        Object inputObject = inputDouble;
        Double actual;

        when(mockedIssue.getFieldByName(estimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(inputObject);

        try {
            actual = advancedIssueService.getEstimation(mockedIssue, estimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }

    }

    @Test
    public void getEstimationTestNullValue() {

        Double expected = 0.0;
        Object inputObject = null;
        Double actual;

        when(mockedIssue.getFieldByName(estimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(inputObject);

        try {
            actual = advancedIssueService.getEstimation(mockedIssue, estimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getEstimationTestStrangeValue() {

        Double expected = 0.0;
        String inputString = "strange Value";
        Object inputObject = inputString;
        Double actual;

        when(mockedIssue.getFieldByName(estimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(inputObject);

        try {
            actual = advancedIssueService.getEstimation(mockedIssue, estimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getEstimationTestNull() {

        Double expected = 0.0;
        Double actual;

        when(mockedIssue.getFieldByName(estimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(null);

        try {
            actual = advancedIssueService.getEstimation(mockedIssue, estimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getAltEstimationTest() {

        String expected = "Testvalue";
        JSONObject returnObject = new JSONObject();
        String actual;

        try {

            returnObject.put("value", expected);

            when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
            when(mockedField.getValue()).thenReturn(returnObject);

            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }

    }

    @Test
    public void getAltEstimationTestNullValue() {

        String nullString = null;
        JSONObject returnObject = new JSONObject();
        String actual;

        try {

            returnObject.put("value", nullString);

            when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
            when(mockedField.getValue()).thenReturn(returnObject);

            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals("", actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getAltEstimationTestNonValue() {

        String expected = "";
        JSONObject returnObject = new JSONObject();
        String actual;

        try {

            when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
            when(mockedField.getValue()).thenReturn(returnObject);

            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getAltEstimationTestStringValue() {

        String returnString = "strange Value";
        String actual;

        when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(returnString);

        try {
            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals(returnString, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getAltEstimationTestStrangeValue() {

        String expected = "";
        Integer returnInt = 42;
        String actual;

        when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(returnInt);

        try {
            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getAltEstimationTestNull() {

        String expected = "";
        String actual;

        when(mockedIssue.getFieldByName(altEstimationFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(null);

        try {
            actual = advancedIssueService.getAltEstimation(mockedIssue, altEstimationFieldName);

            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }
    }

    @Test
    public void getMultipleChoiceFieldValuesTest() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();

        String defectReasonFieldName = "Defect Reason";

        String defectReason1 = "first Reason";
        String defectReason2 = "second Reason";

        try {
            jsonObject1.put("value", defectReason1);
            jsonObject2.put("value", defectReason2);
        } catch (Exception e) {
            fail("Exception was thrown during Test");
        }

        jsonArray.put(jsonObject1);
        jsonArray.put(jsonObject2);

        expectedResult.add(defectReason1);
        expectedResult.add(defectReason2);

        when(mockedIssue.getFieldByName(defectReasonFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(jsonArray);

        actualResult = advancedIssueService.getMultipleChoiceFieldValues(mockedIssue, defectReasonFieldName);

        assertEquals("Defect Reason was not extracted correctly.", expectedResult, actualResult);

    }

    @Test
    public void getMultipleChoiceFieldValuesNull() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        JSONArray jsonArray = null;

        String defectReasonFieldName = "Defect Reason";

        when(mockedIssue.getFieldByName(defectReasonFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(jsonArray);

        actualResult = advancedIssueService.getMultipleChoiceFieldValues(mockedIssue, defectReasonFieldName);

        assertEquals("Defect Reason was not extracted correctly.", expectedResult, actualResult);

    }

    @Test
    public void getMultipleChoiceFieldValuesTestFieldNull() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        String defectReasonFieldName = "Defect Reason";

        when(mockedIssue.getFieldByName(defectReasonFieldName)).thenReturn(null);

        actualResult = advancedIssueService.getMultipleChoiceFieldValues(mockedIssue, defectReasonFieldName);

        assertEquals("Defect Reason was not extracted correctly.", expectedResult, actualResult);

    }

    @Test
    public void getMultiValueFreeTagFieldValuesTest() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();

        String defectReasonInfoFieldName = "Defect Reason Info";

        String defectReason1 = "first Reason";
        String defectReason2 = "second Reason";

        jsonArray.put(defectReason1);
        jsonArray.put(defectReason2);

        expectedResult.add(defectReason1);
        expectedResult.add(defectReason2);

        when(mockedIssue.getFieldByName(defectReasonInfoFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(jsonArray);

        actualResult = advancedIssueService.getMultiValueFreeTagFieldValues(mockedIssue, defectReasonInfoFieldName);

        assertEquals("Defect Reason Info was not extracted correctly.", expectedResult, actualResult);

    }

    @Test
    public void getMultiValueFreeTagFieldValuesTestNull() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        JSONArray jsonArray = null;

        String defectReasonInfoFieldName = "Defect Reason Info";

        when(mockedIssue.getFieldByName(defectReasonInfoFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(jsonArray);

        actualResult = advancedIssueService.getMultiValueFreeTagFieldValues(mockedIssue, defectReasonInfoFieldName);

        assertEquals("Defect Reason Info was not extracted correctly.", expectedResult, actualResult);

    }

    @Test
    public void getMultiValueFreeTagFieldValuesTestFieldNull() {

        List<String> actualResult;
        List<String> expectedResult = new ArrayList<>();

        String defectReasonInfoFieldName = "Defect Reason Info";

        when(mockedIssue.getFieldByName(defectReasonInfoFieldName)).thenReturn(null);

        actualResult = advancedIssueService.getMultiValueFreeTagFieldValues(mockedIssue, defectReasonInfoFieldName);

        assertEquals("Defect Reason Info was not extracted correctly.", expectedResult, actualResult);

    }

    @SuppressWarnings("deprecation")
    @Test
    public void getMinutesSpentTest() {

        Integer actualResult;

        DateTime testStartDate = DateTime.now();
        DateTime testEndDate = DateTime.now().plusDays(14);

        worklogList.add(worklog1);
        worklogList.add(worklog2);
        worklogList.add(worklog3);
        worklogList.add(worklog4);
        worklogList.add(worklog5);

        when(mockedIssue.getWorklogs()).thenReturn(worklogList);

        when(worklog1.getStartDate()).thenReturn(testStartDate.minusDays(2));
        when(worklog1.getMinutesSpent()).thenReturn(15);

        when(worklog2.getStartDate()).thenReturn(testStartDate.minusHours(2));
        when(worklog2.getMinutesSpent()).thenReturn(20);

        when(worklog3.getStartDate()).thenReturn(testStartDate.plusHours(2));
        when(worklog3.getMinutesSpent()).thenReturn(25);

        when(worklog4.getStartDate()).thenReturn(testStartDate.plusDays(3));
        when(worklog4.getMinutesSpent()).thenReturn(30);

        when(worklog5.getStartDate()).thenReturn(testStartDate.plusDays(20));
        when(worklog5.getMinutesSpent()).thenReturn(35);

        actualResult = advancedIssueService.getMinutesSpent(mockedIssue, testStartDate, testEndDate);

        assertEquals("Minutes spent was not calculated correctly.", Integer.valueOf(55), actualResult);

    }

    @After
    public void tearDown() throws Exception {
    }

}
