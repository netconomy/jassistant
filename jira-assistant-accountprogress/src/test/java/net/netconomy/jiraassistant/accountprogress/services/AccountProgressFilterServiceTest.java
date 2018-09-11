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
package net.netconomy.jiraassistant.accountprogress.services;

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

import net.netconomy.jiraassistant.accountprogress.AccountProgressTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { AccountProgressTestDIConfiguration.class })
public class AccountProgressFilterServiceTest {

    @Autowired
    private AccountProgressFilterService accountProgressFilterService;

    private List<Integer> accountIDs = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void generateAccountFilterTest() {

        String expectedFilter = "account in (34) and (type = Arbeitspaket) order by Rank asc";
        String actualFilter;

        accountIDs.add(34);

        actualFilter = accountProgressFilterService.generateAccountFilter(accountIDs, "type = Arbeitspaket");

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateAccountFilterTestNull() {

        String expectedFilter = "account in (34) order by Rank asc";
        String actualFilter;

        accountIDs.add(34);

        actualFilter = accountProgressFilterService.generateAccountFilter(accountIDs, null);

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @Test
    public void generateAccountFilterTestMulti() {

        String expectedFilter = "account in (34,35,39) and (type = Arbeitspaket) order by Rank asc";
        String actualFilter;

        accountIDs.add(34);
        accountIDs.add(35);
        accountIDs.add(39);

        actualFilter = accountProgressFilterService.generateAccountFilter(accountIDs, "type = Arbeitspaket");

        assertEquals("The generated Filter did not fit", expectedFilter, actualFilter);

    }

    @After
    public void tearDown() throws Exception {
    }

}
