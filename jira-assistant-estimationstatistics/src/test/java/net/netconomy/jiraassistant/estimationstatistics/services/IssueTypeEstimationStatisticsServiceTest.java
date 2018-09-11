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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.estimationstatistics.EstimationStatisticsTestDIConfiguration;
import net.netconomy.jiraassistant.estimationstatistics.services.IssueTypeEstimationStatisticsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { EstimationStatisticsTestDIConfiguration.class })
public class IssueTypeEstimationStatisticsServiceTest {

    @Autowired
    private IssueTypeEstimationStatisticsService issueTypeEstimationStatisticsService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private Issue mockedIssue = mock(Issue.class);

    private ProjectConfiguration mockedProjectConfiguration = mock(ProjectConfiguration.class);

    @Before
    public void setUp() throws Exception {
    }

    private Duration durationEstimationToInProgressTest(DateTime lastEstimationDate, DateTime firstInProgressDate) {

        List<String> inProgressStatusList = new ArrayList<>();
        String estimationFieldName = "Story Points";
        Duration actualDuration = null;
        
        inProgressStatusList.add("In Progress");

        try {

            when(mockedConfigurationService.getProjectConfiguration()).thenReturn(mockedProjectConfiguration);
            when(mockedProjectConfiguration.getEstimationFieldName()).thenReturn(estimationFieldName);
            when(mockedProjectConfiguration.getInProgressStatus()).thenReturn(inProgressStatusList);
            when(mockedHistoryIssueService.getDateTimeOfLastFieldChange(mockedIssue, estimationFieldName)).thenReturn(
                    lastEstimationDate);
            when(mockedHistoryIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, inProgressStatusList))
                    .thenReturn(firstInProgressDate);

            actualDuration = issueTypeEstimationStatisticsService.getTimeFromEstimationToInProgress(mockedIssue, false);
        
        } catch (ConfigurationException e) {
            fail("Exception was thrown during Test.");
        }
        
        return actualDuration;

    }

    @Test
    public void getTimeFromEstimationAndFinishedTestShort() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime lastEstimationDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstInProgressDate = new DateTime(2014, 1, 3, 20, 0, 0);

        actualDuration = durationEstimationToInProgressTest(lastEstimationDate, firstInProgressDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(2, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromEstimationAndFinishedTestLong() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime lastEstimationDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstInProgressDate = new DateTime(2014, 1, 20, 20, 0, 0);

        actualDuration = durationEstimationToInProgressTest(lastEstimationDate, firstInProgressDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(19, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromEstimationAndFinishedTestVeryLong() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime lastEstimationDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstInProgressDate = new DateTime(2014, 2, 15, 20, 0, 0);

        actualDuration = durationEstimationToInProgressTest(lastEstimationDate, firstInProgressDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(45, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromEstimationAndFinishedTestNull() {

        Duration actualDuration;

        DateTime lastEstimationDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstInProgressDate = null;

        actualDuration = durationEstimationToInProgressTest(lastEstimationDate, firstInProgressDate);

        assertEquals(null, actualDuration);

    }

    private Duration durationInProgressToFinishedTest(DateTime firstInProgressDate, DateTime firstFinishedDate) {

        List<String> inProgressStatusList = new ArrayList<>();
        List<String> finishedStatusList = new ArrayList<>();
        Duration actualDuration = null;

        inProgressStatusList.add("In Progress");
        finishedStatusList.add("Finished");

        try {

            when(mockedConfigurationService.getProjectConfiguration()).thenReturn(mockedProjectConfiguration);
            when(mockedProjectConfiguration.getInProgressStatus()).thenReturn(inProgressStatusList);
            when(mockedProjectConfiguration.getFinishedStatus()).thenReturn(finishedStatusList);
            when(mockedHistoryIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, inProgressStatusList))
                    .thenReturn(firstInProgressDate);
            when(mockedHistoryIssueService.getDateTimeOfFirstStatusInSpecifiedList(mockedIssue, finishedStatusList))
                    .thenReturn(firstFinishedDate);

            actualDuration = issueTypeEstimationStatisticsService.getTimeFromInProgressToFinished(mockedIssue);

        } catch (ConfigurationException e) {
            fail("Exception was thrown during Test.");
        }

        return actualDuration;

    }

    @Test
    public void getTimeFromInProgressToFinishedTestShort() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime firstInProgressDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstFinishedDate = new DateTime(2014, 1, 3, 20, 0, 0);

        actualDuration = durationInProgressToFinishedTest(firstInProgressDate, firstFinishedDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(2, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromInProgressToFinishedTestLong() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime firstInProgressDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstFinishedDate = new DateTime(2014, 1, 20, 20, 0, 0);

        actualDuration = durationInProgressToFinishedTest(firstInProgressDate, firstFinishedDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(19, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromInProgressToFinishedTestVeryLong() {

        Duration actualDuration;
        Long actualDurationLong;

        DateTime firstInProgressDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstFinishedDate = new DateTime(2014, 2, 15, 20, 0, 0);

        actualDuration = durationInProgressToFinishedTest(firstInProgressDate, firstFinishedDate);

        actualDurationLong = Long.valueOf(actualDuration.getStandardDays());

        assertEquals(45, actualDurationLong.intValue());

    }

    @Test
    public void getTimeFromInProgressToFinishedTestNull() {

        Duration actualDuration;

        DateTime firstInProgressDate = new DateTime(2014, 1, 1, 20, 0, 0);
        DateTime firstFinishedDate = null;

        actualDuration = durationInProgressToFinishedTest(firstInProgressDate, firstFinishedDate);

        assertEquals(null, actualDuration);

    }

    @After
    public void tearDown() throws Exception {
    }

}
