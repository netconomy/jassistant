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
package net.netconomy.jiraassistant.base.services.filters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class ChangedIssuesFilterServiceTest {

    @Autowired
    ChangedIssuesFilterService changedIssuesFilterService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void generateReopenedFilterTestSingleNoAndClause() {
        
        String expectedFilter = "project in ('PROJ') and status changed during ('2016/6/1', '2016/6/10')";
        String actualFilter;
        String projectsString = "PROJ";
        String issueTypesString = null;
        String andClause = null;
        DateTime startDate = new DateTime(2016, 6, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 10, 0, 0, 0);
        
        actualFilter = changedIssuesFilterService.generateChangedIssuesFilter(projectsString, issueTypesString,
                andClause, startDate, endDate);

        assertEquals("Filters for reopened Issues didn't match.", expectedFilter, actualFilter);
        
    }

    @Test
    public void generateReopenedFilterTestMultipleNoAndClause() {

        String expectedFilter = "project in ('PROJ','PROJ2','PROJ3') and status changed during ('2016/6/1', '2016/6/10')";
        String actualFilter;
        String projectsString = "PROJ,PROJ2,PROJ3";
        String issueTypesString = "";
        String andClause = "";
        DateTime startDate = new DateTime(2016, 6, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 10, 0, 0, 0);

        actualFilter = changedIssuesFilterService.generateChangedIssuesFilter(projectsString, issueTypesString,
                andClause, startDate, endDate);

        assertEquals("Filters for reopened Issues didn't match.", expectedFilter, actualFilter);

    }

    @Test
    public void generateReopenedFilterTestSingleAndClause() {

        String expectedFilter = "project in ('PROJ') and status changed during ('2016/6/1', '2016/6/10') and type in ('Bug') and (type in (Arbeitspaket, 'Test'))";
        String actualFilter;
        String projectsString = "PROJ";
        String issueTypesString = "Bug";
        String andClause = "type in (Arbeitspaket, 'Test')";
        DateTime startDate = new DateTime(2016, 6, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 10, 0, 0, 0);

        actualFilter = changedIssuesFilterService.generateChangedIssuesFilter(projectsString, issueTypesString,
                andClause, startDate, endDate);

        assertEquals("Filters for reopened Issues didn't match.", expectedFilter, actualFilter);

    }

    @Test
    public void generateReopenedFilterTestMultipleAndClause() {

        String expectedFilter = "project in ('PROJ','PROJ2','PROJ3') and status changed during ('2016/6/1', '2016/6/10') and type in ('Bug','Defect') and (type in (Arbeitspaket, 'Test'))";
        String actualFilter;
        String projectsString = "PROJ,PROJ2,PROJ3";
        String issueTypesString = "Bug,Defect";
        String andClause = "type in (Arbeitspaket, 'Test')";
        DateTime startDate = new DateTime(2016, 6, 1, 0, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 10, 0, 0, 0);

        actualFilter = changedIssuesFilterService.generateChangedIssuesFilter(projectsString, issueTypesString,
                andClause, startDate, endDate);

        assertEquals("Filters for reopened Issues didn't match.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForCreatedIssuesTest() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 1, 5, 20, 5);

        projects.add("PROJ4");
        projects.add("PROJ5");

        issueTypes.add("Defect");
        issueTypes.add("Bug");

        expectedFilter = "project in ('PROJ4','PROJ5') and issuetype in ('Defect','Bug') and created >= '2015/01/01 10:10' and created <= '2015/01/05 20:05'";

        actualFilter = changedIssuesFilterService.createFilterForCreatedIssues(issueTypes, projects, null, sprintStart,
                sprintEnd);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForCreatedIssuesAndClauseTest() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 1, 5, 20, 5);

        projects.add("PROJ4");
        projects.add("PROJ5");

        issueTypes.add("Defect");
        issueTypes.add("Bug");
        
        String andClause = "priority=Blocker";

        expectedFilter = "project in ('PROJ4','PROJ5') and issuetype in ('Defect','Bug') and created >= '2015/01/01 10:10' and created <= '2015/01/05 20:05' and (priority=Blocker)";

        actualFilter = changedIssuesFilterService.createFilterForCreatedIssues(issueTypes, projects, andClause, sprintStart,
                sprintEnd);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForChangedDuringTimeToStatesTest() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        List<String> stateNames = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 5, 5, 20, 5);

        projects.add("PROJ");
        projects.add("PROJ2");

        issueTypes.add("Defect");

        stateNames.add("QA Tested");
        stateNames.add("Testing (Q)");
        stateNames.add("Customer Accepted");
        stateNames.add("Live deployed (P)");

        expectedFilter = "project in ('PROJ','PROJ2') and issuetype in ('Defect') and status changed to "
                + "('QA Tested','Testing (Q)','Customer Accepted','Live deployed (P)') after '2015/01/01 10:10' "
                + "before '2015/05/05 20:05' and status was not in ('QA Tested','Testing (Q)','Customer Accepted',"
                + "'Live deployed (P)') on '2015/01/01 10:10' and status was in ('QA Tested','Testing (Q)',"
                + "'Customer Accepted','Live deployed (P)') on '2015/05/05 20:05'";

        actualFilter = changedIssuesFilterService.createFilterForChangedDuringTimeToStates(issueTypes, projects, null,
                sprintStart, sprintEnd, stateNames, false);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForChangedDuringTimeToStatesAndClauseTest() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        List<String> stateNames = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 5, 5, 20, 5);

        projects.add("PROJ");
        projects.add("PROJ2");

        issueTypes.add("Defect");

        stateNames.add("QA Tested");
        stateNames.add("Testing (Q)");
        stateNames.add("Customer Accepted");
        stateNames.add("Live deployed (P)");

        String andClause = "priority=Blocker";

        expectedFilter = "project in ('PROJ','PROJ2') and issuetype in ('Defect') and status changed to "
                + "('QA Tested','Testing (Q)','Customer Accepted','Live deployed (P)') after '2015/01/01 10:10' "
                + "before '2015/05/05 20:05' and status was not in ('QA Tested','Testing (Q)','Customer Accepted',"
                + "'Live deployed (P)') on '2015/01/01 10:10' and status was in ('QA Tested','Testing (Q)',"
                + "'Customer Accepted','Live deployed (P)') on '2015/05/05 20:05' and (priority=Blocker)";

        actualFilter = changedIssuesFilterService.createFilterForChangedDuringTimeToStates(issueTypes, projects,
                andClause, sprintStart, sprintEnd, stateNames, false);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForChangedDuringTimeToStatesTestSimple() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        List<String> stateNames = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 5, 5, 20, 5);

        projects.add("PROJ");
        projects.add("PROJ2");

        issueTypes.add("Defect");

        stateNames.add("QA Tested");
        stateNames.add("Testing (Q)");
        stateNames.add("Customer Accepted");
        stateNames.add("Live deployed (P)");

        expectedFilter = "project in ('PROJ','PROJ2') and issuetype in ('Defect') and status changed to "
                + "('QA Tested','Testing (Q)','Customer Accepted','Live deployed (P)') after '2015/01/01 10:10' "
                + "before '2015/05/05 20:05'";

        actualFilter = changedIssuesFilterService.createFilterForChangedDuringTimeToStates(issueTypes, projects, null,
                sprintStart, sprintEnd, stateNames, true);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @Test
    public void createFilterForChangedDuringTimeToStatesAndClauseTestSimple() {

        String expectedFilter;
        String actualFilter;

        List<String> issueTypes = new ArrayList<>();
        List<String> projects = new ArrayList<>();
        List<String> stateNames = new ArrayList<>();
        DateTime sprintStart = new DateTime(2015, 1, 1, 10, 10);
        DateTime sprintEnd = new DateTime(2015, 5, 5, 20, 5);

        projects.add("PROJ");
        projects.add("PROJ2");

        issueTypes.add("Defect");

        stateNames.add("QA Tested");
        stateNames.add("Testing (Q)");
        stateNames.add("Customer Accepted");
        stateNames.add("Live deployed (P)");

        String andClause = "priority=Blocker";

        expectedFilter = "project in ('PROJ','PROJ2') and issuetype in ('Defect') and status changed to "
                + "('QA Tested','Testing (Q)','Customer Accepted','Live deployed (P)') after '2015/01/01 10:10' "
                + "before '2015/05/05 20:05' and (priority=Blocker)";

        actualFilter = changedIssuesFilterService.createFilterForChangedDuringTimeToStates(issueTypes, projects,
                andClause, sprintStart, sprintEnd, stateNames, true);

        assertEquals("The generated Filter was not correct.", expectedFilter, actualFilter);

    }

    @After
    public void tearDown() throws Exception {
    }

}
