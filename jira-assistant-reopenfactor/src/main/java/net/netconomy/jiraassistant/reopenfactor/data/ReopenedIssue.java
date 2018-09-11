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
package net.netconomy.jiraassistant.reopenfactor.data;

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

public class ReopenedIssue {

    private String key;
    private Integer numberOfReopens = 0;
    private String firstReopenAfterTestable;
    private SingleTimeMetric bookedTimeTotal;
    private SingleTimeMetric bookedTimeAfterFirstReopen;
    private Double percentageOfTimeAfterFirstReopen = 0.0;

    public ReopenedIssue() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getNumberOfReopens() {
        return numberOfReopens;
    }

    public void setNumberOfReopens(Integer numberOfReopens) {
        this.numberOfReopens = numberOfReopens;
    }

    public String getFirstReopenAfterTestable() {
        return firstReopenAfterTestable;
    }

    public void setFirstReopenAfterTestable(String firstReopen) {
        this.firstReopenAfterTestable = firstReopen;
    }

    public SingleTimeMetric getBookedTimeTotal() {
        return bookedTimeTotal;
    }

    public void setBookedTimeTotal(Double bookedTimeTotal) {
        this.bookedTimeTotal = new SingleTimeMetric(bookedTimeTotal);
    }

    public SingleTimeMetric getBookedTimeAfterFirstReopen() {
        return bookedTimeAfterFirstReopen;
    }

    public void setBookedTimeAfterFirstReopen(Double bookedTimeAfterFirstReopen) {
        this.bookedTimeAfterFirstReopen = new SingleTimeMetric(bookedTimeAfterFirstReopen);
    }

    public Double getPercentageOfTimeAfterFirstReopen() {
        return percentageOfTimeAfterFirstReopen;
    }

    public void setPercentageOfTimeAfterFirstReopen(Double percentageOfTimeAfterFirstReopen) {
        this.percentageOfTimeAfterFirstReopen = percentageOfTimeAfterFirstReopen;
    }

    @Override
    public String toString() {
        return "ReopenedIssue{" +
                "key='" + key + '\'' +
                ", numberOfReopens=" + numberOfReopens +
                ", firstReopenAfterTestable='" + firstReopenAfterTestable + '\'' +
                ", bookedTimeTotal=" + bookedTimeTotal +
                ", bookedTimeAfterFirstReopen=" + bookedTimeAfterFirstReopen +
                ", percentageOfTimeAfterFirstReopen=" + percentageOfTimeAfterFirstReopen +
                '}';
    }
}
