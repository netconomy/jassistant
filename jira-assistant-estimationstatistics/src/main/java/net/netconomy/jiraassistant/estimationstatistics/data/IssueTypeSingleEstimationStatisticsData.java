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

import java.util.ArrayList;
import java.util.List;

public class IssueTypeSingleEstimationStatisticsData extends EstimationStatisticsData {

    private final String estimation;

    private List<IssueStatisticsData> allIssues = new ArrayList<>();
    private List<IssueStatisticsData> deviantIssues = new ArrayList<>();

    public IssueTypeSingleEstimationStatisticsData(String issueTypeName, String estimation) {
        this.issueType = issueTypeName;
        this.estimation = estimation;
    }

    public void addIssue(IssueStatisticsData statisticsIssueData) {
        allIssues.add(statisticsIssueData);
    }

    public void addDeviantIssue(IssueStatisticsData statisticsIssueData) {
        deviantIssues.add(statisticsIssueData);
    }

    @Override
    public void generateMinutesSpentOfAllIssuesList() {
        for (IssueStatisticsData current : allIssues) {
            this.minutesSpentAllList.add(current.getMinutesSpent());
        }
    }

    @Override
    public void generateMinutesEstimatedAllIssuesList() {
        for (IssueStatisticsData current : allIssues) {
            if (current.getMinutesEstimated() != null && current.getMinutesEstimated() != 0) {
                this.minutesEstimatedAllList.add(current.getMinutesEstimated());
            }
        }
    }

    @Override
    public void generateMinutesDeltaEstimatedToSpentAllIssuesList() {
        for (IssueStatisticsData current : allIssues) {
            if (current.getMinutesDeltaEstimatedToSpent() != null) {
                this.minutesDeltaEstimatedToSpentAllList.add(current.getMinutesDeltaEstimatedToSpent());
            }
        }
    }

    @Override
    public void generateEstimationAgeOfAllIssuesList() {
        for (IssueStatisticsData current : allIssues) {
            this.estimationAgeAllList.add(current.getTimeFromEstimationToInProgressInDays());
        }
    }

    @Override
    public void generateCycleTimeAllIssuesList() {
        for (IssueStatisticsData current : allIssues) {
            this.cycleTimeAllList.add(current.getTimeFromInProgressToFinishedInDays());
        }
    }

    public List<IssueStatisticsData> getAllIssues() {
        return allIssues;
    }

    public void setAllIssues(List<IssueStatisticsData> allIssues) {
        this.allIssues = allIssues;
    }

    public List<IssueStatisticsData> getDeviantIssues() {
        return deviantIssues;
    }

    public void setDeviantIssues(List<IssueStatisticsData> deviantIssues) {
        this.deviantIssues = deviantIssues;
    }

    public String getEstimation() {
        return estimation;
    }

    @Override
    public String toString() {
        String string = "IssueTypeSingleEstimationStatisticsData [ estimation=" + estimation + ", " + super.toString()
                + ", allIssues=" + allIssues + ", deviantIssues=" + deviantIssues + "]";

        return string;
    }

}
