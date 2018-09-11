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
package net.netconomy.jiraassistant.supportanalysis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;

import net.netconomy.jiraassistant.base.data.config.ClientCredentials;
import net.netconomy.jiraassistant.base.data.config.SupportConfiguration;
import net.netconomy.jiraassistant.base.services.issues.AdvancedIssueService;
import net.netconomy.jiraassistant.supportanalysis.SupportAnalysisTestDIConfiguration;
import net.netconomy.jiraassistant.supportanalysis.data.ErrorCategoryData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class, classes = { SupportAnalysisTestDIConfiguration.class })
public class AdditionalSupportDataServiceTest {

    @Autowired
    @ReplaceWithMock
    private AdvancedIssueService mockedAdvancedIssueService;

    @Autowired
    private AdditionalSupportDataService additionalSupportDataService;

    private SupportConfiguration mockedConfig = mock(SupportConfiguration.class);

    private Issue mockedIssue1 = mock(Issue.class);
    private Issue mockedIssue2 = mock(Issue.class);
    private Issue mockedIssue3 = mock(Issue.class);

    private IssueField mockedTechSevField1 = mock(IssueField.class);
    private IssueField mockedTechSevField2 = mock(IssueField.class);
    private IssueField mockedTechSevField3 = mock(IssueField.class);
    
    private ClientCredentials credentials = new ClientCredentials();

    private List<Issue> mockedIssueList = new ArrayList<>();

    private String techSevFieldName = "Technical Severity";
    private String errorCatFieldName = "Error Category";

    @Before
    public void setUp() throws Exception {

        when(mockedIssue1.getFieldByName(techSevFieldName)).thenReturn(mockedTechSevField1);
        when(mockedIssue2.getFieldByName(techSevFieldName)).thenReturn(mockedTechSevField2);
        when(mockedIssue3.getFieldByName(techSevFieldName)).thenReturn(mockedTechSevField3);
        
        mockedIssueList.add(mockedIssue1);
        mockedIssueList.add(mockedIssue2);
        mockedIssueList.add(mockedIssue3);

        when(mockedConfig.getTechnicalSeverityFieldName()).thenReturn(techSevFieldName);
        when(mockedConfig.getErrorCategoryFieldName()).thenReturn(errorCatFieldName);

    }

    @Test
    public void gatherErrorCategoryDataSimpleTest() {

        Map<String, ErrorCategoryData> actual;
        DateTime startDate = new DateTime(2016, 5, 1, 8, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 1, 8, 0, 0);
        List<String> errorCategoryList1 = new ArrayList<>();
        List<String> errorCategoryList2 = new ArrayList<>();
        List<String> errorCategoryList3 = new ArrayList<>();

        String errorCategory1 = "Error Category 1";
        String errorCategory2 = "Error Category 2";
        String errorCategory3 = "Error Category 3";

        errorCategoryList1.add(errorCategory1);
        errorCategoryList2.add(errorCategory2);
        errorCategoryList3.add(errorCategory3);

        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue1, errorCatFieldName)).thenReturn(
                errorCategoryList1);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue2, errorCatFieldName)).thenReturn(
                errorCategoryList2);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue3, errorCatFieldName)).thenReturn(
                errorCategoryList3);

        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue1, startDate, endDate, true))
                .thenReturn(30);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue2, startDate, endDate, true))
                .thenReturn(50);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue3, startDate, endDate, true))
                .thenReturn(60);

        actual = additionalSupportDataService.gatherErrorCategoryData(mockedIssueList, mockedConfig, credentials,
                startDate, endDate);

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(1), actual.get(errorCategory1)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(30), actual
                .get(errorCategory1).getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(30.0), actual
                .get(errorCategory1).getCategoryMinutesSpentAliquot());

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(1), actual.get(errorCategory2)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(50), actual
                .get(errorCategory2).getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(50.0), actual
                .get(errorCategory2).getCategoryMinutesSpentAliquot());

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(1), actual.get(errorCategory3)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(60), actual
                .get(errorCategory3).getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(60.0), actual
                .get(errorCategory3).getCategoryMinutesSpentAliquot());

    }

    @Test
    public void gatherErrorCategoryDataNullTest() {

        Map<String, ErrorCategoryData> actual;
        DateTime startDate = new DateTime(2016, 5, 1, 8, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 1, 8, 0, 0);
        List<String> errorCategoryList1 = new ArrayList<>();
        List<String> errorCategoryList2 = new ArrayList<>();
        List<String> errorCategoryList3 = new ArrayList<>();

        String errorCategory1 = "Error Category 1";
        String errorCategory2 = "Error Category 2";
        String errorCategory3 = "Error Category 3";

        errorCategoryList3.add(errorCategory3);

        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue1, errorCatFieldName)).thenReturn(
                errorCategoryList1);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue2, errorCatFieldName)).thenReturn(
                errorCategoryList2);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue3, errorCatFieldName)).thenReturn(
                errorCategoryList3);

        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue1, startDate, endDate, true))
                .thenReturn(30);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue2, startDate, endDate, true))
                .thenReturn(50);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue3, startDate, endDate, true))
                .thenReturn(60);

        actual = additionalSupportDataService.gatherErrorCategoryData(mockedIssueList, mockedConfig, credentials,
                startDate, endDate);

        try {
            assertEquals("Error Category wasn't counted correctly", null, actual.get(errorCategory1));

            assertEquals("Error Category wasn't counted correctly", null, actual.get(errorCategory2));

            assertEquals("Error Category wasn't counted correctly", Integer.valueOf(1), actual.get(errorCategory3)
                    .getCategoryCount());
            assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(60),
                    actual.get(errorCategory3).getCategoryMinutesSpentFull());
            assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(60.0),
                    actual.get(errorCategory3).getCategoryMinutesSpentAliquot());
        } catch (Exception e) {
            fail("Technical Severity couldn't handle null.");
        }

    }

    @Test
    public void gatherErrorCategoryDataMultipleTest() {

        Map<String, ErrorCategoryData> actual;
        DateTime startDate = new DateTime(2016, 5, 1, 8, 0, 0);
        DateTime endDate = new DateTime(2016, 6, 1, 8, 0, 0);
        List<String> errorCategoryList1 = new ArrayList<>();
        List<String> errorCategoryList2 = new ArrayList<>();
        List<String> errorCategoryList3 = new ArrayList<>();

        String errorCategory1 = "Error Category 1";
        String errorCategory2 = "Error Category 2";
        String errorCategory3 = "Error Category 3";

        errorCategoryList1.add(errorCategory1);
        errorCategoryList2.add(errorCategory1);
        errorCategoryList2.add(errorCategory2);
        errorCategoryList3.add(errorCategory1);
        errorCategoryList3.add(errorCategory2);
        errorCategoryList3.add(errorCategory3);

        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue1, errorCatFieldName)).thenReturn(
                errorCategoryList1);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue2, errorCatFieldName)).thenReturn(
                errorCategoryList2);
        when(mockedAdvancedIssueService.getMultipleChoiceFieldValues(mockedIssue3, errorCatFieldName)).thenReturn(
                errorCategoryList3);

        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue1, startDate, endDate, true))
                .thenReturn(30);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue2, startDate, endDate, true))
                .thenReturn(40);
        when(mockedAdvancedIssueService.getMinutesSpentWorkaround(credentials, mockedIssue3, startDate, endDate, true))
                .thenReturn(90);

        actual = additionalSupportDataService.gatherErrorCategoryData(mockedIssueList, mockedConfig, credentials,
                startDate, endDate);

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(3), actual.get(errorCategory1)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(160), actual
                .get(errorCategory1)
                .getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(80.0), actual
                .get(errorCategory1).getCategoryMinutesSpentAliquot());

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(2), actual.get(errorCategory2)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(130), actual
                .get(errorCategory2)
                .getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(50.0), actual
                .get(errorCategory2).getCategoryMinutesSpentAliquot());

        assertEquals("Error Category wasn't counted correctly", Integer.valueOf(1), actual.get(errorCategory3)
                .getCategoryCount());
        assertEquals("Time on Error Category wasn't counted correctly", Integer.valueOf(90), actual.get(errorCategory3)
                .getCategoryMinutesSpentFull());
        assertEquals("Time on Error Category wasn't counted correctly", Double.valueOf(30.0), actual
                .get(errorCategory3).getCategoryMinutesSpentAliquot());

    }

    @Test
    public void countTechnicalSeveritySimpleTest() {

        Map<String, Integer> actual;
        String techSeverityString1 = "Severity 1";
        String techSeverityString2 = "Severity 2";
        String techSeverityString3 = "Severity 3";
        String techSeverity1 = "{'self':'testurl','value':'" + techSeverityString1 + "','id':'12200'}";
        String techSeverity2 = "{'self':'testurl','value':'" + techSeverityString2 + "','id':'12200'}";
        String techSeverity3 = "{'self':'testurl','value':'" + techSeverityString3 + "','id':'12200'}";

        when(mockedTechSevField1.getValue()).thenReturn(techSeverity1);
        when(mockedTechSevField2.getValue()).thenReturn(techSeverity2);
        when(mockedTechSevField3.getValue()).thenReturn(techSeverity3);

        actual = additionalSupportDataService.countTechnicalSeverity(mockedIssueList, mockedConfig);

        assertEquals("Technical Severity wasn't counted correctly", Integer.valueOf(1), actual.get(techSeverityString1));
        assertEquals("Technical Severity wasn't counted correctly", Integer.valueOf(1), actual.get(techSeverityString2));
        assertEquals("Technical Severity wasn't counted correctly", Integer.valueOf(1), actual.get(techSeverityString3));

    }

    @Test
    public void countTechnicalSeverityNullTest() {

        Map<String, Integer> actual;
        String techSeverityString3 = "Severity 3";
        String techSeverity1 = null;
        String techSeverity2 = "{'self':'testurl','value':'null','id':'12200'}";
        String techSeverity3 = "{'self':'testurl','value':'" + techSeverityString3 + "','id':'12200'}";

        when(mockedTechSevField1.getValue()).thenReturn(techSeverity1);
        when(mockedTechSevField2.getValue()).thenReturn(techSeverity2);
        when(mockedTechSevField3.getValue()).thenReturn(techSeverity3);

        try {
            actual = additionalSupportDataService.countTechnicalSeverity(mockedIssueList, mockedConfig);

            assertEquals("Technical Severity didn't count null and or empty correctly.", 1, actual.size());
        } catch (Exception e) {
            fail("Technical Severity couldn't handle null.");
        }

    }

    @Test
    public void countTechnicalSeverityMultipleTest() {

        Map<String, Integer> actual;
        String techSeverityString1 = "Severity 1";
        String techSeverityString2 = "Severity 2";
        String techSeverity1 = "{'self':'testurl','value':'" + techSeverityString1 + "','id':'12200'}";
        String techSeverity2 = "{'self':'testurl','value':'" + techSeverityString2 + "','id':'12200'}";

        when(mockedTechSevField1.getValue()).thenReturn(techSeverity1);
        when(mockedTechSevField2.getValue()).thenReturn(techSeverity2);
        when(mockedTechSevField3.getValue()).thenReturn(techSeverity1);

        actual = additionalSupportDataService.countTechnicalSeverity(mockedIssueList, mockedConfig);

        assertEquals("Technical Severity wasn't counted correctly", Integer.valueOf(2), actual.get(techSeverityString1));
        assertEquals("Technical Severity wasn't counted correctly", Integer.valueOf(1), actual.get(techSeverityString2));

    }

    @After
    public void tearDown() throws Exception {
    }

}
