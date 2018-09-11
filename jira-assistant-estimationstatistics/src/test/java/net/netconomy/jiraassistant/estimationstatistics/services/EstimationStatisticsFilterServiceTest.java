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
package net.netconomy.jiraassistant.estimationstatistics.services;

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

import net.netconomy.jiraassistant.estimationstatistics.EstimationStatisticsTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { EstimationStatisticsTestDIConfiguration.class })
public class EstimationStatisticsFilterServiceTest {

    @Autowired
    private EstimationStatisticsFilterService estimationStatisticsFilterService;

    private List<String> projects = new ArrayList<>();
    private String issueType;
    private DateTime startDate;
    private DateTime endDate;
    private List<String> statusListSingle = new ArrayList<>();
    private List<String> statusList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        statusListSingle.add("In Preperation");

        statusList.add("Open");
        statusList.add("In Preperation");
        statusList.add("PO Acceptance");

        projects.add("TST");
        issueType = "Bug";
        
        startDate = new DateTime(2015, 1, 1, 20, 0, 0);
        endDate = new DateTime(2015, 1, 5, 20, 0, 0);

    }

    @Test
    public void createFilterForProjectIssueTypeAndStatusListTest() {
        
        String expectedFilter;
        String actualFilter;
        
        expectedFilter = "project in ('"
                + projects.get(0)
                + "') and issuetype = '"
                + issueType
                + "' and status in ('Open','In Preperation','PO Acceptance') and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5'";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeAndStatusList(projects,
                issueType, statusList, startDate, endDate, null);
        
        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        expectedFilter = "project in ('"
                + projects.get(0)
                + "') and issuetype = '"
                + issueType
                + "' and status in ('In Preperation') and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5'";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeAndStatusList(projects,
                issueType, statusListSingle, startDate, endDate, null);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
        
    }

    @Test
    public void createFilterForProjectIssueTypeStatusListAndEstimationTest() {
        String expectedFilter;
        String actualFilter;
        Double estimation;
        String estimationFieldName;

        estimation = Double.valueOf(2);
        estimationFieldName = "Story Points";

        expectedFilter = "project in ('"
                + projects.get(0)
                + "') and issuetype = '"
                + issueType
                + "' and status in ('Open','In Preperation','PO Acceptance') and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5' and 'Story Points' = 2.0";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeStatusListAndEstimation(
                projects, issueType, statusList, estimation, estimationFieldName, startDate, endDate, null);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        expectedFilter = "project in ('"
                + projects.get(0)
                + "') and issuetype = '"
                + issueType
                + "' and status in ('In Preperation') and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5' and 'Story Points' = 2.0";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeStatusListAndEstimation(
                projects, issueType, statusListSingle, estimation, estimationFieldName, startDate, endDate, null);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
    }

    @Test
    public void createFilterForProjectIssueTypeStatusListAndEstimationAndClauseTest() {
        String expectedFilter;
        String actualFilter;
        Double estimation;
        String estimationFieldName;
        String andClause = "type = 'User Story'";

        estimation = Double.valueOf(2);
        estimationFieldName = "Story Points";

        expectedFilter = "project in ('" + projects.get(0) + "') and issuetype = '" + issueType
                + "' and status in ('Open','In Preperation','PO Acceptance') "
                + "and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5' and (" + andClause
                + ") and 'Story Points' = 2.0";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeStatusListAndEstimation(
                projects, issueType, statusList, estimation, estimationFieldName, startDate, endDate, andClause);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        expectedFilter = "project in ('" + projects.get(0) + "') and issuetype = '" + issueType
                + "' and status in ('In Preperation') "
                + "and resolutiondate >= '2015/1/1' and resolutiondate <= '2015/1/5' and (" + andClause
                + ") and 'Story Points' = 2.0";
        actualFilter = estimationStatisticsFilterService.createFilterForProjectIssueTypeStatusListAndEstimation(
                projects, issueType, statusListSingle, estimation, estimationFieldName, startDate, endDate, andClause);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
    }

    @After
    public void tearDown() throws Exception {
    }

}
