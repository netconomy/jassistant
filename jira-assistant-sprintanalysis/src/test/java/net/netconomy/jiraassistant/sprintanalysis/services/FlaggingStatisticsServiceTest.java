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
package net.netconomy.jiraassistant.sprintanalysis.services;

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
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atlassian.jira.rest.client.api.domain.Issue;

import net.netconomy.jiraassistant.base.data.config.ProjectConfiguration;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.SprintAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatisticsPartData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { SprintAnalysisTestDIConfiguration.class })
public class FlaggingStatisticsServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private FlaggingStatisticsService flaggingStatisticsService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private DateTime testStart = DateTime.now();
    private DateTime testEnd = DateTime.now().plusDays(14);
    private Duration testDuration0 = new Duration("PT0S");
    private Duration testDuration30 = new Duration("PT1800S");
    private Duration testDuration60 = new Duration("PT3600S");

    private List<Issue> mockedIssuesList = new ArrayList<>();

    private Issue mockedIssue1 = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);
    private Issue mockedIssue4 = mock(Issue.class);
    private Issue mockedIssue5 = mock(Issue.class);
    private Issue mockedIssue6 = mock(Issue.class);

    private ProjectConfiguration mockedConfiguration = mock(ProjectConfiguration.class);

    private static final String FIELDNAME = "Markiert";
    private static final String FIELDVALUE = "Hindernis";

    @Before
    public void setUp() throws Exception {

        when(mockedConfiguration.getFlaggedFieldName()).thenReturn(FIELDNAME);
        when(mockedConfiguration.getFlaggedFieldValue()).thenReturn(FIELDVALUE);

        when(mockedConfigurationService.getProjectConfiguration()).thenReturn(mockedConfiguration);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue1, FIELDNAME, testStart)).thenReturn(null);
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue1, FIELDNAME, testEnd)).thenReturn("Hindernis");
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue1, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(1);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue1, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration30);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue2, FIELDNAME, testStart)).thenReturn("Hindernis");
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue2, FIELDNAME, testEnd)).thenReturn("");
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue2, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(1);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue2, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration30);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue3, FIELDNAME, testStart)).thenReturn("");
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue3, FIELDNAME, testEnd)).thenReturn("");
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue3, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(0);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue3, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration0);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue4, FIELDNAME, testStart)).thenReturn("Hindernis");
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue4, FIELDNAME, testEnd)).thenReturn("Hindernis");
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue4, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(2);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue4, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration60);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue5, FIELDNAME, testStart)).thenReturn(null);
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue5, FIELDNAME, testEnd)).thenReturn(null);
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue5, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(3);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue5, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration60);

        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue6, FIELDNAME, testStart)).thenReturn("");
        when(mockedHistoryIssueService.getFieldValueAtTime(mockedIssue6, FIELDNAME, testEnd)).thenReturn("");
        when(mockedHistoryIssueService.getValueCountForIssueField(mockedIssue6, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(1);
        when(mockedHistoryIssueService.getValueDurationForIssueField(mockedIssue6, FIELDNAME, FIELDVALUE, testStart, testEnd))
                .thenReturn(testDuration30);

        mockedIssuesList.add(mockedIssue1);
        mockedIssuesList.add(mockedIssue2);
        mockedIssuesList.add(mockedIssue3);
        mockedIssuesList.add(mockedIssue4);
        mockedIssuesList.add(mockedIssue5);

    }

    @Test
    public void processIssueTestStartEnd() throws ConfigurationException {

        FlaggingStatisticsPartData flaggingStatisticsPartData = new FlaggingStatisticsPartData();

        flaggingStatisticsService.processIssue(mockedIssue1, flaggingStatisticsPartData, testStart, testEnd);

        assertEquals("Number of Flagged Issues at the Start was not correct", Integer.valueOf(0),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart());
        assertEquals("Number of Flagged Issues at the End was not correct", Integer.valueOf(1),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd());

        flaggingStatisticsService.processIssue(mockedIssue2, flaggingStatisticsPartData, testStart, testEnd);

        assertEquals("Number of Flagged Issues at the Start was not correct", Integer.valueOf(1),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart());
        assertEquals("Number of Flagged Issues at the End was not correct", Integer.valueOf(1),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd());

        flaggingStatisticsService.processIssue(mockedIssue3, flaggingStatisticsPartData, testStart, testEnd);

        assertEquals("Number of Flagged Issues at the Start was not correct", Integer.valueOf(1),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart());
        assertEquals("Number of Flagged Issues at the End was not correct", Integer.valueOf(1),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd());

        flaggingStatisticsService.processIssue(mockedIssue4, flaggingStatisticsPartData, testStart, testEnd);

        assertEquals("Number of Flagged Issues at the Start was not correct", Integer.valueOf(2),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart());
        assertEquals("Number of Flagged Issues at the End was not correct", Integer.valueOf(2),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd());

        flaggingStatisticsService.processIssue(mockedIssue5, flaggingStatisticsPartData, testStart, testEnd);

        assertEquals("Number of Flagged Issues at the Start was not correct", Integer.valueOf(2),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesStart());
        assertEquals("Number of Flagged Issues at the End was not correct", Integer.valueOf(2),
                flaggingStatisticsPartData.getNumberOfFlaggedIssuesEnd());

    }

    @Test
    public void generateFlaggingStatisticsPartDataTest() throws ConfigurationException {
        
        FlaggingStatisticsPartData flaggingStatisticsPartData;
        List<Issue> testIssueList = new ArrayList<>();
        
        testIssueList.add(mockedIssue1);
        testIssueList.add(mockedIssue2);
        testIssueList.add(mockedIssue3);
        testIssueList.add(mockedIssue4);
        testIssueList.add(mockedIssue5);
        testIssueList.add(mockedIssue6);
        
        flaggingStatisticsPartData = flaggingStatisticsService.generateFlaggingStatisticsPartData(testIssueList,
                    testStart, testEnd);

        assertEquals("Sum of FlaggingDurations was not Correct", Integer.valueOf(210),
                flaggingStatisticsPartData.getSumOfFlaggingDurationsMinutes());

        assertEquals("Mean of FlaggingDurations was not Correct", Double.valueOf(42),
                flaggingStatisticsPartData.getMeanOfFlaggingDurationsMinutes());

        assertEquals("Median of FlaggingDurations was not Correct", Double.valueOf(30),
                flaggingStatisticsPartData.getMedianOfFlaggingDurationsMinutes());

    }

    @After
    public void tearDown() throws Exception {
    }

}
