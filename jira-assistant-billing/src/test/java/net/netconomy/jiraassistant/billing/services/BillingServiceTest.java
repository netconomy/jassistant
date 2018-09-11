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
package net.netconomy.jiraassistant.billing.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.Subtask;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.billing.BillingTestDIConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BillingTestDIConfiguration.class })
public class BillingServiceTest {

    @Autowired
    private BillingService billingService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getPersonDaysTest() {

        assertEquals("Person Days Calculation was not correct.", Double.valueOf(0.125),
                billingService.getPersonDays(60, 8.0));

        assertEquals("Person Days Calculation was not correct.", Double.valueOf(1),
                billingService.getPersonDays(462, 7.7));

        assertEquals("Person Days Calculation was not correct.", Double.valueOf(0.75),
                billingService.getPersonDays(360, 8.0));

    }

    @Test
    public void roundPersonDaysTest() {

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(2.25),
                billingService.roundPersonDays(2.1));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(2.0),
                billingService.roundPersonDays(2.0));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(3.0),
                billingService.roundPersonDays(2.9));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(2.5),
                billingService.roundPersonDays(2.3));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(1.75),
                billingService.roundPersonDays(1.71));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(1.75),
                billingService.roundPersonDays(1.7009));

        assertEquals("Rounding up didn't produce the expected Results.", Double.valueOf(1.0),
                billingService.roundPersonDays(0.750001));
    }

    @Test
    public void parentContainedInList() {

        Issue subIssue = mock(Issue.class);

        Issue issue1 = mock(Issue.class);
        Issue issue2 = mock(Issue.class);
        Issue issue3 = mock(Issue.class);

        Subtask subtask1 = mock(Subtask.class);
        Subtask subtask2 = mock(Subtask.class);
        Subtask subtask3 = mock(Subtask.class);
        Subtask subtask4 = mock(Subtask.class);
        Subtask subtask5 = mock(Subtask.class);

        List<Subtask> subtasks1 = new ArrayList<>();
        List<Subtask> subtasks2 = new ArrayList<>();
        List<Subtask> subtasks3 = new ArrayList<>();

        List<Issue> issueList = new ArrayList<>();

        when(subIssue.getKey()).thenReturn("KEY-5");

        when(subtask1.getIssueKey()).thenReturn("KEY-1");
        when(subtask2.getIssueKey()).thenReturn("KEY-2");
        when(subtask3.getIssueKey()).thenReturn("KEY-3");
        when(subtask4.getIssueKey()).thenReturn("KEY-4");
        when(subtask5.getIssueKey()).thenReturn("KEY-5");

        subtasks1.add(subtask1);
        subtasks1.add(subtask2);
        subtasks2.add(subtask3);
        subtasks2.add(subtask4);
        subtasks3.add(subtask5);

        when(issue1.getSubtasks()).thenReturn(subtasks1);
        when(issue2.getSubtasks()).thenReturn(subtasks2);
        when(issue3.getSubtasks()).thenReturn(subtasks3);

        issueList.add(issue1);
        issueList.add(issue2);

        assertEquals("Parent was found, even tough it was not contained", false,
                billingService.parentContainedInList(subIssue, issueList));

        issueList.add(issue3);

        assertEquals("Parent was not found, even tough it was contained", true,
                billingService.parentContainedInList(subIssue, issueList));

    }

    @Test
    public void fetchAdditionalFieldsTest() {

        List<String> additionalFields = new ArrayList<>();
        Map<String, String> actualMap;
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();

        Issue issue = mock(Issue.class);

        IssueField issueField1 = mock(IssueField.class);
        IssueField issueField2 = mock(IssueField.class);
        IssueField issueField3 = mock(IssueField.class);
        IssueField issueField4 = mock(IssueField.class);
        IssueField issueField5 = mock(IssueField.class);
        IssueField issueField6 = mock(IssueField.class);
        IssueField issueField7 = mock(IssueField.class);

        try {
            jsonObject1.put("value", "testString 2");
            jsonObject2.put("self", "selfString");
        } catch (JSONException e) {
            fail("JSONException: " + e.getMessage());
        }

        jsonArray1.put(jsonObject1);
        jsonArray1.put(jsonObject2);

        jsonArray2.put(jsonObject2);

        additionalFields.add("Field 1");
        additionalFields.add("Field 2");
        additionalFields.add("Field 3");
        additionalFields.add("Field 4");
        additionalFields.add("Field 5");
        additionalFields.add("Field 6");
        additionalFields.add("Field 7");
        additionalFields.add("Field 8");

        when(issueField1.getValue()).thenReturn("testString");
        when(issueField2.getValue()).thenReturn(jsonObject1);
        when(issueField3.getValue()).thenReturn(jsonObject2);
        when(issueField4.getValue()).thenReturn(jsonArray1);
        when(issueField5.getValue()).thenReturn(jsonArray2);
        when(issueField6.getValue()).thenReturn(jsonArray3);
        when(issueField7.getValue()).thenReturn(null);

        when(issue.getFieldByName("Field 1")).thenReturn(issueField1);
        when(issue.getFieldByName("Field 2")).thenReturn(issueField2);
        when(issue.getFieldByName("Field 3")).thenReturn(issueField3);
        when(issue.getFieldByName("Field 4")).thenReturn(issueField4);
        when(issue.getFieldByName("Field 5")).thenReturn(issueField5);
        when(issue.getFieldByName("Field 6")).thenReturn(issueField6);
        when(issue.getFieldByName("Field 7")).thenReturn(issueField7);
        when(issue.getFieldByName("Field 8")).thenReturn(null);


        actualMap = billingService.fetchAdditionalFields(issue, additionalFields);

        //Field 7 and 8 don't contain anything
        assertEquals("Not the right amount of additional Fields read.", 6, actualMap.size());

        assertEquals("Simple String not read correctly", "testString", actualMap.get("Field 1"));
        assertEquals("String from JSON Object not read correctly", "testString 2", actualMap.get("Field 2"));

        assertEquals("JSON Object without Value not read correctly", jsonObject2.toString(), actualMap.get("Field 3"));

        assertEquals("JSON Array with one String and one JSON Object not read correctly", "testString 2, " + jsonObject2.toString(), actualMap.get("Field 4"));
        assertEquals("JSON Array with one JSON Object not read correctly", jsonObject2.toString(), actualMap.get("Field 5"));

        //the JSON Array is empty
        assertEquals("Simple String not read correctly", "", actualMap.get("Field 6"));

    }

    @After
    public void tearDown() throws Exception {
    }

}
