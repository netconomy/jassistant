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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.BasicStatisticsData;

@Service
public class StatisticsService {

    List<Double> intListToDoubleList(List<Integer> intList) {

        List<Double> doubleList = new ArrayList<>();

        for (Integer current : intList) {
            doubleList.add(current.doubleValue());
        }

        return doubleList;

    }

    double[] doubleListToDoubleArray(List<Double> doubleList) {

        double[] doubleArray = new double[doubleList.size()];

        for (int i = 0; i < doubleList.size(); i++) {
            doubleArray[i] = doubleList.get(i);
        }

        return doubleArray;

    }

    public Double calculateMean(List<Double> doubleList) {

        if (doubleList.isEmpty()) {
            return null;
        }

        Mean mean = new Mean();

        mean.setData(doubleListToDoubleArray(doubleList));

        return mean.evaluate();

    }

    public Double calculateMeanInt(List<Integer> intList) {

        return calculateMean(intListToDoubleList(intList));

    }

    public Double calculateMedian(List<Double> doubleList) {

        if (doubleList.isEmpty()) {
            return null;
        }

        Median median = new Median();

        median.setData(doubleListToDoubleArray(doubleList));

        return median.evaluate();

    }

    public Double calculateMedianInt(List<Integer> intList) {

        return calculateMedian(intListToDoubleList(intList));

    }

    public Double calculateStandardDeviation(List<Double> doubleList) {

        if (doubleList.isEmpty()) {
            return null;
        }

        StandardDeviation standardDeviation = new StandardDeviation(false);

        standardDeviation.setData(doubleListToDoubleArray(doubleList));

        return standardDeviation.evaluate();

    }

    public Double calculateStandardDeviationInt(List<Integer> intList) {

        return calculateStandardDeviation(intListToDoubleList(intList));

    }

    /**
     * Calculates the mean, median and standard Deviation of the given Array
     * 
     * @param doubleList
     * @return
     */
    public BasicStatisticsData calculateBasicStatistics(List<Double> doubleList) {

        BasicStatisticsData basicStatisticsData = new BasicStatisticsData();

        basicStatisticsData.setMean(calculateMean(doubleList));
        basicStatisticsData.setMedian(calculateMedian(doubleList));
        basicStatisticsData.setStandardDeviation(calculateStandardDeviation(doubleList));

        return basicStatisticsData;

    }

    public BasicStatisticsData calculateBasicStatisticsInt(List<Integer> intList) {

        return calculateBasicStatistics(intListToDoubleList(intList));

    }

    public Double calculatePercentile(List<Double> doubleList, Integer quantile) {

        if (doubleList.isEmpty()) {
            return null;
        }

        Percentile percentile = new Percentile();

        percentile.setData(doubleListToDoubleArray(doubleList));

        return percentile.evaluate(quantile);

    }

    public Double calculatePercentileInt(List<Integer> intList, Integer quantile) {

        return calculatePercentile(intListToDoubleList(intList), quantile);

    }

}
