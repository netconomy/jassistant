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

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

import java.util.ArrayList;
import java.util.List;

public abstract class EstimationStatisticsData {

    protected String issueType;

    protected SingleTimeMetric meanTimeSpent;
    protected SingleTimeMetric medianTimeSpent;
    protected SingleTimeMetric stdDeviationTimeSpent;
    protected SingleTimeMetric lowerBorderSpent;
    protected SingleTimeMetric upperBorderSpent;
    protected Integer missingOriginalEstimates = 0;
    protected SingleTimeMetric meanTimeEstimated;
    protected SingleTimeMetric medianTimeEstimated;
    protected SingleTimeMetric stdDeviationTimeEstimated;
    protected SingleTimeMetric meanTimeDeltaEstimatedToSpent;
    protected SingleTimeMetric medianTimeDeltaEstimatedToSpent;
    protected SingleTimeMetric stdDeviationTimeDeltaEstimatedToSpent;
    protected Double meanEstimationAgeDays = 0.0;
    protected Double medianEstimationAgeDays = 0.0;
    protected Double stdDeviationEstimationAgeDays = 0.0;
    protected Double meanCycleTimeDays = 0.0;
    protected Double medianCycleTimeDays = 0.0;
    protected Double stdDeviationCycleTimeDays = 0.0;
    
    protected List<Integer> minutesSpentAllList = new ArrayList<>();
    protected List<Integer> minutesEstimatedAllList = new ArrayList<>();
    protected List<Integer> minutesDeltaEstimatedToSpentAllList = new ArrayList<>();
    protected List<Integer> estimationAgeAllList = new ArrayList<>();
    protected List<Integer> cycleTimeAllList = new ArrayList<>();

    public EstimationStatisticsData() {
        
    }

    public abstract void generateMinutesSpentOfAllIssuesList();

    public abstract void generateMinutesEstimatedAllIssuesList();

    public abstract void generateMinutesDeltaEstimatedToSpentAllIssuesList();

    public abstract void generateEstimationAgeOfAllIssuesList();

    public abstract void generateCycleTimeAllIssuesList();

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public Integer getMissingOriginalEstimates() {
        return missingOriginalEstimates;
    }

    public void setMissingOriginalEstimates(Integer missingOriginalEstimates) {
        this.missingOriginalEstimates = missingOriginalEstimates;
    }

    public Double getMeanEstimationAgeDays() {
        return meanEstimationAgeDays;
    }

    public void setMeanEstimationAgeDays(Double meanEstimationAgeDays) {
        this.meanEstimationAgeDays = meanEstimationAgeDays;
    }

    public Double getMedianEstimationAgeDays() {
        return medianEstimationAgeDays;
    }

    public void setMedianEstimationAgeDays(Double medianEstimationAgeDays) {
        this.medianEstimationAgeDays = medianEstimationAgeDays;
    }

    public Double getStdDeviationEstimationAgeDays() {
        return stdDeviationEstimationAgeDays;
    }

    public void setStdDeviationEstimationAgeDays(Double stdDeviationEstimationAgeDays) {
        this.stdDeviationEstimationAgeDays = stdDeviationEstimationAgeDays;
    }

    public Double getMeanCycleTimeDays() {
        return meanCycleTimeDays;
    }

    public void setMeanCycleTimeDays(Double meanCycleTimeDays) {
        this.meanCycleTimeDays = meanCycleTimeDays;
    }

    public Double getMedianCycleTimeDays() {
        return medianCycleTimeDays;
    }

    public void setMedianCycleTimeDays(Double medianCycleTimeDays) {
        this.medianCycleTimeDays = medianCycleTimeDays;
    }

    public Double getStdDeviationCycleTimeDays() {
        return stdDeviationCycleTimeDays;
    }

    public void setStdDeviationCycleTimeDays(Double stdDeviationCycleTimeDays) {
        this.stdDeviationCycleTimeDays = stdDeviationCycleTimeDays;
    }

    public List<Integer> getMinutesSpentAllList() {
        if (minutesSpentAllList.isEmpty()) {
            generateMinutesSpentOfAllIssuesList();
        }
        return minutesSpentAllList;
    }

    public List<Integer> getMinutesEstimatedAllList() {
        if (minutesEstimatedAllList.isEmpty()) {
            generateMinutesEstimatedAllIssuesList();
        }
        return minutesEstimatedAllList;
    }

    public List<Integer> getMinutesDeltaEstimatedToSpentAllList() {
        if (minutesDeltaEstimatedToSpentAllList.isEmpty()) {
            generateMinutesDeltaEstimatedToSpentAllIssuesList();
        }
        return minutesDeltaEstimatedToSpentAllList;
    }

    public List<Integer> getEstimationAgeAllList() {
        if (estimationAgeAllList.isEmpty()) {
            generateEstimationAgeOfAllIssuesList();
        }
        return estimationAgeAllList;
    }

    public List<Integer> getCycleTimeAllList() {
        if (cycleTimeAllList.isEmpty()) {
            generateCycleTimeAllIssuesList();
        }
        return cycleTimeAllList;
    }

    public SingleTimeMetric getMeanTimeSpent() {
        return meanTimeSpent;
    }

    public void setMeanTimeSpent(Double meanTimeSpentMinutes) {
        this.meanTimeSpent = new SingleTimeMetric(meanTimeSpentMinutes);
    }

    public SingleTimeMetric getMedianTimeSpent() {
        return medianTimeSpent;
    }

    public void setMedianTimeSpent(Double medianTimeSpentMinutes) {
        this.medianTimeSpent = new SingleTimeMetric(medianTimeSpentMinutes);
    }

    public SingleTimeMetric getStdDeviationTimeSpent() {
        return stdDeviationTimeSpent;
    }

    public void setStdDeviationTimeSpent(Double stdDeviationTimeSpentMinutes) {
        this.stdDeviationTimeSpent = new SingleTimeMetric(stdDeviationTimeSpentMinutes);
    }

    public SingleTimeMetric getLowerBorderSpent() {
        return lowerBorderSpent;
    }

    public void setLowerBorderSpent(Double lowerBorderSpentMinutes) {
        this.lowerBorderSpent = new SingleTimeMetric(lowerBorderSpentMinutes);
    }

    public SingleTimeMetric getUpperBorderSpent() {
        return upperBorderSpent;
    }

    public void setUpperBorderSpent(Double upperBorderSpentMinutes) {
        this.upperBorderSpent = new SingleTimeMetric(upperBorderSpentMinutes);
    }

    public SingleTimeMetric getMeanTimeEstimated() {
        return meanTimeEstimated;
    }

    public void setMeanTimeEstimated(Double meanTimeEstimatedMinutes) {
        this.meanTimeEstimated = new SingleTimeMetric(meanTimeEstimatedMinutes);
    }

    public SingleTimeMetric getMedianTimeEstimated() {
        return medianTimeEstimated;
    }

    public void setMedianTimeEstimated(Double medianTimeEstimatedMinutes) {
        this.medianTimeEstimated = new SingleTimeMetric(medianTimeEstimatedMinutes);
    }

    public SingleTimeMetric getStdDeviationTimeEstimated() {
        return stdDeviationTimeEstimated;
    }

    public void setStdDeviationTimeEstimated(Double stdDeviationTimeEstimatedMinutes) {
        this.stdDeviationTimeEstimated = new SingleTimeMetric(stdDeviationTimeEstimatedMinutes);
    }

    public SingleTimeMetric getMeanTimeDeltaEstimatedToSpent() {
        return meanTimeDeltaEstimatedToSpent;
    }

    public void setMeanTimeDeltaEstimatedToSpent(Double meanTimeDeltaEstimatedToSpentMinutes) {
        this.meanTimeDeltaEstimatedToSpent = new SingleTimeMetric(meanTimeDeltaEstimatedToSpentMinutes);
    }

    public SingleTimeMetric getMedianTimeDeltaEstimatedToSpent() {
        return medianTimeDeltaEstimatedToSpent;
    }

    public void setMedianTimeDeltaEstimatedToSpent(Double medianTimeDeltaEstimatedToSpentMinutes) {
        this.medianTimeDeltaEstimatedToSpent = new SingleTimeMetric(medianTimeDeltaEstimatedToSpentMinutes);
    }

    public SingleTimeMetric getStdDeviationTimeDeltaEstimatedToSpent() {
        return stdDeviationTimeDeltaEstimatedToSpent;
    }

    public void setStdDeviationTimeDeltaEstimatedToSpent(Double stdDeviationTimeDeltaEstimatedToSpentMinutes) {
        this.stdDeviationTimeDeltaEstimatedToSpent = new SingleTimeMetric(stdDeviationTimeDeltaEstimatedToSpentMinutes);
    }

    @Override
    public String toString() {
        return "EstimationStatisticsData{" +
                "issueType='" + issueType + '\'' +
                ", meanTimeSpent=" + meanTimeSpent +
                ", medianTimeSpent=" + medianTimeSpent +
                ", stdDeviationTimeSpent=" + stdDeviationTimeSpent +
                ", lowerBorderSpent=" + lowerBorderSpent +
                ", upperBorderSpent=" + upperBorderSpent +
                ", missingOriginalEstimates=" + missingOriginalEstimates +
                ", meanTimeEstimated=" + meanTimeEstimated +
                ", medianTimeEstimated=" + medianTimeEstimated +
                ", stdDeviationTimeEstimated=" + stdDeviationTimeEstimated +
                ", meanTimeDeltaEstimatedToSpent=" + meanTimeDeltaEstimatedToSpent +
                ", medianTimeDeltaEstimatedToSpent=" + medianTimeDeltaEstimatedToSpent +
                ", stdDeviationTimeDeltaEstimatedToSpent=" + stdDeviationTimeDeltaEstimatedToSpent +
                ", meanEstimationAgeDays=" + meanEstimationAgeDays +
                ", medianEstimationAgeDays=" + medianEstimationAgeDays +
                ", stdDeviationEstimationAgeDays=" + stdDeviationEstimationAgeDays +
                ", meanCycleTimeDays=" + meanCycleTimeDays +
                ", medianCycleTimeDays=" + medianCycleTimeDays +
                ", stdDeviationCycleTimeDays=" + stdDeviationCycleTimeDays +
                ", minutesSpentAllList=" + minutesSpentAllList +
                ", minutesEstimatedAllList=" + minutesEstimatedAllList +
                ", minutesDeltaEstimatedToSpentAllList=" + minutesDeltaEstimatedToSpentAllList +
                ", estimationAgeAllList=" + estimationAgeAllList +
                ", cycleTimeAllList=" + cycleTimeAllList +
                '}';
    }

}
