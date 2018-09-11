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

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.supportanalysis.SupportAnalysisDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { SupportAnalysisDIConfiguration.class })
public class SupportAnalysisServiceTest {

    @Autowired
    SupportAnalysisService supportAnalysisService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getFirstOfYearTest() {

        DateTime someDayInYear = new DateTime(2016, 05, 23, 1, 0, 0);
        DateTime expectedDate = new DateTime(2016, 01, 01, 0, 0, 0);
        DateTime actualDate;

        actualDate = supportAnalysisService.getFirstOfYear(someDayInYear);

        assertEquals("The derived First Day of the Year was not correct", expectedDate, actualDate);

    }

    @After
    public void tearDown() throws Exception {
    }

}
