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
package net.netconomy.jiraassistant.reopenfactor.services;

import com.atlassian.jira.rest.client.api.domain.Issue;
import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.reopenfactor.ReopenFactorTestDIConfiguration;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = {ReopenFactorTestDIConfiguration.class })
public class ReopenFactorIssueServiceTest {

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService historyIssueService;

    @Autowired
    private ReopenFactorIssueService reopenFactorIssueService;

    private Issue mockedIssue = mock(Issue.class);

    private ProjectConfiguration mockedConfig = mock(ProjectConfiguration.class);

    private List<String> statusList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void firstReopenAfterTestableTest() {

        DateTime firstTimeInTestableState = new DateTime(2016, 6, 1, 20, 0, 0);
        DateTime expected = new DateTime(2016, 7, 1, 20, 0, 0);
        DateTime actual;

        when(mockedConfig.getTestableStatus()).thenReturn(statusList);
        when(mockedConfig.getReopenedStatus()).thenReturn(statusList);

        when(historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, statusList)).thenReturn(firstTimeInTestableState);
        when(historyIssueService.getDateTimeOfFirstStatusInSpecifiedListAfter(mockedIssue, statusList, firstTimeInTestableState)).thenReturn(expected);

        actual = reopenFactorIssueService.firstReopenAfterTestable(mockedIssue, mockedConfig);

        assertEquals(expected, actual);

    }

    @Test
    public void firstReopenAfterTestableTestNull() {

        DateTime actual;

        when(mockedConfig.getTestableStatus()).thenReturn(statusList);
        when(mockedConfig.getReopenedStatus()).thenReturn(statusList);

        when(historyIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, statusList)).thenReturn(null);

        actual = reopenFactorIssueService.firstReopenAfterTestable(mockedIssue, mockedConfig);

        assertNull(actual);

    }

    @After
    public void tearDown() throws Exception {
    }

}
