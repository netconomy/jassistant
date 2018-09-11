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
package net.netconomy.jiraassistant.estimationstatistics.data;

import java.util.HashMap;
import java.util.Map;

public class IssueTypeEstimationStatisticsData extends EstimationStatisticsData {

    private Map<String, IssueTypeSingleEstimationStatisticsData> singleEstimationStatisticsMap = new HashMap<>();

    public IssueTypeEstimationStatisticsData(String issueTypeName) {
        this.issueType = issueTypeName;
    }

    public static String estimationKeyFromEstimation(String estimation, Boolean altEstimations) {
        if (altEstimations) {
            return "Size: " + estimation;
        } else {
            return estimation.replace('.', ',') + " Points";
        }
    }

    @Override
    public void generateMinutesSpentOfAllIssuesList() {
        for (IssueTypeSingleEstimationStatisticsData current : singleEstimationStatisticsMap.values()) {
            this.minutesSpentAllList.addAll(current.getMinutesSpentAllList());
        }
    }

    @Override
    public void generateMinutesEstimatedAllIssuesList() {
        for (IssueTypeSingleEstimationStatisticsData current : singleEstimationStatisticsMap.values()) {
            this.minutesEstimatedAllList.addAll(current.getMinutesEstimatedAllList());
        }
    }

    @Override
    public void generateMinutesDeltaEstimatedToSpentAllIssuesList() {
        for (IssueTypeSingleEstimationStatisticsData current : singleEstimationStatisticsMap.values()) {
            this.minutesDeltaEstimatedToSpentAllList.addAll(current.getMinutesDeltaEstimatedToSpentAllList());
        }
    }

    @Override
    public void generateEstimationAgeOfAllIssuesList() {
        for (IssueTypeSingleEstimationStatisticsData current : singleEstimationStatisticsMap.values()) {
            this.estimationAgeAllList.addAll(current.getEstimationAgeAllList());
        }
    }

    @Override
    public void generateCycleTimeAllIssuesList() {
        for (IssueTypeSingleEstimationStatisticsData current : singleEstimationStatisticsMap.values()) {
            this.cycleTimeAllList.addAll(current.getCycleTimeAllList());
        }
    }

    public void addSingleEstimationStatistics(String estimation,
            IssueTypeSingleEstimationStatisticsData issueTypeSingleEstimationStatistics, Boolean altEstimations) {
        String estimationString = estimationKeyFromEstimation(estimation, altEstimations);
        this.singleEstimationStatisticsMap.put(estimationString, issueTypeSingleEstimationStatistics);
    }

    public Map<String, IssueTypeSingleEstimationStatisticsData> getSingleEstimationStatisticsMap() {
        return singleEstimationStatisticsMap;
    }

    public void setSingleEstimationStatisticsMap(
            Map<String, IssueTypeSingleEstimationStatisticsData> singleEstimationStatisticsMap) {
        this.singleEstimationStatisticsMap = singleEstimationStatisticsMap;
    }

    @Override
    public String toString() {
        String string = "IssueTypeEstimationStatisticsData [" + super.toString() + " singleEstimationStatisticsMap="
                + singleEstimationStatisticsMap + "]";

        return string;
    }

}
