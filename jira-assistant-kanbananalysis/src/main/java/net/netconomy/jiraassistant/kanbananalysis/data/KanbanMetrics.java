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
package net.netconomy.jiraassistant.kanbananalysis.data;

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

public class KanbanMetrics {

    private SingleTimeMetric meanLeadTime;
    private SingleTimeMetric medianLeadTime;
    private SingleTimeMetric standardDeviationLeadTime;

    private SingleTimeMetric meanCycleTime;
    private SingleTimeMetric medianCycleTime;
    private SingleTimeMetric standardDeviationCycleTime;

    private SingleTimeMetric meanWaitingTime;
    private SingleTimeMetric medianWaitingTime;
    private SingleTimeMetric standardDeviationWaitingTime;

    public KanbanMetrics() {

    }

    public SingleTimeMetric getMeanLeadTime() {
        return meanLeadTime;
    }

    public void setMeanLeadTime(Double meanLeadTimeMinutes) {
        this.meanLeadTime = new SingleTimeMetric(meanLeadTimeMinutes);
    }

    public SingleTimeMetric getMedianLeadTime() {
        return medianLeadTime;
    }

    public void setMedianLeadTime(Double medianLeadTimeMinutes) {
        this.medianLeadTime = new SingleTimeMetric(medianLeadTimeMinutes);
    }

    public SingleTimeMetric getStandardDeviationLeadTime() {
        return standardDeviationLeadTime;
    }

    public void setStandardDeviationLeadTime(Double standardDeviationLeadTimeMinutes) {
        this.standardDeviationLeadTime = new SingleTimeMetric(standardDeviationLeadTimeMinutes);
    }

    public SingleTimeMetric getMeanCycleTime() {
        return meanCycleTime;
    }

    public void setMeanCycleTime(Double meanCycleTimeMinutes) {
        this.meanCycleTime = new SingleTimeMetric(meanCycleTimeMinutes);
    }

    public SingleTimeMetric getMedianCycleTime() {
        return medianCycleTime;
    }

    public void setMedianCycleTime(Double medianCycleTimeMinutes) {
        this.medianCycleTime = new SingleTimeMetric(medianCycleTimeMinutes);
    }

    public SingleTimeMetric getStandardDeviationCycleTime() {
        return standardDeviationCycleTime;
    }

    public void setStandardDeviationCycleTime(Double standardDeviationCycleTimeMinutes) {
        this.standardDeviationCycleTime = new SingleTimeMetric(standardDeviationCycleTimeMinutes);
    }

    public SingleTimeMetric getMeanWaitingTime() {
        return meanWaitingTime;
    }

    public void setMeanWaitingTime(Double meanWaitingTimeMinutes) {
        this.meanWaitingTime = new SingleTimeMetric(meanWaitingTimeMinutes);
    }

    public SingleTimeMetric getMedianWaitingTime() {
        return medianWaitingTime;
    }

    public void setMedianWaitingTime(Double medianWaitingTimeMinutes) {
        this.medianWaitingTime = new SingleTimeMetric(medianWaitingTimeMinutes);
    }

    public SingleTimeMetric getStandardDeviationWaitingTime() {
        return standardDeviationWaitingTime;
    }

    public void setStandardDeviationWaitingTime(Double standardDeviationWaitingTimeMinutes) {
        this.standardDeviationWaitingTime = new SingleTimeMetric(standardDeviationWaitingTimeMinutes);
    }

    @Override
    public String toString() {
        return "KanbanMetrics [meanLeadTime=" + meanLeadTime + ", medianLeadTime=" + medianLeadTime
                + ", standardDeviationLeadTime=" + standardDeviationLeadTime + ", meanCycleTime=" + meanCycleTime
                + ", medianCycleTime=" + medianCycleTime + ", standardDeviationCycleTime=" + standardDeviationCycleTime
                + ", meanWaitingTime=" + meanWaitingTime + ", medianWaitingTime=" + medianWaitingTime
                + ", standardDeviationWaitingTime=" + standardDeviationWaitingTime + "]";
    }

}
