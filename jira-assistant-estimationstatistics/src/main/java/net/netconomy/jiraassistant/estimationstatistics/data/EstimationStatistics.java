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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstimationStatistics {

    private final String project;
    private final String statisticsCreationDate;
    private final String analysisStartDate;
    private final String analysisEndDate;
    private final String andClause;
    private Map<String, IssueTypeEstimationStatisticsData> estimationStatisticsMap = new HashMap<>();
    private List<String> inProgressStatus = new ArrayList<>();
    private List<String> finishedStatus = new ArrayList<>();

    public EstimationStatistics(String project, String statisticsCreationDate, String analysisStartDate,
            String analysisEndDate, String andClause) {

        this.project = project;
        this.statisticsCreationDate = statisticsCreationDate;
        this.analysisStartDate = analysisStartDate;
        this.analysisEndDate = analysisEndDate;
        this.andClause = andClause;

    }

    public void addEstimationStatisticsForIssueType(String issueTypeName,
            IssueTypeEstimationStatisticsData issueTypeEstimationStatistics) {
        this.estimationStatisticsMap.put(issueTypeName, issueTypeEstimationStatistics);
    }

    public Map<String, IssueTypeEstimationStatisticsData> getEstimationStatisticsMap() {
        return estimationStatisticsMap;
    }

    public void setEstimationStatisticsMap(Map<String, IssueTypeEstimationStatisticsData> estimationStatisticsMap) {
        this.estimationStatisticsMap = estimationStatisticsMap;
    }

    public String getProject() {
        return project;
    }

    public String getStatisticsCreationDate() {
        return statisticsCreationDate;
    }

    public String getAnalysisStartDate() {
        return analysisStartDate;
    }

    public String getAnalysisEndDate() {
        return analysisEndDate;
    }

    public String getAndClause() {
        return andClause;
    }

    public List<String> getInProgressStatus() {
        return inProgressStatus;
    }

    public void setInProgressStatus(List<String> inProgressStatus) {
        this.inProgressStatus = inProgressStatus;
    }

    public List<String> getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(List<String> finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    @Override
    public String toString() {
        return "EstimationStatistics [project=" + project + ", statisticsCreationDate=" + statisticsCreationDate
                + ", analysisStartDate=" + analysisStartDate + ", analysisEndDate=" + analysisEndDate + ", andClause="
                + andClause + ", estimationStatisticsMap=" + estimationStatisticsMap + ", inProgressStatus="
                + inProgressStatus + ", finishedStatus=" + finishedStatus + "]";
    }

}
