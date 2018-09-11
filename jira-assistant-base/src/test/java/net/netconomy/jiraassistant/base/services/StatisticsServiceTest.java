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

import net.netconomy.jiraassistant.base.BaseTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { BaseTestDIConfiguration.class })
public class StatisticsServiceTest {

    @Autowired
    StatisticsService statisticsService;

    private List<Integer> intList;

    @Before
    public void setUp() throws Exception {

        intList = new ArrayList<>();

        intList.add(2);
        intList.add(3);
        intList.add(5);
        intList.add(7);
        intList.add(9);

    }

    @Test
    public void calculateMeanTest() {
        
        Double expectedMean = 5.2;
        Double actualMean;

        actualMean = statisticsService.calculateMeanInt(intList);

        assertEquals(expectedMean, actualMean);

    }

    @Test
    public void calculateMedianTest() {

        Double expectedMedian = 5.0;
        Double actualMedian;

        actualMedian = statisticsService.calculateMedianInt(intList);

        assertEquals(expectedMedian, actualMedian);

    }

    @Test
    public void calculateStandardDeviationTest() {

        Double expectedStdDev = 2.5612496949731396;

        Double actualStdDev;

        actualStdDev = statisticsService.calculateStandardDeviationInt(intList);

        assertEquals(expectedStdDev, actualStdDev);

    }

    @After
    public void tearDown() throws Exception {
    }

}
