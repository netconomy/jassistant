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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.services.FieldReaderService;

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

import static org.mockito.Mockito.*;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class FieldReaderServiceTest {

    @Autowired
    private FieldReaderService reader;

    private Issue mockedIssue = mock(Issue.class);

    private IssueField mockedField = mock(IssueField.class);

    private String mockedFieldName;
    private List<Object> expectedList;
    private List<Object> actualList;
    private String testString;
    private JSONObject testObject;
    private JSONArray testArray;

    @Before
    public void setUp() throws Exception {

        mockedFieldName = "mockedField";
        expectedList = new ArrayList<>();

        testString = "This is a Teststring";
        testObject = new JSONObject();
        testArray = new JSONArray();

        testObject.put("Testkey", "Testvalue");
        testObject.put("Testkey2", "Testvalue2");

    }

    @Test
    public void testReadComplexFieldValueNull() {

        when(mockedIssue.getFieldByName(mockedFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(null);

        actualList = reader.readFieldValue(mockedIssue, mockedFieldName);

        assertEquals(expectedList, actualList);

    }

    @Test
    public void testReadComplexFieldValueArray() throws JSONException {

        testArray.put(testString);
        testArray.put(testObject);

        expectedList.add(testString);
        expectedList.add(testObject);

        when(mockedIssue.getFieldByName(mockedFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testArray);

        actualList = reader.readFieldValue(mockedIssue, mockedFieldName);

        assertEquals(expectedList, actualList);

    }

    @Test
    public void testReadComplexFieldValueString() {

        expectedList.add(testString);

        when(mockedIssue.getFieldByName(mockedFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testString);

        actualList = reader.readFieldValue(mockedIssue, mockedFieldName);

        assertEquals(expectedList, actualList);

    }

    @Test
    public void testReadComplexFieldValueObjects() {

        expectedList.add(testObject);

        when(mockedIssue.getFieldByName(mockedFieldName)).thenReturn(mockedField);
        when(mockedField.getValue()).thenReturn(testObject);

        actualList = reader.readFieldValue(mockedIssue, mockedFieldName);

        assertEquals(expectedList, actualList);

    }

    @Test
    public void testReadComplexFieldValueUnknown() {

        when(mockedIssue.getFieldByName(mockedFieldName)).thenReturn(null);

        try {

            actualList = reader.readFieldValue(mockedIssue, mockedFieldName);

            fail("Expected Exception was not thrown.");

        } catch (JiraAssistantException e) {
            assertTrue("Expected Exception was not thrown.", true);
        }

    }

    @After
    public void tearDown() throws Exception {

        expectedList.clear();

    }

}
