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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
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

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.base.services.issues.HistoryIssueService;
import net.netconomy.jiraassistant.sprintanalysis.SprintAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.sprintanalysis.data.defectreasons.DefectReasonSingleData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { SprintAnalysisTestDIConfiguration.class })
public class AdditionalDefectBugStatisticsServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AdditionalDefectBugStatisticsService additionalDefectBugStatisticsService;

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    @ReplaceWithMock
    private HistoryIssueService mockedHistoryIssueService;

    @Autowired
    @ReplaceWithMock
    private ConfigurationService mockedConfigurationService;

    private DateTime testStartDate = DateTime.now();
    private DateTime testEndDate = DateTime.now().plusDays(14);

    private Issue mockedIssue = mock(Issue.class);

    private String defectReasonFieldName = "Defect Reason";
    private String defectReasonInfoFieldName = "Defect Reason Info";

    private ClientCredentials credentials = new ClientCredentials();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void generateSingleDefectReasonDataTest() {

        DefectReasonSingleData actualSingleDefectReasonData = new DefectReasonSingleData();

        List<String> defectReasonsList = new ArrayList<>();
        List<String> defectReasonInfosList = new ArrayList<>();

        defectReasonsList.add("Reason1");
        defectReasonsList.add("Reason2");
        defectReasonsList.add("Reason3");

        defectReasonInfosList.add("ReasonInfo1");
        defectReasonInfosList.add("ReasonInfo2");
        defectReasonInfosList.add("ReasonInfo3");

        when(mockedIssue.getKey()).thenReturn("TESTKEY-1");
        when(
                mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue, testStartDate,
                        testEndDate, false)).thenReturn(30);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue, defectReasonFieldName)).thenReturn(
                defectReasonsList);
        when(mockedAdvancedIssueService.getMultiValueFreeTagFieldValues(mockedIssue, defectReasonInfoFieldName))
                .thenReturn(defectReasonInfosList);

        actualSingleDefectReasonData = additionalDefectBugStatisticsService.generateSingleDefectReasonData(mockedIssue,
                testStartDate, testEndDate, defectReasonFieldName, defectReasonInfoFieldName, credentials);

        assertEquals("minutesSpent was not extracted correctly", Integer.valueOf(30),
                actualSingleDefectReasonData.getMinutesSpent());
        assertEquals("defectReasonsList was not extracted correctly", defectReasonsList,
                actualSingleDefectReasonData.getDefectReasons());
        assertEquals("defectReasonInfosList was not extracted correctly", defectReasonInfosList,
                actualSingleDefectReasonData.getDefectReasonInfos());

        assertEquals("minutesSpentAliquot on Reason1 was not calculated correctly", Double.valueOf(10),
                actualSingleDefectReasonData.getMinutesPerReasonAliquot().get("Reason1"));
        assertEquals("minutesSpentAliquot on Reason2 was not calculated correctly", Double.valueOf(10),
                actualSingleDefectReasonData.getMinutesPerReasonAliquot().get("Reason2"));
        assertEquals("minutesSpentAliquot on Reason3 was not calculated correctly", Double.valueOf(10),
                actualSingleDefectReasonData.getMinutesPerReasonAliquot().get("Reason3"));

    }

    @After
    public void tearDown() throws Exception {
    }

}
