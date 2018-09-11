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
package net.netconomy.jiraassistant.supportanalysis.data.sla;

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

public class SlaStatisticsData {

    private Integer numberOfIssues = 0;

    private Integer numberOfBrokenSLAs = 0;
    private Integer numberOfKeptSLAs = 0;

    private SingleTimeMetric meanReactionTimeInTimeFrame;
    private SingleTimeMetric medianReactionTimeInTimeFrame;
    private SingleTimeMetric standardDeviationReactionTimeInTimeFrame;
    private SingleTimeMetric reactionTimeInTimeFrame90thPercentile;
    private SingleTimeMetric reactionTimeInTimeFrame99thPercentile;

    private SingleTimeMetric meanInteractionTimeInTimeFrame;
    private SingleTimeMetric medianInteractionTimeInTimeFrame;
    private SingleTimeMetric standardDeviationInteractionTimeInTimeFrame;
    private SingleTimeMetric interactionTimeInTimeFrame90thPercentile;
    private SingleTimeMetric interactionTimeInTimeFrame99thPercentile;

    private SingleTimeMetric meanSolutionTime;
    private SingleTimeMetric medianSolutionTime;
    private SingleTimeMetric standardDeviationSolutionTime;
    private SingleTimeMetric solutionTime90thPercentile;
    private SingleTimeMetric solutionTime99thPercentile;

    private Boolean fallbackUsedForReactionTime = true;
    private Boolean fallbackUsedForInteractionTime = true;
    private Boolean fallbackUsedForSolutionTime = true;

    public SlaStatisticsData() {

    }

    public void raiseNumberOfBrokenSLAs() {
        this.numberOfBrokenSLAs++;
    }

    public void raiseNumberOfKeptSLAs() {
        this.numberOfKeptSLAs++;
    }

    // Getters and Setters
    public Integer getNumberOfBrokenSLAs() {
        return numberOfBrokenSLAs;
    }

    public void setNumberOfBrokenSLAs(Integer numberOfBrockenSLAs) {
        this.numberOfBrokenSLAs = numberOfBrockenSLAs;
    }

    public Integer getNumberOfKeptSLAs() {
        return numberOfKeptSLAs;
    }

    public void setNumberOfKeptSLAs(Integer numberOfKeptSLAs) {
        this.numberOfKeptSLAs = numberOfKeptSLAs;
    }

    public SingleTimeMetric getMeanReactionTimeInTimeFrame() {
        return meanReactionTimeInTimeFrame;
    }

    public void setMeanReactionTimeInTimeFrame(Double meanReactionTimeInTimeFrameMinutes) {
        this.meanReactionTimeInTimeFrame = new SingleTimeMetric(meanReactionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getMedianReactionTimeInTimeFrame() {
        return medianReactionTimeInTimeFrame;
    }

    public void setMedianReactionTimeInTimeFrame(Double medianReactionTimeInTimeFrameMinutes) {
        this.medianReactionTimeInTimeFrame = new SingleTimeMetric(medianReactionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getStandardDeviationReactionTimeInTimeFrame() {
        return standardDeviationReactionTimeInTimeFrame;
    }

    public void setStandardDeviationReactionTimeInTimeFrame(Double standardDeviationReactionTimeInTimeFrameMinutes) {
        this.standardDeviationReactionTimeInTimeFrame = new SingleTimeMetric(
                standardDeviationReactionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getMeanInteractionTimeInTimeFrame() {
        return meanInteractionTimeInTimeFrame;
    }

    public void setMeanInteractionTimeInTimeFrame(Double meanInteractionTimeInTimeFrameMinutes) {
        this.meanInteractionTimeInTimeFrame = new SingleTimeMetric(meanInteractionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getMedianInteractionTimeInTimeFrame() {
        return medianInteractionTimeInTimeFrame;
    }

    public void setMedianInteractionTimeInTimeFrame(Double medianInteractionTimeInTimeFrameMinutes) {
        this.medianInteractionTimeInTimeFrame = new SingleTimeMetric(medianInteractionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getStandardDeviationInteractionTimeInTimeFrame() {
        return standardDeviationInteractionTimeInTimeFrame;
    }

    public void setStandardDeviationInteractionTimeInTimeFrame(Double standardDeviationInteractionTimeInTimeFrameMinutes) {
        this.standardDeviationInteractionTimeInTimeFrame = new SingleTimeMetric(
                standardDeviationInteractionTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getMeanSolutionTime() {
        return meanSolutionTime;
    }

    public void setMeanSolutionTime(Double meanSolutionTimeMinutes) {
        this.meanSolutionTime = new SingleTimeMetric(meanSolutionTimeMinutes);
    }

    public SingleTimeMetric getMedianSolutionTime() {
        return medianSolutionTime;
    }

    public void setMedianSolutionTime(Double medianSolutionTimeMinutes) {
        this.medianSolutionTime = new SingleTimeMetric(medianSolutionTimeMinutes);
    }

    public SingleTimeMetric getStandardDeviationSolutionTime() {
        return standardDeviationSolutionTime;
    }

    public void setStandardDeviationSolutionTime(Double standardDeviationSolutionTimeMinutes) {
        this.standardDeviationSolutionTime = new SingleTimeMetric(standardDeviationSolutionTimeMinutes);
    }

    public Integer getNumberOfIssues() {
        return numberOfIssues;
    }

    public void setNumberOfIssues(Integer numberOfIssues) {
        this.numberOfIssues = numberOfIssues;
    }

    public SingleTimeMetric getReactionTimeInTimeFrame90thPercentile() {
        return reactionTimeInTimeFrame90thPercentile;
    }

    public void setReactionTimeInTimeFrame90thPercentile(Double reactionTimeInTimeFrame90thPercentileMinutes) {
        reactionTimeInTimeFrame90thPercentile = new SingleTimeMetric(reactionTimeInTimeFrame90thPercentileMinutes);
    }

    public SingleTimeMetric getReactionTimeInTimeFrame99thPercentile() {
        return reactionTimeInTimeFrame99thPercentile;
    }

    public void setReactionTimeInTimeFrame99thPercentile(Double reactionTimeInTimeFrame99thPercentileMinutes) {
        reactionTimeInTimeFrame99thPercentile = new SingleTimeMetric(reactionTimeInTimeFrame99thPercentileMinutes);
    }

    public SingleTimeMetric getInteractionTimeInTimeFrame90thPercentile() {
        return interactionTimeInTimeFrame90thPercentile;
    }

    public void setInteractionTimeInTimeFrame90thPercentile(Double interactionTimeInTimeFrame90thPercentileMinutes) {
        interactionTimeInTimeFrame90thPercentile = new SingleTimeMetric(interactionTimeInTimeFrame90thPercentileMinutes);
    }

    public SingleTimeMetric getInteractionTimeInTimeFrame99thPercentile() {
        return interactionTimeInTimeFrame99thPercentile;
    }

    public void setInteractionTimeInTimeFrame99thPercentile(Double interactionTimeInTimeFrame99thPercentileMinutes) {
        interactionTimeInTimeFrame99thPercentile = new SingleTimeMetric(interactionTimeInTimeFrame99thPercentileMinutes);
    }

    public SingleTimeMetric getSolutionTime90thPercentile() {
        return solutionTime90thPercentile;
    }

    public void setSolutionTime90thPercentile(Double solutionTime90thPercentileMinutes) {
        solutionTime90thPercentile = new SingleTimeMetric(solutionTime90thPercentileMinutes);
    }

    public SingleTimeMetric getSolutionTime99thPercentile() {
        return solutionTime99thPercentile;
    }

    public void setSolutionTime99thPercentile(Double solutionTime99thPercentileMinutes) {
        solutionTime99thPercentile = new SingleTimeMetric(solutionTime99thPercentileMinutes);
    }

    public void setMeanReactionTimeInTimeFrame(SingleTimeMetric meanReactionTimeInTimeFrame) {
        this.meanReactionTimeInTimeFrame = meanReactionTimeInTimeFrame;
    }

    public Boolean getFallbackUsedForReactionTime() {
        return fallbackUsedForReactionTime;
    }

    public void setFallbackUsedForReactionTime(Boolean fallbackUsedForReactionTime) {
        this.fallbackUsedForReactionTime = fallbackUsedForReactionTime;
    }

    public Boolean getFallbackUsedForInteractionTime() {
        return fallbackUsedForInteractionTime;
    }

    public void setFallbackUsedForInteractionTime(Boolean fallbackUsedForInteractionTime) {
        this.fallbackUsedForInteractionTime = fallbackUsedForInteractionTime;
    }

    public Boolean getFallbackUsedForSolutionTime() {
        return fallbackUsedForSolutionTime;
    }

    public void setFallbackUsedForSolutionTime(Boolean fallbackUsedForSolutionTime) {
        this.fallbackUsedForSolutionTime = fallbackUsedForSolutionTime;
    }

    @Override
    public String toString() {
        return "SlaStatisticsData{" +
                "numberOfIssues=" + numberOfIssues +
                ", numberOfBrokenSLAs=" + numberOfBrokenSLAs +
                ", numberOfKeptSLAs=" + numberOfKeptSLAs +
                ", meanReactionTimeInTimeFrame=" + meanReactionTimeInTimeFrame +
                ", medianReactionTimeInTimeFrame=" + medianReactionTimeInTimeFrame +
                ", standardDeviationReactionTimeInTimeFrame=" + standardDeviationReactionTimeInTimeFrame +
                ", reactionTimeInTimeFrame90thPercentile=" + reactionTimeInTimeFrame90thPercentile +
                ", reactionTimeInTimeFrame99thPercentile=" + reactionTimeInTimeFrame99thPercentile +
                ", meanInteractionTimeInTimeFrame=" + meanInteractionTimeInTimeFrame +
                ", medianInteractionTimeInTimeFrame=" + medianInteractionTimeInTimeFrame +
                ", standardDeviationInteractionTimeInTimeFrame=" + standardDeviationInteractionTimeInTimeFrame +
                ", interactionTimeInTimeFrame90thPercentile=" + interactionTimeInTimeFrame90thPercentile +
                ", interactionTimeInTimeFrame99thPercentile=" + interactionTimeInTimeFrame99thPercentile +
                ", meanSolutionTime=" + meanSolutionTime +
                ", medianSolutionTime=" + medianSolutionTime +
                ", standardDeviationSolutionTime=" + standardDeviationSolutionTime +
                ", solutionTime90thPercentile=" + solutionTime90thPercentile +
                ", solutionTime99thPercentile=" + solutionTime99thPercentile +
                ", fallbackUsedForReactionTime=" + fallbackUsedForReactionTime +
                ", fallbackUsedForInteractionTime=" + fallbackUsedForInteractionTime +
                ", fallbackUsedForSolutionTime=" + fallbackUsedForSolutionTime +
                '}';
    }
}
