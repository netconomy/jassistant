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

import net.netconomy.jiraassistant.billing.BillingTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BillingTestDIConfiguration.class })
public class BillingJiraFilterServiceTest {

    @Autowired
    BillingJiraFilterService billingJiraFilterService;

    List<String> projectKeys = new ArrayList<>();
    List<String> accountKeys = new ArrayList<>();
    List<String> issueTypes = new ArrayList<>();
    DateTime startDate = new DateTime(2017, 1, 2, 10, 0);
    DateTime endDate = new DateTime(2017, 1, 30, 10, 0);
    String andClause = "assignee = testuser";

    @Before
    public void setUp() throws Exception {

        projectKeys.add("TST");

        accountKeys.add("1348");

        issueTypes.add("Arbeitspaket");

    }

    @Test
    public void generateFilterBasedOnProjectsTestSimple() {

        String actualFilter;
        String expectedFilter = "(project in ('TST')) and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype not in subTaskIssueTypes() order by Rank asc";

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate, null,
                null, null, false, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate,
                new ArrayList<>(), "", new ArrayList<>(), false, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnProjectsTestSimpleSubIssues() {

        String actualFilter;
        String expectedFilter = "(project in ('TST')) and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype in subTaskIssueTypes() order by Rank asc";

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate, null,
                null, null, true, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate,
                new ArrayList<>(), "", new ArrayList<>(), true, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnAccountsTestSimple() {

        String actualFilter;
        String expectedFilter = "account in ('1348') and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype not in subTaskIssueTypes() order by Rank asc";

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate, null,
                null, null,false,false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate,
                new ArrayList<>(), "", null, false, false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnAccountsTestSimpleSubIssues() {

        String actualFilter;
        String expectedFilter = "account in ('1348') and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype in subTaskIssueTypes() order by Rank asc";

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate, null,
                null, null, true, false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate,
                new ArrayList<>(), "", null, true, false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnProjectsTestFull() {

        String actualFilter;
        String expectedFilter = "(project in ('TST','TST2') or account in ('1348','2465','2389')) "
                + "and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype in ('Arbeitspaket','Bug','Defect') "
                + "and (assignee = testuser) and issuetype not in subTaskIssueTypes() order by Rank asc";

        projectKeys.add("TST2");
        accountKeys.add("2465");
        accountKeys.add("2389");
        issueTypes.add("Bug");
        issueTypes.add("Defect");

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate,
                issueTypes, andClause, accountKeys, false, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnProjectsTestFullSubIssues() {

        String actualFilter;
        String expectedFilter = "(project in ('TST','TST2') or account in ('1348','2465','2389')) "
                + "and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' and issuetype in ('Arbeitspaket','Bug','Defect') "
                + "and (assignee = testuser) and issuetype in subTaskIssueTypes() order by Rank asc";

        projectKeys.add("TST2");
        accountKeys.add("2465");
        accountKeys.add("2389");
        issueTypes.add("Bug");
        issueTypes.add("Defect");

        actualFilter = billingJiraFilterService.generateBillingFilter(projectKeys, startDate, endDate,
                issueTypes, andClause, accountKeys, true, true);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnAccountsTestFull() {

        String actualFilter;
        String expectedFilter = "account in ('1348','2465','2389') and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' "
                + "and issuetype in ('Arbeitspaket','Bug','Defect') and (assignee = testuser) and issuetype not in subTaskIssueTypes() order by Rank asc";

        accountKeys.add("2465");
        accountKeys.add("2389");
        issueTypes.add("Bug");
        issueTypes.add("Defect");

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate,
                issueTypes, andClause, null, false, false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterBasedOnAccountsTestFullSubIssues() {

        String actualFilter;
        String expectedFilter = "account in ('1348','2465','2389') and worklogDate >= '2017/1/2' and worklogDate < '2017/1/30' "
                + "and issuetype in ('Arbeitspaket','Bug','Defect') and (assignee = testuser) and issuetype in subTaskIssueTypes() order by Rank asc";

        accountKeys.add("2465");
        accountKeys.add("2389");
        issueTypes.add("Bug");
        issueTypes.add("Defect");

        actualFilter = billingJiraFilterService.generateBillingFilter(accountKeys, startDate, endDate,
                issueTypes, andClause, null,true, false);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @After
    public void tearDown() throws Exception {
    }

}
