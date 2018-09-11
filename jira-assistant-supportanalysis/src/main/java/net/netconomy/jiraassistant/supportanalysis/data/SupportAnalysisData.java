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
package net.netconomy.jiraassistant.supportanalysis.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;
import net.netconomy.jiraassistant.supportanalysis.data.sla.SlaStatisticsData;

public class SupportAnalysisData {

    private String analysisDate;

    private List<String> projects;

    private String startDate;
    private String endDate;

    private List<String> issueTypes;

    private String andClause;

    private Integer numberOfAnalysedIssues;

    private SlaStatisticsData slaStatisticsData;

    private Map<String, SlaStatisticsData> slaStatisticsDataByPriority;

    private Integer createdIssuesInTimeFrame;
    private Integer createdIssuesThisYear;

    private Map<String, Integer> createdIssuesInTimeFrameByPriority;
    private Map<String, Integer> createdIssuesThisYearByPriority;

    private Integer solvedIssuesDuringTimeFrame;
    private Integer solvedIssuesThisYear;

    private Map<String, Integer> solvedIssuesDuringTimeFrameByPriority;
    private Map<String, Integer> solvedIssuesThisYearByPriority;

    private Integer numberOfBillableIssuesInTimeFrame;
    private Integer numberOfUnbillableIssuesInTimeFrame;

    private SingleTimeMetric meanTimeSpentDuringTimeFrame;
    private SingleTimeMetric medianTimeSpentDuringTimeFrame;
    private SingleTimeMetric standardDeviationTimeSpentDuringTimeFrame;
    private SingleTimeMetric timeSpentDuringTimeFrame90thPercentile;
    private SingleTimeMetric timeSpentDuringTimeFrame99thPercentile;

    private SingleTimeMetric meanTimeSpentOnIssues;
    private SingleTimeMetric medianTimeSpentOnIssues;
    private SingleTimeMetric standardDeviationTimeSpentOnIssues;
    private SingleTimeMetric timeSpentOnIssues90thPercentile;
    private SingleTimeMetric timeSpentOnIssues99thPercentile;

    private Map<String, ErrorCategoryData> errorCategoryData;

    private Map<String, Integer> technicalSeverityData;

    private Integer numberOfOpenIssuesStartDate;

    private SingleTimeMetric meanAgeOfOpenIssuesStartDate;
    private SingleTimeMetric medianAgeOfOpenIssuesStartDate;
    private SingleTimeMetric standardDeviationAgeOfOpenIssuesStartDate;
    private SingleTimeMetric ageOfOpenIssuesStartDate90thPercentile;
    private SingleTimeMetric ageOfOpenIssuesStartDate99thPercentile;

    private Integer numberOfOpenIssuesEndDate;

    private SingleTimeMetric meanAgeOfOpenIssuesEndDate;
    private SingleTimeMetric medianAgeOfOpenIssuesEndDate;
    private SingleTimeMetric standardDeviationAgeOfOpenIssuesEndDate;
    private SingleTimeMetric ageOfOpenIssuesEndDate90thPercentile;
    private SingleTimeMetric ageOfOpenIssuesEndDate99thPercentile;
    
    private List<String> openStatus;
    private List<String> solvedStatus;

    private List<String> reactionTimeEndStatus;
    private List<String> interactionTimeStartStatus;
    private List<String> interactionTimeEndStatus;
    private List<String> solutionTimeStartStatus;
    private List<String> solutionTimeEndStatus;

    public SupportAnalysisData() {

    }

    // Adders
    public void addTechnicalSeverityData(Map<String, Integer> technicalSeverityData) {

        Integer currentCount = 0;
        Integer newCount = 0;

        if(this.technicalSeverityData == null) {
            this.technicalSeverityData = new HashMap<>();
        }

        for (Entry<String, Integer> currentEntry : technicalSeverityData.entrySet()) {

            if (this.technicalSeverityData.containsKey(currentEntry.getKey())) {
                currentCount = this.technicalSeverityData.get(currentEntry.getKey());
                newCount = currentCount + currentEntry.getValue();
            } else {
                newCount = currentEntry.getValue();
            }

            this.technicalSeverityData.put(currentEntry.getKey(), newCount);

        }

    }

    public void addTechnicalSeverityData(List<String> technicalSeverityData) {

        Integer currentCount = 0;
        Integer newCount = 0;

        if(this.technicalSeverityData == null) {
            this.technicalSeverityData = new HashMap<>();
        }

        for (String currentKey : technicalSeverityData) {

            if (this.technicalSeverityData.containsKey(currentKey)) {
                currentCount = this.technicalSeverityData.get(currentKey);
                newCount = currentCount + 1;
            } else {
                newCount = 1;
            }

            this.technicalSeverityData.put(currentKey, newCount);

        }

    }

    public void addSlaStatisticsDataByPriority(String priority, SlaStatisticsData slaStatisticsData) {

        if(this.slaStatisticsDataByPriority == null) {
            this.slaStatisticsDataByPriority = new HashMap<>();
        }

        this.slaStatisticsDataByPriority.put(priority, slaStatisticsData);
    }

    // Getters and Setters
    public String getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(String analysisDate) {
        this.analysisDate = analysisDate;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    public Integer getNumberOfAnalysedIssues() {
        return numberOfAnalysedIssues;
    }

    public void setNumberOfAnalysedIssues(Integer numberOfAnalysedIssues) {
        this.numberOfAnalysedIssues = numberOfAnalysedIssues;
    }

    public SlaStatisticsData getSlaStatisticsData() {
        return slaStatisticsData;
    }

    public void setSlaStatisticsData(SlaStatisticsData slaStatisticsData) {
        this.slaStatisticsData = slaStatisticsData;
    }

    public Integer getCreatedIssuesInTimeFrame() {
        return createdIssuesInTimeFrame;
    }

    public void setCreatedIssuesInTimeFrame(Integer createdIssuesInTimeFrame) {
        this.createdIssuesInTimeFrame = createdIssuesInTimeFrame;
    }

    public Integer getCreatedIssuesThisYear() {
        return createdIssuesThisYear;
    }

    public void setCreatedIssuesThisYear(Integer createdIssuesThisYear) {
        this.createdIssuesThisYear = createdIssuesThisYear;
    }

    public Integer getSolvedIssuesDuringTimeFrame() {
        return solvedIssuesDuringTimeFrame;
    }

    public void setSolvedIssuesDuringTimeFrame(Integer solvedIssuesDuringTimeFrame) {
        this.solvedIssuesDuringTimeFrame = solvedIssuesDuringTimeFrame;
    }

    public Integer getSolvedIssuesThisYear() {
        return solvedIssuesThisYear;
    }

    public void setSolvedIssuesThisYear(Integer solvedIssuesThisYear) {
        this.solvedIssuesThisYear = solvedIssuesThisYear;
    }

    public Integer getNumberOfBillableIssuesInTimeFrame() {
        return numberOfBillableIssuesInTimeFrame;
    }

    public void setNumberOfBillableIssuesInTimeFrame(Integer numberOfBillableIssuesInTimeFrame) {
        this.numberOfBillableIssuesInTimeFrame = numberOfBillableIssuesInTimeFrame;
    }

    public Integer getNumberOfUnbillableIssuesInTimeFrame() {
        return numberOfUnbillableIssuesInTimeFrame;
    }

    public void setNumberOfUnbillableIssuesInTimeFrame(Integer numberOfUnbillableIssuesInTimeFrame) {
        this.numberOfUnbillableIssuesInTimeFrame = numberOfUnbillableIssuesInTimeFrame;
    }

    public SingleTimeMetric getMeanTimeSpentDuringTimeFrame() {
        return meanTimeSpentDuringTimeFrame;
    }

    public void setMeanTimeSpentDuringTimeFrame(Double meanTimeSpentDuringTimeFrame) {
        this.meanTimeSpentDuringTimeFrame = new SingleTimeMetric(meanTimeSpentDuringTimeFrame);
    }

    public SingleTimeMetric getMedianTimeSpentDuringTimeFrame() {
        return medianTimeSpentDuringTimeFrame;
    }

    public void setMedianTimeSpentDuringTimeFrame(Double medianTimeSpentDuringTimeFrame) {
        this.medianTimeSpentDuringTimeFrame = new SingleTimeMetric(medianTimeSpentDuringTimeFrame);
    }

    public SingleTimeMetric getStandardDeviationTimeSpentDuringTimeFrame() {
        return standardDeviationTimeSpentDuringTimeFrame;
    }

    public void setStandardDeviationTimeSpentDuringTimeFrame(Double standardDeviationTimeSpentDuringTimeFrame) {
        this.standardDeviationTimeSpentDuringTimeFrame = new SingleTimeMetric(standardDeviationTimeSpentDuringTimeFrame);
    }

    public SingleTimeMetric getMeanTimeSpentOnIssues() {
        return meanTimeSpentOnIssues;
    }

    public void setMeanTimeSpentOnIssues(Double meanTimeSpentOnIssues) {
        this.meanTimeSpentOnIssues = new SingleTimeMetric(meanTimeSpentOnIssues);
    }

    public SingleTimeMetric getMedianTimeSpentOnIssues() {
        return medianTimeSpentOnIssues;
    }

    public void setMedianTimeSpentOnIssues(Double medianTimeSpentOnIssues) {
        this.medianTimeSpentOnIssues = new SingleTimeMetric(medianTimeSpentOnIssues);
    }

    public SingleTimeMetric getStandardDeviationTimeSpentOnIssues() {
        return standardDeviationTimeSpentOnIssues;
    }

    public void setStandardDeviationTimeSpentOnIssues(Double standardDeviationTimeSpentOnIssues) {
        this.standardDeviationTimeSpentOnIssues = new SingleTimeMetric(standardDeviationTimeSpentOnIssues);
    }

    public Map<String, ErrorCategoryData> getErrorCategoryData() {
        return errorCategoryData;
    }

    public void setErrorCategoryData(Map<String, ErrorCategoryData> errorCategoryData) {
        this.errorCategoryData = errorCategoryData;
    }

    public Map<String, Integer> getTechnicalSeverityData() {
        return technicalSeverityData;
    }

    public void setTechnicalSeverityData(Map<String, Integer> technicalSeverityData) {
        this.technicalSeverityData = technicalSeverityData;
    }

    public Integer getNumberOfOpenIssuesStartDate() {
        return numberOfOpenIssuesStartDate;
    }

    public void setNumberOfOpenIssuesStartDate(Integer numberOfOpenIssuesStartDate) {
        this.numberOfOpenIssuesStartDate = numberOfOpenIssuesStartDate;
    }

    public SingleTimeMetric getMeanAgeOfOpenIssuesStartDate() {
        return meanAgeOfOpenIssuesStartDate;
    }

    public void setMeanAgeOfOpenIssuesStartDate(Double meanAgeOfOpenIssuesStartDate) {
        this.meanAgeOfOpenIssuesStartDate = new SingleTimeMetric(meanAgeOfOpenIssuesStartDate);
    }

    public SingleTimeMetric getMedianAgeOfOpenIssuesStartDate() {
        return medianAgeOfOpenIssuesStartDate;
    }

    public void setMedianAgeOfOpenIssuesStartDate(Double medianAgeOfOpenIssuesStartDate) {
        this.medianAgeOfOpenIssuesStartDate = new SingleTimeMetric(medianAgeOfOpenIssuesStartDate);
    }

    public SingleTimeMetric getStandardDeviationAgeOfOpenIssuesStartDate() {
        return standardDeviationAgeOfOpenIssuesStartDate;
    }

    public void setStandardDeviationAgeOfOpenIssuesStartDate(Double standardDeviationAgeOfOpenIssuesStartDate) {
        this.standardDeviationAgeOfOpenIssuesStartDate = new SingleTimeMetric(standardDeviationAgeOfOpenIssuesStartDate);
    }

    public Integer getNumberOfOpenIssuesEndDate() {
        return numberOfOpenIssuesEndDate;
    }

    public void setNumberOfOpenIssuesEndDate(Integer numberOfOpenIssuesEndDate) {
        this.numberOfOpenIssuesEndDate = numberOfOpenIssuesEndDate;
    }

    public SingleTimeMetric getMeanAgeOfOpenIssuesEndDate() {
        return meanAgeOfOpenIssuesEndDate;
    }

    public void setMeanAgeOfOpenIssuesEndDate(Double meanAgeOfOpenIssuesEndDate) {
        this.meanAgeOfOpenIssuesEndDate = new SingleTimeMetric(meanAgeOfOpenIssuesEndDate);
    }

    public SingleTimeMetric getMedianAgeOfOpenIssuesEndDate() {
        return medianAgeOfOpenIssuesEndDate;
    }

    public void setMedianAgeOfOpenIssuesEndDate(Double medianAgeOfOpenIssuesEndDate) {
        this.medianAgeOfOpenIssuesEndDate = new SingleTimeMetric(medianAgeOfOpenIssuesEndDate);
    }

    public SingleTimeMetric getStandardDeviationAgeOfOpenIssuesEndDate() {
        return standardDeviationAgeOfOpenIssuesEndDate;
    }

    public void setStandardDeviationAgeOfOpenIssuesEndDate(Double standardDeviationAgeOfOpenIssuesEndDate) {
        this.standardDeviationAgeOfOpenIssuesEndDate = new SingleTimeMetric(standardDeviationAgeOfOpenIssuesEndDate);
    }

    public List<String> getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(List<String> openStatus) {
        this.openStatus = openStatus;
    }

    public List<String> getSolvedStatus() {
        return solvedStatus;
    }

    public void setSolvedStatus(List<String> solvedStatus) {
        this.solvedStatus = solvedStatus;
    }

    public List<String> getReactionTimeEndStatus() {
        return reactionTimeEndStatus;
    }

    public void setReactionTimeEndStatus(List<String> reactionTimeEndStatus) {
        this.reactionTimeEndStatus = reactionTimeEndStatus;
    }

    public List<String> getInteractionTimeStartStatus() {
        return interactionTimeStartStatus;
    }

    public void setInteractionTimeStartStatus(List<String> interactionTimeStartStatus) {
        this.interactionTimeStartStatus = interactionTimeStartStatus;
    }

    public List<String> getInteractionTimeEndStatus() {
        return interactionTimeEndStatus;
    }

    public void setInteractionTimeEndStatus(List<String> interactionTimeEndStatus) {
        this.interactionTimeEndStatus = interactionTimeEndStatus;
    }

    public List<String> getSolutionTimeStartStatus() {
        return solutionTimeStartStatus;
    }

    public void setSolutionTimeStartStatus(List<String> solutionTimeStartStatus) {
        this.solutionTimeStartStatus = solutionTimeStartStatus;
    }

    public List<String> getSolutionTimeEndStatus() {
        return solutionTimeEndStatus;
    }

    public void setSolutionTimeEndStatus(List<String> solutionTimeEndStatus) {
        this.solutionTimeEndStatus = solutionTimeEndStatus;
    }

    public Map<String, SlaStatisticsData> getSlaStatisticsDataByPriority() {
        return slaStatisticsDataByPriority;
    }

    public void setSlaStatisticsDataByPriority(Map<String, SlaStatisticsData> slaStatisticsDataByPriority) {
        this.slaStatisticsDataByPriority = slaStatisticsDataByPriority;
    }

    public Map<String, Integer> getCreatedIssuesInTimeFrameByPriority() {
        return createdIssuesInTimeFrameByPriority;
    }

    public void setCreatedIssuesInTimeFrameByPriority(Map<String, Integer> createdIssuesInTimeFrameByPriority) {
        this.createdIssuesInTimeFrameByPriority = createdIssuesInTimeFrameByPriority;
    }

    public Map<String, Integer> getCreatedIssuesThisYearByPriority() {
        return createdIssuesThisYearByPriority;
    }

    public void setCreatedIssuesThisYearByPriority(Map<String, Integer> createdIssuesThisYearByPriority) {
        this.createdIssuesThisYearByPriority = createdIssuesThisYearByPriority;
    }

    public Map<String, Integer> getSolvedIssuesDuringTimeFrameByPriority() {
        return solvedIssuesDuringTimeFrameByPriority;
    }

    public void setSolvedIssuesDuringTimeFrameByPriority(Map<String, Integer> solvedIssuesDuringTimeFrameByPriority) {
        this.solvedIssuesDuringTimeFrameByPriority = solvedIssuesDuringTimeFrameByPriority;
    }

    public Map<String, Integer> getSolvedIssuesThisYearByPriority() {
        return solvedIssuesThisYearByPriority;
    }

    public void setSolvedIssuesThisYearByPriority(Map<String, Integer> solvedIssuesThisYearByPriority) {
        this.solvedIssuesThisYearByPriority = solvedIssuesThisYearByPriority;
    }

    public SingleTimeMetric getTimeSpentDuringTimeFrame90thPercentile() {
        return timeSpentDuringTimeFrame90thPercentile;
    }

    public void setTimeSpentDuringTimeFrame90thPercentile(Double timeSpentDuringTimeFrame90thPercentileMinutes) {
        timeSpentDuringTimeFrame90thPercentile = new SingleTimeMetric(timeSpentDuringTimeFrame90thPercentileMinutes);
    }

    public SingleTimeMetric getTimeSpentDuringTimeFrame99thPercentile() {
        return timeSpentDuringTimeFrame99thPercentile;
    }

    public void setTimeSpentDuringTimeFrame99thPercentile(Double timeSpentDuringTimeFrame99thPercentileMinutes) {
        timeSpentDuringTimeFrame99thPercentile = new SingleTimeMetric(timeSpentDuringTimeFrame99thPercentileMinutes);
    }

    public SingleTimeMetric getTimeSpentOnIssues90thPercentile() {
        return timeSpentOnIssues90thPercentile;
    }

    public void setTimeSpentOnIssues90thPercentile(Double timeSpentOnIssues90thPercentileMinutes) {
        timeSpentOnIssues90thPercentile = new SingleTimeMetric(timeSpentOnIssues90thPercentileMinutes);
    }

    public SingleTimeMetric getTimeSpentOnIssues99thPercentile() {
        return timeSpentOnIssues99thPercentile;
    }

    public void setTimeSpentOnIssues99thPercentile(Double timeSpentOnIssues99thPercentileMinutes) {
        timeSpentOnIssues99thPercentile = new SingleTimeMetric(timeSpentOnIssues99thPercentileMinutes);
    }

    public SingleTimeMetric getAgeOfOpenIssuesStartDate90thPercentile() {
        return ageOfOpenIssuesStartDate90thPercentile;
    }

    public void setAgeOfOpenIssuesStartDate90thPercentile(Double ageOfOpenIssuesStartDate90thPercentileMinutes) {
        ageOfOpenIssuesStartDate90thPercentile = new SingleTimeMetric(ageOfOpenIssuesStartDate90thPercentileMinutes);
    }

    public SingleTimeMetric getAgeOfOpenIssuesStartDate99thPercentile() {
        return ageOfOpenIssuesStartDate99thPercentile;
    }

    public void setAgeOfOpenIssuesStartDate99thPercentile(Double ageOfOpenIssuesStartDate99thPercentileMinutes) {
        ageOfOpenIssuesStartDate99thPercentile = new SingleTimeMetric(ageOfOpenIssuesStartDate99thPercentileMinutes);
    }

    public SingleTimeMetric getAgeOfOpenIssuesEndDate90thPercentile() {
        return ageOfOpenIssuesEndDate90thPercentile;
    }

    public void setAgeOfOpenIssuesEndDate90thPercentile(Double ageOfOpenIssuesEndDate90thPercentileMinutes) {
        ageOfOpenIssuesEndDate90thPercentile = new SingleTimeMetric(ageOfOpenIssuesEndDate90thPercentileMinutes);
    }

    public SingleTimeMetric getAgeOfOpenIssuesEndDate99thPercentile() {
        return ageOfOpenIssuesEndDate99thPercentile;
    }

    public void setAgeOfOpenIssuesEndDate99thPercentile(Double ageOfOpenIssuesEndDate99thPercentileMinutes) {
        ageOfOpenIssuesEndDate99thPercentile = new SingleTimeMetric(ageOfOpenIssuesEndDate99thPercentileMinutes);
    }

    @Override
    public String toString() {
        return "SupportAnalysisData{" +
                "analysisDate='" + analysisDate + '\'' +
                ", projects=" + projects +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", issueTypes=" + issueTypes +
                ", andClause='" + andClause + '\'' +
                ", numberOfAnalysedIssues=" + numberOfAnalysedIssues +
                ", slaStatisticsData=" + slaStatisticsData +
                ", slaStatisticsDataByPriority=" + slaStatisticsDataByPriority +
                ", createdIssuesInTimeFrame=" + createdIssuesInTimeFrame +
                ", createdIssuesThisYear=" + createdIssuesThisYear +
                ", createdIssuesInTimeFrameByPriority=" + createdIssuesInTimeFrameByPriority +
                ", createdIssuesThisYearByPriority=" + createdIssuesThisYearByPriority +
                ", solvedIssuesDuringTimeFrame=" + solvedIssuesDuringTimeFrame +
                ", solvedIssuesThisYear=" + solvedIssuesThisYear +
                ", solvedIssuesDuringTimeFrameByPriority=" + solvedIssuesDuringTimeFrameByPriority +
                ", solvedIssuesThisYearByPriority=" + solvedIssuesThisYearByPriority +
                ", numberOfBillableIssuesInTimeFrame=" + numberOfBillableIssuesInTimeFrame +
                ", numberOfUnbillableIssuesInTimeFrame=" + numberOfUnbillableIssuesInTimeFrame +
                ", meanTimeSpentDuringTimeFrame=" + meanTimeSpentDuringTimeFrame +
                ", medianTimeSpentDuringTimeFrame=" + medianTimeSpentDuringTimeFrame +
                ", standardDeviationTimeSpentDuringTimeFrame=" + standardDeviationTimeSpentDuringTimeFrame +
                ", timeSpentDuringTimeFrame90thPercentile=" + timeSpentDuringTimeFrame90thPercentile +
                ", timeSpentDuringTimeFrame99thPercentile=" + timeSpentDuringTimeFrame99thPercentile +
                ", meanTimeSpentOnIssues=" + meanTimeSpentOnIssues +
                ", medianTimeSpentOnIssues=" + medianTimeSpentOnIssues +
                ", standardDeviationTimeSpentOnIssues=" + standardDeviationTimeSpentOnIssues +
                ", timeSpentOnIssues90thPercentile=" + timeSpentOnIssues90thPercentile +
                ", timeSpentOnIssues99thPercentile=" + timeSpentOnIssues99thPercentile +
                ", errorCategoryData=" + errorCategoryData +
                ", technicalSeverityData=" + technicalSeverityData +
                ", numberOfOpenIssuesStartDate=" + numberOfOpenIssuesStartDate +
                ", meanAgeOfOpenIssuesStartDate=" + meanAgeOfOpenIssuesStartDate +
                ", medianAgeOfOpenIssuesStartDate=" + medianAgeOfOpenIssuesStartDate +
                ", standardDeviationAgeOfOpenIssuesStartDate=" + standardDeviationAgeOfOpenIssuesStartDate +
                ", ageOfOpenIssuesStartDate90thPercentile=" + ageOfOpenIssuesStartDate90thPercentile +
                ", ageOfOpenIssuesStartDate99thPercentile=" + ageOfOpenIssuesStartDate99thPercentile +
                ", numberOfOpenIssuesEndDate=" + numberOfOpenIssuesEndDate +
                ", meanAgeOfOpenIssuesEndDate=" + meanAgeOfOpenIssuesEndDate +
                ", medianAgeOfOpenIssuesEndDate=" + medianAgeOfOpenIssuesEndDate +
                ", standardDeviationAgeOfOpenIssuesEndDate=" + standardDeviationAgeOfOpenIssuesEndDate +
                ", ageOfOpenIssuesEndDate90thPercentile=" + ageOfOpenIssuesEndDate90thPercentile +
                ", ageOfOpenIssuesEndDate99thPercentile=" + ageOfOpenIssuesEndDate99thPercentile +
                ", openStatus=" + openStatus +
                ", solvedStatus=" + solvedStatus +
                ", reactionTimeEndStatus=" + reactionTimeEndStatus +
                ", interactionTimeStartStatus=" + interactionTimeStartStatus +
                ", interactionTimeEndStatus=" + interactionTimeEndStatus +
                ", solutionTimeStartStatus=" + solutionTimeStartStatus +
                ", solutionTimeEndStatus=" + solutionTimeEndStatus +
                '}';
    }
}
