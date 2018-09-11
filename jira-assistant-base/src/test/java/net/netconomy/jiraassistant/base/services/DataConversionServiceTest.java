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

import org.joda.time.DateTime;
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
public class DataConversionServiceTest {

    @Autowired
    private DataConversionService dataConversionService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void convertDateTimeToJQLDateTest() {

        DateTime testDate1 = new DateTime(2015, 2, 1, 20, 0, 0);
        String expectedString = "2015/2/1";
        String actualString;

        actualString = dataConversionService.convertDateTimeToJQLDate(testDate1);

        assertEquals(expectedString, actualString);

    }

    @Test
    public void convertDateTimeToJQLDateTimeTest() {

        DateTime testDate1 = new DateTime(2015, 2, 1, 20, 7, 5);
        String expectedString = "2015/2/1 20:7";
        String actualString;

        actualString = dataConversionService.convertDateTimeToJQLDateTime(testDate1);

        assertEquals(expectedString, actualString);

    }

    @Test
    public void convertDateTimeToJQLDateTimeTest2() {

        DateTime testDate1 = new DateTime(2015, 2, 1, 5, 10, 10);
        String expectedString = "2015/2/1 5:10";
        String actualString;

        actualString = dataConversionService.convertDateTimeToJQLDateTime(testDate1);

        assertEquals(expectedString, actualString);

    }

    @After
    public void tearDown() throws Exception {
    }

}
