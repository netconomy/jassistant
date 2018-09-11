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

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class CSVServiceTest {

    @Autowired
    private CSVService csvService;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void sanitizeCSVStringLineBreaksSemiColonTest() {

        String inputString = "This is a Test String ;-) , just to test \r how a sanitized String \n should look.";

        String actualString;
        String expectedString = "\"This is a Test String ;-) , just to test \r how a sanitized String \n should look.\"";

        char separator = ';';

        actualString = csvService.sanitizeCSVString(inputString, separator);

        assertEquals("The Sanitized String was not correct.", expectedString, actualString);

    }

    @Test
    public void sanitizeCSVStringLineBreaksTest() {

        String inputString = "This is a Test String just to test \r how a sanitized String \n should look.";

        String actualString;
        String expectedString = "\"This is a Test String just to test \r how a sanitized String \n should look.\"";

        char separator = ';';

        actualString = csvService.sanitizeCSVString(inputString, separator);

        assertEquals("The Sanitized String was not correct.", expectedString, actualString);

    }

    @Test
    public void sanitizeCSVStringSemiColonsTest() {

        String inputString = "This is a Test String; just to test how a sanitized String should look.";

        String actualString;
        String expectedString = "\"This is a Test String; just to test how a sanitized String should look.\"";

        char separator = ';';

        actualString = csvService.sanitizeCSVString(inputString, separator);

        assertEquals("The Sanitized String was not correct.", expectedString, actualString);

    }

    @Test
    public void sanitizeCSVStringNothingTest() {

        String inputString = "This is a Test String just to test how a sanitized String should look.";

        String actualString;
        String expectedString = "This is a Test String just to test how a sanitized String should look.";

        char separator = ';';

        actualString = csvService.sanitizeCSVString(inputString, separator);

        assertEquals("The Sanitized String was not correct.", expectedString, actualString);

    }

    @Test
    public void sanitizeCSVStringNullTest() {

        String inputString = null;

        String actualString;
        String expectedString = null;

        char separator = ';';

        actualString = csvService.sanitizeCSVString(inputString, separator);

        assertEquals("The Sanitized String was not correct.", expectedString, actualString);

    }

    @After
    public void tearDown() throws Exception {

    }

}
