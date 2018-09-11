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


public class IssueStatisticsData {

    private final String issueKey;
    private final Integer minutesSpent;
    private final Integer minutesEstimated;
    private final Integer minutesDeltaEstimatedToSpent;
    private final Integer timeFromEstimationToInProgressInDays;
    private final Integer timeFromInProgressToFinishedInDays;

    public IssueStatisticsData(String issueKey, Integer minutesSpent, Integer minutesEstimated,
            Integer timeFromEstimationToInProgressInDays, Integer timeFromInProgressToFinishedInDays) {
        this.issueKey = issueKey;
        this.minutesSpent = minutesSpent;
        this.minutesEstimated = minutesEstimated;

        if (minutesEstimated != null && minutesEstimated != 0) {
            this.minutesDeltaEstimatedToSpent = minutesSpent - minutesEstimated;
        } else {
            this.minutesDeltaEstimatedToSpent = null;
        }

        this.timeFromEstimationToInProgressInDays = timeFromEstimationToInProgressInDays;
        this.timeFromInProgressToFinishedInDays = timeFromInProgressToFinishedInDays;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public Integer getMinutesSpent() {
        return minutesSpent;
    }

    public Integer getMinutesEstimated() {
        return minutesEstimated;
    }

    public Integer getMinutesDeltaEstimatedToSpent() {
        return minutesDeltaEstimatedToSpent;
    }

    public Integer getTimeFromEstimationToInProgressInDays() {
        return timeFromEstimationToInProgressInDays;
    }

    public Integer getTimeFromInProgressToFinishedInDays() {
        return timeFromInProgressToFinishedInDays;
    }

    @Override
    public String toString() {
        return "IssueStatisticsData [issueKey=" + issueKey + ", minutesSpent=" + minutesSpent + ", minutesEstimated="
                + minutesEstimated + ", minutesDeltaEstimatedToSpent=" + minutesDeltaEstimatedToSpent
                + ", timeFromEstimationToInProgressInDays=" + timeFromEstimationToInProgressInDays
                + ", timeFromInProgressToFinishedInDays=" + timeFromInProgressToFinishedInDays + "]";
    }

}
