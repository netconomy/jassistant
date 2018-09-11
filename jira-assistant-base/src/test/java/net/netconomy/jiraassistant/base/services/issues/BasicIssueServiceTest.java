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
package net.netconomy.jiraassistant.base.services.issues;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.base.BaseDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseDIConfiguration.class })
public class BasicIssueServiceTest {

    @Autowired
    private BasicIssueService basicIssueService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void projectKeyFromIssueKeyTest() {

        String issueKey = "PROJ0-345";
        String issueKey2 = "PROJ1-3409";
        String issueKey3 = "PROJ2-10994";

        String expectedProjectKey = "PROJ0";
        String expectedProjectKey2 = "PROJ1";
        String expectedProjectKey3 = "PROJ2";

        String actualProjectKey;

        actualProjectKey = basicIssueService.projectKeyFromIssueKey(issueKey);

        assertEquals(expectedProjectKey, actualProjectKey);

        actualProjectKey = basicIssueService.projectKeyFromIssueKey(issueKey2);

        assertEquals(expectedProjectKey2, actualProjectKey);

        actualProjectKey = basicIssueService.projectKeyFromIssueKey(issueKey3);

        assertEquals(expectedProjectKey3, actualProjectKey);

    }

    @After
    public void tearDown() throws Exception {
    }

}
