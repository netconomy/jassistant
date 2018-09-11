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
package net.netconomy.jiraassistant.sprintanalysis.data.flagging;

import java.util.ArrayList;
import java.util.List;

public class FlaggingStatisticsPartData {

    private Integer numberOfFlaggedIssuesStart = 0;
    private Integer numberOfFlaggedIssuesEnd = 0;
    private Integer numberOfFlaggings = 0;

    private Integer sumOfFlaggingDurationsMinutes = 0;
    private Double meanOfFlaggingDurationsMinutes = 0.0;
    private Double medianOfFlaggingDurationsMinutes = 0.0;

    private List<FlaggingSingleData> flaggingSingleData = new ArrayList<>();

    public FlaggingStatisticsPartData() {

    }

    public void addSingleFlaggingData(FlaggingSingleData flaggingSingleData) {
        this.flaggingSingleData.add(flaggingSingleData);
    }

    public Integer getNumberOfFlaggedIssuesStart() {
        return numberOfFlaggedIssuesStart;
    }

    public void setNumberOfFlaggedIssuesStart(Integer numberOfFlaggedIssuesStart) {
        this.numberOfFlaggedIssuesStart = numberOfFlaggedIssuesStart;
    }

    public Integer getNumberOfFlaggedIssuesEnd() {
        return numberOfFlaggedIssuesEnd;
    }

    public void setNumberOfFlaggedIssuesEnd(Integer numberOfFlaggedIssuesEnd) {
        this.numberOfFlaggedIssuesEnd = numberOfFlaggedIssuesEnd;
    }

    public Integer getNumberOfFlaggings() {
        return numberOfFlaggings;
    }

    public void setNumberOfFlaggings(Integer numberOfFlaggings) {
        this.numberOfFlaggings = numberOfFlaggings;
    }

    public Integer getSumOfFlaggingDurationsMinutes() {
        return sumOfFlaggingDurationsMinutes;
    }

    public void setSumOfFlaggingDurationsMinutes(Integer sumOfFlaggingDurationsMinutes) {
        this.sumOfFlaggingDurationsMinutes = sumOfFlaggingDurationsMinutes;
    }

    public Double getMeanOfFlaggingDurationsMinutes() {
        return meanOfFlaggingDurationsMinutes;
    }

    public void setMeanOfFlaggingDurationsMinutes(Double meanOfFlaggingDurationsMinutes) {
        this.meanOfFlaggingDurationsMinutes = meanOfFlaggingDurationsMinutes;
    }

    public Double getMedianOfFlaggingDurationsMinutes() {
        return medianOfFlaggingDurationsMinutes;
    }

    public void setMedianOfFlaggingDurationsMinutes(Double medianOfFlaggingDurationsMinutes) {
        this.medianOfFlaggingDurationsMinutes = medianOfFlaggingDurationsMinutes;
    }

    public List<FlaggingSingleData> getFlaggingSingleData() {
        return flaggingSingleData;
    }

    public void setFlaggingSingleData(List<FlaggingSingleData> flaggingSingleData) {
        this.flaggingSingleData = flaggingSingleData;
    }

    @Override
    public String toString() {
        return "FlaggingStatisticsPartData [numberOfFlaggedIssuesStart=" + numberOfFlaggedIssuesStart
                + ", numberOfFlaggedIssuesEnd=" + numberOfFlaggedIssuesEnd + ", numberOfFlaggings=" + numberOfFlaggings
                + ", sumOfFlaggingDurationsMinutes=" + sumOfFlaggingDurationsMinutes
                + ", meanOfFlaggingDurationsMinutes=" + meanOfFlaggingDurationsMinutes
                + ", medianOfFlaggingDurationsMinutes=" + medianOfFlaggingDurationsMinutes + ", flaggingSingleData="
                + flaggingSingleData + "]";
    }

}
