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
package net.netconomy.jiraassistant.base.services;

import static org.junit.Assert.assertEquals;

import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;
import net.netconomy.jiraassistant.base.data.AccountBaseData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class AccountBaseDataServiceTest {

    @Autowired
    private AccountBaseDataService accountBaseDataService;

    private JSONObject testAccountData = new JSONObject();
    private JSONObject testLeadObject = new JSONObject();
    private JSONObject testCustomerObject = new JSONObject();
    private JSONObject testCategoryObject = new JSONObject();

    @Before
    public void setUp() throws Exception {

        testLeadObject.put("username", "testuser");
        testLeadObject.put("displayName", "Test User");

        testCustomerObject.put("key", "TST");
        testCustomerObject.put("name", "Testkunde AG");

        testCategoryObject.put("key", "DIR");
        testCategoryObject.put("name", "Directly Billable");

        testAccountData.put("id", 12);
        testAccountData.put("key", "56432");
        testAccountData.put("name", "Test Account 12");
        testAccountData.put("status", "OPEN");
        testAccountData.put("lead", testLeadObject);
        testAccountData.put("customer", testCustomerObject);
        testAccountData.put("category", testCategoryObject);

    }

    @Test
    public void parseAccountBaseDataTest() {

        AccountBaseData accountBaseData;

        accountBaseData = accountBaseDataService.parseAccountBaseData(testAccountData);

        assertEquals("parsed ID was not correct", Integer.valueOf(12), accountBaseData.getId());
        assertEquals("parsed Key was not correct", "56432", accountBaseData.getKey());
        assertEquals("parsed Name was not correct", "Test Account 12", accountBaseData.getName());
        assertEquals("parsed Status was not correct", "OPEN", accountBaseData.getStatus());
        assertEquals("parsed LeadUserName was not correct", "testuser", accountBaseData.getLeadUserName());
        assertEquals("parsed LeadName was not correct", "Test User", accountBaseData.getLeadName());
        assertEquals("parsed CustomerShortName was not correct", "TST", accountBaseData.getCustomerShortName());
        assertEquals("parsed CustomerFullName was not correct", "Testkunde AG", accountBaseData.getCustomerFullName());
        assertEquals("parsed CategoryShortName was not correct", "DIR", accountBaseData.getCategoryShortName());
        assertEquals("parsed CategoryLongName was not correct", "Directly Billable",
                accountBaseData.getCategoryLongName());

    }

    @Test
    public void parseAccountBaseDataTestNulls() {

        AccountBaseData accountBaseData;

        testAccountData.remove("lead");

        accountBaseData = accountBaseDataService.parseAccountBaseData(testAccountData);

        assertEquals("parsed ID was not correct", Integer.valueOf(12), accountBaseData.getId());
        assertEquals("parsed Key was not correct", "56432", accountBaseData.getKey());
        assertEquals("parsed Name was not correct", "Test Account 12", accountBaseData.getName());
        assertEquals("parsed Status was not correct", "OPEN", accountBaseData.getStatus());
        assertEquals("parsed CustomerShortName was not correct", "TST", accountBaseData.getCustomerShortName());
        assertEquals("parsed CustomerFullName was not correct", "Testkunde AG", accountBaseData.getCustomerFullName());
        assertEquals("parsed CategoryShortName was not correct", "DIR", accountBaseData.getCategoryShortName());
        assertEquals("parsed CategoryLongName was not correct", "Directly Billable",
                accountBaseData.getCategoryLongName());

        testAccountData.remove("customer");

        accountBaseData = accountBaseDataService.parseAccountBaseData(testAccountData);

        assertEquals("parsed ID was not correct", Integer.valueOf(12), accountBaseData.getId());
        assertEquals("parsed Key was not correct", "56432", accountBaseData.getKey());
        assertEquals("parsed Name was not correct", "Test Account 12", accountBaseData.getName());
        assertEquals("parsed Status was not correct", "OPEN", accountBaseData.getStatus());
        assertEquals("parsed CategoryShortName was not correct", "DIR", accountBaseData.getCategoryShortName());
        assertEquals("parsed CategoryLongName was not correct", "Directly Billable",
                accountBaseData.getCategoryLongName());

        testAccountData.remove("category");

        accountBaseData = accountBaseDataService.parseAccountBaseData(testAccountData);

        assertEquals("parsed ID was not correct", Integer.valueOf(12), accountBaseData.getId());
        assertEquals("parsed Key was not correct", "56432", accountBaseData.getKey());
        assertEquals("parsed Name was not correct", "Test Account 12", accountBaseData.getName());
        assertEquals("parsed Status was not correct", "OPEN", accountBaseData.getStatus());

    }

    @Test
    public void parseAccountBaseDataTestBasicNull() {

        AccountBaseData accountBaseData;

        testAccountData.remove("name");

        accountBaseData = accountBaseDataService.parseAccountBaseData(testAccountData);

        assertEquals("parsed ID was not correct", Integer.valueOf(12), accountBaseData.getId());
        assertEquals("parsed Key was not correct", "56432", accountBaseData.getKey());
        assertEquals("parsed Status was not correct", "OPEN", accountBaseData.getStatus());
        assertEquals("parsed LeadUserName was not correct", "testuser", accountBaseData.getLeadUserName());
        assertEquals("parsed LeadName was not correct", "Test User", accountBaseData.getLeadName());
        assertEquals("parsed CustomerShortName was not correct", "TST", accountBaseData.getCustomerShortName());
        assertEquals("parsed CustomerFullName was not correct", "Testkunde AG", accountBaseData.getCustomerFullName());
        assertEquals("parsed CategoryShortName was not correct", "DIR", accountBaseData.getCategoryShortName());
        assertEquals("parsed CategoryLongName was not correct", "Directly Billable",
                accountBaseData.getCategoryLongName());

    }

    @After
    public void tearDown() throws Exception {
    }

}
