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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netconomy.jiraassistant.base.data.SingleTimeMetric;
import org.joda.time.DateTime;

public class ReopenFactorData {

    private String creationDate;
    private String projects;
    private String startDate;
    private String endDate;
    private String andClause;
    private Integer threshold = 0;

    private Integer issuesChangingState = 0;

    private Integer inTestingIssuesAtStart = 0;
    private Integer reopenedIssuesAtStart = 0;
    private Integer testedIssuesAtStart = 0;

    private Integer inTestingCount = 0;
    private Integer reopenCount = 0;
    private Integer testedCount = 0;

    private Integer numberOfReopenedIssues = 0;

    private Double reopensByProcessed = 0.0;
    private Double testedByProcessed = 0.0;
    private Double reopensByTested = 0.0;

    private Double processedByInTesting = 0.0;

    private SingleTimeMetric meanTimeSpentAfterFirstReopen;
    private SingleTimeMetric medianTimeSpentAfterFirstReopen;
    private SingleTimeMetric stdDeviationTimeSpentAfterFirstReopen;

    private Double meanPercentageOfTimeSpentAfterFirstReopen;
    private Double medianPercentageOfTimeSpentAfterFirstReopen;
    private Double stdDeviationPercentageOfTimeSpentAfterFirstReopen;

    private List<ReopenedIssue> issuesPastThreshold = new ArrayList<>();

    private List<String> inTestingStatus = new ArrayList<>();
    private List<String> reopenedStatus = new ArrayList<>();
    private List<String> testedStatus = new ArrayList<>();

    public ReopenFactorData() {

    }

    // Adders
    public void addInTestingStatus(String inTestingStatusName) {
        this.inTestingStatus.add(inTestingStatusName);
    }

    public void addReopenedStatus(String reopenedStatusName) {
        this.reopenedStatus.add(reopenedStatusName);
    }

    public void addTestedStatus(String testedStatusName) {
        this.testedStatus.add(testedStatusName);
    }

    public void addIssuePastThreshold(ReopenedIssue issuePastThreshold) {
        issuesPastThreshold.add(issuePastThreshold);
    }

    // Getters and Setters
    public DateTime getStartDateAsDateTime() {
        return DateTime.parse(startDate);
    }

    public DateTime getEndDateAsDateTime() {
        return DateTime.parse(endDate);
    }

    // Getters and Setters
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
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

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getInTestingCount() {
        return inTestingCount;
    }

    public void setInTestingCount(Integer inTestingCount) {
        this.inTestingCount = inTestingCount;
    }

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getTestedCount() {
        return testedCount;
    }

    public void setTestedCount(Integer testedCount) {
        this.testedCount = testedCount;
    }

    public Double getReopensByTested() {
        return reopensByTested;
    }

    public void setReopensByTested(Double reopensByTested) {
        this.reopensByTested = reopensByTested;
    }

    public List<ReopenedIssue> getIssuesPastThreshold() {
        return issuesPastThreshold;
    }

    public void setIssuesPastThreshold(List<ReopenedIssue> issuesPastThreshold) {
        this.issuesPastThreshold = issuesPastThreshold;
    }

    public Integer getIssuesChangingState() {
        return issuesChangingState;
    }

    public void setIssuesChangingState(Integer issuesChangingState) {
        this.issuesChangingState = issuesChangingState;
    }

    public Integer getInTestingIssuesAtStart() {
        return inTestingIssuesAtStart;
    }

    public void setInTestingIssuesAtStart(Integer inTestingIssuesAtStart) {
        this.inTestingIssuesAtStart = inTestingIssuesAtStart;
    }

    public Integer getReopenedIssuesAtStart() {
        return reopenedIssuesAtStart;
    }

    public void setReopenedIssuesAtStart(Integer reopenedIssuesAtStart) {
        this.reopenedIssuesAtStart = reopenedIssuesAtStart;
    }

    public Integer getTestedIssuesAtStart() {
        return testedIssuesAtStart;
    }

    public void setTestedIssuesAtStart(Integer testedIssuesAtStart) {
        this.testedIssuesAtStart = testedIssuesAtStart;
    }

    public Double getProcessedByInTesting() {
        return processedByInTesting;
    }

    public void setProcessedByInTesting(Double processedByInTesting) {
        this.processedByInTesting = processedByInTesting;
    }

    public Double getReopensByProcessed() {
        return reopensByProcessed;
    }

    public void setReopensByProcessed(Double reopensByProcessed) {
        this.reopensByProcessed = reopensByProcessed;
    }

    public Double getTestedByProcessed() {
        return testedByProcessed;
    }

    public void setTestedByProcessed(Double testedByProcessed) {
        this.testedByProcessed = testedByProcessed;
    }

    public List<String> getInTestingStatus() {
        return inTestingStatus;
    }

    public void setInTestingStatus(List<String> inTestingStatus) {
        this.inTestingStatus = inTestingStatus;
    }

    public List<String> getReopenedStatus() {
        return reopenedStatus;
    }

    public void setReopenedStatus(List<String> reopenedStatus) {
        this.reopenedStatus = reopenedStatus;
    }

    public List<String> getTestedStatus() {
        return testedStatus;
    }

    public void setTestedStatus(List<String> testedStatus) {
        this.testedStatus = testedStatus;
    }

    public SingleTimeMetric getMeanTimeSpentAfterFirstReopen() {
        return meanTimeSpentAfterFirstReopen;
    }

    public void setMeanTimeSpentAfterFirstReopen(Double meanTimeSpentAfterFirstReopen) {
        this.meanTimeSpentAfterFirstReopen = new SingleTimeMetric(meanTimeSpentAfterFirstReopen);
    }

    public SingleTimeMetric getMedianTimeSpentAfterFirstReopen() {
        return medianTimeSpentAfterFirstReopen;
    }

    public void setMedianTimeSpentAfterFirstReopen(Double medianTimeSpentAfterFirstReopen) {
        this.medianTimeSpentAfterFirstReopen = new SingleTimeMetric(medianTimeSpentAfterFirstReopen);
    }

    public SingleTimeMetric getStdDeviationTimeSpentAfterFirstReopen() {
        return stdDeviationTimeSpentAfterFirstReopen;
    }

    public void setStdDeviationTimeSpentAfterFirstReopen(Double stdDeviationTimeSpentAfterFirstReopen) {
        this.stdDeviationTimeSpentAfterFirstReopen = new SingleTimeMetric(stdDeviationTimeSpentAfterFirstReopen);
    }

    public Double getMeanPercentageOfTimeSpentAfterFirstReopen() {
        return meanPercentageOfTimeSpentAfterFirstReopen;
    }

    public void setMeanPercentageOfTimeSpentAfterFirstReopen(Double meanPercentageOfTimeSpentAfterFirstReopen) {
        this.meanPercentageOfTimeSpentAfterFirstReopen = meanPercentageOfTimeSpentAfterFirstReopen;
    }

    public Double getMedianPercentageOfTimeSpentAfterFirstReopen() {
        return medianPercentageOfTimeSpentAfterFirstReopen;
    }

    public void setMedianPercentageOfTimeSpentAfterFirstReopen(Double medianPercentageOfTimeSpentAfterFirstReopen) {
        this.medianPercentageOfTimeSpentAfterFirstReopen = medianPercentageOfTimeSpentAfterFirstReopen;
    }

    public Double getStdDeviationPercentageOfTimeSpentAfterFirstReopen() {
        return stdDeviationPercentageOfTimeSpentAfterFirstReopen;
    }

    public void setStdDeviationPercentageOfTimeSpentAfterFirstReopen(Double stdDeviationPercentageOfTimeSpentAfterFirstReopen) {
        this.stdDeviationPercentageOfTimeSpentAfterFirstReopen = stdDeviationPercentageOfTimeSpentAfterFirstReopen;
    }

    public Integer getNumberOfReopenedIssues() {
        return numberOfReopenedIssues;
    }

    public void setNumberOfReopenedIssues(Integer numberOfReopenedIssues) {
        this.numberOfReopenedIssues = numberOfReopenedIssues;
    }

    @Override
    public String toString() {
        return "ReopenFactorData{" +
                "creationDate='" + creationDate + '\'' +
                ", projects='" + projects + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", andClause='" + andClause + '\'' +
                ", threshold=" + threshold +
                ", issuesChangingState=" + issuesChangingState +
                ", inTestingIssuesAtStart=" + inTestingIssuesAtStart +
                ", reopenedIssuesAtStart=" + reopenedIssuesAtStart +
                ", testedIssuesAtStart=" + testedIssuesAtStart +
                ", inTestingCount=" + inTestingCount +
                ", reopenCount=" + reopenCount +
                ", testedCount=" + testedCount +
                ", numberOfReopenedIssues=" + numberOfReopenedIssues +
                ", reopensByProcessed=" + reopensByProcessed +
                ", testedByProcessed=" + testedByProcessed +
                ", reopensByTested=" + reopensByTested +
                ", processedByInTesting=" + processedByInTesting +
                ", meanTimeSpentAfterFirstReopen=" + meanTimeSpentAfterFirstReopen +
                ", medianTimeSpentAfterFirstReopen=" + medianTimeSpentAfterFirstReopen +
                ", stdDeviationTimeSpentAfterFirstReopen=" + stdDeviationTimeSpentAfterFirstReopen +
                ", meanPercentageOfTimeSpentAfterFirstReopen=" + meanPercentageOfTimeSpentAfterFirstReopen +
                ", medianPercentageOfTimeSpentAfterFirstReopen=" + medianPercentageOfTimeSpentAfterFirstReopen +
                ", stdDeviationPercentageOfTimeSpentAfterFirstReopen=" + stdDeviationPercentageOfTimeSpentAfterFirstReopen +
                ", issuesPastThreshold=" + issuesPastThreshold +
                ", inTestingStatus=" + inTestingStatus +
                ", reopenedStatus=" + reopenedStatus +
                ", testedStatus=" + testedStatus +
                '}';
    }
}
