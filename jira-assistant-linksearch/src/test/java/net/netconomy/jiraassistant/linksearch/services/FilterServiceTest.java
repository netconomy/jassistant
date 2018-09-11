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
package net.netconomy.jiraassistant.linksearch.services;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.linksearch.LinkSearchTestDIConfiguration;
import net.netconomy.jiraassistant.linksearch.data.ProjectSearchData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { LinkSearchTestDIConfiguration.class })
public class FilterServiceTest {

    @Autowired
    private FilterService filterService;

    private ProjectSearchData testProjectSearchData = new ProjectSearchData();
    private List<String> issueTypeList1 = new ArrayList<>();
    private List<String> issueTypeList2 = new ArrayList<>();
    private List<String> statusList1 = new ArrayList<>();
    private List<String> statusList2 = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        issueTypeList1.add("technische Aufgabe");
        issueTypeList1.add("Bug");

        issueTypeList2.add("Arbeitspaket");
        issueTypeList2.add("Defect");
        issueTypeList2.add("Tempo");
        issueTypeList2.add("Change Request");
        issueTypeList2.add("Story");

        statusList1.add("Open");

        statusList2.add("Closed");
        statusList2.add("Tested");
        statusList2.add("Testing Q");
        statusList2.add("Live Deployed");
        statusList2.add("Testing");
        statusList2.add("Implemented");

    }

    @Test
    public void createFilterTestSimple() {

        String expectedFilter = "project = 'TEST' and issuetype in ('technische Aufgabe','Bug') and status in ('Open')";
        String actualFilter;

        testProjectSearchData.setProject("TEST");
        testProjectSearchData.setIssueTypes(issueTypeList1);
        testProjectSearchData.setNegateIssueTypes(false);
        testProjectSearchData.setStatus(statusList1);
        testProjectSearchData.setNegateStatus(false);

        actualFilter = filterService.createFilter(testProjectSearchData);

        assertEquals(expectedFilter, actualFilter);

    }

    @Test
    public void createFilterTestSimpleNegated() {

        String expectedFilter = "project = 'TEST' and issuetype not in ('technische Aufgabe','Bug') and status not in ('Open')";
        String actualFilter;

        testProjectSearchData.setProject("TEST");
        testProjectSearchData.setIssueTypes(issueTypeList1);
        testProjectSearchData.setNegateIssueTypes(true);
        testProjectSearchData.setStatus(statusList1);
        testProjectSearchData.setNegateStatus(true);

        actualFilter = filterService.createFilter(testProjectSearchData);

        assertEquals(expectedFilter, actualFilter);

    }

    @Test
    public void createFilterTestMoreData() {

        String expectedFilter = "project = 'TEST' and issuetype not in ('Arbeitspaket','Defect','Tempo',"
                + "'Change Request','Story') and status in ('Closed','Tested','Testing Q','Live Deployed',"
                + "'Testing','Implemented')";
        String actualFilter;

        testProjectSearchData.setProject("TEST");
        testProjectSearchData.setIssueTypes(issueTypeList2);
        testProjectSearchData.setNegateIssueTypes(true);
        testProjectSearchData.setStatus(statusList2);
        testProjectSearchData.setNegateStatus(false);

        actualFilter = filterService.createFilter(testProjectSearchData);

        assertEquals(expectedFilter, actualFilter);

    }

    @Test
    public void createFilterTestAndClause() {

        String expectedFilter = "project = 'TEST' and issuetype not in ('Arbeitspaket','Defect','Tempo',"
                + "'Change Request','Story') and status in ('Closed','Tested','Testing Q','Live Deployed',"
                + "'Testing','Implemented') and timespent is not empty";
        String actualFilter;

        testProjectSearchData.setProject("TEST");
        testProjectSearchData.setIssueTypes(issueTypeList2);
        testProjectSearchData.setNegateIssueTypes(true);
        testProjectSearchData.setStatus(statusList2);
        testProjectSearchData.setNegateStatus(false);
        testProjectSearchData.setAndClause("timespent is not empty");

        actualFilter = filterService.createFilter(testProjectSearchData);

        assertEquals(expectedFilter, actualFilter);

    }

    @After
    public void tearDown() throws Exception {
    }

}
