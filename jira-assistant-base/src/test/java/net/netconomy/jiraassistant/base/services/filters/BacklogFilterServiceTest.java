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
public class BacklogFilterServiceTest {

    @Autowired
    private BacklogFilterService backlogFilterUtil;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void generateBacklogFilterBasedOnFilterWithOrderTest() {

        String expectedFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Rank asc";
        String actualFilter = "";

        String givenFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Rank desc";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnFilter(givenFilter);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        givenFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Summary";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnFilter(givenFilter);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
    }

    @Test
    public void generateBacklogFilterBasedOnFilterWithoutOrderTest() {

        String expectedFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Rank asc";
        String actualFilter = "";

        String givenFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo)";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnFilter(givenFilter);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
    }

    @Test
    public void generateBacklogFilterBasedOnFilterWithAndClauseTest() {

        String expectedFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) and (timespent is not empty) order by Rank asc";
        String actualFilter = "";

        String andClauseString = "timespent is not empty";

        String givenFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Rank desc";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnFilter(givenFilter, andClauseString);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        givenFilter = "project in ('PROJ1', 'PROJ2', 'PROJ3') AND type not in (Test, Tempo) order by Summary";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnFilter(givenFilter, andClauseString);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);
    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesWithAndClauseTest() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and (labels in (Test)) "
                + "and status not in('Closed','Tested','Live Deployed (P)','Done') "
                + "and issuetype not in ('Bug','Defect') and (sprint not in openSprints() or sprint is empty) "
                + "order by Rank asc";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";
        String andClauseString = "labels in (Test)";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, andClauseString, null,
                excludedStatus, excludedTypes, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesNoAndClauseTest() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and status not in('Closed','Tested',"
                + "'Live Deployed (P)','Done') and issuetype not in ('Bug','Defect') "
                + "and (sprint not in openSprints() or sprint is empty) order by Rank asc";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";
        String andClauseString = "";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, andClauseString, null,
                excludedStatus, excludedTypes, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesNoTypesOrStatusTest() {

        String expectedFilter = "";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";
        String andClauseString = "";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();
        List<String> emptyList = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and status not in('Closed','Tested',"
                + "'Live Deployed (P)','Done') and (sprint not in openSprints() or sprint is empty) "
                + "order by Rank asc";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, andClauseString, null,
                excludedStatus, emptyList, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

        expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and issuetype not in ('Bug','Defect') "
                + "and (sprint not in openSprints() or sprint is empty) order by Rank asc";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, andClauseString, null,
                emptyList, excludedTypes, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesNullTest() {

        String expectedFilter = "";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";

        expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') order by Rank asc";

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, null, null, null, null,
                null);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesWithoutSprintRestriction() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') "
                + "and status not in('Closed','Tested','Live Deployed (P)','Done') "
                + "and issuetype not in ('Bug','Defect') order by Rank asc";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, null, null,
                excludedStatus,
                excludedTypes, false);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesWithProjectsListTest() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and (labels in (Test)) "
                + "and status not in('Closed','Tested','Live Deployed (P)','Done') "
                + "and issuetype not in ('Bug','Defect') and (sprint not in openSprints() or sprint is empty) "
                + "order by Rank asc";
        String actualFilter = "";

        String andClauseString = "labels in (Test)";

        List<String> projectsList = new ArrayList<>();
        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        projectsList.add("PROJ2");
        projectsList.add("PROJ1");
        projectsList.add("PROJ3");

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsList, andClauseString,
                null,
                excludedStatus, excludedTypes, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsExcludesWithSecondAndClauseTest() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and (labels in (Test)) and (status changed during ('date1', 'date 2')) "
                + "and status not in('Closed','Tested','Live Deployed (P)','Done') "
                + "and issuetype not in ('Bug','Defect') and (sprint not in openSprints() or sprint is empty) "
                + "order by Rank asc";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";
        String andClauseString = "labels in (Test)";
        String andClause2String = "status changed during ('date1', 'date 2')";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjectsExcludes(projectsString, andClauseString,
                andClause2String, excludedStatus, excludedTypes, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateBacklogFilterBasedOnProjectsWithIncludesTest() {

        String expectedFilter = "project in ('PROJ2','PROJ1','PROJ3') and (labels in (Test)) "
                + "and status in('Closed','Tested','Live Deployed (P)','Done') "
                + "and issuetype in ('Bug','Defect') and (sprint not in openSprints() or sprint is empty) "
                + "order by Rank asc";
        String actualFilter = "";

        String projectsString = "PROJ2,PROJ1,PROJ3";
        String andClauseString = "labels in (Test)";

        List<String> excludedStatus = new ArrayList<>();
        List<String> excludedTypes = new ArrayList<>();

        excludedStatus.add("Closed");
        excludedStatus.add("Tested");
        excludedStatus.add("Live Deployed (P)");
        excludedStatus.add("Done");

        excludedTypes.add("Bug");
        excludedTypes.add("Defect");

        actualFilter = backlogFilterUtil.generateBacklogFilterBasedOnProjects(projectsString, andClauseString, null,
                excludedStatus, excludedTypes, false, true);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterForIssueKeysTestSingle() {

        String expectedFilter = "key in (PROJ-5050) order by key";
        String actualFilter = "";

        List<String> keyList = new ArrayList<>();

        keyList.add("PROJ-5050");

        actualFilter = backlogFilterUtil.generateFilterForIssueKeys(keyList);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterForIssueKeysTestMulitple() {

        String expectedFilter = "key in (PROJ-5050,PROJ-5051,PROJ-5052) order by key";
        String actualFilter = "";

        List<String> keyList = new ArrayList<>();

        keyList.add("PROJ-5050");
        keyList.add("PROJ-5051");
        keyList.add("PROJ-5052");

        actualFilter = backlogFilterUtil.generateFilterForIssueKeys(keyList);

        assertEquals("generated Filter did not match Expectations", expectedFilter, actualFilter);

    }

    @Test
    public void generateFilterForIssueKeysTestNone() {

        String actualFilter = "";

        List<String> keyList = new ArrayList<>();

        actualFilter = backlogFilterUtil.generateFilterForIssueKeys(keyList);

        assertEquals("generated Filter did not match Expectations", null, actualFilter);

    }

    @Test
    public void generateFilterForIssueKeysTestNull() {

        String actualFilter = "";

        List<String> keyList = null;

        actualFilter = backlogFilterUtil.generateFilterForIssueKeys(keyList);

        assertEquals("generated Filter did not match Expectations", null, actualFilter);

    }

    @After
    public void tearDown() throws Exception {
    }

}
